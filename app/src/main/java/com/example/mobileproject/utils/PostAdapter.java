package com.example.mobileproject.utils;

import android.app.Application;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.mobileproject.R;
import com.example.mobileproject.models.Post.Post;
import com.google.android.material.snackbar.Snackbar;

import java.util.List;

public class PostAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    public interface OnItemClickListener{
        void onItemClicked(Post p);
    }

    private static final int NORMAL_TYPE = 0;
    private static final int LOADING_TYPE = 1;
    private final List<Post> postSet;
    private final OnItemClickListener l;
    private final Application appl; //per il caricamento con Glide

    public PostAdapter(List<Post> pl, Application a, OnItemClickListener l){
        this.postSet = pl;
        this.appl = a;
        this.l = l;
    }

    @Override
    public int getItemViewType(int position){ // lazy loading
        if (postSet.get(position) == null){
            return LOADING_TYPE;
        }
        return NORMAL_TYPE;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v;
        if(viewType == NORMAL_TYPE){
            v = LayoutInflater.from(parent.getContext()).inflate(R.layout.postlist_item, parent, false);
            return new PostViewHolder(v);
        } else {
            v = LayoutInflater.from(parent.getContext()).inflate(R.layout.loading_item, parent, false);
            return new LoadingPostViewHolder(v);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if(holder instanceof PostViewHolder) {
            ((PostViewHolder) holder).bind(postSet.get(position));
        } else if (holder instanceof LoadingPostViewHolder){
            ((LoadingPostViewHolder) holder).activate();
        }
    }

    @Override
    public int getItemCount() {
        if(postSet != null){
            return postSet.size();
        }
        return 0;
    }

    public class PostViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        private final ImageView iv;
        public PostViewHolder(View item){
            super(item);
            iv = item.findViewById(R.id.post_image_bg);
            item.setOnClickListener(this);
        }
        public void bind(Post p){
            //Caricamento immagine da URL con Glide
            //TODO: sistemare riferimento ad Application e cambiare foto di rimpiazzo (se qualcuno vuole)
            Glide.with(appl).load(p.getImage()).placeholder(R.drawable.baseline_photo_camera_24).into(iv);
        }

        @Override
        public void onClick(View v) {
            l.onItemClicked(postSet.get(getAdapterPosition()));
            //Snackbar.make(v, "Item clicked", Snackbar.LENGTH_SHORT).show();
        }
    }
    public static class LoadingPostViewHolder extends RecyclerView.ViewHolder {
        private final ProgressBar pb;
        LoadingPostViewHolder(View view){
            super(view);
            pb = view.findViewById(R.id.progressbar_item);
        }
        public void activate(){
            pb.setIndeterminate(true);
        }
    }
}
