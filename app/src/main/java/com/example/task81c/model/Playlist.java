package com.example.task81c.model;

public class Playlist {
    private int _playlistId;
    private int _userId;
    private String _url;
    private String _fullUrl;

    public Playlist() { }

    public Playlist(String _url, int _userId) {
        this._url = _url;
        this._userId = _userId;
    }

    @Override
    public String toString() { return _fullUrl; }

    public int get_playlistId() {
        return _playlistId;
    }

    public void set_playlistId(int _playlistId) {
        this._playlistId = _playlistId;
    }

    public int get_userId() {
        return _userId;
    }

    public void set_userId(int _userId) {
        this._userId = _userId;
    }

    public String get_url() {
        return _url;
    }

    public void set_url(String _url) {
        this._url = _url;
    }

    public String get_fullUrl() {
        return _fullUrl;
    }

    public void set_fullUrl(String _fullUrl) {
        this._fullUrl = _fullUrl;
    }
}
