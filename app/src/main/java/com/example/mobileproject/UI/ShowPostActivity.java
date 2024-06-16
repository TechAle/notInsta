package com.example.mobileproject.UI;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Build;
import android.os.Bundle;

import com.bumptech.glide.Glide;
import com.example.mobileproject.databinding.ActivityShowPostBinding;
import com.example.mobileproject.models.Post.Post;

public final class ShowPostActivity extends AppCompatActivity {
    private Post p;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            p = getIntent().getParcelableExtra("post", Post.class);
        } else {
            p = getIntent().getParcelableExtra("post"); //deprecated in API 33+, required in others (target: API 29)
        }
        ActivityShowPostBinding binding = ActivityShowPostBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setSupportActionBar(binding.toolbar3);
        binding.toolbar3.setNavigationOnClickListener(listener -> {
            finish();
        });
        Glide.with(getApplicationContext())
                .load(p.getImage())
                .into(binding.imageView2);
        binding.description.setText(p.getDescrizione());
        if(p.getLikes()!= null)
            binding.likes.setText(String.valueOf(p.getLikes().size()));
        binding.author.setText(p.getAutore());
        binding.publishDate.setText(p.getPubblicazione().toString());
        getSupportActionBar().setTitle(p.getId()); //false warning: toolbar setted
    }
}