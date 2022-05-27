package com.example.task81c.model;

public class User {
    private int _userId;
    private String _username;
    private String _password;

    public User() { }

    public User(String _username, String _password) {
        this._username = _username;
        this._password = _password;
    }

    public int get_userId() {
        return _userId;
    }

    public void set_userId(int _userId) {
        this._userId = _userId;
    }

    public String get_username() {
        return _username;
    }

    public void set_username(String _username) {
        this._username = _username;
    }

    public String get_password() {
        return _password;
    }

    public void set_password(String _password) {
        this._password = _password;
    }
}
