package com.github.kevin.learning.learning_7;

public class SnowflakeIdGenerator {
    // 起始时间戳（2023-01-01 00:00:00）
    private final static long START_TIMESTAMP = 1672531200000L;

    // 各部分位数
    private final static long SEQUENCE_BIT = 12;   // 序列号位数
    private final static long WORKER_BIT = 5;      // 机器ID位数
    private final static long DATACENTER_BIT = 5;  // 数据中心位数

    // 最大值计算（位运算优化）
    private final static long MAX_SEQUENCE = ~(-1L << SEQUENCE_BIT);
    private final static long MAX_WORKER_ID = ~(-1L << WORKER_BIT);
    private final static long MAX_DATACENTER_ID = ~(-1L << DATACENTER_BIT);

    // 移位偏移量
    private final static long WORKER_LEFT = SEQUENCE_BIT;
    private final static long DATACENTER_LEFT = SEQUENCE_BIT + WORKER_BIT;
    private final static long TIMESTAMP_LEFT = DATACENTER_LEFT + DATACENTER_BIT;

    private final long workerId;      // 机器ID
    private final long datacenterId;  // 数据中心ID
    private long sequence = 0L;      // 序列号
    private long lastTimestamp = -1L; // 上次生成时间

    public SnowflakeIdGenerator(long workerId, long datacenterId) {
        if (workerId > MAX_WORKER_ID || workerId < 0) {
            throw new IllegalArgumentException("Worker ID超出范围");
        }
        if (datacenterId > MAX_DATACENTER_ID || datacenterId < 0) {
            throw new IllegalArgumentException("Datacenter ID超出范围");
        }
        this.workerId = workerId;
        this.datacenterId = datacenterId;
    }

    public synchronized long nextId() {
        long currentTimestamp = System.currentTimeMillis();

        // 时钟回拨处理
        if (currentTimestamp < lastTimestamp) {
            throw new RuntimeException("时钟回拨，拒绝生成ID");
        }

        // 同一毫秒内生成
        if (lastTimestamp == currentTimestamp) {
            sequence = (sequence + 1) & MAX_SEQUENCE;
            if (sequence == 0) { // 当前毫秒序列号用尽，等待下一毫秒
                currentTimestamp = waitNextMillis(lastTimestamp);
            }
        } else {
            sequence = 0L; // 新毫秒重置序列号
        }

        lastTimestamp = currentTimestamp;

        // 拼接各部分数据
        return ((currentTimestamp - START_TIMESTAMP) << TIMESTAMP_LEFT)
                | (datacenterId << DATACENTER_LEFT)
                | (workerId << WORKER_LEFT)
                | sequence;
    }

    // 阻塞到下一毫秒
    private long waitNextMillis(long lastTimestamp) {
        long currentTimestamp = System.currentTimeMillis();
        while (currentTimestamp <= lastTimestamp) {
            currentTimestamp = System.currentTimeMillis();
        }
        return currentTimestamp;
    }

    // 测试
    public static void main(String[] args) {
        SnowflakeIdGenerator generator = new SnowflakeIdGenerator(1, 1);
        for (int i = 0; i < 10; i++) {
            System.out.println("生成ID: " + generator.nextId());
        }
    }
}