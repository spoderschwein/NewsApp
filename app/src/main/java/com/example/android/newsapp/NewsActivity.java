package com.example.android.newsapp;

import android.app.LoaderManager;
import android.content.Context;
import android.content.Intent;
import android.content.Loader;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.app.LoaderManager.LoaderCallbacks;
import android.widget.TextView;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import static android.icu.lang.UCharacter.GraphemeClusterBreak.L;
import static android.icu.lang.UCharacter.GraphemeClusterBreak.T;
import static android.os.Build.VERSION_CODES.M;


public class NewsActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<List<News>> {

    private NewsAdapter mNewsAdapter;
    public static String NEWS_REQUEST_URL = "http://content.guardianapis.com/search?";
    //    public static String NEWS_REQUEST_URL = " http://content.guardianapis.com/search?q=world&api-key=81ca43a0-5473-4176-96bc-7f6511db51c0";
    private static int NEWS_LOADER_ID = 1;
    LoaderManager loaderManager;
    private boolean isConnected;
    private TextView mEmptyView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news);


        ListView newsList = (ListView) findViewById(R.id.list);

        mNewsAdapter = new NewsAdapter(NewsActivity.this, new ArrayList<News>());
        newsList.setAdapter(mNewsAdapter);

        mEmptyView = (TextView) findViewById(R.id.nothingToShow);
        newsList.setEmptyView(mEmptyView);

        // check if internet connection is true!
        ConnectivityManager connectivityManager = (ConnectivityManager) getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = connectivityManager.getActiveNetworkInfo();
        isConnected = activeNetwork != null && activeNetwork.isConnectedOrConnecting();





        if (isConnected == false) {
            TextView noConnec = (TextView) findViewById(R.id.noInternetConnection);
            mEmptyView.setVisibility(View.GONE);
            noConnec.setText(R.string.noConnection);
        }


        loaderManager = getLoaderManager();
        loaderManager.initLoader(NEWS_LOADER_ID, null, this);

        newsList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                // find current item
                News currentNews = mNewsAdapter.getItem(i);

                // convert the String url into a uri obj so we can pass it to a intent
                Uri newsUri = Uri.parse(currentNews.getmURL());
                //create a new intent
                Intent websiteIntent = new Intent(Intent.ACTION_VIEW, newsUri);
                //sned intent
                startActivity(websiteIntent);
            }
        });

    }

    @Override
    public Loader<List<News>> onCreateLoader(int i, Bundle bundle) {
        //create Preference Manager to set prio
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        String newsPref = sharedPreferences.getString(getString(R.string.settings_news_prio_key), getString(R.string.settings_news_prio_default));

        // create uri to build new URL
        Uri baseUri = Uri.parse(NEWS_REQUEST_URL);
        Uri.Builder uriBuilder = baseUri.buildUpon();
        uriBuilder.appendQueryParameter("api-key", "81ca43a0-5473-4176-96bc-7f6511db51c0");
        uriBuilder.appendQueryParameter("q", newsPref);
        Log.v("NewsActivity", uriBuilder.toString());

        return new NewsLoader(this, uriBuilder.toString());
    }

    public void onLoadFinished(Loader<List<News>> loader, List<News> news) {
        //clear the adapter
        mNewsAdapter.clear();
        if (news != null && !news.isEmpty()) {
            // add everything
            mNewsAdapter.addAll(news);

        }
        if (mNewsAdapter.isEmpty()) {
            mEmptyView.setText(R.string.emptyView);
        }

    }

    public void onLoaderReset(Loader<List<News>> loader) {
        // Reset loader
        mNewsAdapter.clear();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            Intent settingsIntent = new Intent(this, SettingsActivity.class);
            startActivity(settingsIntent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
