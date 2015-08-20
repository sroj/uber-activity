package com.connect.connectcom.uberactivity.presenter;

import com.connect.connectcom.uberactivity.view.MainView;

/**
 * Created by Simon on 8/16/2015.
 */
public class MainPresenterImpl implements MainPresenter {

    private final MainView mainView;

    public MainPresenterImpl(MainView mainView) {
        this.mainView = mainView;
    }

    @Override
    public void requestUber() {
        if (mainView == null) {
            return;
        }

        mainView.showToast("Requesting Uber!");
        mainView.navigateToUberActivity();
    }
}
