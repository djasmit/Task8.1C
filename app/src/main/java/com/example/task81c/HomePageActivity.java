package com.example.task81c;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.task81c.data.DatabaseHelper;
import com.example.task81c.model.Playlist;
import com.example.task81c.model.User;

public class HomePageActivity extends AppCompatActivity {
    private int _user;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();                   // hide the title bar
        setContentView(R.layout.activity_home_page);
        getIntentExtras();

        Log.e("Intent", String.valueOf(_user));

        //define buttons
        Button PlayVideoButton = findViewById(R.id.HomePagePlayButton);
        Button ViewPlaylistButton = findViewById(R.id.HomePageViewButton);
        Button AddToListButton = findViewById(R.id.HomePageAddButton);

        //play video button actions
        PlayVideoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                playVideo();
            }
        });

        //view playlist button actions
        ViewPlaylistButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                displayPlaylist();
            }
        });

        //add to list button actions
        AddToListButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addToPlaylist();
            }
        });
    }

    //gets the intent and extra details from previous activities
    private void getIntentExtras() {
        Intent intent = getIntent();
        _user = intent.getIntExtra("user", -1);
    }

    //play the youtube video given in input box
    private void playVideo() {

        //get url from input box and convert to string
        EditText urlEditText = findViewById(R.id.HomePageURLEditText);
        String urlString = urlEditText.getText().toString();

        //return if input is invalid
        if (invalidFields(urlString)) { return; }

        //valid input, so start video player activity
        Intent intent = new Intent(getApplicationContext(), VideoPlayerActivity.class);
        intent.putExtra("url", urlString);
        startActivity(intent);
    }

    //jumps to the display playlist activity
    private void displayPlaylist() {
        //valid input, so start video player activity
        Intent intent = new Intent(getApplicationContext(), ViewPlaylistActivity.class);
        intent.putExtra("user", _user);
        startActivity(intent);
    }

    //adds the input url to the user's playlist
    private void addToPlaylist() {
        DatabaseHelper db = new DatabaseHelper(getApplicationContext());

        //get url from input box and convert to string
        EditText urlEditText = findViewById(R.id.HomePageURLEditText);
        String urlString = urlEditText.getText().toString();

        //return if input is invalid
        if (invalidFields(urlString)) { return; }

        //insert url with given userID into playlist database
        Playlist newPlaylist = new Playlist(urlString, _user);
        long result = db.insertPlaylist(newPlaylist);

        //fail if user wasn't entered successfully
        if (result <= 0) {
            Toast.makeText(this, "Failed to add to video to playlist!", Toast.LENGTH_SHORT).show();
            return;
        }

        //user entered successfully, so set toast and leave this fragment
        Toast.makeText(this, "Video added to playlist successfully!", Toast.LENGTH_SHORT).show();
    }

    //check if given input is invalid
    private boolean invalidFields(String url) {
        String trimmedUrl = url.trim();

        //make sure username and password aren't empty
        if (trimmedUrl.equals("") || url == null) {
            Toast.makeText(this, "URL can't be empty!", Toast.LENGTH_SHORT).show();
            return true;
        }

        //valid youtube URLs are 11 character codes
        if (trimmedUrl.length() != 11) {
            Toast.makeText(this, "Invalid URL!", Toast.LENGTH_SHORT).show();
            return true;
        }

        //all fields valid, so return false
        return false;
    }

}