package com.amosnail.networktools.ping;

/**
 * @author amosnail
 * @date 2019/5/7
 * @desc
 */
public class PingOptions {
    /**
     * 超时时间
     */
    private int timeoutMillis;
    /**
     * 指定IP包被路由器丢弃之前允许通过的最大网段数量
     */
    private int timeToLive;

    public PingOptions() {
        this.timeoutMillis = 5000;
        this.timeToLive = 128;
    }

    public int getTimeoutMillis() {
        return timeoutMillis;
    }

    public PingOptions setTimeoutMillis(int timeoutMillis) {
        this.timeoutMillis = timeoutMillis;
        return this;
    }

    public int getTimeToLive() {
        return timeToLive;
    }

    public PingOptions setTimeToLive(int timeToLive) {
        this.timeToLive = timeToLive;
        return this;
    }
}
