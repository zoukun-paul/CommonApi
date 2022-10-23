package com.frame.kzou.business.service.proxy_pool.store;


import com.frame.kzou.business.pojo.ProxyIp;
import com.frame.kzou.business.service.proxy_pool.ProxyIpService;
import com.frame.kzou.business.service.proxy_pool.check.ProxyIpCheck;
import com.frame.kzou.utils.Collections;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import java.util.*;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.function.Function;

/**
 * Created with IntelliJ IDEA.
 *
 * @author: HP-zouKun
 * Date: 2022/10/19
 * Time: 19:46
 * Description: 存储模块
 */
@Slf4j
@Component
public class StoreProxyIp {

    private final ProxyIpService proxyIpService;

    private final ProxyIpCheck proxyIpCheck;

    private final ThreadPoolExecutor threadPoolExecutor;

    public StoreProxyIp(ProxyIpService proxyIpService, ProxyIpCheck proxyIpCheck, ThreadPoolExecutor threadPoolExecutor) {
        this.proxyIpService = proxyIpService;
        this.proxyIpCheck = proxyIpCheck;
        this.threadPoolExecutor = threadPoolExecutor;
    }

    /**
     *  life 越大越优先，大顶堆
     */
    class PriorityQueueCache extends AbstractQueue<ProxyIp> {
        private ProxyIp[] cache = new ProxyIp[16];
        private final Lock lock = new ReentrantLock();
        private int size = 0;

        public PriorityQueueCache(Collection<ProxyIp> data) {
            for (ProxyIp datum : data) {
                add(datum);
            }
        }
        public PriorityQueueCache(){}

        @Override
        public void clear() {
            lock.lock();
            try {
                for (int i = 1; i <= size; i++) {
                    cache[i] = null;
                }
                size = 0;
            }finally {
                lock.unlock();
            }
        }

        /**
         * 不支持按优先级遍历
         * @return
         */
        @Override
        public Iterator<ProxyIp> iterator() {
            return new Iterator<ProxyIp>() {
                private int index = 1;

                @Override
                public boolean hasNext() {
                    return index <= size;
                }

                @Override
                public ProxyIp next() {
                    return cache[index++];
                }
            };
        }

        @Override
        public int size() {
            return size;
        }

        @Override
        public boolean add(ProxyIp proxyIp) {
            return offer(proxyIp);
        }

        @Override
        public boolean offer(ProxyIp proxyIp) {
            if (proxyIp==null) {
                throw new NullPointerException();
            }
            lock.lock();
            try {
                if (isGrow()) {
                    grow();
                }
                cache[++size]=proxyIp;
                fixUp(size);
            }finally {
                lock.unlock();
            }
            return false;
        }

        @Override
        public ProxyIp poll() {
            ProxyIp proxyIp = cache[1];
            cache[1] = cache[size];
            cache[size--] = null;
            fixDown(1);
            return proxyIp;
        }

        /**
         * 过滤
         */
        public void filter(Function<ProxyIp, Boolean> fun) {
            int index = 0;
            ProxyIp[] proxyIps = new ProxyIp[cache.length];
            lock.lock();
            try {
                for (int i = 1; i < size; i++) {
                    if (fun.apply(cache[i])) {
                        proxyIps[++index] = cache[i];
                    }
                }
                size = index;
                cache = proxyIps;
                // 堆化
                heapify();
            } finally {
                lock.unlock();
            }
        }


        @Override
        public ProxyIp peek() {
            return cache[1];
        }

        public List<ProxyIp> peek(int num) {
            PriorityQueue<ProxyIp> tmp = new PriorityQueue<>((o1, o2) -> o2.getLife() - o1.getLife());
            List<ProxyIp> res = new ArrayList<>(num * 2);
            num = Math.min(num, size);
            lock.lock();
            try {
                for (int i = 1, startIndex = 1; i < num + 1; i++, startIndex *= 2) {
                    // 添加的个数，当前i行中的所有元素
                    int index = startIndex;
                    for (int j = 0; j < Math.pow(2, i - 1) && index <= size; j++) {
                        tmp.add(cache[index++]);
                    }
                    res.add(tmp.poll());
                }
            }finally {
                lock.unlock();
            }
            return  res;
        }

        private int indexOf(ProxyIp proxyIp) {
            if (proxyIp != null) {
                for (int i = 0; i < size; i++) {
                    if (cache[i]==proxyIp){
                        return i;
                    }
                }
            }
            return -1;
        }

