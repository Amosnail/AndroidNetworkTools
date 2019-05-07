package com.amosnail.networktools.ping;

import com.amosnail.networktools.ip.IPTools;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetAddress;

/**
 * @author amosnail
 * @date 2019/5/7
 * @desc Os ping tool
 */
public class PingNative {

    public PingNative() {
    }

    /**
     * analysisCurrentPingResult interprets the text result of a Linux activity_ping command
     * <p>
     * Set pingError on error and return null
     * <p>
     * http://en.wikipedia.org/wiki/Ping
     * <p>
     * PING 127.0.0.1 (127.0.0.1) 56(84) bytes of data.
     * 64 bytes from 127.0.0.1: icmp_seq=1 ttl=64 time=0.251 ms
     * 64 bytes from 127.0.0.1: icmp_seq=2 ttl=64 time=0.294 ms
     * 64 bytes from 127.0.0.1: icmp_seq=3 ttl=64 time=0.295 ms
     * 64 bytes from 127.0.0.1: icmp_seq=4 ttl=64 time=0.300 ms
     * <p>
     * --- 127.0.0.1 activity_ping statistics ---
     * 4 packets transmitted, 4 received, 0% packet loss, time 0ms
     * rtt min/avg/max/mdev = 0.251/0.285/0.300/0.019 ms
     * <p>
     * PING 192.168.0.2 (192.168.0.2) 56(84) bytes of data.
     * <p>
     * --- 192.168.0.2 activity_ping statistics ---
     * 1 packets transmitted, 0 received, 100% packet loss, time 0ms
     * <p>
     * # activity_ping 321321.
     * activity_ping: unknown host 321321.
     * <p>
     * 1. Check if output contains 0% packet loss : Branch to success - Get stats
     * 2. Check if output contains 100% packet loss : Branch to fail - No stats
     * 3. Check if output contains 25% packet loss : Branch to partial success - Get stats
     * 4. Check if output contains "unknown host"
     *
     * @param pingResultInfo - the current ping result
     * @param resultData     - result from ping command
     * @return The ping analyzing result
     */
    public static PingResultInfo analysisCurrentPingResult(PingResultInfo pingResultInfo, String resultData) {
        String pingError;
        if (resultData.contains("0% packet loss")) {
            int start = resultData.indexOf("/mdev = ");
            int end = resultData.indexOf(" ms\n", start);
            pingResultInfo.setResult(resultData);
            if (start == -1 || end == -1) {
                pingError = "Error: " + resultData;
            } else {
                resultData = resultData.substring(start + 8, end);
                String stats[] = resultData.split("/");
                pingResultInfo.setReachable(true);
                pingResultInfo.setTimeTaken(Float.parseFloat(stats[1]));
                return pingResultInfo;
            }
        } else if (resultData.contains("100% packet loss")) {
            pingError = "100% packet loss";
        } else if (resultData.contains("% packet loss")) {
            pingError = "partial packet loss";
        } else if (resultData.contains("unknown host")) {
            pingError = "unknown host";
        } else {
            pingError = "unknown error in analysisCurrentPingResult";
        }
        pingResultInfo.setErrorInfo(pingError);
        return pingResultInfo;
    }

    /**
     * Nattive ping
     *
     * @param inetAddress ip address
     * @param pingOptions ping config
     * @return
     * @throws IOException
     * @throws InterruptedException
     */
    public static PingResultInfo ping(InetAddress inetAddress, PingOptions pingOptions) throws IOException, InterruptedException {
        PingResultInfo pingResultInfo = new PingResultInfo();
        pingResultInfo.setInetAddress(inetAddress);

        if (pingResultInfo == null) {
            pingResultInfo.setReachable(false);
            return pingResultInfo;
        }
        StringBuilder echo = new StringBuilder();
        Runtime runtime = Runtime.getRuntime();

        int timeoutSeconds = Math.max(pingOptions.getTimeoutMillis() / 1000, 1);
        int ttl = Math.max(pingOptions.getTimeToLive(), 1);

        String address = inetAddress.getHostAddress();
        String pingCommand = "ping";

        if (address != null) {
            if (IPTools.isIPv6Address(address)) {
                // If we detect this is a ipv6 address, change the to the ping6 binary
                pingCommand = "ping6";
            }
        } else {
            // Not sure if getHostAddress ever returns null, but if it does, use the hostname as a fallback
            address = inetAddress.getHostName();
        }

        Process proc = runtime.exec(pingCommand + " -c 1 -W " + timeoutSeconds + " -t " + ttl + " " + address);
        proc.waitFor();
        int exit = proc.exitValue();
        String pingError;
        switch (exit) {
            case 0:
                InputStreamReader reader = new InputStreamReader(proc.getInputStream());
                BufferedReader buffer = new BufferedReader(reader);
                String line;
                while ((line = buffer.readLine()) != null) {
                    echo.append(line).append("\n");
                }
                return analysisCurrentPingResult(pingResultInfo, echo.toString());
            case 1:
                pingError = "failed, exit = 1";
                break;
            default:
                pingError = "error, exit = 2";
                break;
        }
        pingResultInfo.setErrorInfo(pingError);

        return pingResultInfo;
    }


}
