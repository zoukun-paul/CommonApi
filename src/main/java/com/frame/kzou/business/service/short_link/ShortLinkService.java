package com.frame.kzou.business.service.short_link;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.service.IService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.frame.kzou.business.mapper.ShortLinkMapper;
import com.frame.kzou.business.pojo.ShortLink;
import com.frame.kzou.enums.DateFormatEnum;
import com.frame.kzou.utils.DateUtil;
import com.google.common.hash.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.UncategorizedSQLException;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * Created with IntelliJ IDEA.
 *
 * @author: HP-zouKun
 * Date: 2022/10/17
 * Time: 15:06
 * Description:
 * 实现思路：https://mp.weixin.qq.com/s/lBdxlJbrDsqKgagYW6Xl2w
 */
@Service
@Slf4j
public class ShortLinkService extends ServiceImpl<ShortLinkMapper, ShortLink> implements IService<ShortLink> {

    private final HashFunction hashFunction = Hashing.murmur3_32();

    private final ReentrantReadWriteLock readWriteLock = new ReentrantReadWriteLock();
    private final ReentrantReadWriteLock.ReadLock readLock = readWriteLock.readLock();
    private final ReentrantReadWriteLock.WriteLock writeLock = readWriteLock.writeLock();

    /**
     * 布隆过滤器
     */
    private BloomFilter<String> boomFilter = BloomFilter.create(Funnels.stringFunnel(Charset.defaultCharset()), 100_000, 0.01);

    private final String baseUrl;

    private Map<String, Integer> map = new ConcurrentHashMap<>();

    public ShortLinkService(String baseUrl) {
        this.baseUrl = baseUrl;
    }

    /**
     * 获取短链接对象
     *
     * @param surlSuffix 短链接唯一标识符后缀
     * @return ShortLink 短链接实体
     */
    public ShortLink getByShortUrlSuffix(String surlSuffix) {
        QueryWrapper<ShortLink> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("suffix", surlSuffix);
        try {
            readLock.lock();
            map.merge(surlSuffix, 1, Integer::sum);
        } finally {
            readLock.unlock();
        }
        return baseMapper.selectOne(queryWrapper);
    }

    /**
     * 持久化短链接
     *
     * @param shortLink 短连接对象
     */
    public void saveEntry(ShortLink shortLink) {
        RuntimeException exception = null;
        // 最多冲突100次
        for (int i = 0; i < 100; i++) {
            try {
                // 如果不冲突
                if (boomFilter.mightContain(shortLink.getSuffix())) {
                    String suffix = genSuffix(shortLink.getSuffix());
                    shortLink.setSurl(setShortLink(suffix));
                    shortLink.setSuffix(suffix);
                    continue;
                }
                save(shortLink);
                return;
            } catch (UncategorizedSQLException e) {
                log.warn("hash 冲突：{} => {}", shortLink, e.getMessage());
                exception = e;
            } finally {
                boomFilter.put(shortLink.getSuffix());
            }
        }
        throw exception;
    }

    public String genSuffix(String lUrl) {
        HashCode hashCode = hashFunction.hashString(lUrl, StandardCharsets.UTF_8);
        return hashCode.toString();
    }

    public String genShortLink(String lUrl) {
        return baseUrl + "/rd/" + genSuffix(lUrl);
    }

    public String setShortLink(String suffix) {
        return baseUrl + "/rd/" + suffix;
    }


    /**
     * 每一个小时执行一次
     */
    @Scheduled(cron = "0 0 1 * * ?")
    public void scheduledTask() {
        log.info("短链接点击率数据-定时入库 at: {}, 待处理数据量：{}",
                DateUtil.getNowFormat(DateFormatEnum.STANDARD_IGNORE_S),
                map.size()
        );
        if (map.size() == 0) {
            return;
        }
        HashMap<String, Integer> tmpMap = new HashMap<>(map.size() * 2);
        try {
            writeLock.lock();
            tmpMap.putAll(map);
            map.clear();
        } finally {
            writeLock.unlock();
        }
        // db 入库
        UpdateWrapper<ShortLink> updateWrapper = new UpdateWrapper<>();
        for (Map.Entry<String, Integer> entry : tmpMap.entrySet()) {
            String key = entry.getKey();
            Integer value = entry.getValue();
            updateWrapper.eq("suffix", key)
                    .set("click_times", value);
            baseMapper.update(null, updateWrapper);
            // 清空条件
            updateWrapper.clear();
        }
    }

}
