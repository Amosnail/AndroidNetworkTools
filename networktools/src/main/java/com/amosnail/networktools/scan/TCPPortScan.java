package com.amosnail.networktools.scan;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;

/**
 * @author amosnail
 * @date 2019/5/7
 * @desc
 */
public class TCPPortScan {

    public TCPPortScan() {
    }

    /**
     * Check if a port is open with TCP
     *
     * @param ia            address to scan
     * @param portNo        port to scan
     * @param timeoutMillis timeout
     * @return true if port is open, false if not or unknown
     */
    public static boolean scanAddress(InetAddress ia, int portNo, int timeoutMillis) {

        Socket s = null;
        try {
            s = new Socket();
            s.connect(new InetSocketAddress(ia, portNo), timeoutMillis);
            return true;
        } catch (IOException e) {
            // Don't log anything as we are expecting a lot of these from closed ports.
        } finally {
            if (s != null) {
                try {
                    s.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return false;
    }
}
