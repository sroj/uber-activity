package com.connect.connectcom.uberactivity.presenter;

/**
 * Created by Simon on 8/16/2015.
 */
public class MainPresenterImpl extends MainPresenter {

    @Override
    public void requestUber() {
        if (getView() == null) {
            return;
        }

        getView().showToast("Requesting Uber");
        getView().navigateToUberView();
    }

}
