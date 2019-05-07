package com.amosnail.networktools.ping;

import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;

import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * @author amosnail
 * @date 2019/5/7
 * @desc
 */
public class Ping {

    private final String address;
    private final PingOptions pingOptions;
    private final long pingIntervalTime;
    private final int pingTimes;
    private final PingFutureListener futureListener;

    private boolean cancelled = false;

    public Ping(PingBuilder builder) {
        this.address = builder.address;
        this.pingOptions = builder.pingOptions;
        this.pingIntervalTime = builder.pingIntervalTime;
        this.pingTimes = builder.pingTimes;
        this.futureListener = builder.futureListener;
    }

    public interface PingFutureListener {
        void onResult(PingResultInfo resultInfo);

        void onFinished(PingStatsInfo statsInfo);

        void onError(Exception e);
    }

    private HandlerThread pingHandlerThread;
    private Looper pingHandlerLooper;
    private Handler pingHandler;
    private Handler mainHandler;
    private final int STATUS_ON_RESULT = 0X1001;
    private final int STATUS_ON_FINISHED = 0X2001;
    private final int STATUS_ON_ERROR = 0X3001;

    public Ping doPing() throws UnknownHostException {

        String threadName = this.getClass().getSimpleName();
        pingHandlerThread = new HandlerThread("[" + threadName + "]" + "Thread");
        pingHandlerThread.start();
        pingHandlerLooper = pingHandlerThread.getLooper();
        mainHandler = new Handler(Looper.getMainLooper()) {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                if (null == futureListener) {
                    return;
                }
                switch (msg.what) {
                    case STATUS_ON_RESULT:
                        PingResultInfo pingResultInfo = (PingResultInfo) msg.obj;
                        futureListener.onResult(pingResultInfo);
                        break;
                    case STATUS_ON_FINISHED:
                        PingStatsInfo pingStatsInfo = (PingStatsInfo) msg.obj;
                        futureListener.onFinished(pingStatsInfo);
                        break;
                    case STATUS_ON_ERROR:
                        Exception e = (Exception) msg.obj;
                        futureListener.onError(e);
                        break;
                    default:
                        break;
                }
            }
        };
        pingHandler = new Handler(pingHandlerLooper) {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                try {
                    InetAddress inetAddress = parsesAddress(address);
                    doPing(inetAddress);
                } catch (UnknownHostException e) {
                    sendMessageToMain(STATUS_ON_ERROR,e);
                }
            }
        };
        pingHandler.sendEmptyMessage(0);
        return this;
    }

    private void doPing(InetAddress inetAddress) {
        Message msg = new Message();
        if (null == inetAddress) {
            sendMessageToMain(STATUS_ON_ERROR, new NullPointerException("Address is null"));
        }
        long pingCompletedTimes = 0L;
        long lostPacketTimes = 0L;
        float totalPingTimeTaken = 0F;
        float minPingTimeTaken = -1;
        float maxPingTimeTaken = -1;

        int times = pingTimes;
        // pingTimes == 0 is the case that we can continuous scanning
        while (pingTimes == 0 || times > 0) {
            PingResultInfo pingResultInfo = PingTools.doPing(inetAddress, pingOptions);
            sendMessageToMain(STATUS_ON_RESULT,pingResultInfo);

            // update ping stats info
            pingCompletedTimes++;

            if (pingResultInfo.hasErrorInfo()) {
                lostPacketTimes++;
            } else {
                float timeTaken = pingResultInfo.getTimeTaken();
                totalPingTimeTaken += timeTaken;
                if (maxPingTimeTaken == -1 || timeTaken > maxPingTimeTaken) {
                    maxPingTimeTaken = timeTaken;
                }
                if (minPingTimeTaken == -1 || timeTaken < minPingTimeTaken) {
                    minPingTimeTaken = timeTaken;
                }
            }
            times--;
            if (cancelled) {
                break;
            }
            if (pingIntervalTime > 0L) {
                try {
                    Thread.sleep(pingIntervalTime);
                } catch (InterruptedException e) {
                    sendMessageToMain(STATUS_ON_ERROR,e);
                    return;
                }
            }
        }
        PingStatsInfo pingStatsInfo = new PingStatsInfo(inetAddress,
                pingCompletedTimes,
                lostPacketTimes,
                totalPingTimeTaken,
                minPingTimeTaken,
                maxPingTimeTaken);
        sendMessageToMain(STATUS_ON_FINISHED,pingStatsInfo);
    }

    private void sendMessageToMain(int what, Object obj) {
        Message msg = new Message();
        msg.what = what;
        msg.obj = obj;
        mainHandler.sendMessage(msg);
    }

    public void setCancelled(boolean cancelled) {
        this.cancelled = cancelled;
        if (null != pingHandlerLooper) {
            pingHandlerLooper.quit();
        }
        if (null != pingHandler) {
            pingHandler.removeCallbacksAndMessages(null);
        }
        if (null != mainHandler) {
            mainHandler.removeCallbacksAndMessages(null);
        }
    }

    /**
     * Parses the addressString to an address
     *
     * @throws UnknownHostException if host cannot be found
     */
    private InetAddress parsesAddress(String address) throws UnknownHostException {
        return InetAddress.getByName(address);
    }

    public static class PingBuilder {
        private String address;
        private PingOptions pingOptions = new PingOptions();
        private long pingIntervalTime = 0L;
        private int pingTimes = 4;
        private PingFutureListener futureListener;

        public PingBuilder setAddress(String address) {
            this.address = address;
            return this;
        }

        public PingBuilder setPingOptions(PingOptions pingOptions) {
            this.pingOptions = pingOptions;
            return this;
        }

        public PingBuilder setPingIntervalTime(long pingIntervalTime) {
            this.pingIntervalTime = pingIntervalTime;
            return this;
        }

        public PingBuilder setPingTimes(int pingTimes) {
            this.pingTimes = pingTimes;
            return this;
        }

        public PingBuilder setFutureListener(PingFutureListener futureListener) {
            this.futureListener = futureListener;
            return this;
        }

        public Ping build() {
            return new Ping(this);
        }
    }
}
