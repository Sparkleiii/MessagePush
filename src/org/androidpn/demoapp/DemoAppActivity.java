
package org.androidpn.demoapp;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import org.androidpn.client.ServiceManager;
import org.androidpn.fragment.HistoryFragment;
import org.androidpn.fragment.HomeFragment;
import org.androidpn.fragment.SettingsFragment;
import org.androidpn.view.SlidingMenu;

public class DemoAppActivity extends Activity implements View.OnClickListener{
    public static String CLASS_NAME = "org.androidpn.demoapp.DemoAppActivity";
    private SlidingMenu mLeftMenu;
    private ServiceManager serviceManager;
    private Context context;
    private Intent intent;

    //tab
    private LinearLayout mTabHome;
    private LinearLayout mTabHistory;
    private LinearLayout mTabSetting;
    //img_btn
    private ImageButton ibtn_home;
    private ImageButton ibtn_history;
    private ImageButton ibtn_settings;

    //fragment
    private Fragment home;
    private Fragment history;
    private Fragment settings;
    //left_menu
    private TextView tv_personset;
    private TextView tv_inforset;
    private TextView tv_about;
    private TextView tv_logout;
    private TextView tv_username;
    private String username;
    //private

    @Override
    public void onCreate(Bundle savedInstanceState) {
        Log.d("DemoAppActivity", "onCreate()...");
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.main);
        initView();
        initEvents();
        setSelect(0);
        serviceManager = new ServiceManager(this);
        context = getApplicationContext();
        mLeftMenu = (SlidingMenu) findViewById(R.id.sliding_menu);
        username = getIntent().getStringExtra("username");
        tv_username.setText(username);
        serviceManager.setAlias(username);
        // Settings
    }


    /**
     * 初始化控件
     */
    private void initView() {
        mTabHome = (LinearLayout) findViewById(R.id.tab_home);
        mTabHistory = (LinearLayout) findViewById(R.id.tab_history);
        mTabSetting = (LinearLayout) findViewById(R.id.tab_settings);
        //
        ibtn_home = (ImageButton) findViewById(R.id.tab_home_img);
        ibtn_history = (ImageButton) findViewById(R.id.tab_history_img);
        ibtn_settings = (ImageButton) findViewById(R.id.tab_settings_img);
        //
        tv_personset = (TextView) findViewById(R.id.tv_personset);
        tv_about = (TextView) findViewById(R.id.tv_about);
        tv_inforset = (TextView) findViewById(R.id.tv_inforset);
        tv_logout = (TextView) findViewById(R.id.tv_logout);
        tv_username = (TextView) findViewById(R.id.tv_left_username);
    }

    /**
     * 初始化事件
     */
    private void initEvents() {
        mTabHome.setOnClickListener(this);
        mTabHistory.setOnClickListener(this);
        mTabSetting.setOnClickListener(this);
        tv_personset.setOnClickListener(this);
        tv_about.setOnClickListener(this);
        tv_inforset.setOnClickListener(this);
        tv_logout.setOnClickListener(this);

    }
    public void toggleMenu(View view){
        mLeftMenu.toggle();
    }

    @Override
    public void onClick(View v) {
        ResetImg();
        switch (v.getId()){
            case R.id.tab_home:
                setSelect(0);
                break;
            case R.id.tab_history:
                setSelect(1);
                break;
            case R.id.tab_settings:
                setSelect(2);
                break;
            case R.id.tv_personset:
                //
                intent = new Intent(DemoAppActivity.this,InformationSettingsActivity.class);
                intent.putExtra("username",username);
                startActivity(intent);
                break;
            case R.id.tv_inforset:
                intent = new Intent(DemoAppActivity.this,NotificationSettingsActivity.class);
                startActivity(intent);
                break;
            case R.id.tv_about:
                //
                break;
            case R.id.tv_logout:
                finish();
                break;
            default:break;
        }
    }

    /**
     * 设置图片
     * 设置内容区域
     * @param i
     */
    private void setSelect(int i){
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        hideFragment(transaction);
        switch (i){
            case 0:
                if(home == null){
                    home = new HomeFragment();
                    transaction.add(R.id.id_frame,home);
                }else {
                    transaction.show(home);
                }
                ibtn_home.setImageResource(R.drawable.tab_home_pressed);
                break;
            case 1:
                if(history == null){
                    history = new HistoryFragment();
                    transaction.add(R.id.id_frame,history);
                }else {
                    transaction.show(history);
                }
                ibtn_history.setImageResource(R.drawable.tab_history_pressed);
                break;
            case 2:
                if(settings == null){
                    settings = new SettingsFragment();
                    transaction.add(R.id.id_frame,settings);
                }else {
                    transaction.show(settings);
                }
                ibtn_settings.setImageResource(R.drawable.tab_setting_pressed);
                break;
            default:
                break;
        }
        transaction.commit();
    }

    private void hideFragment(FragmentTransaction transaction) {
        if(home != null){
            transaction.hide(home);
        }
        if(history != null){
            transaction.hide(history);
        }
        if(settings != null){
            transaction.hide(settings);
        }
    }

    /**
     * 图片变暗
     */
    private void ResetImg() {
        ibtn_home.setImageResource(R.drawable.tab_home);
        ibtn_history.setImageResource(R.drawable.tab_history);
        ibtn_settings.setImageResource(R.drawable.tab_setting);
    }
}