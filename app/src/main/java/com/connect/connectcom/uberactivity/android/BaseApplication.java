package com.connect.connectcom.uberactivity.android;

import android.app.Application;

import com.connect.connectcom.uberactivity.module.BaseApplicationModule;

import dagger.ObjectGraph;
import mortar.MortarScope;
import mortar.dagger1support.ObjectGraphService;

/**
 * Created by Simon on 8/17/2015.
 */
public class BaseApplication extends Application {

    private MortarScope rootScope;

    @Override
    public Object getSystemService(String name) {
        if (rootScope == null) {
            rootScope = MortarScope.buildRootScope().withService(ObjectGraphService.SERVICE_NAME,
                                                                 ObjectGraph.create(new BaseApplicationModule(this))).build(
                    getScopeName());
        }

        return rootScope.hasService(name) ? rootScope.getService(name) : super.getSystemService(name);
    }

    private String getScopeName() {
        return BaseApplication.class.getSimpleName() + "Scope";
    }
}
