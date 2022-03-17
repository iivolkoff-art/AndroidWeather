package com.example.pogoda;

import android.os.AsyncTask;
import android.view.View;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class MainActivity extends AppCompatActivity {
    private EditText PlainTextCity;
    private Button Button;
    private TextView ResultInfo;
    private TextView ResultInfo2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        PlainTextCity = findViewById(R.id.PlainTextCity);
        Button = findViewById(R.id.button);
        ResultInfo = findViewById(R.id.ResultInfo);
        ResultInfo2 = findViewById(R.id.ResultInfo2);


        Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(PlainTextCity.getText().toString().equals(""))
                {
                    Toast.makeText(MainActivity.this, R.string.UserInput, Toast.LENGTH_LONG).show();
                }
                else {
                    String City = PlainTextCity.getText().toString();
                    String Key = "7084821fa41a4110500afb7192c476c8";
                    String Url = "https://api.openweathermap.org/data/2.5/weather?q=" + City + "&appid=" + Key + "&units=metric&lang=ru";

                    new GetURLData().execute(Url);
                }
            }
        });
    }


    private class GetURLData extends AsyncTask<String, String, String>{
        protected void onPreExcute(){
            super.onPreExecute();
            ResultInfo.setText("Загрузка...");
            ResultInfo2.setText("Загрузка...");
        }

        @Override
        protected String doInBackground(String... strings) {
            HttpURLConnection Connection = null;
            BufferedReader Reader = null;

            try {
                URL url = new URL(strings[0]);
                Connection = (HttpURLConnection) url.openConnection();
                Connection.connect();

                InputStream stream = Connection.getInputStream();
                Reader = new BufferedReader(new InputStreamReader(stream));

                StringBuffer buffer = new StringBuffer();
                String line = "";

                while((line = Reader.readLine())!= null) {
                    buffer.append(line).append("\n");

                    return buffer.toString();
                }
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            finally{
                if(Connection != null){
                    Connection.disconnect();
                }
                if(Reader != null){
                    try {
                        Reader.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(String result){
            super.onPostExecute(result);

            try {
                JSONObject jsonObject= new JSONObject((result));
                ResultInfo.setText("Температура:" + jsonObject.getJSONObject("main").getDouble("temp") + "°");
                ResultInfo2.setText(jsonObject.getJSONArray("weather").getJSONObject(0).getString("description") + "");
               // ImageWeather.setImageResource(jsonObject.getJSONArray("weather").getJSONObject(0).getString("icon"));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}