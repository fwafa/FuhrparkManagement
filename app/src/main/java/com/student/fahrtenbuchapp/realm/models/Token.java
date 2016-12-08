package com.student.fahrtenbuchapp.realm.models;

/**
 * Created by Fabian on 08.12.16.
 */

public class Token {
    private String token;

    public Token(String token) {
        this.token = token;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
