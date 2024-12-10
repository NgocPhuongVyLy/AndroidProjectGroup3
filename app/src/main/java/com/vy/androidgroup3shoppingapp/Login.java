package com.vy.androidgroup3shoppingapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class Login extends AppCompatActivity {



    private EditText usernameEditText, passwordEditText;
    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Initialize views
        usernameEditText = findViewById(R.id.usernameTE); // For username input
        passwordEditText = findViewById(R.id.passwordET); // For password input

        // SharedPreferences to retrieve saved user data
        sharedPreferences = getSharedPreferences("UserPreferences", MODE_PRIVATE);
    }

    // Called when the Login button is clicked
    public void LoginClicked(View view) {
        String username = usernameEditText.getText().toString().trim();
        String password = passwordEditText.getText().toString().trim();

        // Check if fields are empty
        if (username.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Please fill in all fields.", Toast.LENGTH_SHORT).show();
            return;
        }

        // Retrieve saved credentials from SharedPreferences
        String savedUsername = sharedPreferences.getString("username", ""); // Retrieve saved username
        String savedPassword = sharedPreferences.getString("userPassword", ""); // Retrieve saved password
        int userId = sharedPreferences.getInt(username + "_userId", -1); // Fetch userId

        // Validate login credentials
        if (username.equals(savedUsername) && password.equals(savedPassword)) {
            // Login successful
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putInt("userId", userId); // Save userId for the logged-in user
            editor.apply();

            Toast.makeText(this, "Login Successful!", Toast.LENGTH_SHORT).show();

            // Navigate to MainActivity
            Intent intent = new Intent(Login.this, MainActivity.class);
            startActivity(intent);
            finish();
        } else {
            // Invalid login credentials
            Toast.makeText(this, "Incorrect Username or Password, or User does not exist.", Toast.LENGTH_SHORT).show();
        }
    }

}
