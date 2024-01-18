package com.example.mobileproject.utils;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mobileproject.R;
import com.example.mobileproject.models.Post;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;

import java.util.List;


public class PostAdapter extends RecyclerView.Adapter<PostAdapter.PostViewHolder> {

    private final List<Post> postSet;
    public static class PostViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        private final ImageView iv;
        public PostViewHolder(View item){
            super(item);
            //TODO: fare l'ID
            iv = item.findViewById(R.id.qualcosa);
            item.setOnClickListener(this);
        }
        //TODO: sistemare il bind
        public void bind(Post p){
            iv.setImageDrawable(p.photo);
        }

        @Override
        public void onClick(View v) {
            //TODO: sistemare questa funzione
            Snackbar.make(v, "Item clicked", BaseTransientBottomBar.LENGTH_SHORT).show();
        }
    }
    public PostAdapter(List<Post> pl){
        this.postSet = pl;
    }

    //TODO: uncomment these lines
/*    @Override
    public int getItemViewType(int position){
        if (postSet.get(position) == null){
            return *****;
        }
        return *****;
    }*/

    @NonNull
    @Override
    public PostViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v;
/*        if(viewType == *****){
            v = LayoutInflater.from(parent.getContext()).inflate(R.layout.postlist_item, parent, false);
            return new PostViewHolder(v);
        } else{
            v = LayoutInflater.from(parent.getContext()).inflate(R.layout.postlist_item, parent, false);
            return new LoadingPostViewHolder(v);
        }*/
        //TODO: uncomment previous lines and comment nexts; returning type: RecyclerView.ViewHolder
        v = LayoutInflater.from(parent.getContext()).inflate(R.layout.postlist_item, parent, false);
        return new PostViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull PostViewHolder holder, int position) {
        holder.bind(postSet.get(position));
    }

    @Override
    public int getItemCount() {
        if(postSet != null){
            return postSet.size();
        }
        return 0;
    }

    public static class LoadingPostViewHolder extends RecyclerView.ViewHolder {
        private final ProgressBar pb;

        LoadingPostViewHolder(View view){
            super(view);
            ProgressBar = 
        }
    }
}
