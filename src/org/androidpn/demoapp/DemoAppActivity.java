
package org.androidpn.demoapp;

import android.content.Intent;
import org.androidpn.client.ServiceManager;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import java.util.ArrayList;
import java.util.List;


public class DemoAppActivity extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        Log.d("DemoAppActivity", "onCreate()...");

        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        // Settings
        Button okButton = (Button) findViewById(R.id.btn_settings);
        okButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                ServiceManager.viewNotificationSettings(DemoAppActivity.this);
            }
        });

        Button historyButton = (Button)findViewById(R.id.btn_histories);
        historyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DemoAppActivity.this,NotificationHistoryActivity.class);
                startActivity(intent);
            }
        });

        // Start the service
        ServiceManager serviceManager = new ServiceManager(this);
        serviceManager.setNotificationIcon(R.drawable.icon_main_small);
        serviceManager.startService();
        serviceManager.setAlias("123");
        List<String> tagsList = new ArrayList<String>();
        tagsList.add("sports");
        tagsList.add("music");
        serviceManager.setTags(tagsList);



    }

}