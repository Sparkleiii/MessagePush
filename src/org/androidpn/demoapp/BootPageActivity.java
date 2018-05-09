package org.androidpn.demoapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.TypedValue;
import android.view.View;
import android.view.Window;
import android.widget.LinearLayout;
import org.androidpn.client.Constants;
import org.androidpn.fragment.BootFragment;
import org.androidpn.util.ViewPagerAdapter;

import java.util.ArrayList;
import java.util.List;

public class BootPageActivity extends FragmentActivity {
    private ViewPager viewPager;
    private LinearLayout indicator;
    private PagerAdapter pagerAdapter;
    private List<Fragment> fragments = new ArrayList<Fragment>();
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    private boolean isFirst;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.boot_page);
        initView();
        editor = sharedPreferences.edit();
        String result = sharedPreferences.getString(Constants.IS_FIRST,"isfirst");
        if(result.equals("isfirst")){
            editor.putString(Constants.IS_FIRST,"notfirst").commit();
        }else{
            Intent intent = new Intent(BootPageActivity.this,LoginActivity.class);
            startActivity(intent);
            finish();
        }
        //创建Fragment
        for(int i=0;i<4;i++){
            BootFragment fragment = new BootFragment();
            Bundle bundle = new Bundle();
            bundle.putInt("index",i);
            fragment.setArguments(bundle);
            fragments.add(fragment);
        }
        pagerAdapter = new ViewPagerAdapter(getSupportFragmentManager(),fragments);
        viewPager.setAdapter(pagerAdapter);
        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int index, float v, int i1) {
                for (int i=0;i<fragments.size();i++){
                    indicator.getChildAt(i).setBackgroundResource(index==i?
                            R.drawable.dot_selected:R.drawable.dot_normal);
                }
            }
            @Override
            public void onPageSelected(int i) {
            }
            @Override
            public void onPageScrollStateChanged(int i) {
            }
        });
        initIndicator();

    }

    /**
     * 初始化指示器
     */
    private void initIndicator() {
        int width = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,10f,getResources().getDisplayMetrics());
        LinearLayout.LayoutParams lp= new LinearLayout.LayoutParams(width,width);
        lp.rightMargin = 2*width;
        for (int i=0;i<fragments.size();i++){
            View view = new View(this);
            view.setId(i);
            view.setBackgroundResource(i==0?
                    R.drawable.dot_selected:R.drawable.dot_normal);
            view.setLayoutParams(lp);
            indicator.addView(view,i);

        }
    }

    private void initView() {
        sharedPreferences = getSharedPreferences(Constants.IS_FIRST,MODE_PRIVATE);
        viewPager = (ViewPager) findViewById(R.id.boot_viewpager);
        indicator = (LinearLayout) findViewById(R.id.boot_ll_indicatoer);
    }
}
