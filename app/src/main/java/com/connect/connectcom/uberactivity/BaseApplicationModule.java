package com.connect.connectcom.uberactivity;

import android.content.Context;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Created by Simon on 8/17/2015.
 */

@Module(library = true)
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
