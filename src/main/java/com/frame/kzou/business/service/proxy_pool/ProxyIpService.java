package com.frame.kzou.business.service.proxy_pool;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.IService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.frame.kzou.business.mapper.ProxyIpMapper;
import com.frame.kzou.business.pojo.ProxyIp;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 *
 * @author: HP-zouKun
 * Date: 2022/10/20
 * Time: 14:26
 * Description: service 服务
 */
@Service
public class ProxyIpService extends ServiceImpl<ProxyIpMapper, ProxyIp> implements IService<ProxyIp> {

    public List<ProxyIp> queryActiveProxyIp(int limit) {
        QueryWrapper<ProxyIp> wrapper = new QueryWrapper<>();
        wrapper.orderBy(true, false, "lift").last(" limit " + limit);
        return baseMapper.selectList(wrapper);
    }
}
