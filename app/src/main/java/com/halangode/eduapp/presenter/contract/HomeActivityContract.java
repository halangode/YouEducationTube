package com.halangode.eduapp.presenter.contract;

import com.halangode.eduapp.webservices.bean.Item;
import com.halangode.eduapp.webservices.bean.YouTubeSearchResponse;

/**
 * Created by Harikumar Alangode on 23-Jul-17.
 */

public class HomeActivityContract {
    public interface View{

        void onSearchResultsReady(YouTubeSearchResponse body);

        void onVideoItemClicked(Item item);

        void onMoreClicked();

        void onNextPageReceived(YouTubeSearchResponse body);

        void onTocButtonClicked(Item item);
    }
    
    public interface UserAction{

        void onSearchClicked(String s);

        void onSearchClicked(String s, String nextPageToken);
    }
}
