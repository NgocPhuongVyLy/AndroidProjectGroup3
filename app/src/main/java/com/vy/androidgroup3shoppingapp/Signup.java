package com.vy.androidgroup3shoppingapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class Signup extends AppCompatActivity {

    private EditText usernameField, emailField, passwordField;
    private Button signUpButton, googleButton, gmailButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        usernameField = findViewById(R.id.username);
        emailField = findViewById(R.id.email);
        passwordField = findViewById(R.id.password);
        signUpButton = findViewById(R.id.signupbt);
        /*googleButton = findViewById(R.id.google_button);
        gmailButton = findViewById(R.id.gmail_button);*/

        signUpButton.setOnClickListener(v -> {
            String username = usernameField.getText().toString().trim();
            String email = emailField.getText().toString().trim();
            String password = passwordField.getText().toString().trim();

            if (validateInput(username, email, password)) {
                saveToSharedPreferences(username, email, password);
                navigateToLogin();
            }
        });

      /*  googleButton.setOnClickListener(v -> {
            String googleEmail = "user@google.com";
            String googleUsername = "GoogleUser";
            String googlePassword = "google123";
            saveToSharedPreferences(googleUsername, googleEmail, googlePassword);
            Toast.makeText(this, "Signed up with Google!", Toast.LENGTH_SHORT).show();
            navigateToLogin();
        });*/

    /*    gmailButton.setOnClickListener(v -> {
            String gmailEmail = "user@gmail.com";
            String gmailUsername = "GmailUser";
            String gmailPassword = "gmail123";
            saveToSharedPreferences(gmailUsername, gmailEmail, gmailPassword);
            Toast.makeText(this, "Signed up with Gmail!", Toast.LENGTH_SHORT).show();
            navigateToLogin();
        });*/
    }

    // Method to validate user input
    private boolean validateInput(String username, String email, String password) {
        if (username.isEmpty()) {
            Toast.makeText(this, "Username cannot be empty", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            Toast.makeText(this, "Invalid email", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (password.isEmpty() || password.length() < 6) {
            Toast.makeText(this, "Password must be at least 6 characters long", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    // Method to save data in SharedPreferences
    // Method to save data in SharedPreferences
    private void saveToSharedPreferences(String username, String email, String password) {
        SharedPreferences sharedPreferences = getSharedPreferences("UserPreferences", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        int userId = generateUserId(); // Generate a unique user ID
        editor.putString("username", username);
        editor.putString("email", email);
        editor.putString("userPassword", password);
        editor.putInt(username + "_userId", userId); // Save userId with the username key
        editor.putInt("userId", userId); // Store globally for logged-in user
        editor.apply();

        Toast.makeText(this, "Sign-up successful! User ID: " + userId, Toast.LENGTH_SHORT).show();
    }


    private int generateUserId() {
        // Generate a unique user ID (e.g., based on a counter or database logic)
        return (int) (Math.random() * 10000); // Example logic
    }


    // Navigate to Login activity
    private void navigateToLogin() {
        Intent intent = new Intent(Signup.this, Login.class);
        startActivity(intent);
        finish();
    }
}