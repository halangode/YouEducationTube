package com.halangode.eduapp.view.activity;

import android.app.ProgressDialog;
import android.app.SearchManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.SearchView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.halangode.eduapp.R;
import com.halangode.eduapp.presenter.HomeActivityPresenter;
import com.halangode.eduapp.presenter.contract.HomeActivityContract;
import com.halangode.eduapp.view.adapter.SearchListAdapter;
import com.halangode.eduapp.view.fragment.TopicListDialogFragment;
import com.halangode.eduapp.webservices.bean.Item;
import com.halangode.eduapp.webservices.bean.YouTubeSearchResponse;

import java.util.ArrayList;
import java.util.List;


public class HomeActivity extends AppCompatActivity implements HomeActivityContract.View, SearchView.OnQueryTextListener, TopicListDialogFragment.Listener{

    private RecyclerView searchListRV;
    private SearchListAdapter searchListAdapter;
    private List<Item> searchItems;
    private SearchView searchView;

    private HomeActivityContract.UserAction homePresenter;
    private YouTubeSearchResponse youTubeSearchResponse;
    private MenuItem searchMenuItem;
    private ProgressDialog progressDialog;
    private TextView welcomeTV;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        if(getResources().getString(R.string.youtube_api_key).equals("paste your YouTube API key here")) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage("You have not set the youtube api key in the strings resource. Set the Youtube api key to the 'youtube_api_key' key in the string resource")
                    .setCancelable(false)
                    .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            HomeActivity.this.finish();
                        }
                    })
                    .create()
                    .show();
        }

        searchItems = new ArrayList<>();
        welcomeTV = (TextView) findViewById(R.id.welcomeTV);
        searchListRV = (RecyclerView) findViewById(R.id.search_rv);

        if(savedInstanceState != null){
            searchItems = savedInstanceState.getParcelableArrayList("searchItems");
            youTubeSearchResponse = savedInstanceState.getParcelable("searchResponse");
            if(youTubeSearchResponse != null && youTubeSearchResponse.getItems().size() > 0){
                searchListRV.setVisibility(View.VISIBLE);
                welcomeTV.setVisibility(View.GONE);
            }

        }
        homePresenter = new HomeActivityPresenter(this);

        progressDialog = new ProgressDialog(this);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setCancelable(false);


        searchListAdapter = new SearchListAdapter(searchItems, this);
        searchListRV.setLayoutManager(new LinearLayoutManager(this));
        searchListRV.setAdapter(searchListAdapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.search_menu, menu);

        SearchManager searchManager = (SearchManager)
                getSystemService(Context.SEARCH_SERVICE);
        searchMenuItem = menu.findItem(R.id.search);
        searchView = (SearchView) searchMenuItem.getActionView();
        searchView.setMaxWidth( Integer.MAX_VALUE );
        searchView.setSearchableInfo(searchManager.
                getSearchableInfo(getComponentName()));
        searchView.setSubmitButtonEnabled(true);
        searchView.setOnQueryTextListener(this);

        return true;
    }


    @Override
    public void onSearchResultsReady(YouTubeSearchResponse response) {
        searchListRV.setVisibility(View.VISIBLE);
        welcomeTV.setVisibility(View.GONE);
        dismissProgressDialog();
        searchView.clearFocus();
        youTubeSearchResponse = response;
        searchItems.clear();
        searchItems = youTubeSearchResponse.getItems();
        searchListAdapter = new SearchListAdapter(searchItems, HomeActivity.this);
        searchListRV.setAdapter(searchListAdapter);
    }

    @Override
    public void onVideoItemClicked(Item item) {
        Intent intent = new Intent(this, PlayVideoActivity.class);
        intent.putExtra("videoId", item.getId().getVideoId());
        startActivity(intent);
    }

    @Override
    public void onMoreClicked() {
        showProgressDialog("Fetching...");
        homePresenter.onSearchClicked(searchView.getQuery().toString(), youTubeSearchResponse.getNextPageToken());
    }

    @Override
    public void onNextPageReceived(YouTubeSearchResponse response) {
        dismissProgressDialog();
        youTubeSearchResponse = response;
        for(Item i : youTubeSearchResponse.getItems()){
            searchItems.add(i);
        }
        searchListAdapter.notifyDataSetChanged();
    }

    @Override
    public void onTocButtonClicked(Item item) {
        TopicListDialogFragment.newInstance(6, item).show(getSupportFragmentManager(), "dialog");
    }

    public void showProgressDialog(String s){
        progressDialog.setMessage(s);
        progressDialog.show();
    }

    public void dismissProgressDialog(){
        if(progressDialog.isShowing()){
            progressDialog.dismiss();
        }
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        homePresenter.onSearchClicked(query);
        showProgressDialog("Searching...");
        return false;
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        if(searchItems != null){
            outState.putParcelableArrayList("searchItems", new ArrayList<>(searchItems));
        }

        if(youTubeSearchResponse != null){
            outState.putParcelable("searchResponse", youTubeSearchResponse);
        }
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onDestroy() {
        dismissProgressDialog();
        super.onDestroy();
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        return false;
    }

    @Override
    public void onTopicClicked(Item item, int position) {
        Intent intent = new Intent(this, PlayVideoActivity.class);
        intent.putExtra("videoId", item.getId().getVideoId());
        intent.putExtra("duration", getResources().getStringArray(R.array.duration)[position]);
        startActivity(intent);
    }
}
