package com.connect.connectcom.uberactivity.presenter;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.FrameLayout;

import mortar.ViewPresenter;

/**
 * Created by Simon on 8/16/2015.
 */
public abstract class MainPresenter extends ViewPresenter<MainPresenter.MainView> {
    public abstract void requestUber();

    public static abstract class MainView extends FrameLayout {

        public MainView(Context context) {
            super(context);
        }

        public MainView(Context context, AttributeSet attrs) {
            super(context, attrs);
        }

        public MainView(Context context, AttributeSet attrs, int defStyleAttr) {
            super(context, attrs, defStyleAttr);
        }

        public abstract void showToast(String message);

        public abstract void navigateToUberView();
    }
}
