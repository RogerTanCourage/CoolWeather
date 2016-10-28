package com.example.kttan.coolweather.util;

/**
 * Created by kttan on 10/27/16.
 */
public interface HttpCallbackListener {
    void onFinish(String response);
    void onError(Exception e);
}
