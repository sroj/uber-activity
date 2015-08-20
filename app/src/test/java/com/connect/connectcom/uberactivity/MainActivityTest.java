package com.connect.connectcom.uberactivity;

import android.os.Build;
import android.view.View;

import junit.framework.Assert;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.annotation.Config;
import org.robolectric.shadows.ShadowApplication;
import org.robolectric.shadows.ShadowToast;

/**
 * Created by Simon on 8/16/2015.
 */
@RunWith(RobolectricGradleTestRunner.class)
@Config(constants = BuildConfig.class, sdk = Build.VERSION_CODES.LOLLIPOP)
public class MainActivityTest {

    @Test
    public void clickingRequestUberShouldShowToast() {
        MainActivity mainActivity = Robolectric.setupActivity(MainActivity.class);
        final View btnUber = mainActivity.findViewById(R.id.btn_get_uber);

        btnUber.performClick();

        Assert.assertEquals(ShadowApplication.getInstance().getResources().getString(R.string.requesting_uber), ShadowToast.getTextOfLatestToast());
    }

}