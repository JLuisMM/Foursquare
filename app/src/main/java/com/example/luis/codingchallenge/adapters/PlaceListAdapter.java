package com.example.luis.codingchallenge.adapters;

import android.content.Context;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import com.example.luis.codingchallenge.R;
import com.example.luis.codingchallenge.model.Venue;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class PlaceListAdapter extends RecyclerView.Adapter<PlaceListAdapter.PlaceViewHolder> {

    private ArrayList<Venue> mVenuesList;
    private Context context;

    public PlaceListAdapter(ArrayList<Venue> mVenuesList) {
        this.mVenuesList = mVenuesList;
    }

    @Override
    public PlaceViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_place_item, parent, false);
        context = view.getContext();
        return new PlaceViewHolder(view);
    }

    @Override
    public void onBindViewHolder(PlaceViewHolder holder, int position) {
        if (holder == null) {
            return;
        }

        Venue venue = mVenuesList.get(position);
        if (venue == null) {
            return;
        }

        holder.mTitle.setText(venue.getName());
        holder.mAddress.setText(venue.getAddress());
        holder.mCountry.setText(venue.getLocation().getReadableLoc());

        animate(holder);
    }

    @Override
    public int getItemCount() {
        if (mVenuesList == null) {
            return 0;
        }
        return mVenuesList.size();
    }

    private void animate(RecyclerView.ViewHolder viewHolder) {
        final Animation animAnticipateOvershoot = AnimationUtils.loadAnimation(context, R.anim.anticipate_overshoot_interpolator);
        viewHolder.itemView.setAnimation(animAnticipateOvershoot);
    }

    public static class PlaceViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.card_title)
        AppCompatTextView mTitle;

        @BindView(R.id.card_address)
        AppCompatTextView mAddress;

        @BindView(R.id.card_country)
        AppCompatTextView mCountry;

        PlaceViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
