package com.example.android.newsapp;

import android.content.AsyncTaskLoader;
import android.content.Context;
import android.content.Loader;

import java.util.List;

/**
 * Created by Marcoli on 28.04.2017.
 */
public class NewsLoader extends AsyncTaskLoader<List<News>> {

    private static final String LOG_TAG = NewsLoader.class.getSimpleName();

    //Books url
    private String mUrl;

    public NewsLoader(Context context, String mUrl) {
        super(context);
        this.mUrl = mUrl;
    }

    @Override
    protected void onStartLoading(){forceLoad();}

    public List<News> loadInBackground(){
        if(mUrl == null){
            return null;
        }

        List<News> news = Query.fetchNewsData(mUrl);
        return  news;
    }
}
