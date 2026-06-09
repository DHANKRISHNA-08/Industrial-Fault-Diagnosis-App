package com.dhan.industrialfaultdiagnosis;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity {

    EditText etLoginEmail, etLoginPassword;
    Button btnLogin;
    TextView txtRegister;

    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        etLoginEmail = findViewById(R.id.etLoginEmail);
        etLoginPassword = findViewById(R.id.etLoginPassword);
        btnLogin = findViewById(R.id.btnLogin);
        txtRegister = findViewById(R.id.txtRegister);

        mAuth = FirebaseAuth.getInstance();

        btnLogin.setOnClickListener(v -> {

            String email = etLoginEmail.getText().toString().trim();
            String password = etLoginPassword.getText().toString().trim();

            if (TextUtils.isEmpty(email) ||
                    TextUtils.isEmpty(password)) {

                Toast.makeText(LoginActivity.this,
                        "Fill all fields",
                        Toast.LENGTH_SHORT).show();
                return;
            }

            mAuth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(task -> {

                        if (task.isSuccessful()) {

                            Toast.makeText(LoginActivity.this,
                                    "Login Successful",
                                    Toast.LENGTH_SHORT).show();

                            startActivity(new Intent(
                                    LoginActivity.this,
                                    MainActivity.class));

                            finish();

                        } else {

                            Toast.makeText(LoginActivity.this,
                                    task.getException().getMessage(),
                                    Toast.LENGTH_LONG).show();
                        }
                    });
        });

        txtRegister.setOnClickListener(v -> {
            startActivity(new Intent(
                    LoginActivity.this,
                    RegisterActivity.class));
        });
    }
}