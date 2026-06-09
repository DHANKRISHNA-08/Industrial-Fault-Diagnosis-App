package com.dhan.industrialfaultdiagnosis;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

public class HistoryActivity extends AppCompatActivity {

    TextView txtHistoryData;
    FirebaseFirestore db;
    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        txtHistoryData = findViewById(R.id.txtHistoryData);

        db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();

        loadHistory();
    }

    private void loadHistory() {

        String userEmail =
                mAuth.getCurrentUser().getEmail();

        db.collection("Diagnoses")
                .whereEqualTo("userEmail", userEmail)
                .orderBy("timestamp", Query.Direction.DESCENDING)
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {

                    StringBuilder history =
                            new StringBuilder();

                    for (com.google.firebase.firestore.QueryDocumentSnapshot document :
                            queryDocumentSnapshots) {

                        String fault =
                                document.getString("fault");

                        String time =
                                document.getString("time");

                        history.append("🔧 ")
                                .append(fault)
                                .append("\n🕒 ")
                                .append(time)
                                .append("\n\n");
                    }

                    txtHistoryData.setText(
                            history.toString());
                })
                .addOnFailureListener(e -> {

                    txtHistoryData.setText(e.getMessage());
                });
    }
}