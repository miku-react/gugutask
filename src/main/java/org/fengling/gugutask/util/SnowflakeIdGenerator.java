package org.fengling.gugutask.util;

import org.springframework.stereotype.Component;

import java.net.NetworkInterface;
import java.util.Enumeration;

@Component
public class SnowflakeIdGenerator {

    // 开始时间戳 (2024-01-01 00:00:00)
    private static final long EPOCH = 1704067200000L;

    private static final long MACHINE_ID_BITS = 5L;    // 机器ID所占的位数
    private static final long SEQUENCE_BITS = 12L;     // 序列号所占的位数

    private static final long MAX_MACHINE_ID = (1L << MACHINE_ID_BITS) - 1;   // 最大机器ID
    private static final long MAX_SEQUENCE = (1L << SEQUENCE_BITS) - 1;       // 最大序列号

    private static final long MACHINE_ID_SHIFT = SEQUENCE_BITS;               // 机器ID左移位数
    private static final long TIMESTAMP_LEFT_SHIFT = MACHINE_ID_SHIFT + MACHINE_ID_BITS; // 时间戳左移位数

    private final long machineId;       // 机器ID
    private long sequence = 0L;         // 序列号
    private long lastTimestamp = -1L;   // 上次生成ID的时间戳

    // 构造函数，初始化机器ID
    public SnowflakeIdGenerator() {
        this.machineId = generateMachineId();
    }

    // 自动生成机器ID的方法，基于MAC地址
    private long generateMachineId() {
        try {
            // 遍历所有网络接口，找到第一个非回环的MAC地址
            Enumeration<NetworkInterface> networkInterfaces = NetworkInterface.getNetworkInterfaces();
            while (networkInterfaces.hasMoreElements()) {
                NetworkInterface networkInterface = networkInterfaces.nextElement();
                if (networkInterface.isLoopback() || networkInterface.isVirtual() || !networkInterface.isUp()) {
                    continue;
                }
                byte[] mac = networkInterface.getHardwareAddress();
                if (mac != null) {
                    long macAddressAsLong = ((0x000000FF & (long) mac[mac.length - 1])
                            | (0x0000FF00 & (((long) mac[mac.length - 2]) << 8)));
                    return macAddressAsLong % (MAX_MACHINE_ID + 1);  // 生成机器ID
                }
            }
        } catch (Exception e) {
            throw new RuntimeException("Failed to get machine ID from MAC address", e);
        }
        throw new RuntimeException("Cannot find a valid MAC address for machine ID");
    }


    // 生成唯一ID的方法
    public synchronized long generateId() {
        long timestamp = System.currentTimeMillis();

        // 如果当前时间戳小于上一次生成ID的时间，抛出异常
        if (timestamp < lastTimestamp) {
            throw new RuntimeException("Clock moved backwards. Refusing to generate id.");
        }

        // 如果在同一毫秒内生成多个ID，则增加序列号
        if (lastTimestamp == timestamp) {
            sequence = (sequence + 1) & MAX_SEQUENCE;
            // 如果序列号达到最大值，等待下一毫秒
            if (sequence == 0) {
                timestamp = waitForNextMillis(lastTimestamp);
            }
        } else {
            sequence = 0;  // 重置序列号
        }

        lastTimestamp = timestamp;

        // 生成ID (时间戳左移，机器ID左移，序列号)
        return ((timestamp - EPOCH) << TIMESTAMP_LEFT_SHIFT)
                | (machineId << MACHINE_ID_SHIFT)
                | sequence;
    }

    // 等待直到下一毫秒
    private long waitForNextMillis(long lastTimestamp) {
        long timestamp = System.currentTimeMillis();
        while (timestamp <= lastTimestamp) {
            timestamp = System.currentTimeMillis();
        }
        return timestamp;
    }
}

