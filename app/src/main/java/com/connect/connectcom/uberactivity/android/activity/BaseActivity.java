package com.connect.connectcom.uberactivity.android.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import mortar.MortarScope;
import mortar.bundler.BundleServiceRunner;
import mortar.dagger1support.ObjectGraphService;

/**
 * Created by Simon on 8/17/2015.
 */
public abstract class BaseActivity extends AppCompatActivity {

    private MortarScope activityScope;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        MortarScope parentScope = MortarScope.getScope(getApplication());
        activityScope = parentScope.findChild(getScopeName());
        if (activityScope == null) {
            activityScope = MortarScope.buildChild(getApplicationContext()).withService(BundleServiceRunner.SERVICE_NAME,
                                                                                        new BundleServiceRunner()).build(
                    getScopeName());
        }

        ObjectGraphService.inject(this, this);
        BundleServiceRunner.getBundleServiceRunner(this).onCreate(savedInstanceState);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        BundleServiceRunner.getBundleServiceRunner(this).onSaveInstanceState(outState);
    }

    @Override
    public Object getSystemService(String name) {
        return activityScope != null && activityScope.hasService(name) ? activityScope.getService(name) : super.getSystemService(
                name);
    }

    private String getScopeName() {
        return getLocalClassName() + "-task-" + getTaskId();
    }

    @Override
    protected void onDestroy() {

        if (activityScope != null) {
            activityScope.destroy();
            activityScope = null;
        }

        super.onDestroy();
    }
}
