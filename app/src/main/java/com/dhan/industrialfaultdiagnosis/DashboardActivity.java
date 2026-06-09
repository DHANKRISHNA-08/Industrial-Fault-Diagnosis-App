package com.dhan.industrialfaultdiagnosis;

import android.os.Bundle;
import android.widget.TextView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import android.graphics.pdf.PdfDocument;
import android.os.Environment;
import android.widget.Button;

import java.io.File;
import java.io.FileOutputStream;

import androidx.appcompat.app.AppCompatActivity;

public class DashboardActivity extends AppCompatActivity {

    TextView txtDashboardData;
    FirebaseFirestore db;
    FirebaseAuth mAuth;
    private Button btnGeneratePdf;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        txtDashboardData =
                findViewById(R.id.txtDashboardData);
        btnGeneratePdf =
                findViewById(R.id.btnGeneratePdf);
        db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();
        btnGeneratePdf.setOnClickListener(v -> generatePdf());

        loadDashboard();
    }
    private void loadDashboard() {

        String userEmail =
                mAuth.getCurrentUser().getEmail();

        db.collection("Diagnoses")
                .whereEqualTo("userEmail", userEmail)
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {

                    int total = 0;
                    int motor = 0;
                    int sensor = 0;
                    int valve = 0;
                    int pump = 0;
                    int vibration = 0;

                    for (com.google.firebase.firestore.QueryDocumentSnapshot document
                            : queryDocumentSnapshots) {

                        total++;

                        String fault =
                                document.getString("fault");

                        if ("Motor Overheating".equals(fault))
                            motor++;

                        else if ("Sensor Failure".equals(fault))
                            sensor++;

                        else if ("Valve Stuck".equals(fault))
                            valve++;

                        else if ("Pump Cavitation".equals(fault))
                            pump++;

                        else if ("High Vibration".equals(fault))
                            vibration++;
                    }

                    String analytics =
                            "Total Diagnoses: " + total +

                                    "\n\nMotor Overheating : " + motor +

                                    "\nSensor Failure : " + sensor +

                                    "\nValve Stuck : " + valve +

                                    "\nPump Cavitation : " + pump +

                                    "\nHigh Vibration : " + vibration;

                    txtDashboardData.setText(analytics);
                });
    }
    private void generatePdf() {

        PdfDocument pdfDocument = new PdfDocument();

        PdfDocument.PageInfo pageInfo =
                new PdfDocument.PageInfo.Builder(595, 842, 1).create();

        PdfDocument.Page page =
                pdfDocument.startPage(pageInfo);

        page.getCanvas().drawText(
                txtDashboardData.getText().toString(),
                50,
                100,
                new android.graphics.Paint()
        );

        pdfDocument.finishPage(page);

        try {

            File file = new File(
                    getExternalFilesDir(null),
                    "Fault_Report.pdf"
            );

            pdfDocument.writeTo(
                    new FileOutputStream(file)
            );

            pdfDocument.close();

            android.widget.Toast.makeText(
                    this,
                    "PDF Saved:\n" + file.getAbsolutePath(),
                    android.widget.Toast.LENGTH_LONG
            ).show();

        } catch (Exception e) {

            android.widget.Toast.makeText(
                    this,
                    e.getMessage(),
                    android.widget.Toast.LENGTH_LONG
            ).show();
        }
    }
}