package com.connect.connectcom.uberactivity;

import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.connect.connectcom.uberactivity.presenter.MainPresenterImpl;
import com.connect.connectcom.uberactivity.view.MainView;

public class MainActivity extends BaseActivity implements MainView {

    private MainPresenterImpl mainPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mainPresenter = new MainPresenterImpl(this);

        setClickListeners();
    }

    private void setClickListeners() {
        findViewById(R.id.btn_get_uber).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mainPresenter.requestUber();
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void navigateToUberActivity() {
        Log.d(MainActivity.class.getSimpleName(), "Navigating to Uber Activity");
        // Hardcoded test values since there's no Uber here in Venezuela.
        startActivity(UberRideActivity.buildDefaultIntent(this, 37.768082, -122.424425));
    }
}
