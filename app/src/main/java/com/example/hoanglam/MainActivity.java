package com.example.hoanglam;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;

public class MainActivity extends AppCompatActivity {
    EditText edtname;
    Button btnnext;
    TextView tvtp,tvqg,tvnhietdo,tvtrangthai,tvdrop,tvcloud,tvwind,tvday;
    ImageView imgmay;
    String City = "";
    ImageButton btntimkiem;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Anhxa();
        GetCurrentWeatherData("Thái nguyên");
        btntimkiem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String city = edtname.getText().toString();
                if (city.equals("")){
                    City = "Thái nguyên";
                    GetCurrentWeatherData(City);
                }else {
                    City = city;
                }
                GetCurrentWeatherData(city);
            }
        });
        btnnext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String city = edtname.getText().toString();
                Intent intent = new Intent(MainActivity.this, Activity_DetailWeather.class);
                intent.putExtra("name", city);
                startActivity(intent);
            }
        });
    }

    public void GetCurrentWeatherData(String data){
        RequestQueue requestQueue = Volley.newRequestQueue(MainActivity.this);
        String url = "http://api.openweathermap.org/data/2.5/weather?q="+data+"&units=metric&appid=be8d3e323de722ff78208a7dbb2dcd6f";
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String day = jsonObject.getString("dt");
                            String name = jsonObject.getString("name");
                            tvtp.setText(name);

                            long l = Long.valueOf(day);
                            Date date = new Date(l*1000L);
                            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH-mm-ss   EEEE  dd-MM-yyyy");
                            String Day = simpleDateFormat.format(date);

                            tvday.setText(Day);
                            JSONArray jsonArrayWeather = jsonObject.getJSONArray("weather");
                            JSONObject jsonObjectWeather = jsonArrayWeather.getJSONObject(0);
                            String status = jsonObjectWeather.getString("main");
                            String icon = jsonObjectWeather.getString("icon");

                            Picasso.with(MainActivity.this).load("http://openweathermap.org/img/w/"+icon+".png").into(imgmay);
                            tvtrangthai.setText(status);

                            JSONObject jsonObjectMain = jsonObject.getJSONObject("main");
                            String nhietdo = jsonObjectMain.getString("temp");
                            String doam = jsonObjectMain.getString("humidity");
                            Double a = Double.valueOf(nhietdo);
                            String Nhietdo = String.valueOf(a.intValue());

                            tvnhietdo.setText(Nhietdo+"°C");
                            tvdrop.setText(doam+"%");

                            JSONObject jsonObjectWind = jsonObject.getJSONObject("wind");
                            String gio = jsonObjectWind.getString("speed");
                            tvwind.setText(gio+"m/s");

                            JSONObject jsonObjectClouds = jsonObject.getJSONObject("clouds");
                            String may = jsonObjectClouds.getString("all");
                            tvcloud.setText(may+"%");

                            JSONObject jsonObjectSys = jsonObject.getJSONObject("sys");
                            String country = jsonObjectSys.getString("country");
                            tvqg.setText(country);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                });
        requestQueue.add(stringRequest);
    }

    private void Anhxa() {
        edtname =findViewById(R.id.edtname);
        btnnext = findViewById(R.id.btnnext);
        btntimkiem = findViewById(R.id.btntimkiem);
        tvtp = findViewById(R.id.tvtp);
        tvqg = findViewById(R.id.tvqg);
        tvnhietdo = findViewById(R.id.tvnhietdo);
        tvtrangthai = findViewById(R.id.tvtrangthai);
        tvdrop = findViewById(R.id.tvdrop);
        tvcloud = findViewById(R.id.tvcloud);
        tvwind = findViewById(R.id.tvwind);
        tvday = findViewById(R.id.tvday);
        imgmay = findViewById(R.id.imgmay);
    }
}