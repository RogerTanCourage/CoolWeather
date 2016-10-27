package com.example.kttan.coolweather.model;

/**
 * Created by kttan on 10/27/16.
 */
public class Province {
    private  int id;
    private String provinceName;
    private String provinceCode;

    public int getId() {
        return id;
    }
    public void setId(int id) {this.id = id;}

    public String getProvinceName() {return this.provinceName;}
    public void setProvinceName(String provinceName) {this.provinceName = provinceName;}

    public String getProvinceCode() {return this.provinceCode;}
    public void setProvinceCode(String provinceCode) {this.provinceCode = provinceCode;}
}