        public boolean contains(ProxyIp proxyIp) {
            return indexOf(proxyIp) != -1;
        }

        private void fixDown(int k) {
            int p;
            while ((p = k << 1) <= size && p > 0) {
                if (p < size && cache[p].getLife() < cache[p + 1].getLife()) {
                    p++;
                }
                if (cache[p].getLife() <= cache[k].getLife()) {
                    break;
                }
                ProxyIp tmp = cache[p];
                cache[p] = cache[k];
                cache[k] = tmp;
                k = p;
            }
        }

        private void fixUp(int k) {
            while (k > 1) {
                int i = k >> 1;
                if (cache[i].getLife() >= cache[k].getLife()) {
                    break;
                }
                ProxyIp tmp = cache[i];cache[i]=cache[k];cache[k]=tmp;
                k = i;
            }
        }

        private void heapify() {
            for (int i = 0; i < size; i++) {
                fixDown(i);
            }
        }

        private void grow() {
            int oldCapacity = cache.length;
            if (oldCapacity > Integer.MAX_VALUE / 6) {
                // GC
                filter(proxyIp -> proxyIp.getLife() < 40);
                log.info("Cache GC old capacity:{}, new capacity: {}", oldCapacity, size);
            }
            if (isGrow()) {
                int newCapacity = oldCapacity << 1;
                if (newCapacity < oldCapacity) {
                    newCapacity = Integer.MAX_VALUE;
                }
                cache = Arrays.copyOf(cache, newCapacity);
            }
            log.info("缓存扩容，当前IP代理数{}，当前容量{}，扩容后容量{}", size, oldCapacity, cache.length);
        }

        private boolean isGrow() {
            return this.size >= cache.length * 0.75;
        }
    }



    private final PriorityQueueCache cache = new PriorityQueueCache();

    /**
     *  持久化入库
     * @param proxyIps  IP item
     */
    @Transactional(rollbackFor = Exception.class)
    public void saveToDB(List<ProxyIp> proxyIps) {
        for (ProxyIp proxyIp : proxyIps) {
            proxyIpService.save(proxyIp);
        }
    }

    /**
     * 添加到缓存中
     * @param proxyIps IP
     * @return 添加成功的个数
     */
    public int saveToCache(List<ProxyIp> proxyIps) {
        int res = 0;
        for (ProxyIp proxyIp : proxyIps) {
            proxyIpCheck.validProxyIp(proxyIp);
            if (proxyIp.getLife() > 0) {
                cache.add(proxyIp);
                res++;
            }
        }
        return res;
    }

    /**
     * 获取指定个数的IP（不会删除元素）
     * @param limit 个数
     * @return  List<ProxyIp>
     */
    public List<ProxyIp> queryProxyIp(int limit) {
        List<ProxyIp> dbProxyIps = null;
        if (limit > cache.size()) {
            // db 获取 ~
            dbProxyIps = proxyIpService.queryActiveProxyIp(limit);
            // 异步 校验、入缓存 ~
            List<ProxyIp> finalDbProxyIps = dbProxyIps;
            threadPoolExecutor.execute(() -> {
                for (ProxyIp proxyIp : finalDbProxyIps) {
                    // 移除失效的代理
                    proxyIpCheck.validProxyIp(proxyIp);
                    if (proxyIp.getLife() <= 0) {
                        finalDbProxyIps.remove(proxyIp);
                        proxyIpService.removeById(proxyIp.getId());
                    }
                }
                // 添加到缓存中
                cache.addAll(finalDbProxyIps);
            });
        }
        List<ProxyIp> cacheProxyIps = cache.peek(limit);
        return Collections.sortListMerge(
                cacheProxyIps,
                dbProxyIps,
                (o1, o2) -> o2.getLife() - o1.getLife()
        ).subList(0, limit);
    }

    /**
     * 条件清除
     * @param fun   里面尽量不要有阻塞代码
     */
    public void clear(Function<ProxyIp, Boolean> fun) {
        cache.filter(fun);
    }

    public ProxyIp poll() {
        return cache.poll();
    }

    public List<ProxyIp> pollAll() {
        cache.lock.lock();
        try {
            ArrayList<ProxyIp> res = new ArrayList<>(cache);
            cache.clear();
            return res;
        }finally {
            cache.lock.unlock();
        }
    }

    public void push(ProxyIp proxyIp) {
        cache.add(proxyIp);
    }
}
