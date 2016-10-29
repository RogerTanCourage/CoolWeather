package com.example.kttan.coolweather.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.kttan.coolweather.R;
import com.example.kttan.coolweather.model.City;
import com.example.kttan.coolweather.model.CoolWeatherDB;
import com.example.kttan.coolweather.model.County;
import com.example.kttan.coolweather.model.Province;
import com.example.kttan.coolweather.util.HttpCallbackListener;
import com.example.kttan.coolweather.util.HttpUtil;
import com.example.kttan.coolweather.util.Utility;

import java.util.ArrayList;
import java.util.List;

public class ChooseAreaActivity extends Activity {

    public static  final int LEVEL_PROVINCE = 0;
    public static final int LEVEL_CITY = 1;
    public static final int LEVEL_COUNTY = 2;

    private ProgressDialog progressDialog;
    private TextView titleText;
    private ListView listView;
    private ArrayAdapter<String> adapter;

    private CoolWeatherDB coolWeatherDB;
    private List<String> dataList = new ArrayList<String>();

    private  List<Province> provinceList;
    private  List<City> cityList;
    private  List<County> countyList;

    private Province selectedProvince;
    private City selectedCity;
    private County selectedCounty;

    private int currentLevel;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        if(prefs.getBoolean("city_selected", false)){
            Intent intent = new Intent(this, WeatherActivity.class);
            startActivity(intent);
            finish();
            return;
        }

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.choose_area);
        listView = (ListView) findViewById(R.id.list_view);
        titleText = (TextView) findViewById(R.id.title_text);
        //dataList.add("test");
        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, dataList);

        listView.setAdapter(adapter);
        coolWeatherDB = CoolWeatherDB.getIntance(this);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int index, long l) {
                if (currentLevel == LEVEL_PROVINCE){
                    selectedProvince = provinceList.get(index);
                    Log.d("query","queryCityes");
                    queryCites();
                } else if(currentLevel == LEVEL_CITY){
                    selectedCity = cityList.get(index);
                    Log.d("query","queryCounties");
                    queryCounties();
                }else if(currentLevel == LEVEL_COUNTY){
                    String countyCode = countyList.get(index).getCountyCode();
                    Intent intent = new Intent(ChooseAreaActivity.this, WeatherActivity.class);
                    intent.putExtra("county_code",countyCode);
                    startActivity(intent);
                    finish();
                }
            }
        });
        Log.d("query","queryProvinces");
        queryProvinces();
    }

    private void queryProvinces() {
        provinceList = coolWeatherDB.loadProvince();
        if(provinceList.size() > 0){
            Log.d("queryProvince","found");
            dataList.clear();
            for(Province province : provinceList){
                dataList.add(province.getProvinceName());
            }
            adapter.notifyDataSetChanged();
            listView.setSelection(0);
            titleText.setText("china");
            currentLevel = LEVEL_PROVINCE;
        }else {
            Log.d("queryProvince","queryFromServer");
            queryFromServer(null, "province");
        }

    }

    private void queryCites() {
        cityList = coolWeatherDB.loadCites(selectedProvince.getId());
        if(cityList.size() > 0) {
            Log.d("queryCity","found");
            dataList.clear();
            for(City city : cityList) {
                dataList.add(city.getCityName());
            }
            adapter.notifyDataSetChanged();
            listView.setSelection(0);
            titleText.setText(selectedProvince.getProvinceName());
            currentLevel = LEVEL_CITY;
        }else {
            Log.d("queryCity","queryFromServer");
            queryFromServer(selectedProvince.getProvinceCode(), "city");
        }
    }

    private void queryCounties() {
        countyList = coolWeatherDB.loadCounties(selectedCity.getId());
        if(countyList.size() > 0){
            Log.d("queryCounty", "found");
            dataList.clear();
            for(County county : countyList){
                dataList.add(county.getCountyName());
            }
            adapter.notifyDataSetInvalidated();
            listView.setSelection(0);
            titleText.setText(selectedCity.getCityName());
            currentLevel = LEVEL_COUNTY;

        }else {
            Log.d("queryCounty", "queryFromServer");
            queryFromServer(selectedCity.getCityCode(), "county");
        }
    }


    private void queryFromServer(final String code, final String type) {

        String address = "http://www.weather.com.cn/data/list3/city.xml";
        if(code != null){

            address = "http://www.weather.com.cn/data/list3/city" + code +".xml";
        }else {
            address = "http://www.weather.com.cn/data/list3/city.xml";
        }
        Log.d("query from server",code +" "+type);

        showProgressDialog();
        HttpUtil.sendHttpResponse(address, new HttpCallbackListener() {
            @Override
            public void onFinish(String response) {
                boolean result = false;
                if("province".equals(type)){
                    result = Utility.handleProvincesResponse(coolWeatherDB,
                            response);
                }else if("city".equals(type)){
                    result = Utility.handleCitesResponse(coolWeatherDB,
                            response,selectedProvince.getId());
                }else {
                    result = Utility.handCountiesResponse(coolWeatherDB,
                            response, selectedCity.getId());
                }

                if(result) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            closeProgressDialog();
                            if("province".equals(type)){
                                queryProvinces();
                            }else if("city".equals(type)) {
                                queryCites();
                            }else if("county".equals(type)){
                                queryCounties();
                            }
                        }
                    });
                }
            }

            @Override
            public void onError(Exception e) {


                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        closeProgressDialog();
                        Toast.makeText(ChooseAreaActivity.this, "load failure", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }

    private void showProgressDialog() {
        if(progressDialog == null){
            progressDialog = new ProgressDialog(this);
            progressDialog.setMessage("loading...");
            progressDialog.setCanceledOnTouchOutside(false);
        }
        progressDialog.show();
    }
    private void closeProgressDialog() {
        if(progressDialog != null){
            progressDialog.dismiss();
        }
    }

    @Override
    public void onBackPressed() {
        if(currentLevel == LEVEL_COUNTY)
            queryCites();
        else if(currentLevel == LEVEL_CITY)
            queryProvinces();
        else
            finish();
    }
}
