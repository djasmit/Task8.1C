package com.example.task81c;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.task81c.data.DatabaseHelper;
import com.example.task81c.model.User;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();                   // hide the title bar
        setContentView(R.layout.activity_main);

        EditText usernameEditText = findViewById(R.id.LoginPageUsernameEditText);
        EditText passwordEditText = findViewById(R.id.LoginPagePasswordEditText);

        //define buttons
        Button loginButton = findViewById(R.id.LoginPageLoginButton);
        Button signupButton = findViewById(R.id.LoginPageSignupButton);

        //login button click response
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                login();
            }
        });

        //signup button click response
        signupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), SignupPageActivity.class);
                startActivity(intent);
            }
        });
    }

    private void login() {
        DatabaseHelper db;
        db = new DatabaseHelper(getApplicationContext());

        EditText usernameEditText = findViewById(R.id.LoginPageUsernameEditText);
        EditText passwordEditText = findViewById(R.id.LoginPagePasswordEditText);

        String usernameString = usernameEditText.getText().toString();
        String passwordString = passwordEditText.getText().toString();

        //return if input fields empty
        if (invalidFields(db, usernameString, passwordString)) { return; }

        //successfully logged in, so get user details and log in
        Toast.makeText(this, "Successfully logged in!", Toast.LENGTH_SHORT).show();
        User user = db.getUser(usernameString);
        int userId = user.get_userId();

        startLoginActivity(userId);
    }

    //check for invalid inputs
    private boolean invalidFields(DatabaseHelper db, String username, String password) {
        db = new DatabaseHelper(getApplicationContext());

        //make sure username and password aren't empty
        if (username.trim().equals("") || username == null) {
            Toast.makeText(this, "Username can't be empty!", Toast.LENGTH_SHORT).show();
            return true;
        }

        if (password.trim().equals("") || password == null) {
            Toast.makeText(this, "Password can't be empty!", Toast.LENGTH_SHORT).show();
            return true;
        }

        //fail if wrong user name
        if (db.fetchUser(username) == false) {
            Toast.makeText(this, "Incorrect username.", Toast.LENGTH_SHORT).show();
            return true;
        }

        //fail if wrong username + password
        if (db.fetchUser(username, password) == false) {
            Toast.makeText(this, "Incorrect password.", Toast.LENGTH_SHORT).show();
            return true;
        }

        return false;
    }

    //starts activity with intent
    private void startLoginActivity(int userId) {
        Intent intent = new Intent(getApplicationContext(), HomePageActivity.class);
        intent.putExtra("user", userId);
        startActivity(intent);
    }

    @Override
    protected void onStop() {
        super.onStop();

        //define inputs
        EditText usernameEditText = findViewById(R.id.LoginPageUsernameEditText);
        EditText passwordEditText = findViewById(R.id.LoginPagePasswordEditText);

        //clear username and password boxes for security
        usernameEditText.setText(null);
        passwordEditText.setText(null);
    }
}