package com.snipex.shantu.androidarchitecturecomponentsmvvmretrofitwithjava.adapter;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

import android.graphics.Movie;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.snipex.shantu.androidarchitecturecomponentsmvvmretrofitwithjava.R;
import com.snipex.shantu.androidarchitecturecomponentsmvvmretrofitwithjava.databinding.ListEachRowMovieArticleBinding;
import com.snipex.shantu.androidarchitecturecomponentsmvvmretrofitwithjava.model.Article;

import java.util.ArrayList;

public class MovieArticleAdapter extends RecyclerView.Adapter<MovieArticleAdapter.ViewHolder> {

    private Context context;
    private ArrayList<Article> articleArrayList;

    public MovieArticleAdapter( ArrayList<Article> articleArrayList) {
        this.articleArrayList = articleArrayList;
    }


    @NonNull
    @Override
    public MovieArticleAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        ListEachRowMovieArticleBinding listItemsBinding = DataBindingUtil.inflate(layoutInflater, R.layout.list_each_row_movie_article, parent, false);
        return new ViewHolder(listItemsBinding);
    }


    @Override
    public void onBindViewHolder(@NonNull MovieArticleAdapter.ViewHolder viewHolder, int i) {
        Article article=articleArrayList.get(i);
        if(article!=null)
        {
            viewHolder.itemsBinding.setMovie(article);
            viewHolder.itemsBinding.tvAuthorAndPublishedAt.setText("-"+article.getAuthor() +" | "+"Piblishetd At: "+article.getPublishedAt());

        }
    }

    @Override
    public int getItemCount() {
        return articleArrayList.size();
    }



    class ViewHolder extends RecyclerView.ViewHolder {

        private ListEachRowMovieArticleBinding itemsBinding;

        ViewHolder(ListEachRowMovieArticleBinding listItemsBinding) {
            super(listItemsBinding.getRoot());
            this.itemsBinding = listItemsBinding;
        }
    }

}
