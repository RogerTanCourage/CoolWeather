package com.example.kttan.coolweather.util;

import android.util.Log;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by kttan on 10/27/16.
 */
public class HttpUtil {
    public static  void sendHttpResponse(final String address,
                                         final HttpCallbackListener listener) {
        Log.d("sendHttpResponse", "start");
        new Thread(new Runnable() {
            @Override
            public void run() {
                HttpURLConnection connection = null;
                Log.d("sendHttpRespone", address);
                try {
                    Log.d("con","con");
                    URL url = new URL(address);
                    connection = (HttpURLConnection) url.openConnection();
                    connection.setRequestMethod("GET");
                    connection.setConnectTimeout(8000);
                    connection.setConnectTimeout(8000);

                    InputStream in = connection.getInputStream();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(in));

                    StringBuilder response = new StringBuilder();
                    String line;
                    while((line = reader.readLine()) != null){
                        response.append(line);
                    }

                    if(listener != null){
                        listener.onFinish(response.toString());
                    }
                    Log.d("httpSend",address);
                }catch (Exception e){
                    if(listener != null){
                        Log.e("call","error");
                        listener.onError(e);
                    }
                }finally {
                    if (connection != null)
                        connection.disconnect();
                }
            }

        }).start();


    }
}
