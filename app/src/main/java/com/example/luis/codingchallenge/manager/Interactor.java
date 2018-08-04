package com.example.luis.codingchallenge.manager;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static com.example.luis.codingchallenge.ui.fragments.VenueDetailFragment.BASE_URL;

public class Interactor {

    private static RestInteractor restInteractor;

    public static RestInteractor restInteractor() {
        if (restInteractor == null) {
            restInteractor = new RestInteractor();
        }
        return restInteractor;
    }

    public static class RestInteractor {
        private Retrofit retrofit;

        RestInteractor() {
            init();
        }

        private void init() {
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }

        public Retrofit retrofit() {
            return retrofit;
        }
    }
}
