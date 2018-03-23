package com.example.sohaila.androidlabs;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import com.example.sohaila.androidlabs.WeatherForecast;

public class StartActivity extends Activity {

    protected static final String ACTIVITY_NAME = "StartActivity";
    Button weatherButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
        Log.i(ACTIVITY_NAME, "In onCreate()");

        Button b1 = (Button) findViewById(R.id.button);
        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(StartActivity.this, ListItemsActivity.class);
                // startActivity(intent);
                startActivityForResult(intent, 50);
            }
        } );

        Button start_chat_btn = (Button) findViewById(R.id.start_chat_btn);
        start_chat_btn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Log.i(ACTIVITY_NAME, "User clicked "+R.string.start_chat_text);
                Intent intent = new Intent(StartActivity.this, ChatWindow.class);
                startActivity(intent);
            }
        });
        onClickWeather();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == 50 && resultCode == Activity.RESULT_OK){
            Log.i(ACTIVITY_NAME, "Returns to StartActivity.onActivityResult");
            String messgPassed = data.getStringExtra("Response");
            CharSequence text = "ListItemsActivity Passed:"+messgPassed;
            Toast toast = Toast.makeText(StartActivity.this,text,Toast.LENGTH_LONG);
            toast.show();
            Log.i(ACTIVITY_NAME,messgPassed);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.i(ACTIVITY_NAME, "In onResume()");
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.i(ACTIVITY_NAME, "In onStart()");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.i(ACTIVITY_NAME, "In onPause()");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.i(ACTIVITY_NAME, "In onStop()");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.i(ACTIVITY_NAME, "In onDestroy()");
    }

    public void onClickWeather(){
        weatherButton = (Button)findViewById(R.id.button_Weather_forecast);
        weatherButton.setOnClickListener(new View.OnClickListener(){
                                             @Override
                                             public void onClick(View v){
                                                 Intent intent = new Intent(StartActivity.this, WeatherForecast.class);
                                                 startActivity(intent);
                                             }
                                         }
        );
    }
}
