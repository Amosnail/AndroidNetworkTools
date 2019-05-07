package com.amosnail.networktools.ping;

import java.io.IOException;
import java.net.InetAddress;

/**
 * @author amosnail
 * @date 2019/5/7
 * @desc java ping tool
 */
public class PingJava {

    public PingJava() {
    }

    /**
     * Tries to reach this {@code InetAddress}.
     * <p>
     * This method first tries to use ICMP <i>(ICMP ECHO REQUEST)</i>,
     * falling back to a TCP connection on port 7 (Echo) of the remote host.
     * </p>
     *
     * @param inetAddress address to ping
     * @param pingOptions ping command options
     * @return the ping results
     */
    public static PingResultInfo ping(InetAddress inetAddress, PingOptions pingOptions) {
        PingResultInfo pingResultInfo = new PingResultInfo();
        pingResultInfo.setInetAddress(inetAddress);

        if (inetAddress == null) {
            pingResultInfo.setReachable(false);
            return pingResultInfo;
        }

        try {
            long startTime = System.nanoTime();
            final boolean reached = inetAddress.isReachable(null, pingOptions.getTimeToLive(), pingOptions.getTimeoutMillis());
            float timeTaken = (float) ((System.nanoTime() - startTime) * 1.0 / 1e6f);
            pingResultInfo.setTimeTaken(timeTaken);
            pingResultInfo.setReachable(reached);
            if (!reached) pingResultInfo.setErrorInfo("Timed Out");
        } catch (IOException e) {
            pingResultInfo.setReachable(false);
            pingResultInfo.setErrorInfo("IOException: " + e.getMessage());
        }
        return pingResultInfo;
    }
}
