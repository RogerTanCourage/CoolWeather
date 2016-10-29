package com.example.kttan.coolweather.util;

import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.icu.text.SimpleDateFormat;
import android.preference.PreferenceManager;
import android.text.TextUtils;

import com.example.kttan.coolweather.model.City;
import com.example.kttan.coolweather.model.CoolWeatherDB;
import com.example.kttan.coolweather.model.County;
import com.example.kttan.coolweather.model.Province;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Date;
import java.util.Locale;

import static java.util.Locale.CHINA;

/**
 * Created by kttan on 10/27/16.
 */
public class Utility {
    public synchronized static boolean handleProvincesResponse(CoolWeatherDB coolWeatherDB,
                                                              String response) {
        if(response != null){
            String[] allProvinces = response.split(",");
            if(allProvinces != null && allProvinces.length > 0){
                for(String p: allProvinces){
                    String[] array = p.split("\\|");
                    Province province = new Province();
                    province.setProvinceCode(array[0]);
                    province.setProvinceName(array[1]);

                    coolWeatherDB.saveProvince(province);
                }
            }
            return true;
        }
        return false;
    }

    public static boolean handleCitesResponse(CoolWeatherDB coolWeatherDB,
                                              String response,int provinceId){
        if(!TextUtils.isEmpty(response)){
            String[] allCities = response.split(",");
            if (allCities != null && allCities.length > 0){
                for(String c : allCities){
                    String[] array = c.split("\\|");
                    City city = new City();

                    city.setCityCode(array[0]);
                    city.setCityName(array[1]);
                    city.setProvinceId(provinceId);
                    coolWeatherDB.saveCity(city);
                }

            }
            return true;
        }
        return false;
    }

    public static boolean handCountiesResponse(CoolWeatherDB coolWeatherDB,
                                               String response,int cityId){
        if(!TextUtils.isEmpty(response)){
            String[] allCounties = response.split(",");
            if(allCounties != null && allCounties.length > 0){
                for(String c : allCounties){
                    String[] array = c.split("\\|");
                    County county = new County();
                    county.setCountyCode(array[0]);
                    county.setCountyName(array[1]);
                    county.setCityId(cityId);

                    coolWeatherDB.saveCounty(county);
                }
            }
            return true;
        }
        return false;
    }


    /**
     * 解析服务器返回的JSON天气数据，并存储到本地
     */
    public static void handleWeatherResponse(Context context, String response){

        try {
            JSONObject jsonObject = new JSONObject(response);
            JSONObject weatherInfo = jsonObject.getJSONObject("weatherinfo");
            String cityName = weatherInfo.getString("city");
            String weatherCode = weatherInfo.getString("cityid");
            String temp1 = weatherInfo.getString("temp1");
            String temp2 = weatherInfo.getString("temp2");
            String weatherDesp = weatherInfo.getString("weather");
            String publishTime = weatherInfo.getString("ptime");
            saveWeatheInfo(context,cityName,weatherCode,temp1,temp2,weatherDesp,publishTime);

        }catch (JSONException e){
            e.printStackTrace();
        }
    }
    /**
     * 将服务器返回的天气数据存储到SHAREDPRE中
     */
    public static void saveWeatheInfo(Context context, String cityName,
                                      String weatherCode, String temp1,
                                      String temp2, String weatherDesp,
                                      String publishTime){

        // ---------VERSION
       // SimpleDateFormat sdf = new SimpleDateFormat("yyyy年M月d日", CHINA);


        SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(context).edit();
        editor.putBoolean("city_selected",true);
        editor.putString("city_name",cityName);
        editor.putString("weather_code",weatherCode);
        editor.putString("temp1", temp1);
        editor.putString("temp2", temp2);
        editor.putString("weather_desp",weatherDesp);
        editor.putString("publish_time",publishTime);
       // editor.putString("current_date",sdf.format(new Date()));
        editor.putString("current_date","20161029");

        editor.commit();

    }

}
