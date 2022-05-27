package com.example.task81c;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.example.task81c.data.DatabaseHelper;
import com.example.task81c.model.Playlist;

import java.util.ArrayList;
import java.util.List;

public class ViewPlaylistActivity extends AppCompatActivity {
    private int _user;
    private static final String TAG = "View Playlist";

    //defines
    private ListView _playlistListView;
    private List<Playlist> _playlistList;
    private ArrayList<Playlist> _playlistArrayList = new ArrayList<>();
    private ArrayAdapter<Playlist> _playlistArrayAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();                   // hide the title bar
        setContentView(R.layout.activity_view_playlist);
        getIntentExtras();

        //get user's playlist from database, returning if there's no items
        DatabaseHelper db = new DatabaseHelper(getApplicationContext());
        _playlistList = db.fetchAllPlaylistsFrom(_user);
        if (_playlistList.size() == 0) {
            Log.e(TAG, "No playlist");
            Toast.makeText(getApplicationContext(), "Playlist is empty!", Toast.LENGTH_SHORT).show();
            finish();
        }

        //connect list view with array adapter to display
        _playlistArrayList.addAll(_playlistList);
        _playlistListView = findViewById(R.id.PlaylistListView);
        _playlistArrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, _playlistArrayList);
        _playlistListView.setAdapter(_playlistArrayAdapter);

        //action when an item in the list is clicked
        _playlistListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                playVideo(position);
            }
        });
    }

    //gets the intent and extra details from previous activities
    private void getIntentExtras() {
        Intent intent = getIntent();
        _user = intent.getIntExtra("user", -1);
    }

    private void playVideo(int position) {
        String url = _playlistList.get(position).get_url();

        //go to play video activity
        Intent intent = new Intent(getApplicationContext(), VideoPlayerActivity.class);
        intent.putExtra("url", url);
        startActivity(intent);
    }
}