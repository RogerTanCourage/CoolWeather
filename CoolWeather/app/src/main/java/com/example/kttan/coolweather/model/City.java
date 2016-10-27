package com.example.kttan.coolweather.model;

/**
 * Created by kttan on 10/27/16.
 */
public class City {
    private int id;
    private String cityName;
    private String cityCode;
    private int provinceId;

    public int getId(){return  this.id;}
    public void setId(int id) {this.id = id;}

    public String getCityName() {return this.cityName;}
    public void setCityName(String cityName) {this.cityName = cityName;}

    public String getCityCode() {return cityCode;}
    public void setCityCode(String cityCode) {this.cityCode = cityCode;}

    public int getProvinceId() {return this.provinceId;}
    public void setProvinceId(int provinceId) {this.provinceId = provinceId;}
}
