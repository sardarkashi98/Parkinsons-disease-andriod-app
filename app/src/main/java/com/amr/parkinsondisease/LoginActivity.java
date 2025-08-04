package com.amr.parkinsondisease;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity {

    private EditText mEmail, mPassword;
    private Button mLoginBtn;
    private TextView mForgotPassword, mRegisterNow, mLoadingText;
    private FirebaseAuth fAuth;
    private SharedPreferences sharedPreferences;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Initialize views with correct IDs
        mEmail = findViewById(R.id.etEmail);
        mPassword = findViewById(R.id.etPassword);
        mLoginBtn = findViewById(R.id.btnLogin);
        mForgotPassword = findViewById(R.id.tvForgotPassword);
        mRegisterNow = findViewById(R.id.tvSignup);
        mLoadingText = findViewById(R.id.loadingText);
        progressBar = findViewById(R.id.progressBar);

        // Initialize Firebase Auth
        fAuth = FirebaseAuth.getInstance();

        // Initialize SharedPreferences
        sharedPreferences = getSharedPreferences("user_prefs", MODE_PRIVATE);

        // Check if user is already logged in
        if (sharedPreferences.getBoolean("isLoggedIn", false)) {
            startActivity(new Intent(getApplicationContext(), MainActivity.class));
            finish();
        }

        // Animate views on activity start
        animateViews();

        // Login button click listener
        mLoginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = mEmail.getText().toString().trim();
                String password = mPassword.getText().toString().trim();

                // Validate inputs
                if (TextUtils.isEmpty(email)) {
                    mEmail.setError("Email is required.");
                    return;
                }
                if (TextUtils.isEmpty(password)) {
                    mPassword.setError("Password is required.");
                    return;
                }

                // Show loading state
                showLoading(true);

                // Authenticate user with Firebase
                fAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        // Hide loading state
                        showLoading(false);

                        if (task.isSuccessful()) {
                            FirebaseUser user = fAuth.getCurrentUser();
                            if (user != null) {
                                // Check if email is verified or if it has been verified before
                                if (user.isEmailVerified() || sharedPreferences.getBoolean("email_verified_" + user.getUid(), false)) {
                                    // Mark email as verified in SharedPreferences
                                    sharedPreferences.edit().putBoolean("email_verified_" + user.getUid(), true).apply();
                                    // Mark user as logged in
                                    sharedPreferences.edit().putBoolean("isLoggedIn", true).apply();
                                    // Redirect to MainActivity on success
                                    startActivity(new Intent(getApplicationContext(), MainActivity.class));
                                    finish(); // Close the login activity
                                } else {
                                    Toast.makeText(LoginActivity.this, "Please verify your email address.", Toast.LENGTH_SHORT).show();
                                }
                            }
                        } else {
                            // Show error message
                            Toast.makeText(LoginActivity.this, "Error: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });

        // Forgot password click listener
        mForgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), ForgetActivity.class));
            }
        });

        // Register now click listener
        mRegisterNow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), RegisterActivity.class));
            }
        });
    }

    /**
     * Shows or hides the loading progress indicator
     * @param isLoading true to show loading, false to hide
     */
    private void showLoading(boolean isLoading) {
        if (isLoading) {
            progressBar.setVisibility(View.VISIBLE);
            mLoadingText.setVisibility(View.VISIBLE);
            mLoginBtn.setEnabled(false);
            mLoginBtn.setAlpha(0.7f); // Make button look disabled
        } else {
            progressBar.setVisibility(View.GONE);
            mLoadingText.setVisibility(View.GONE);
            mLoginBtn.setEnabled(true);
            mLoginBtn.setAlpha(1f); // Make button look enabled again
        }
    }

    // Method to animate views
    private void animateViews() {
        // Animate logo
        findViewById(R.id.logo).animate()
                .translationY(0f)
                .alpha(1f)
                .setDuration(800)
                .start();

        // Animate email input
        findViewById(R.id.etEmail).animate()
                .translationY(0f)
                .alpha(1f)
                .setStartDelay(200)
                .setDuration(800)
                .start();

        // Animate password input
        findViewById(R.id.etPassword).animate()
                .translationY(0f)
                .alpha(1f)
                .setStartDelay(400)
                .setDuration(800)
                .start();

        // Animate login button
        findViewById(R.id.btnLogin).animate()
                .translationY(0f)
                .alpha(1f)
                .setStartDelay(600)
                .setDuration(800)
                .start();

        // Animate progress bar
        findViewById(R.id.progressBar).animate()
                .translationY(0f)
                .alpha(1f)
                .setStartDelay(700)
                .setDuration(800)
                .start();

        // Animate forgot password text
        findViewById(R.id.tvForgotPassword).animate()
                .translationY(0f)
                .alpha(1f)
                .setStartDelay(800)
                .setDuration(800)
                .start();

        // Animate register now text
        findViewById(R.id.tvSignup).animate()
                .translationY(0f)
                .alpha(1f)
                .setStartDelay(1000)
                .setDuration(800)
                .start();
    }
}