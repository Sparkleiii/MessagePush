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
import android.widget.ImageView;
import android.widget.Toast;
import org.androidpn.client.NotificationService;
import org.androidpn.client.ServiceManager;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.util.ArrayList;
import java.util.List;

public class LoginActivity extends Activity {
    private EditText et_username;
    private EditText et_password;
    private String account;
    private String password;
    private Button btn_login;
    private Button btn_register;
    private ImageView iv_username;
    private ImageView iv_password;
    private ServiceManager serviceManager;
    private Context context;
    List<NameValuePair> params;
    private NotificationService notificationService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.login);
        context = getApplicationContext();
        initView();
        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent;
                notificationService = NotificationService.getNotificationService();
                //获取客户端编号
                String clientId = notificationService.getXmppManager().getUsername();
                Log.d("clientId",clientId);
                //登陆用户名
                account = et_username.getText().toString();
                //登陆密码
                password = et_password.getText().toString();
                if(account==null){
                    Toast.makeText(context,context.getString(R.string.no_account),Toast.LENGTH_SHORT).show();
                }
                if(password==null&&account!=null){
                    Toast.makeText(context,context.getString(R.string.no_password),Toast.LENGTH_SHORT).show();
                }
                //用户名密码不为空
                if(account!=null&&password!=null){
                    params = new ArrayList<NameValuePair>();
                    params.add(new BasicNameValuePair("clientId",clientId));
                    params.add(new BasicNameValuePair("username",account));
                    params.add(new BasicNameValuePair("password",password));
                    serviceManager.setParams(params);
                    serviceManager.setUrl("/user.do?action=login");
                    serviceManager = serviceManager.RequestToServer(serviceManager);
                    if(serviceManager.getJsonData().equals("success")){
                            Toast.makeText(context, context.getString(R.string.user_valid_success), Toast.LENGTH_LONG).show();
                            serviceManager.setSign(false);
                            intent = new Intent(LoginActivity.this, DemoAppActivity.class);
                            intent.putExtra("username",account);
                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent);
                    }else{
                            Toast.makeText(context, context.getString(R.string.user_valid_failed), Toast.LENGTH_LONG).show();
                        }
                    }
                }
        });
        btn_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this,RegisterActivity.class);
                startActivity(intent);
            }
        });
    }

    private void initView() {
        btn_login = (Button) findViewById(R.id.btn_login);
        btn_register = (Button) findViewById(R.id.btn_register);
        serviceManager = new ServiceManager(this);
        serviceManager.setNotificationIcon(R.drawable.icon_main_small);
        serviceManager.startService();
        et_username = (EditText) findViewById(R.id.edit_username);
        et_password = (EditText) findViewById(R.id.edit_password);
    }
}