package com.example.kttan.coolweather.model;

/**
 * Created by kttan on 10/27/16.
 */
public class County {
    private int id;
    private String countyName;
    private String countyCode;
    private int cityId;

    public int getId() {return this.id;}
    public void setId(int id) {this.id = id;}

    public String getCountyName() {return this.countyName;}
    public void setCountyName(String countyName) {this.countyName = countyName;}

    public String getCountyCode() {return  this.countyCode;}
    public void setCountyCode(String countyCode) {this.countyCode = countyCode;}

    public int getCityId() {return this.cityId;}
    public void setCityId(int cityId) {this.cityId = cityId;}
}
