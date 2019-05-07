package com.amosnail.networktools.sample;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.amosnail.networktools.ping.Ping;
import com.amosnail.networktools.ping.PingResultInfo;
import com.amosnail.networktools.ping.PingStatsInfo;

import java.net.UnknownHostException;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
    }

    private void initView() {
        Ping ping = new Ping.PingBuilder()
                .setAddress("www.baidu.com")
                .setPingTimes(5)
                .setFutureListener(new Ping.PingFutureListener() {
                    @Override
                    public void onResult(PingResultInfo resultInfo) {
                        String result = "来自 " + resultInfo.getInetAddress().getHostAddress() + " 的回复:" +
                                "时间：" + resultInfo.getTimeTaken() + "ms \n";
                        Log.d("MainActivity Result:", result);
                    }

                    @Override
                    public void onFinished(PingStatsInfo statsInfo) {
                        Log.d("MainActivity Finished:",statsInfo.toString());
                    }

                    @Override
                    public void onError(Exception e) {
                        Log.d("MainActivity Error:", e.toString());
                    }
                })
                .setPingIntervalTime(0L)
                .build();
        try {
            ping.doPing();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
    }
}
