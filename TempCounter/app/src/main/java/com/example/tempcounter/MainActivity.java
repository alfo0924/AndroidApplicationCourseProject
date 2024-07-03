package com.example.tempcounter;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private TextView textView;
    private Switch switch1;
    private EditText limit;
    private boolean isCelsius = true;
    private double currentTemp = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textView = findViewById(R.id.textView);
        switch1 = findViewById(R.id.switch1);
        limit = findViewById(R.id.limit);

        limit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() > 0) {
                    currentTemp = Double.parseDouble(s.toString());
                    if (!isCelsius) {
                        currentTemp = (currentTemp - 32) * 5 / 9;
                    }
                    textView.setText(String.format("%.2f", currentTemp));
                } else {
                    textView.setText("0");
                }
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });

        switch1.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                if (limit.getText().length() > 0) {
                    currentTemp = (currentTemp * 9 / 5) + 32;
                    textView.setText(String.format("%.2f", currentTemp));
                    switch1.setText("華氏");
                }
            } else {
                if (limit.getText().length() > 0) {
                    currentTemp = (currentTemp - 32) * 5 / 9;
                    textView.setText(String.format("%.2f", currentTemp));
                    switch1.setText("攝氏");
                }
            }
            isCelsius = !isChecked;
        });
    }
}