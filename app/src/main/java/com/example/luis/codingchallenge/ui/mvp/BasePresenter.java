package com.example.luis.codingchallenge.ui.mvp;

import android.os.Bundle;

public interface BasePresenter<View extends BaseView> {

    void attachView(View view);

    void detachView();
}