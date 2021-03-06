package com.example.jonsnow.moviesizing;

import android.graphics.Movie;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import static com.example.jonsnow.moviesizing.R.*;

/**
 * Created by jonsnow on 24/10/16.
 */

public class MovieViewHolder extends RecyclerView.ViewHolder {

    private CardView cardView;
    private ImageView imageView;
    private TextView titleTextView;
    private TextView descriptionTextView;
    private TextView ratingTextView;


    public CardView getCardView() {
        return cardView;
    }

    public void setCardView(CardView cardView) {
        this.cardView = cardView;
    }

    public ImageView getImageView() {
        return imageView;
    }

    public void setImageView(ImageView imageView) {
        this.imageView = imageView;
    }

    public TextView getTitleTextView() {
        return titleTextView;
    }

    public void setTitleTextView(TextView titleTextView) {
        this.titleTextView = titleTextView;
    }

    public TextView getDescriptionTextView() {
        return descriptionTextView;
    }

    public void setDescriptionTextView(TextView descriptionTextView) {
        this.descriptionTextView = descriptionTextView;
    }

    public TextView getRatingTextView() {
        return ratingTextView;
    }

    public void setRatingTextView(TextView ratingTextView) {
        this.ratingTextView = ratingTextView;
    }

    public MovieViewHolder(View view) {
        super(view);
        cardView = (CardView) view.findViewById(R.id.cardView);
        imageView = (ImageView) view.findViewById(R.id.imageView);
        titleTextView = (TextView) view.findViewById(R.id.title);
        descriptionTextView = (TextView) view.findViewById(R.id.description);
        ratingTextView = (TextView) view.findViewById(id.rating);




    }
}