package com.halangode.eduapp.presenter;

import android.content.Context;
import android.util.Log;

import com.halangode.eduapp.model.DataManager;
import com.halangode.eduapp.presenter.contract.HomeActivityContract;
import com.halangode.eduapp.view.activity.HomeActivity;
import com.halangode.eduapp.webservices.bean.YouTubeSearchResponse;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Harikumar Alangode on 23-Jul-17.
 */

public class HomeActivityPresenter implements HomeActivityContract.UserAction {
    HomeActivityContract.View homeView;
    DataManager dataManager;
    Context context;

    public HomeActivityPresenter(Context context){
        homeView = (HomeActivity) context;
        dataManager = DataManager.getInstance();
        this.context = context;
    }

    @Override
    public void onSearchClicked(String s) {


        Callback<YouTubeSearchResponse> callback = new Callback<YouTubeSearchResponse>() {
            @Override
            public void onResponse(Call<YouTubeSearchResponse> call, Response<YouTubeSearchResponse> response) {
                Log.d("Response", "onResponse: " + response.toString());
                homeView.onSearchResultsReady(response.body());
            }

            @Override
            public void onFailure(Call<YouTubeSearchResponse> call, Throwable t) {
            }
        };

        dataManager.searchYouTubeVideo(s, callback, context);
    }

    @Override
    public void onSearchClicked(String s, String nextPageToken) {
        Callback<YouTubeSearchResponse> callback = new Callback<YouTubeSearchResponse>() {
            @Override
            public void onResponse(Call<YouTubeSearchResponse> call, Response<YouTubeSearchResponse> response) {
                Log.d("Response", "onResponse: " + response.toString());
                homeView.onNextPageReceived(response.body());
            }

            @Override
            public void onFailure(Call<YouTubeSearchResponse> call, Throwable t) {
            }
        };

        dataManager.searchYouTubeVideo(s, callback, nextPageToken, context);
    }
}
