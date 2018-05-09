package org.androidpn.fragment;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;
import org.androidpn.client.LogUtil;
import org.androidpn.client.ServiceManager;
import org.androidpn.demoapp.InformationSettingsActivity;
import org.androidpn.demoapp.LoginActivity;
import org.androidpn.demoapp.NotificationSettingsActivity;
import org.androidpn.demoapp.R;

public class SettingsFragment extends Fragment implements View.OnClickListener{
    private static final String LOGTAG = LogUtil
            .makeLogTag(SettingsFragment.class);
    private Context context;
    private TextView tv_personset;
    private TextView tv_inforset;
    private TextView tv_switch;
    private TextView tv_about;
    private TextView tv_logout;
    private ServiceManager serviceManager;
    private String username;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view =inflater.inflate(R.layout.tab_settings,container,false);
        context = getActivity().getApplicationContext();
        serviceManager = new ServiceManager(getActivity());
        initView(view);
        return view;
    }


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Log.d("onActivityCreated","onActivityCreated");
        tv_personset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(),InformationSettingsActivity.class);
                username = getActivity().getIntent().getStringExtra("username");
                Log.d(LOGTAG,"usernameSettingFragment"+username);
                intent.putExtra("username",username);
                startActivity(intent);
            }
        });
        tv_about.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("tv_about","tv_about Clicked");
                Toast.makeText(getActivity(),"tv_about",Toast.LENGTH_LONG).show();
            }
        });
        tv_inforset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(),NotificationSettingsActivity.class);

                startActivity(intent);
            }
        });
        tv_switch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(),LoginActivity.class);
                startActivity(intent);
            }
        });
        tv_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("tv_logout","tv_logout Clicked");
                getActivity().finish();
            }
        });
    }

    public void initView(View view){
        tv_personset = (TextView)view.findViewById(R.id.tv_set_personset);
        tv_about = (TextView) view.findViewById(R.id.tv_set_about);
        tv_inforset = (TextView) view.findViewById(R.id.tv_set_inforset);
        tv_logout = (TextView) view.findViewById(R.id.tv_set_logout);
        tv_switch = (TextView) view.findViewById(R.id.tv_set_switch);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.tv_about:
                Log.d("tv_about","tv_about Clicked");
                break;
            case R.id.tv_logout:
                Log.d("tv_logout","tv_logout Clicked");
                getActivity().finish();
                break;
            default:break;
        }
    }
}
