package com.connect.connectcom.uberactivity;

import android.app.Fragment;
import android.os.Bundle;

/**
 * Created by Simon on 8/19/2015.
 */
public class BaseFragment extends Fragment {

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        ((BaseActivity) getActivity()).inject(this);
    }
}
