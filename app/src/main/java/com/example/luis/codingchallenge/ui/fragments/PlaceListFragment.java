package com.example.luis.codingchallenge.ui.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.luis.codingchallenge.R;
import com.example.luis.codingchallenge.adapters.PlaceListAdapter;
import com.example.luis.codingchallenge.adapters.RecyclerItemClickListener;
import com.example.luis.codingchallenge.manager.events.ShowVenueDetailEvent;
import com.example.luis.codingchallenge.manager.events.ShowVenuesMarkersEvent;
import com.example.luis.codingchallenge.model.ResponseVenues;
import com.example.luis.codingchallenge.model.Venue;

import org.greenrobot.eventbus.EventBus;

import butterknife.BindView;
import butterknife.ButterKnife;


public class PlaceListFragment extends Fragment {

    public static final String RESPONSE_OBJ = "response_obj";

    @BindView(R.id.recyclerview_placelist)
    RecyclerView mPlacesRecyclerView;

    @BindView(R.id.tv_placelist_empty)
    TextView mEmptyTv;

    private FloatingActionButton mShowAll;

    private ResponseVenues mResponse;


    public PlaceListFragment() {
    }

    public static PlaceListFragment NewInstance(ResponseVenues response) {
        Bundle mBundle = new Bundle();
        mBundle.putParcelable(RESPONSE_OBJ, response);

        PlaceListFragment placeListFragment = new PlaceListFragment();
        placeListFragment.setArguments(mBundle);

        return placeListFragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            mResponse = getArguments().getParcelable(RESPONSE_OBJ);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_place_list, container, false);

        mShowAll = (FloatingActionButton) view.findViewById(R.id.floatingActionButton2);

        ButterKnife.bind(this, view);

        initView();

        return view;
    }

    private void initView() {
        if (mResponse == null || mResponse.getVenues() == null) {
            mEmptyTv.setVisibility(View.VISIBLE);
            mShowAll.setVisibility(View.GONE);
            return;
        }

        PlaceListAdapter mAdapter = new PlaceListAdapter(mResponse.getVenues());
        LinearLayoutManager mLinearLayoutManager = new LinearLayoutManager(getContext());
        RecyclerView.ItemDecoration itemDecoration = new
                DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL);

        mPlacesRecyclerView.addItemDecoration(itemDecoration);
        mPlacesRecyclerView.setLayoutManager(mLinearLayoutManager);
        mPlacesRecyclerView.setAdapter(mAdapter);

        mPlacesRecyclerView.addOnItemTouchListener(new RecyclerItemClickListener(getContext(),
                mPlacesRecyclerView, new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Venue selectedVenue = mResponse.getVenue(position);
                EventBus.getDefault().post(new ShowVenueDetailEvent(selectedVenue));
            }

            @Override
            public void onLongItemClick(View view, int position) {
                // no-op
            }

        }));

        mShowAll.setVisibility(View.VISIBLE);
        mShowAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EventBus.getDefault().post(new ShowVenuesMarkersEvent(mResponse));
            }
        });
    }
}
