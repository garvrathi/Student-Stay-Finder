package com.example.welcomepage;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.TextAppearanceSpan;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import io.appwrite.Client;
import io.appwrite.coroutines.CoroutineCallback;
import io.appwrite.services.Account;


public class SignInActivity extends AppCompatActivity {

    private Client client;
    private EditText emailEditText;
    private ProgressBar progressBar;
    private EditText passwordEditText;
    private CheckBox rememberMeCheckBox;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        client = new Client(getApplicationContext())
                .setEndpoint("https://cloud.appwrite.io/v1")
                .setProject("64b17fb0695bc6e1ce70");

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        progressBar = findViewById(R.id.progressBar);
        rememberMeCheckBox = findViewById(R.id.rememberMe);

        ImageView back_button = findViewById(R.id.back_button);

        emailEditText = findViewById(R.id.email_mobile);
        passwordEditText = findViewById(R.id.password);

        TextView textView = findViewById(R.id.textView);
        String text = "Don't have an account? Sign Up";

        SpannableString spannableString = new SpannableString(text);

        ClickableSpan clickableSpan = new ClickableSpan() {
            @Override
            public void onClick(View view) {
                // Open Sign Up activity or perform the desired action
                Intent intent = new Intent(SignInActivity.this, SignUpActivity.class);
                startActivity(intent);
            }
        };
        back_button.setOnClickListener(v -> onBackPressed());

        spannableString.setSpan(clickableSpan, text.indexOf("Sign Up"), text.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        spannableString.setSpan(new TextAppearanceSpan(this, android.R.style.TextAppearance_Medium), text.indexOf("Sign Up"), text.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        textView.setText(spannableString);
        textView.setMovementMethod(LinkMovementMethod.getInstance());

        FrameLayout framelayout = findViewById(R.id.frameLayout);
        Button signInButton = framelayout.findViewById(R.id.Continuebtn);

        signInButton.setOnClickListener(v -> signIn());

        Button forgotPasswordButton = findViewById(R.id.ForgotPasswordButton);
        forgotPasswordButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Open the ForgotPasswordActivity when the button is clicked
                Intent intent = new Intent(SignInActivity.this, ForgotPassword.class);
                startActivity(intent);
            }
        });
    }

    private void signIn() {

        String email = emailEditText.getText().toString();
        String password = passwordEditText.getText().toString();

        if (email.isEmpty()) {
            emailEditText.setError("Email cannot be empty");
            return;
        }

        if (!isValidEmail(email)) {
            emailEditText.setError("Invalid email format");
            return;
        }

        if (password.isEmpty()) {
            passwordEditText.setError("Password cannot be empty");
            return;
        }

        progressBar.setVisibility(View.VISIBLE);

        Account account = new Account(client);

        account.createEmailSession(email, password, new CoroutineCallback<>((session, error) -> {


            if (error == null) {
                // Handle successful login
                // You can access the user details from the 'user' object
                Intent intent = new Intent(SignInActivity.this, loginsuccessful.class);
                startActivity(intent);

                // Save the login status if "Remember Me" is checked
                boolean rememberMe = rememberMeCheckBox.isChecked();
                saveLoginStatus(rememberMe);
                progressBar.setVisibility(View.GONE);
            } else {
                // Handle login failure
                Intent intent = new Intent(SignInActivity.this, LoginFailed.class);
                startActivity(intent);
                progressBar.setVisibility(View.GONE);
            }
        }));

    }

    private boolean isValidEmail(String email) {
        // Use a regular expression to validate email format
        // For example, you can use the android.util.Patterns.EMAIL_ADDRESS matcher
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    private void saveLoginStatus(boolean isRemembered) {
        SharedPreferences preferences = getSharedPreferences("login_status", MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean("is_remembered", isRemembered);
        editor.apply();
    }

}
