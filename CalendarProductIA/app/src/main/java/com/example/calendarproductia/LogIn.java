package com.example.calendarproductia;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class LogIn extends AppCompatActivity {
    SQLiteDBHandler sqLiteDBHandler;

    @SuppressLint("WrongViewCast")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        Button signUpButton = findViewById(R.id.singUpButton);
        this.sqLiteDBHandler = SQLiteDBHandler.instanceDB();
        boolean userExists = userExists();
        if (userExists)
            signUpButton.setVisibility(View.GONE);
        else
            signUpButton.setVisibility(View.VISIBLE);

        signUpButton.setOnClickListener(v -> {
            Intent intent = new Intent(LogIn.this, SignUp.class);
            startActivity(intent);
        });

        EditText username = findViewById(R.id.username);
        EditText password = findViewById(R.id.password);
        Button loginButton = findViewById(R.id.loginButton);

        loginButton.setOnClickListener(view -> {
            String usernameInput = username.getText().toString();
            String passwordInput = password.getText().toString();
            String hashedPassword = hashPassword(passwordInput);
            boolean userRegistered = SQLiteDBHandler.instanceDB().checkUserCredentials(usernameInput, hashedPassword);
            if (userRegistered) {
                Toast.makeText(LogIn.this, "Login successful!", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(LogIn.this, MainActivity.class);
                startActivity(intent);
            } else {
                Toast.makeText(LogIn.this, "Login unsuccessful! Try again!", Toast.LENGTH_SHORT).show();
                username.setText("");
                password.setText("");
            }
        });
    }

    private boolean userExists() {
        SQLiteDBHandler sqLiteDBHandler = new SQLiteDBHandler(this);
        SQLiteDatabase db = sqLiteDBHandler.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT COUNT(*) FROM Users", null);
        cursor.moveToFirst();
        int userCount = cursor.getInt(0);
        cursor.close();
        db.close();
        if (userCount > 0)
            return true;
        else
            return false;
    }

    private String hashPassword(String password) {
        try {
            MessageDigest messageDigest = MessageDigest.getInstance("SHA-256");
            byte[] hashedBytes = messageDigest.digest(password.getBytes(StandardCharsets.UTF_8));

            StringBuilder sb = new StringBuilder();
            for (byte b : hashedBytes) {
                sb.append(String.format("%02x", b));
            }
            return sb.toString();
        } catch (NoSuchAlgorithmException e) {
            Log.e("PasswordHashing", "Error hashing password: " + e.getMessage());
            return null;
        }
    }
}
