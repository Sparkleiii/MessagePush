package org.androidpn.demoapp;

import android.app.Activity;
import android.os.Bundle;

public class ParentActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.parent_activity);
    }
}