package com.example.task81c;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.task81c.data.DatabaseHelper;
import com.example.task81c.model.User;

public class SignupPageActivity extends AppCompatActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();                   // hide the title bar
        setContentView(R.layout.activity_signup_page);

        Button newAccountButton = findViewById(R.id.CreateAccountButton);

        //signup button click response
        newAccountButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (signup() == false) { return; }
                finish();
            }
        });
    }

    private boolean signup() {
        DatabaseHelper db;
        db = new DatabaseHelper(getApplicationContext());

        //define input boxes
        EditText usernameEditText = findViewById(R.id.SignupPageUsernameEditText);
        EditText passwordEditText = findViewById(R.id.SignupPagePasswordEditText);
        EditText confPassEditText = findViewById(R.id.SignupPageConfPassEditText);

        //get strings from inputs
        String usernameString = usernameEditText.getText().toString();
        String passwordString = passwordEditText.getText().toString();
        String confPassString = confPassEditText.getText().toString();

        //return if we have empty inputs
        if (invalidFields(db, usernameString, passwordString, confPassString)) {
            return false;
        }

        //create new user and insert into database
        User newUser = new User(usernameString, passwordString);
        long result = db.insertUser(newUser);

        //fail if user wasn't entered successfully
        if (result <= 0) {
            Toast.makeText(this, "Failed to Register User!", Toast.LENGTH_SHORT).show();
            return false;
        }

        //user entered successfully, so set toast and leave this fragment
        Toast.makeText(this, "Registered user successfully!", Toast.LENGTH_SHORT).show();
        return true;
    }

    //makes sure none of the important inputs are null
    private boolean invalidFields(DatabaseHelper db, String username, String password, String confPass) {
        //make sure username and password aren't empty
        if (username.trim().equals("") || username == null) {
            Toast.makeText(this, "Username can't be empty!", Toast.LENGTH_SHORT).show();
            return true;
        }

        if (password.trim().equals("") || password == null) {
            Toast.makeText(this, "Password can't be empty!", Toast.LENGTH_SHORT).show();
            return true;
        }

        //fail if username already taken
        if (db.fetchUser(username) == true) {
            Toast.makeText(this, "Username already taken!", Toast.LENGTH_SHORT).show();
            return true;
        }

        //fail if passwords don't match
        if (confPass.equals(password) == false) {
            Toast.makeText(this, "Passwords do not match!", Toast.LENGTH_SHORT).show();
            return true;
        }

        //all checks passed, so return false
        return false;
    }
}