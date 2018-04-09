package com.whl.cornerstone.util;

import com.google.common.base.Preconditions;

/**
 * Created by whling on 2018/4/10.
 */
public class IdWorkerUtils {

    private static volatile IdWorkerUtils instance;
    public static final long WORKER_ID_BITS = 10L;
    public static final long SEQUENCE_BITS = 12L;
    public static final long BEGIN_TIME = 1320681600000L;
    public final long WORKER_ID;
    private long lastTimeDelta = System.currentTimeMillis() - 1320681600000L;
    private long sequence = 0L;

    public static IdWorkerUtils getInstance() {
        return getInstance(0L);
    }

    public static IdWorkerUtils getInstance(long workerId) {
        if (instance == null) {
            synchronized (IdWorkerUtils.class) {
                if (instance == null) {
                    instance = new IdWorkerUtils(workerId);
                }
            }
        }
        return instance;
    }

    private IdWorkerUtils(long workerId) {
        int maxId = 1024;
        Preconditions.checkArgument((workerId < maxId) && (workerId >= 0L), "当前workId=%s, 不在[0,%s)中!", new Object[]{
                Long.valueOf(workerId),
                Integer.valueOf(maxId)});
        this.WORKER_ID = workerId;
    }

    public synchronized long nextId() throws InterruptedException {
        if (this.sequence >= 4096L) {
            while (true) {
                long newTimeDelta = System.currentTimeMillis() - 1320681600000L;
                long delta = this.lastTimeDelta - newTimeDelta;
                if (delta == 0L)
                    continue;
                if (delta < 0L) {
                    this.sequence = 0L;
                    this.lastTimeDelta = newTimeDelta;
                    break;
                }
                if (delta > 2000L) {
                    throw new RuntimeException("ID生成器检测到时钟倒退大于 2000 ms!");
                }
                Thread.sleep(delta);
            }
        }
        return this.lastTimeDelta << 22 | this.WORKER_ID << 12 | this.sequence++;
    }

    //public static void main(String[] strings)
    //        throws Exception {
    //    Map map = new ConcurrentHashMap();
    //    IdWorkerUtils idWorkerUtil = getInstance(1023L);
    //    ExecutorService executorService = Executors.newFixedThreadPool(200);
    //    long starttime = System.currentTimeMillis();
    //    AtomicInteger integer = new AtomicInteger(0);
    //    for (int i = 0; i < 200; i++)
    //        executorService.submit(new Runnable(map, integer, starttime) {
    //            public void run() {
    //                while (true)
    //                    try {
    //                        String id = GuidUtils.getNextUid("");
    //                        if (map.containsKey(id)) {
    //                            System.out.println(id + "exists");
    //                        } else {
    //                            map.put(id, id);
    //                            map.getAndIncrement();
    //                        }
    //                        if (integer.get() == 100000) {
    //                            System.out.println(System.currentTimeMillis() - this.val$starttime);
    //                        }
    //
    //                    } catch (Exception e) {
    //                        e.printStackTrace();
    //                    }
    //            }
    //        });
    //}

}
