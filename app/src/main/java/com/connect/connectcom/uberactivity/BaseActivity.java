package com.connect.connectcom.uberactivity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import java.util.Arrays;
import java.util.List;

import dagger.ObjectGraph;

/**
 * Created by Simon on 8/17/2015.
 */
public class BaseActivity extends AppCompatActivity {

    private ObjectGraph objectGraph;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        objectGraph = ((BaseApplication) getApplication()).getApplicationGraph().plus(getModules().toArray());

    }

    protected List<Object> getModules() {
        return Arrays.<Object>asList(new BaseActivityModule());
    }

    public void inject(Object object) {
        objectGraph.inject(object);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        objectGraph = null;
    }
}
