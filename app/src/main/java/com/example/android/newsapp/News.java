package com.example.android.newsapp;

/**
 * Created by Marcoli on 27.04.2017.
 */

public class News {

    private String  mHeader;
    private String mText;
    private String mURL;

    public News(String header, String text , String url) {
        this.mHeader = header;
        this.mText = text;
        this.mURL = url;
    }

    public String getmURL() {
        return mURL;
    }

    public String getmHeader() {
        return mHeader;
    }

    public String getmText() {
        return mText;
    }


}
