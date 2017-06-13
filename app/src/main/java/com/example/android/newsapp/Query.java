package com.example.android.newsapp;

import android.text.TextUtils;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

import static android.os.Build.VERSION_CODES.M;
import static com.example.android.newsapp.R.id.header;

/**
 * Created by Marcoli on 27.04.2017.
 */

public class Query {

    private static final String LOG_TAG = Query.class.getSimpleName();


    public Query() {
    }

    public static List<News> fetchNewsData(String requestURL) {
        URL url = createURL(requestURL);
        String jsonResponse = null;

        try {
            jsonResponse = makeHttpRequest(url);
        } catch (IOException e) {
            e.printStackTrace();
        }

        List<News> news = getNewsFromJson(jsonResponse);

        return news;
    }


    private static URL createURL(String stringUrl) {
        URL url = null;

        try {
            url = new URL(stringUrl);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return url;
    }


    private static String makeHttpRequest(URL url) throws IOException {
        String jsonResponse = "";

        //if URL is null return empty String
        if (url == null)
            return jsonResponse;

        HttpURLConnection urlConnection = null;
        InputStream inputStream = null;

        try {
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setReadTimeout(10000 /*milliseconds*/);
            urlConnection.setConnectTimeout(15000 /*milliseconds*/);
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();
            if (urlConnection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                inputStream = urlConnection.getInputStream();
                jsonResponse = readFromStream(inputStream);
                Log.e(LOG_TAG, "The Response code is: " + urlConnection.getResponseMessage());
            } else {
                Log.e(LOG_TAG, "Error response code: " + urlConnection.getResponseCode());
            }
        } catch (ProtocolException e) {
            Log.e(LOG_TAG, "Problem w/ connection", e);
        } catch (IOException e) {
            Log.e(LOG_TAG, "Problem w/ connection1");
            e.printStackTrace();
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (inputStream != null) {
                inputStream.close();
            }
            return jsonResponse;
        }
    }

    private static String readFromStream(InputStream inputStream) throws IOException {
        StringBuilder output = new StringBuilder();
        if (inputStream != null) {
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            String str = bufferedReader.readLine();
            while (str != null) {
                output.append(str);
                str = bufferedReader.readLine();
            }
        }
        return output.toString();
    }


    public static List<News> getNewsFromJson(String newsJson) {
        if (TextUtils.isEmpty(newsJson)) {
            return null;
        }

        List<News> news = new ArrayList<>();



        try {
            // Call a JsonObject
            JSONObject jsonObject = new JSONObject(newsJson);
            // Call an Array
            JSONObject jsonObject1 = jsonObject.getJSONObject("response");
            JSONArray jsonArray = jsonObject1.getJSONArray("results");

            String header;
            String text;
            String url;

           for (int i =0; i<jsonArray.length();i++){
               JSONObject obj = jsonArray.getJSONObject(i);
                header = obj.getString("sectionName");
                text = obj.getString("webTitle");
                url = obj.getString("webUrl");

               News newsObj = new News(header, text, url);
               news.add(newsObj);
           }

        } catch (JSONException e) {
            Log.e("Query", "Problem parsing the News JSON results", e);
        }
        return news;
    }
}

