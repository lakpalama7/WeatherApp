package com.bits.bytes.globalweather;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.net.Network;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    private Button btn;
    private TextView address, updated_at, temp_min, temp_max,temp,status,sunrise,sunset,wind, pressure, humidity, cord ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        address=findViewById(R.id.address);
        updated_at=findViewById(R.id.updated_at);
        temp_min=findViewById(R.id.temp_min);
        temp_max=findViewById(R.id.temp_max);
        temp=findViewById(R.id.temp);
        status=findViewById(R.id.status);
        sunrise=findViewById(R.id.sunrise);
        sunset=findViewById(R.id.sunset);
        wind=findViewById(R.id.wind);
        pressure=findViewById(R.id.pressure);
        humidity=findViewById(R.id.humidity);
        cord=findViewById(R.id.cord);

        new GetData().execute();

       /* btn = findViewById(R.id.click);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String loc = "1600 Amphitheatre Parkway, CA";

                Uri.Builder uri = new Uri.Builder();
                uri.scheme("geo")
                        .path("47.6,-122.3");

                Uri path = uri.build();

                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(path);


                if (intent.resolveActivity(getPackageManager()) != null) {
                    startActivity(intent);
                }
            }
        });
*/

    }

    class GetData extends AsyncTask<Void, Void, String> {

        @Override
        protected String doInBackground(Void... voids) {
            String r = "http://api.openweathermap.org/data/2.5/weather?q=chittoor&APPID=yourapikeys&units=metric";
            String s = null;
            try {
                URL url = new URL(r);
                HttpURLConnection con = (HttpURLConnection) url.openConnection();
                con.setRequestMethod("GET");
                InputStream in = con.getInputStream();
                BufferedReader br = new BufferedReader(new InputStreamReader(in));
                StringBuilder b = new StringBuilder();
                String line = null;
                while ((line = br.readLine()) != null) {
                    b.append(line);
                }
                s = b.toString();
                con.disconnect();


            } catch (ProtocolException e) {
                e.printStackTrace();
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return s;

        }


            @Override
            protected void onPostExecute (String s){
                super.onPostExecute(s);

                JSONObject json = null;
                try {
                    json = new JSONObject(s);
                    JSONArray weather = json.getJSONArray("weather");
                    JSONObject objw = weather.getJSONObject(0);
                    status.setText(objw.get("description").toString().toUpperCase());

                    JSONObject main = json.getJSONObject("main");
                    temp.setText(main.get("temp").toString() + "°C");
                    pressure.setText(main.get("pressure").toString() + "mb/hPa");
                    humidity.setText(main.get("humidity").toString() +"%");
                    temp_min.setText("Min Temp :"+main.get("temp_min").toString() +"°C");
                    temp_max.setText("Max Temp :"+main.get("temp_max").toString() + "°C");


                    JSONObject sys=json.getJSONObject("sys");


                    sunrise.setText(new SimpleDateFormat("hh:mm a",Locale.ENGLISH).format(new Date(sys.getLong("sunrise") * 1000)));
                    sunset.setText(new SimpleDateFormat("hh:mm a", Locale.ENGLISH).format(new Date(sys.getLong("sunset") * 1000)));
                    address.setText(json.get("name").toString());

                    JSONObject windobj=json.getJSONObject("wind");
                    wind.setText(windobj.get("speed").toString() + "km/h");
                    Long updatedAt=json.getLong("dt");

                    String updatedAtText = "Updated at: " + new SimpleDateFormat("dd/MM/yyyy hh:mm a", Locale.ENGLISH).format(new Date(updatedAt * 1000));
                    updated_at.setText(updatedAtText);

                    JSONObject coord=json.getJSONObject("coord");
                    double lon=coord.getDouble("lon");
                    double lat=coord.getDouble("lat");
                    cord.setText(lon +" " +lat);

                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        }

    }

