package com.connect.connectcom.uberactivity.view;

import android.content.Context;
import android.support.design.widget.Snackbar;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Toast;

import com.connect.connectcom.uberactivity.R;
import com.connect.connectcom.uberactivity.android.activity.MainActivity;
import com.connect.connectcom.uberactivity.flow.LandingScreen;
import com.connect.connectcom.uberactivity.flow.UberRidesScreen;
import com.connect.connectcom.uberactivity.presenter.MainPresenter;
import com.connect.connectcom.uberactivity.util.Ln;

import javax.inject.Inject;

import butterknife.ButterKnife;
import flow.Flow;
import mortar.dagger1support.ObjectGraphService;

/**
 * Created by Simon on 8/22/2015.
 */
public class MainViewImpl extends MainPresenter.MainView implements Flow.Dispatcher {

    @Inject
    MainPresenter mainPresenter;

    public MainViewImpl(Context context) {
        super(context);
    }

    public MainViewImpl(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MainViewImpl(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();

        ButterKnife.bind(this, this);
        ObjectGraphService.inject(getContext(), this);
    }

    @Override
    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        mainPresenter.takeView(this);
    }

    @Override
    public void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        mainPresenter.dropView(this);
    }

    @Override
    public void showToast(String message) {
        Snackbar.make(this, message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void navigateToUberView() {
        Log.d(MainActivity.class.getSimpleName(), "Navigating to Uber Activity");
        Ln.d("Setting UberRidesScreeen");
        Flow.get(getContext()).set(new UberRidesScreen());
    }

    @Override
    public void dispatch(Flow.Traversal traversal, Flow.TraversalCallback callback) {
        Ln.d("Called dispatch");
        final Object top = traversal.destination.top();
        showScreenFor(top);
        callback.onTraversalCompleted();
    }

    private void showScreenFor(Object top) {
        Ln.d("general screen");
        // TODO Work around the POJO design choice in Flow to OOP this
        if (top instanceof LandingScreen) {
            switchToUberButton();
        } else if (top instanceof UberRidesScreen) {
            switchToUberRidesView();
        }
    }

    private void switchToUberButton() {
        removeCurrentView();
        final View uberButton = LayoutInflater.from(getContext()).inflate(R.layout.button_get_uber, this, false);
        uberButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                mainPresenter.requestUber();
            }
        });
        addView(uberButton);
    }

    private void switchToUberRidesView() {
        removeCurrentView();
        View.inflate(getContext(), R.layout.view_uber_rides, this);
    }

    private void removeCurrentView() {
        if (getChildCount() > 0) {
            removeViewAt(0);
        }
    }
}