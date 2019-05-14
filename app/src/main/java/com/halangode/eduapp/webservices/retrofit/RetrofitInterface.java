package com.halangode.eduapp.webservices.retrofit;

import com.halangode.eduapp.webservices.bean.YouTubeSearchResponse;

import java.util.HashMap;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.QueryMap;

/**
 * Created by Harikumar Alangode on 02-Jul-17.
 */

public class RetrofitInterface {
    public interface YouTubeSearchInterface{
        @GET("search?")
        Call<YouTubeSearchResponse> getSearchResults(@QueryMap HashMap<String, String> query);
    }
}
