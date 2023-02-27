package com.example.myapplication;

import android.widget.TextView;

import java.util.Calendar;

public class Memo {

    private int memoID;
    private String subject;
    private String body;
    private int rating;
    private Calendar date;

    public Memo() {
        memoID = -1;
        date = Calendar.getInstance();
    }

    public int getMemoID() {
        return memoID;
    }

    public void setMemoID(int i) {
        memoID = i;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String s) {
        subject = s;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String b) {
        body = b;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int r) {
        rating = r;
    }

    public Calendar getDate() {
        return date;
    }

    public void setDate(Calendar c) {
        date = c;
    }
}
