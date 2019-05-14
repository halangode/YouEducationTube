package com.halangode.eduapp.view.adapter;

import android.content.Context;

import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.youtube.player.YouTubeThumbnailView;
import com.halangode.eduapp.R;
import com.halangode.eduapp.model.DataManager;
import com.halangode.eduapp.presenter.contract.HomeActivityContract;
import com.halangode.eduapp.view.activity.HomeActivity;
import com.halangode.eduapp.webservices.bean.Item;

import java.util.List;

/**
 * Created by Harikumar Alangode on 23-Jul-17.
 */

public class SearchListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{
    private List<Item> mDataset;
    public Context context;
    private HomeActivityContract.View presenter;


    private class VIEW_TYPES {
        public static final int Normal = 2;
        public static final int Footer = 3;
    }


    public SearchListAdapter(List<Item> myDataset, Context context) {
        mDataset = myDataset;
        this.context = context;
        presenter = (HomeActivity) context;
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;
        switch (viewType){
            case VIEW_TYPES.Normal:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.search_row, parent, false);
                return new ListViewHolder(view);

            case VIEW_TYPES.Footer:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.footer_row, parent, false);
                return new FooterVH(view);
        }

        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder vh, int position) {
        if(vh instanceof ListViewHolder){
            ListViewHolder holder = (ListViewHolder) vh;
            final Item item = mDataset.get(position);
            holder.videoTitle.setText(item.getSnippet().getTitle());
            holder.videoDescription.setText(item.getSnippet().getDescription());
            Glide.with(context.getApplicationContext())
                    .load(item.getSnippet().getThumbnails().getMedium().getUrl())
                    .thumbnail(Glide.with(context.getApplicationContext()).load(R.drawable.loading_icon))
                    .into(holder.thumbnail);

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    presenter.onVideoItemClicked(item);
                }
            });

            holder.tocButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    presenter.onTocButtonClicked(item);
                }
            });

        }
        else{
            FooterVH footerVH = (FooterVH) vh;

            if(mDataset.size() != 0 && mDataset.size() % DataManager.MAX_RESULTS == 0){
                footerVH.moreTV.setVisibility(View.VISIBLE);
            }
            else{
                footerVH.moreTV.setVisibility(View.GONE);
            }
            footerVH.moreTV.setText("more...");
            footerVH.moreTV.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    presenter.onMoreClicked();
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return mDataset.size() + 1;
    }

    @Override
    public int getItemViewType(int position) {

        if(position == mDataset.size())
            return VIEW_TYPES.Footer;
        else
            return VIEW_TYPES.Normal;
    }

    public class ListViewHolder extends RecyclerView.ViewHolder {

        TextView videoTitle;
        TextView videoDescription;
        YouTubeThumbnailView thumbnail;
        ImageButton tocButton;

        public ListViewHolder(View itemView) {
            super(itemView);
            thumbnail = (YouTubeThumbnailView) itemView.findViewById(R.id.videoThumbnail);
            videoTitle = (TextView) itemView.findViewById(R.id.videoTitle);
            videoDescription = (TextView) itemView.findViewById(R.id.videoDescription);
            tocButton = (ImageButton) itemView.findViewById(R.id.tocButton);
        }
    }

    public class FooterVH extends RecyclerView.ViewHolder {

        TextView moreTV;


        public FooterVH(View itemView) {
            super(itemView);
            moreTV = (TextView) itemView.findViewById(R.id.footer);
        }
    }
}
