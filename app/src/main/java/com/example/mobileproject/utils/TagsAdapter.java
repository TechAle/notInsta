package com.example.mobileproject.utils;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mobileproject.R;

import java.util.List;

public class TagsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    public interface OnItemClickListener{
        void onTagClicked(String s, boolean selected);
    }
    private static final int SELECTED = 1;
    private static final int NORMAL = 0;
    private final List<String> tagList;
    private final OnItemClickListener l;
    public TagsAdapter(List<String> tagList, OnItemClickListener l) {
        this.tagList = tagList;
        this.l = l;
    }
    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.taglist_item, parent, false);
        return new TagViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof TagViewHolder){
            ((TagViewHolder)holder).bind(tagList.get(position));
        }
    }

    @Override
    public int getItemCount() {
        if(tagList != null){
            return tagList.size();
        }
        return 0;
    }

    public class TagViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        private final TextView tagContainer;
        private boolean selected;
        public TagViewHolder(@NonNull View itemView) {
            super(itemView);
            tagContainer = itemView.findViewById(R.id.tagView);
            itemView.setOnClickListener(this);
        }
        public void bind(String s){
            tagContainer.setText(s);
            selected = false;
        }

        @Override
        public void onClick(View v) {
            selected = !selected;
            l.onTagClicked((String) tagContainer.getText(), selected);
        }
    }
}
