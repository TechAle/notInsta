package com.example.mobileproject.utils;

import android.app.Application;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mobileproject.R;
import com.example.mobileproject.models.Post;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;

import java.util.List;


public class PostAdapter extends RecyclerView.Adapter<PostAdapter.LoadingPostViewHolder> {

    private static final int NORMAL_TYPE = 0;
    private static final int LOADING_TYPE = 0;
    private final List<Post> postSet;
    private final Application a;

    public static class PostViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        //private final ImageView iv;
        public PostViewHolder(View item){
            super(item);
            //TODO: fare l'ID
            //iv = item.findViewById(R.id.qualcosa);
            item.setOnClickListener(this);
        }
        //TODO: sistemare il bind (e trovare un modo di caricare un Drawable)
        public void bind(Post p){
            //iv.setImageDrawable(p.photo);
            //Caricamento immagine da URL con Glide
            //TODO: sistemare riferimento ad Application e cambiare foto di rimpiazzo (se qualcuno vuole)
            //Glide.with(a).load(p.getPhoto()).placeholder(R.drawable.baseline_photo_camera_24).into(iv);
        }

        @Override
        public void onClick(View v) {
            //TODO: sistemare questa funzione
            Snackbar.make(v, "Item clicked", BaseTransientBottomBar.LENGTH_SHORT).show();
        }
    }
    public PostAdapter(List<Post> pl, Application a){
        this.postSet = pl;
        this.a = a;
    }

    @Override
    public int getItemViewType(int position){
        if (postSet.get(position) == null){
            return LOADING_TYPE;
        }
        return NORMAL_TYPE;
    }

    @NonNull
    @Override
    public LoadingPostViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v;
        if(viewType == NORMAL_TYPE){
            v = LayoutInflater.from(parent.getContext()).inflate(R.layout.postlist_item, parent, false);
            //return new PostViewHolder(v);
            return null;
        } else {
            v = LayoutInflater.from(parent.getContext()).inflate(R.layout.loading_item, parent, false);
            return new LoadingPostViewHolder(v);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull LoadingPostViewHolder holder, int position) {
        /*
        if(holder instanceof PostViewHolder) {
            ((PostViewHolder) holder).bind(postSet.get(position));
        } else if (holder instanceof LoadingPostViewHolder){
            ((LoadingPostViewHolder) holder).activate();
        }
         */
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
            pb = view.findViewById(R.id.progressbar_item);
        }
        public void activate(){
            pb.setIndeterminate(true);
        }
    }
}
