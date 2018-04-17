package org.androidpn.fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import org.androidpn.client.LogUtil;
import org.androidpn.demoapp.R;

public class SettingsFragment extends Fragment{
    private static final String LOGTAG = LogUtil
            .makeLogTag(SettingsFragment.class);
    public SettingsFragment() {

    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view =inflater.inflate(R.layout.tab_settings,container,false);
        return view;
    }
}
