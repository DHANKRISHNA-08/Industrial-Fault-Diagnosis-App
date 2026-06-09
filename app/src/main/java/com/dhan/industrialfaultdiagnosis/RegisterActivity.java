package com.dhan.industrialfaultdiagnosis;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.google.firebase.firestore.FirebaseFirestore;
import java.util.HashMap;
import java.util.Map;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;

public class RegisterActivity extends AppCompatActivity {

    EditText etName, etEmail, etPassword;
    Button btnRegister;
    TextView txtLogin;

    FirebaseAuth mAuth;
    FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        etName = findViewById(R.id.etName);
        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);
        btnRegister = findViewById(R.id.btnRegister);
        txtLogin = findViewById(R.id.txtLogin);

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        btnRegister.setOnClickListener(v -> {

            String name = etName.getText().toString().trim();
            String email = etEmail.getText().toString().trim();
            String password = etPassword.getText().toString().trim();

            if (TextUtils.isEmpty(name) ||
                    TextUtils.isEmpty(email) ||
                    TextUtils.isEmpty(password)) {

                Toast.makeText(RegisterActivity.this,
                        "Fill all fields",
                        Toast.LENGTH_SHORT).show();
                return;
            }

            mAuth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(task -> {

                        if (task.isSuccessful()) {

                            String userId =
                                    mAuth.getCurrentUser().getUid();

                            Map<String, Object> userData =
                                    new HashMap<>();

                            userData.put("username", name);
                            userData.put("email", email);

                            db.collection("Users")
                                    .document(userId)
                                    .set(userData);

                            Toast.makeText(RegisterActivity.this,
                                    "Registration Successful",
                                    Toast.LENGTH_SHORT).show();

                            startActivity(new Intent(
                                    RegisterActivity.this,
                                    LoginActivity.class));

                            finish();
                        } else {

                            Toast.makeText(RegisterActivity.this,
                                    task.getException().getMessage(),
                                    Toast.LENGTH_LONG).show();
                        }
                    });
        });

        txtLogin.setOnClickListener(v -> {
            startActivity(new Intent(
                    RegisterActivity.this,
                    LoginActivity.class));
        });
    }
}