package com.connect.connectcom.uberactivity;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import java.io.IOException;

/**
 * Created by sven on 2/28/15.
 */
public class ConnectivityStatusManager {
    private static ConnectivityStatusManager INSTANCE = null;
    private ConnectivityManager connectivityManager = null;
    //   private Context context;

    public static ConnectivityStatusManager getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new ConnectivityStatusManager();
        }
        return INSTANCE;
    }

    public void initConnectivityManager(Context context) {
        //      this.context = context;
        connectivityManager = (ConnectivityManager) context.getSystemService(
                Context.CONNECTIVITY_SERVICE
        );
    }

    public boolean isConnected() {
        final NetworkInfo ni = connectivityManager.getActiveNetworkInfo();
        if (ni != null && ni.isConnectedOrConnecting()) {
            return true;
        } else {
            return false;
        }
    }

    public static class NoConnectivityException
            extends IOException {
        public NoConnectivityException(String sMessage) {
            super(sMessage);
        }
    }
}
