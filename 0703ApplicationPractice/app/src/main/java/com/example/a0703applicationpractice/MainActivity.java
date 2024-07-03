package com.example.a0703applicationpractice;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Switch;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.constraintlayout.widget.ConstraintLayout;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class MainActivity extends AppCompatActivity {

    private Switch switch1;
    private TextView textView;
    private TextView textClock;
    private ConstraintLayout mainLayout;
    private boolean isDarkMode = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        switch1 = findViewById(R.id.switch1);
        textView = findViewById(R.id.textView);
        textClock = findViewById(R.id.textClock);
        mainLayout = findViewById(R.id.main);

        // Set initial mode to light mode
        setDarkMode(false);

        switch1.setOnCheckedChangeListener((buttonView, isChecked) -> {
            setDarkMode(isChecked);
        });

        // Update the time every second
        new Thread(() -> {
            while (true) {
                runOnUiThread(this::updateTime);
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    private void setDarkMode(boolean isChecked) {
        isDarkMode = isChecked;
        runOnUiThread(() -> {
            if (isDarkMode) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                textView.setText("Dark Mode");
                mainLayout.setBackgroundColor(getResources().getColor(R.color.dark_mode_background));
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                textView.setText("Light Mode");
                mainLayout.setBackgroundColor(getResources().getColor(R.color.light_mode_background));
            }
        });
    }

    private void updateTime() {
        new FetchTimeTask().execute();
    }

    private class FetchTimeTask extends AsyncTask<Void, Void, String> {
        @Override
        protected String doInBackground(Void... params) {
            try {
                URL url = new URL("http://worldtimeapi.org/api/timezone/Asia/Taipei");
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("GET");

                BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                StringBuilder response = new StringBuilder();
                String inputLine;
                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }
                in.close();

                // Parse the response to get the current time
                int startIndex = response.indexOf("\"datetime\":\"") + 12;
                int endIndex = response.indexOf("\"", startIndex);
                String currentTime = response.substring(startIndex, endIndex);
                return currentTime;
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onPostExecute(String currentTime) {
            if (currentTime != null) {
                textClock.setText(currentTime);
            }
        }
    }
}