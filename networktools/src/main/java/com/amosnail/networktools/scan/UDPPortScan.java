package com.amosnail.networktools.scan;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketTimeoutException;

/**
 * @author amosnail
 * @date 2019/5/7
 * @desc
 */
public class UDPPortScan {

    public UDPPortScan() {
    }

    /**
     * Check if a port is open with UDP, note that this isn't reliable
     * as UDP will does not send ACKs
     *
     * @param ia            address to scan
     * @param portNo        port to scan
     * @param timeoutMillis timeout
     * @return true if port is open, false if not or unknown
     */
    public static boolean scanAddress(InetAddress ia, int portNo, int timeoutMillis) {

        try {
            byte[] bytes = new byte[128];
            DatagramPacket dp = new DatagramPacket(bytes, bytes.length);

            DatagramSocket ds = new DatagramSocket();
            ds.setSoTimeout(timeoutMillis);
            ds.connect(ia, portNo);
            ds.send(dp);
            ds.isConnected();
            ds.receive(dp);
            ds.close();

        } catch (SocketTimeoutException e) {
            return true;
        } catch (Exception ignore) {

        }

        return false;
    }
}
