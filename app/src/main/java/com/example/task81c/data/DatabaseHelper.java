package com.example.task81c.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.Nullable;

import com.example.task81c.model.Playlist;
import com.example.task81c.model.User;
import com.example.task81c.util.Util;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {
    public DatabaseHelper(@Nullable Context context) {
        super(context, Util.DATABASE_NAME, null, Util.DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        //users table
        String CREATE_USER_TABLE =
                "CREATE TABLE " + Util.USERS_TABLE_NAME
                        + "(" + Util.USERS_ID + " INTEGER PRIMARY KEY AUTOINCREMENT , " //automatically add new id to every new entry
                        + Util.USERS_USERNAME + " TEXT NOT NULL UNIQUE,"
                        + Util.USERS_PASSWORD + " TEXT)";

        //playlists table
        String CREATE_PLAYLIST_TABLE =
                "CREATE TABLE " + Util.PLAYLISTS_TABLE_NAME
                        + "(" + Util.PLAYLISTS_ID + " INTEGER PRIMARY KEY AUTOINCREMENT , " //automatically add new id to every new entry
                        + Util.PLAYLISTS_URL + " TEXT NOT NULL,"
                        + Util.USERS_ID + " TEXT NOT NULL)";

        sqLiteDatabase.execSQL(CREATE_USER_TABLE);
        sqLiteDatabase.execSQL(CREATE_PLAYLIST_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        String DROP_USER_TABLE = "DROP TABLE IF EXISTS " + Util.USERS_TABLE_NAME;
        String DROP_PLAYLIST_TABLE = "DROP TABLE IF EXISTS " + Util.PLAYLISTS_TABLE_NAME;

        onCreate(sqLiteDatabase);
    }

    //adds a new user to the database
    public long insertUser(User user) {
        SQLiteDatabase db = this.getWritableDatabase();

        //define content to use for query
        ContentValues contentValues = new ContentValues();
        contentValues.put(Util.USERS_USERNAME, user.get_username());
        contentValues.put(Util.USERS_PASSWORD, user.get_password());

        //make sure user isn't already in database
        if (fetchUser(user.get_username())) {
            return -1;
        }

        //attempt to insert user into database and return the row id
        long newRowId = db.insert(Util.USERS_TABLE_NAME, null, contentValues);
        db.close();

        return newRowId;
    }

    //adds a new user to the database
    public long insertPlaylist(Playlist playlist) {
        SQLiteDatabase db = this.getWritableDatabase();

        //define content to use for query
        ContentValues contentValues = new ContentValues();
        contentValues.put(Util.PLAYLISTS_URL, playlist.get_url());
        contentValues.put(Util.USERS_ID, playlist.get_userId());

        //attempt to insert user into database and return the row id
        long newRowId = db.insert(Util.PLAYLISTS_TABLE_NAME, null, contentValues);
        db.close();

        return newRowId;
    }

    //fetches first user with given username
    public boolean fetchUser(String username) {
        SQLiteDatabase db = this.getReadableDatabase();
        int numberOfRows;

        //run query to search for user with given username
        Cursor cursor = db.query(Util.USERS_TABLE_NAME,
                new String[]{Util.USERS_ID},
                Util.USERS_USERNAME + "=?",
                new String[]{username}, null, null, null);
        numberOfRows = cursor.getCount();

        if (numberOfRows > 0) {
            return true;
        }
        else { return false; }
    }

    //selects user with given username and password
    public boolean fetchUser(String username, String password) {
        SQLiteDatabase db = this.getReadableDatabase();
        int numberOfRows;

        try {
            Cursor cursor = db.query(Util.USERS_TABLE_NAME, new String[]{Util.USERS_ID}, Util.USERS_USERNAME + "=? and " + Util.USERS_PASSWORD + "=?",
                    new String[]{username, password}, null, null, null);
            numberOfRows = cursor.getCount();
        }
        catch (Exception e) {
            Log.e("Error", e.getMessage());
            numberOfRows = 0;
        }

        if (numberOfRows > 0) { return true; }
        else { return false; }
    }

    //returns the user with selected username
    public User getUser(String username) {
        User user;

        //search database for the entry that contains the given username
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(Util.USERS_TABLE_NAME,
                new String[]{"*"},                      //make sure we select all columns
                Util.USERS_USERNAME + "=?",     //only select rows with given username
                new String[]{username}, null, null, null);

        //move to our first position
        if (cursor.moveToFirst()) {
            user = new User();
            user.set_userId(cursor.getInt(0));
            user.set_username(cursor.getString(1));
            user.set_password(cursor.getString(2));
            return user;
        }

        //didn't get an entry, so return null
        return null;
    }

    //returns playlist belonging to specified user id
    public List<Playlist> fetchAllPlaylistsFrom(int userID) {
        List<Playlist> playList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        //define query in string format, then run that query
        Cursor cursor = db.query(Util.PLAYLISTS_TABLE_NAME,
                new String[]{"*"},                      //make sure we select all columns
                Util.USERS_ID + "=?",     //only select rows with given username
                new String[]{String.valueOf(userID)}, null, null, null);
        Log.e("Database", "Attempting to add Playlist");

        //starts from first row in database, each iteration cycles to next row until hitting last
        if (cursor.moveToFirst()) {
            int i = 0;
            do {
                i++;
                Log.e("Playlist number ", String.valueOf(i));
                Playlist playlist = new Playlist();
                playlist.set_playlistId(cursor.getInt(0));
                String url = cursor.getString(1);
                playlist.set_url(url);
                playlist.set_userId(cursor.getInt(2));
                playlist.set_fullUrl("https://www.youtube.com/watch?v=" + url);

                playList.add(playlist);
            }while (cursor.moveToNext());
        }
        else { Log.e("Playlist Fail", "Failed to add playlists"); }

        //filled our list, so return it
        return playList;
    }
}
