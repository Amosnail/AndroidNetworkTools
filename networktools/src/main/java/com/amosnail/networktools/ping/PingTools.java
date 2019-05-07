package com.amosnail.networktools.ping;

import java.io.IOException;
import java.net.InetAddress;

/**
 * @author amosnail
 * @date 2019/5/7
 * @desc
 */
public class PingTools {
    public PingTools() {
    }

    /**
     * Perform a ping using the native ping tool and fall back to using java echo request
     * on failure.
     *
     * @param inetAddress address to ping
     * @param pingOptions ping command options
     * @return the ping results
     */
    public static PingResultInfo doPing(InetAddress inetAddress, PingOptions pingOptions) {

        // Try native ping first
        try {
            return PingTools.doNativePing(inetAddress, pingOptions);
        } catch (InterruptedException e) {
            PingResultInfo pingResultInfo = new PingResultInfo();
            pingResultInfo.setInetAddress(inetAddress);
            pingResultInfo.setReachable(false);
            pingResultInfo.setErrorInfo("Interrupted");
            return pingResultInfo;
        } catch (Exception ignored) {
        }

        // Fallback to java based ping
        return PingTools.doJavaPing(inetAddress, pingOptions);
    }


    /**
     * Perform a ping using the native ping binary
     *
     * @param inetAddress address to ping
     * @param pingOptions ping command options
     * @return the ping results
     * @throws IOException          IO error running ping command
     * @throws InterruptedException thread interrupt
     */
    public static PingResultInfo doNativePing(InetAddress inetAddress, PingOptions pingOptions) throws IOException, InterruptedException {
        return PingNative.ping(inetAddress, pingOptions);
    }

    /**
     * Tries to reach this {@code InetAddress}. This method first tries to use
     * ICMP <i>(ICMP ECHO REQUEST)</i>, falling back to a TCP connection
     * on port 7 (Echo) of the remote host.
     *
     * @param inetAddress address to ping
     * @param pingOptions ping command options
     * @return the ping results
     */
    public static PingResultInfo doJavaPing(InetAddress inetAddress, PingOptions pingOptions) {
        return PingJava.ping(inetAddress, pingOptions);
    }
}
