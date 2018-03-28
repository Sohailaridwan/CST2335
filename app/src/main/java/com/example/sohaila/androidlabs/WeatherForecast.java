package com.example.sohaila.androidlabs;


import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.util.Xml;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class WeatherForecast extends Activity {
    protected static final String TAG = "WeatherForecast";
    private ProgressBar progressBar;
    private TextView minTemp;
    private TextView maxTemp;
    private TextView curTemp;
    private TextView windSpeed;
    private ImageView weatherImage;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather_forecast);
        progressBar = (ProgressBar)findViewById(R.id.progressBar2);
        progressBar.setVisibility(View.VISIBLE);
        minTemp = (TextView)findViewById(R.id.min_temperature);
        maxTemp = (TextView)findViewById(R.id.max_temperature);
        curTemp = (TextView)findViewById(R.id.current_temperature);
        windSpeed = (TextView)findViewById(R.id.wind_speed);
        weatherImage = (ImageView)findViewById(R.id.imageView2);

        ForecastQuery forecast = new ForecastQuery();
        String  url = "http://api.openweathermap.org/data/2.5/weather?q=ottawa,ca&APPID=d99666875e0e51521f0040a3d97d0f6a&mode=xml&units=metric";
        forecast.execute(url);
    }

    public boolean isFileExists(String fileName){
        File file = getBaseContext().getFileStreamPath(fileName);
        Log.i(TAG, file.toString());

        return file.exists();

    }

    public Bitmap getImage(URL url){
        Log.i(TAG, "In getImage");
        HttpURLConnection connection = null;
        try{
            connection = (HttpURLConnection)url.openConnection();
            int responseCode = connection.getResponseCode();
            if(responseCode == 200){
                Log.i(TAG, "downloading image");
                Bitmap bm = BitmapFactory.decodeStream(connection.getInputStream());
                return bm;

            }
            else{
                return null;
            }

        }catch(MalformedURLException e){
            e.printStackTrace();
            return null;
        }catch (IOException e) {
            e.printStackTrace();
            return null;
        } finally {
            if (connection != null){
                connection.disconnect();
            }
        }
    }

    public class ForecastQuery extends AsyncTask<String, Integer, String> {

        String min;
        String max;
        String winds;
        String currentTemp;
        String iconName;
        Bitmap icon;

        @Override
        protected void onPreExecute(){
            Log.i(TAG,"Starting AsyncTask");
            progressBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected String doInBackground(String... params) {
            Log.i(TAG, "In doInBackgroud");
            // HttpURLConnection connection = null;
            try{
                URL dataUrl = new URL(params[0]);
                HttpURLConnection connection = (HttpURLConnection)dataUrl.openConnection();
                connection.setDoOutput(true);
                connection.setRequestMethod("GET");
                connection.connect();
                int status = connection.getResponseCode();
                Log.d("connection", "status " + status);
                InputStream is = connection.getInputStream();
                XmlPullParser parser = Xml.newPullParser();
                parser.setInput(is, null);

                while(parser.next() != XmlPullParser.END_DOCUMENT){
                    if(parser.getEventType() != XmlPullParser.START_TAG){
                        continue;

                    }
                    if(parser.getName().equals("temperature")){
                        currentTemp = parser.getAttributeValue(null, "value");
                        publishProgress(25);
                        min = parser.getAttributeValue(null,"min");
                        publishProgress(50);
                        max = parser.getAttributeValue(null, "max");
                        publishProgress(75);

                    }
                    if(parser.getName().equals("speed")){
                        winds = parser.getAttributeValue(null, "value");
                        publishProgress(75);
                    }
                    if(parser.getName().equals("weather")){
                        iconName = parser.getAttributeValue(null, "icon");
                        String iconFile = iconName + ".png";
                        if(isFileExists(iconFile)){
                            FileInputStream inputStream = null;
                            try{
                                inputStream = new FileInputStream(getBaseContext().getFileStreamPath(iconFile));
                            }catch(Exception e ){
                                e.printStackTrace();
                            }
                            icon = BitmapFactory.decodeStream(inputStream);
                            Log.i(TAG, "image file exists already");
                        }

                        else{
                            URL ImageURL = new URL("http://openweathermap.org/img/w/" + iconName + ".png");
                            icon  = getImage(ImageURL);
                            FileOutputStream outputStream = openFileOutput( iconName + ".png", Context.MODE_PRIVATE);
                            icon.compress(Bitmap.CompressFormat.PNG, 80, outputStream);
                            outputStream.flush();
                            outputStream.close();
                            Log.i(TAG, "downloaded image");
                        }
                        Log.i(TAG, "filename " + iconFile);
                        publishProgress(100);
                    }
                }

            }catch(MalformedURLException e ){

            } catch (IOException e) {
                e.printStackTrace();
            } catch (XmlPullParserException e) {
                e.printStackTrace();
            }catch (NullPointerException e) {
                e.printStackTrace();
            }
            return null;
        }

        public void onProgressUpdate(Integer... value){
            Log.i(TAG, "in onProgressUpdate");
            progressBar.setVisibility(View.VISIBLE);
            progressBar.setProgress(value[0]);

        }

        public void onPostExecute(String result){
            String degree = Character.toString((char) 0x00B0);
            curTemp.setText(curTemp.getText()+ currentTemp + degree + "C");
            minTemp.setText(minTemp.getText() + min + degree + "C");
            maxTemp.setText(maxTemp.getText()+ max + degree + "C");
            windSpeed.setText(windSpeed.getText()+ winds + "KM/H");
            weatherImage.setImageBitmap(icon);
            progressBar.setVisibility(View.INVISIBLE);
        }



    }

}