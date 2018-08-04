package com.example.luis.codingchallenge.utility;

import android.widget.ImageView;

import com.squareup.picasso.Picasso;

public class ViewUtility {

    public static void loadProfileView(final ImageView imageView, final String url) {
        if (imageView == null || url == null) {
            return;
        }
        Picasso.with(imageView.getContext()).load(url).into(imageView);
    }
}
