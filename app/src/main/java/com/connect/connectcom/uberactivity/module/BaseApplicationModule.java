package com.connect.connectcom.uberactivity.module;

import android.content.Context;

import com.connect.connectcom.uberactivity.android.activity.MainActivity;
import com.connect.connectcom.uberactivity.view.MainViewImpl;
import com.connect.connectcom.uberactivity.view.UberRidesViewImpl;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Created by Simon on 8/17/2015.
 */

@Module(
        injects = {MainActivity.class, UberRidesViewImpl.class, MainViewImpl.class},
        includes = BaseActivityModule.class)
public class BaseApplicationModule {

    private final Context appContext;

    public BaseApplicationModule(Context appContext) {
        this.appContext = appContext;
    }

    @Provides
    @Singleton
    Context provideAppContext() {
        return appContext;
    }

}
