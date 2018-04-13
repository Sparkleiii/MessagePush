
package org.androidpn.demoapp;

import android.content.Intent;
import android.widget.Button;
import org.androidpn.client.ServiceManager;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import org.androidpn.model.Tags;
import org.androidpn.model.User;
import org.androidpn.view.SlidingMenu;

import java.util.ArrayList;
import java.util.List;


public class DemoAppActivity extends Activity {
    private SlidingMenu mLeftMenu;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        Log.d("DemoAppActivity", "onCreate()...");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        mLeftMenu = (SlidingMenu) findViewById(R.id.sliding_menu);
        User user = (User) getIntent().getSerializableExtra("user");

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
        serviceManager.setAlias(user.getUsername());
        List<Tags> tagsList = user.getTagsList();
        List<String> tlist = new ArrayList<String>();
        for(Tags tags:tagsList){
            tlist.add(tags.getTag_name());
        }
        serviceManager.setTags(tlist);
    }

    public void toggleMenu(View view){
        mLeftMenu.toggle();
    }

}