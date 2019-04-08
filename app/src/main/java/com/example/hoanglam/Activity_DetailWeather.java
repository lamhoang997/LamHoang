package com.example.hoanglam;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class Activity_DetailWeather extends AppCompatActivity {

    String tenthanhpho = "";
    Button imgback;
    TextView textviewtp, txtday;
    ListView listview;
    ThoiTietAdapter thoiTietAdapter;
    ArrayList<Thoitiet> mangthoitiet;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity__detail_weather);
        Anhxa();
        Intent intent = getIntent();
        String city = intent.getStringExtra("name");
        Log.d("ketqua", "Dữ liệu truyền qua : "+city);
        if (city.equals("")){
            tenthanhpho = "Thái nguyên";
            Get7DaysData(tenthanhpho);
        }else {
            tenthanhpho = city;
            Get7DaysData(tenthanhpho);
        }
        imgback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Thoitiet thoitiet = mangthoitiet.get(position);
                Toast.makeText(Activity_DetailWeather.this, "Chào Ngày Mới"+" "+thoitiet.Day+"/"+thoitiet.MaxTemp+"°C", Toast.LENGTH_SHORT).show();
            }
        });
        /*lvview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                showinfoweathear(position);
            }
        });*/
    }
    /*private void  showinfoweathear(int position){
        Thoitiet thoitiet = mangthoitiet.get(position);
        Intent i = new Intent(this, InfoThoiTiet_Activity.class);
        startActivity(i);

    }*/

    private void Get7DaysData(String data) {
        String url = "http://api.openweathermap.org/data/2.5/forecast/daily?q="+data+"&units=metric&cnt=16&appid=be8d3e323de722ff78208a7dbb2dcd6f";
        RequestQueue requestQueue = Volley.newRequestQueue(Activity_DetailWeather.this);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            JSONObject jsonObjectCity = jsonObject.getJSONObject("city");
                            String name = jsonObjectCity.getString("name");
                            textviewtp.setText(name);

                            JSONArray jsonArrayList = jsonObject.getJSONArray("list");
                            for (int i = 0; i < jsonArrayList.length(); i++){
                                JSONObject jsonObjectList = jsonArrayList.getJSONObject(i);
                                String ngay = jsonObjectList.getString("dt");

                                long l = Long.valueOf(ngay);
                                Date date = new Date(l*1000L);
                                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("EEEE dd-MM-yyyy");
                                String Day = simpleDateFormat.format(date);

                                JSONArray jsonArrayWeather = jsonObjectList.getJSONArray("weather");
                                JSONObject jsonObjectWeather = jsonArrayWeather.getJSONObject(0);
                                String status = jsonObjectWeather.getString("description");
                                String icon = jsonObjectWeather.getString("icon");

                                JSONObject jsonObjectTemp = jsonObjectList.getJSONObject("temp");
                                String max = jsonObjectTemp.getString("max");
                                String min = jsonObjectTemp.getString("min");

                                Double a = Double.valueOf(max);
                                Double b = Double.valueOf(min);
                                String Tempmax = String.valueOf(a.intValue());
                                String Tempmin = String.valueOf(b.intValue());

                                mangthoitiet.add(new Thoitiet(Day,status,icon,Tempmax,Tempmin));

                            }
                            thoiTietAdapter.notifyDataSetChanged();
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
        imgback = findViewById(R.id.imgback);
        textviewtp = findViewById(R.id.textviewtp);
        txtday = findViewById(R.id.txtviewngay);
        listview = findViewById(R.id.listview);
        mangthoitiet = new ArrayList<Thoitiet>();
        thoiTietAdapter = new ThoiTietAdapter(Activity_DetailWeather.this, mangthoitiet);
        listview.setAdapter(thoiTietAdapter);
    }
}
