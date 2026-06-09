package com.dhan.industrialfaultdiagnosis;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.view.View;
import android.widget.Spinner;
import android.widget.ArrayAdapter;
import android.graphics.Color;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import android.content.Intent;
import com.google.firebase.auth.FirebaseAuth;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.firestore.FirebaseFirestore;
import java.util.HashMap;
import java.util.Map;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {

    Button btnDiagnose;
    TextView txtResult;
    TextView txtStatus;
    Spinner spinnerFault;
    Button btnReset;
    TextView txtCounter;
    int diagnosisCount = 0;
    String history = "";
    String previousFault = "None";
    Button btnLogout;
    FirebaseAuth mAuth;
    FirebaseFirestore db;
    TextView txtWelcome;
    Button btnHistory;
    Button btnDashboard;
    Button btnAbout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnDiagnose = findViewById(R.id.btnDiagnose);
        txtResult = findViewById(R.id.txtResult);
        spinnerFault = findViewById(R.id.spinnerFault);
        btnReset = findViewById(R.id.btnReset);
        btnHistory = findViewById(R.id.btnHistory);
        btnDashboard = findViewById(R.id.btnDashboard);
        btnAbout = findViewById(R.id.btnAbout);
        txtStatus = findViewById(R.id.txtStatus);
        txtCounter = findViewById(R.id.txtCounter);
        txtWelcome = findViewById(R.id.txtWelcome);
        btnLogout = findViewById(R.id.btnLogout);
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();

        if (user != null) {

            String userId = user.getUid();

            db.collection("Users")
                    .document(userId)
                    .get()
                    .addOnSuccessListener(documentSnapshot -> {

                        if (documentSnapshot.exists()) {

                            String username =
                                    documentSnapshot.getString("username");

                            txtWelcome.setText(
                                    "Welcome, " + username);

                        }
                    });
        }
        //loadHistory();

        txtStatus.setText("System Status: Ready");
        txtStatus.setTextColor(Color.parseColor("#2E7D32"));

        ArrayAdapter<CharSequence> adapter =
                ArrayAdapter.createFromResource(
                        this,
                        R.array.faults,
                        android.R.layout.simple_spinner_item);

        adapter.setDropDownViewResource(
                android.R.layout.simple_spinner_dropdown_item);

        spinnerFault.setAdapter(adapter);

        btnDiagnose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String selectedFault = spinnerFault.getSelectedItem().toString();
                String currentTime = new SimpleDateFormat("hh:mm a", Locale.getDefault()).format(new Date());
                diagnosisCount++;
                txtCounter.setText("Total Diagnoses: " + diagnosisCount);

                txtStatus.setText("System Status: Fault Detected");
                txtStatus.setTextColor(Color.parseColor("#D32F2F"));

                Map<String, Object> faultData = new HashMap<>();

                faultData.put("fault", selectedFault);
                faultData.put("time", currentTime);
                faultData.put("userEmail", mAuth.getCurrentUser().getEmail());
                faultData.put("timestamp", System.currentTimeMillis());

                db.collection("Diagnoses")
                        .add(faultData);

                if(selectedFault.equals("Motor Overheating"))
                {
                    txtResult.setText(
                            "FAULT REPORT\n\n" +
                                    "MOTOR OVERHEATING\n\n" +
                                    "Category: Motor\n" +
                                    "Severity: HIGH\n" +
                                    "Time: " + currentTime +
                                    "\n\n🔴 Immediate Action Required"
                    );
                    txtResult.setTextColor(Color.RED);
                }
                else if(selectedFault.equals("Sensor Failure"))
                {
                    txtResult.setText(
                            "FAULT REPORT\n\n" +
                                    "SENSOR FAILURE\n\n" +
                                    "Category: Sensor\n" +
                                    "Severity: MEDIUM\n" +
                                    "Time: " + currentTime +
                                    "\n\n🟠 Maintenance Required"
                    );
                    txtResult.setTextColor(Color.rgb(255,165,0));
                }
                else if(selectedFault.equals("Valve Stuck"))
                {
                    txtResult.setText(
                            "FAULT REPORT\n\n" +
                                    "VALVE STUCK\n\n" +
                                    "Category: Valve\n" +
                                    "Severity: HIGH\n" +
                                    "Time: " + currentTime +
                                    "\n\n🔴 Immediate Action Required"
                    );
                    txtResult.setTextColor(Color.RED);
                }
                else if(selectedFault.equals("Pump Cavitation"))
                {
                    txtResult.setText(
                            "FAULT REPORT\n\n" +
                                    "PUMP CAVITATION\n\n" +
                                    "Category: Pump\n" +
                                    "Severity: MEDIUM\n" +
                                    "Time: " + currentTime +
                                    "\n\n🟠 Maintenance Required"
                    );
                    txtResult.setTextColor(Color.rgb(255,165,0));
                }
                else if(selectedFault.equals("High Vibration"))
                {
                    txtResult.setText(
                            "FAULT REPORT\n\n" +
                                    "HIGH VIBRATION\n\n" +
                                    "Category: Mechanical\n" +
                                    "Severity: MEDIUM\n" +
                                    "Time: " + currentTime +
                                    "\n\n🟡 Inspect Equipment"
                    );
                    txtResult.setTextColor(Color.RED);
                }
                }
        });
        btnReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                txtResult.setText("Result will appear here");
                txtResult.setTextColor(Color.BLACK);
                spinnerFault.setSelection(0);
                txtStatus.setText("System Status: Ready");
                txtStatus.setTextColor(Color.parseColor("#2E7D32"));
            }
        });
        btnHistory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent =
                        new Intent(MainActivity.this,
                                HistoryActivity.class);

                startActivity(intent);
            }
        });
        btnDashboard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent =
                        new Intent(MainActivity.this,
                                ScadaActivity.class);

                startActivity(intent);
            }
        });
        btnAbout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent =
                        new Intent(MainActivity.this,
                                AboutActivity.class);

                startActivity(intent);
            }
        });
        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAuth.signOut();

                Intent intent = new Intent(MainActivity.this,
                        LoginActivity.class);

                startActivity(intent);
                finish();
            }
        });
    }
}
