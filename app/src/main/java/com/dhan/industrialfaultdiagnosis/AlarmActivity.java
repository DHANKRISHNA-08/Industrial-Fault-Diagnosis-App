package com.dhan.industrialfaultdiagnosis;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class AlarmActivity extends AppCompatActivity {

    Button btnBack;
    TextView txtAlarmHistory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarm);

        btnBack = findViewById(R.id.btnBack);
        txtAlarmHistory = findViewById(R.id.txtAlarmHistory);

        String alarms =
                "08:15 PM  HIGH TEMPERATURE\n\n" +
                        "08:10 PM  MOTOR OVERHEATING\n\n" +
                        "08:05 PM  HIGH VIBRATION\n\n" +
                        "08:00 PM  SENSOR FAILURE\n\n" +
                        "07:55 PM  PUMP CAVITATION";

        txtAlarmHistory.setText(alarms);

        btnBack.setOnClickListener(v -> finish());
    }
}