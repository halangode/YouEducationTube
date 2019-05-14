package com.halangode.eduapp.model;

import android.content.Context;

import com.halangode.eduapp.R;
import com.halangode.eduapp.webservices.bean.YouTubeSearchResponse;
import com.halangode.eduapp.webservices.retrofit.ApiUtils;
import com.halangode.eduapp.webservices.retrofit.RetrofitInterface;

import java.util.HashMap;
import retrofit2.Callback;

/**
 * Created by Harikumar Alangode on 23-Jul-17.
 */

public class DataManager {

    public static int MAX_RESULTS = 20;
    private RetrofitInterface.YouTubeSearchInterface retrofitInstance;
    private String EDUCATION_ID = "27";

    private static DataManager dataManager;

    private DataManager(){
    }

    public static DataManager getInstance(){
        if(dataManager == null){
            dataManager = new DataManager();
        }
        return dataManager;
    }

    private HashMap<String, String> getArguments(String key, Context context){
        HashMap<String, String> query = new HashMap<>();
        query.put("part", "snippet");
        query.put("q", key);
        query.put("type", "video");
        query.put("maxResults", String.valueOf(MAX_RESULTS));
        query.put("topics", "/m/01k8wb");
        query.put("videoCaption", "closedCaption");
        query.put("safeSearch", "strict");
        query.put("videoCategoryId", EDUCATION_ID);
        query.put("key", context.getString(R.string.youtube_api_key));

        return query;
    }



    public void searchYouTubeVideo(String key, Callback<YouTubeSearchResponse> callback, Context context){
        key.replace(" ", "+");
        retrofitInstance = ApiUtils.getInstance();
        retrofitInstance.getSearchResults(getArguments(key, context)).enqueue(callback);
    }

    public void searchYouTubeVideo(String key, Callback<YouTubeSearchResponse> callback, String token, Context context){
        key.replace(" ", "+");
        HashMap<String, String> arguments = getArguments(key, context);
        arguments.put("pageToken", token);
        retrofitInstance = ApiUtils.getInstance();
        retrofitInstance.getSearchResults(arguments).enqueue(callback);
    }
}
