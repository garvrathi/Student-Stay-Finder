package com.example.welcomepage;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import io.appwrite.Client;
import io.appwrite.coroutines.CoroutineCallback;
import io.appwrite.exceptions.AppwriteException;
import io.appwrite.services.Account;

public class ForgotPassword extends AppCompatActivity {

    private EditText emailEditText;
    private Client client;
    private Account account;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        client = new Client(getApplicationContext())
                .setEndpoint("https://cloud.appwrite.io/v1")
                .setProject("64b17fb0695bc6e1ce70");

        account = new Account(client);

        emailEditText = findViewById(R.id.email_mobile);

        ImageView back_button = findViewById(R.id.back_button);
        back_button.setOnClickListener(v -> onBackPressed());

        Button resetPasswordButton = findViewById(R.id.Continuebtn);
        resetPasswordButton.setOnClickListener(v -> resetPassword());
    }

    private void resetPassword() {
        String email = emailEditText.getText().toString();

        if (email.isEmpty()) {
            emailEditText.setError("Email cannot be empty");
            return;
        }
        account.createRecovery(email, "https://example.com", new CoroutineCallback<>((recovery, error) -> {
            if (error == null) {
                // Password recovery request successful
                // Show a message to the user indicating that the recovery email has been sent
                Toast.makeText(this, "Password recovery email sent", Toast.LENGTH_SHORT).show();
            } else {
                // Password recovery request failed
                // Show an error message to the user
                Toast.makeText(this, "Failed to request password recovery", Toast.LENGTH_SHORT).show();
            }
        }));
    }

    private boolean isValidEmail(String email) {
        // Implement your email validation logic here
        // For example, you can use the android.util.Patterns.EMAIL_ADDRESS matcher
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }
    }
