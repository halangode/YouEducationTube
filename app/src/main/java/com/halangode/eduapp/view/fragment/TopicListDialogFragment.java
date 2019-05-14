package com.halangode.eduapp.view.fragment;

import android.content.Context;
import android.os.Bundle;
import androidx.annotation.Nullable;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.halangode.eduapp.R;
import com.halangode.eduapp.webservices.bean.Item;

public class TopicListDialogFragment extends BottomSheetDialogFragment {

    private static final String ARG_ITEM_COUNT = "item_count";
    private Listener mListener;
    private static Item item;

    public static TopicListDialogFragment newInstance(int itemCount, Item i) {
        item = i;
        final TopicListDialogFragment fragment = new TopicListDialogFragment();
        final Bundle args = new Bundle();
        args.putInt(ARG_ITEM_COUNT, itemCount);
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_topic_list_dialog, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        final RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.list);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(new TopicAdapter(getArguments().getInt(ARG_ITEM_COUNT)));
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        final Fragment parent = getParentFragment();
        if (parent != null) {
            mListener = (Listener) parent;
        } else {
            mListener = (Listener) context;
        }
    }

    @Override
    public void onDetach() {
        mListener = null;
        super.onDetach();
    }

    public interface Listener {
        void onTopicClicked(Item item, int position);
    }

    private class ViewHolder extends RecyclerView.ViewHolder {

        final TextView text;
        final TextView duration;

        ViewHolder(LayoutInflater inflater, ViewGroup parent) {
            super(inflater.inflate(R.layout.fragment_topic_list_dialog_item, parent, false));
            text = (TextView) itemView.findViewById(R.id.topic);
            duration = (TextView) itemView.findViewById(R.id.duration);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mListener != null) {
                        mListener.onTopicClicked(item, getAdapterPosition());
                        dismiss();
                    }
                }
            });
        }

    }

    private class TopicAdapter extends RecyclerView.Adapter<ViewHolder> {

        private final int mItemCount;
        private String[] topics;
        private String[] duration;

        TopicAdapter(int itemCount) {
            mItemCount = itemCount;
            topics = getResources().getStringArray(R.array.topics);
            duration = getResources().getStringArray(R.array.duration);
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new ViewHolder(LayoutInflater.from(parent.getContext()), parent);
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            holder.text.setText(topics[position]);
            holder.duration.setText(duration[position]);
        }

        @Override
        public int getItemCount() {
            return mItemCount;
        }

    }
}
