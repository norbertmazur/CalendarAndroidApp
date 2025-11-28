package com.example.calendarproductia;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputLayout;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.regex.Pattern;

public class SignUp extends AppCompatActivity {
    private static final Pattern PASSWORD_PATTERN =
            Pattern.compile("^" +
                    "(?=.*[0-9])" +         //at least 1 digit
                    "(?=.*[a-z])" +         //at least 1 lower case letter
                    "(?=.*[A-Z])" +         //at least 1 upper case letter
                    "(?=.*[@#$%^&+=!*])" +  //at least 1 special character
                    "(?=\\S+$)" +           //no white spaces
                    ".{8,20}" +               //at least 8 characters
                    "$");
    private TextInputLayout textInputUsername;
    private TextInputLayout textInputPassword;
    private TextInputLayout textInputConfirmPassword;
    SQLiteDBHandler sqLiteDBHandler = SQLiteDBHandler.instanceDB();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        Button signUpButton = findViewById(R.id.signUpButton);
        textInputUsername = findViewById(R.id.textInput_username);
        textInputPassword = findViewById(R.id.textInput_password);
        textInputConfirmPassword = findViewById(R.id.textInput_confirm_password);

        signUpButton.setOnClickListener(v -> {
            String username = textInputUsername.getEditText().getText().toString().trim();
            String password = textInputPassword.getEditText().getText().toString().trim();
            if (usernameValidation() & passwordValidation() & confirmPasswordValidation()) {
                String passwordHash = hashPassword(password); // password is hashed
                Toast.makeText(SignUp.this, "Successfully created an account!", Toast.LENGTH_SHORT).show();
                sqLiteDBHandler.addUser(username, passwordHash); // null object reference
                Intent intent = new Intent(SignUp.this, LogIn.class);
                startActivity(intent);
            } else {
                Toast.makeText(SignUp.this, "Incorrect! Try again!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private boolean usernameValidation() {
        String inputUsername = textInputUsername.getEditText().getText().toString().trim();

        if (inputUsername.isEmpty()) {
            textInputUsername.setError("Fill this field!");
            return false;
        } else if (inputUsername.length() > 15) {
            textInputUsername.setError("Username too long!");
            return false;
        } else if (inputUsername.length() < 6) {
            textInputUsername.setError("Username too short! Minimum length is 6 characters");
            return false;
        } else {
            textInputUsername.setError(null);
            return true;
        }
    }

    private boolean passwordValidation() {
        String inputPassword = textInputPassword.getEditText().getText().toString().trim();

        if (inputPassword.isEmpty()) {
            textInputPassword.setError("Fill this field!");
            return false;
        } else if (!PASSWORD_PATTERN.matcher(inputPassword).matches()) {
            textInputPassword.setError("Password too weak! Include at least: 1 lowercase letter, 1 uppercase letter, 1 special character, 1 digit.");
            return false;
        } else {
            textInputPassword.setError(null);
            return true;
        }
    }

    private boolean confirmPasswordValidation() {
        String inputPassword = textInputPassword.getEditText().getText().toString().trim();
        String inputConfirmPassword = textInputConfirmPassword.getEditText().getText().toString().trim();

        if (inputConfirmPassword.isEmpty()) {
            textInputConfirmPassword.setError("Fill this field!");
            return false;
        } else if (!inputConfirmPassword.equals(inputPassword)) {
            textInputConfirmPassword.setError("The password does not match.");
            return false;
        } else {
            textInputConfirmPassword.setError(null);
            return true;
        }
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

    public void goBack(View view) {
        onBackPressed();
    }
}

