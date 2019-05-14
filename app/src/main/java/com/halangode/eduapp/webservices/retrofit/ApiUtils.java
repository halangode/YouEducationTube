package com.halangode.eduapp.webservices.retrofit;

/**
 * Created by Harikumar Alangode on 02-Jul-17.
 */

public class ApiUtils {

    private static final String BASE_URL = "https://www.googleapis.com/youtube/v3/";

    public static RetrofitInterface.YouTubeSearchInterface getInstance() {
        return RetrofitClient.getClient(BASE_URL).create(RetrofitInterface.YouTubeSearchInterface.class);
    }
}
