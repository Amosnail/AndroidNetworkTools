package com.amosnail.networktools.ping;

import java.net.InetAddress;

/**
 * @author amosnail
 * @date 2019/5/7
 * @desc ping执行后的结果统计类
 */
public class PingStatsInfo {
    /**
     * IP地址
     */
    private final InetAddress inetAddress;
    /**
     * ping的次数
     */
    private final long pingTimes;
    /**
     * 数据包丢失次数
     */
    private final long packetLostTimes;
    /**
     * 数据包丢失次率
     */
    private final float packetLostRate;
    /**
     * ping ${pingTimes} 次后的平均所用时间
     */
    private final float avgTimeTaken;
    /**
     * ping ${pingTimes} 次后的最少所用时间
     */
    private final float minTimeTaken;
    /**
     * ping ${pingTimes} 次后的最多所用时间
     */
    private final float maxTimeTaken;
    /**
     * 是否能连通
     */
    private final boolean isReachable;

    public PingStatsInfo(InetAddress inetAddress,
                         long pingTimes,
                         long packetLostTimes,
                         float packetLostRate,
                         float avgTimeTaken,
                         float minTimeTaken,
                         float maxTimeTaken,
                         boolean isReachable) {
        this.inetAddress = inetAddress;
        this.pingTimes = pingTimes;
        this.packetLostTimes = packetLostTimes;
        this.packetLostRate = packetLostRate;
        this.avgTimeTaken = avgTimeTaken;
        this.minTimeTaken = minTimeTaken;
        this.maxTimeTaken = maxTimeTaken;
        this.isReachable = isReachable;
    }

    public PingStatsInfo(InetAddress inetAddress,
                         long pingTimes,
                         long packetLostTimes,
                         float totalTimeTaken,
                         float minTimeTaken,
                         float maxTimeTaken) {
        this.inetAddress = inetAddress;
        this.pingTimes = pingTimes;
        this.packetLostTimes = packetLostTimes;
        this.packetLostRate = packetLostTimes / pingTimes;
        this.avgTimeTaken = totalTimeTaken / pingTimes;
        this.minTimeTaken = minTimeTaken;
        this.maxTimeTaken = maxTimeTaken;
        this.isReachable = pingTimes - packetLostTimes > 0;
    }

    public InetAddress getInetAddress() {
        return inetAddress;
    }

    public long getPingTimes() {
        return pingTimes;
    }

    public long getPacketLostTimes() {
        return packetLostTimes;
    }

    public float getPacketLostRate() {
        return packetLostRate;
    }

    public float getAvgTimeTaken() {
        return avgTimeTaken;
    }

    public float getMinTimeTaken() {
        return minTimeTaken;
    }

    public float getMaxTimeTaken() {
        return maxTimeTaken;
    }

    public boolean isReachable() {
        return isReachable;
    }

    @Override
    public String toString() {
        return "PingStatsInfo{" +
                "inetAddress=" + inetAddress +
                ", pingTimes=" + pingTimes +
                ", packetLostTimes=" + packetLostTimes +
                ", packetLostRate=" + packetLostRate +
                ", avgTimeTaken=" + avgTimeTaken +
                ", minTimeTaken=" + minTimeTaken +
                ", maxTimeTaken=" + maxTimeTaken +
                ", isReachable=" + isReachable +
                '}';
    }
}
