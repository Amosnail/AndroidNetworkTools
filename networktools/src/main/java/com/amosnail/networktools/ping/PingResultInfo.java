package com.amosnail.networktools.ping;


import java.net.InetAddress;

/**
 * @author amosnail
 * @date 2019/5/7
 * @desc ping后返回信息的包装类
 */
public class PingResultInfo {
    /**
     * IP地址
     */
    private InetAddress inetAddress;
    /**
     * ping所用时间
     */
    private float timeTaken;
    /**
     * 是否能连通
     */
    private boolean isReachable;
    /**
     * 返回的信息内容
     */
    private String result;
    /**
     * ping失败后的提取的提示信息
     */
    private String errorInfo;

    public InetAddress getInetAddress() {
        return inetAddress;
    }

    public PingResultInfo setInetAddress(InetAddress inetAddress) {
        this.inetAddress = inetAddress;
        return this;
    }

    public float getTimeTaken() {
        return timeTaken;
    }

    public PingResultInfo setTimeTaken(float timeTaken) {
        this.timeTaken = timeTaken;
        return this;
    }

    public boolean isReachable() {
        return isReachable;
    }

    public PingResultInfo setReachable(boolean reachable) {
        isReachable = reachable;
        return this;
    }

    public String getResult() {
        return result;
    }

    public PingResultInfo setResult(String result) {
        this.result = result;
        return this;
    }

    public String getErrorInfo() {
        return errorInfo;
    }

    public PingResultInfo setErrorInfo(String errorInfo) {
        this.errorInfo = errorInfo;
        return this;
    }

    public boolean hasErrorInfo() {
        return this.errorInfo != null;
    }

    @Override
    public String toString() {
        return "PingResultInfo{" +
                "inetAddress=" + inetAddress +
                ", timeTaken=" + timeTaken +
                ", isReachable=" + isReachable +
                ", result='" + result + '\'' +
                ", errorInfo='" + errorInfo + '\'' +
                '}';
    }
}
