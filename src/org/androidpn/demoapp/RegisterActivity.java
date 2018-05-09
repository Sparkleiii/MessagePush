package org.androidpn.demoapp;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import org.androidpn.client.NotificationService;
import org.androidpn.client.ServiceManager;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.util.ArrayList;
import java.util.List;

public class RegisterActivity extends Activity {
    private EditText et_account;
    private EditText et_password;
    private String account;
    private String password;
    private Button btn_register;
    private ServiceManager serviceManager;
    private Context context;
    List<NameValuePair> params;
    private Button ibtn_back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.register);
        context = getApplicationContext();
        serviceManager = new ServiceManager(this);
        initView();
        btn_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent;
                NotificationService notificationService = NotificationService.getNotificationService();
                String clientId = notificationService.getXmppManager().getUsername();
                account = et_account.getText().toString();
                password = et_password.getText().toString();
                Log.d("account",account);
                Log.d("password",password);
                params = new ArrayList<NameValuePair>();
                params.add(new BasicNameValuePair("clientId",clientId));
                params.add(new BasicNameValuePair("username",account));
                params.add(new BasicNameValuePair("password",password));
                serviceManager.setParams(params);
                serviceManager.setUrl("/user.do?action=register");
                serviceManager = serviceManager.RequestToServer(serviceManager);
                Log.d("result",serviceManager.getJsonData());
                if(serviceManager.getJsonData().equals("success")){
                    Toast.makeText(context, context.getString(R.string.user_register_success), Toast.LENGTH_LONG).show();
                    serviceManager.setSign(false);
                    intent = new Intent(context, DemoAppActivity.class);
                    intent.putExtra("username",account);
                    startActivity(intent);
                }else if(serviceManager.getJsonData().equals("exist")){
                    Toast.makeText(context, context.getString(R.string.user_register_failed), Toast.LENGTH_LONG).show();
                }else{
                    Log.d("error","error");
                }
            }
        });

        ibtn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
    public void initView(){
        btn_register = (Button) findViewById(R.id.btn_re_register);
        et_account = (EditText) findViewById(R.id.edit_re_username);
        et_password = (EditText) findViewById(R.id.edit_re_password);
        ibtn_back = (Button) findViewById(R.id.ibtn_back);
    }
}