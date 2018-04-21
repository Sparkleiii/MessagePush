
package org.androidpn.demoapp;

import android.app.Activity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import com.android.volley.toolbox.NetworkImageView;

public class SetTagsActivity extends Activity {
    private NetworkImageView networkImageView;
    private TextView title;
    private TextView content;
    private Button btn_ok;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getActionBar().setDisplayHomeAsUpEnabled(true);
        setContentView(R.layout.set_tags);

    }
}