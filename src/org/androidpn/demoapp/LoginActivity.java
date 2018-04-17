package org.androidpn.demoapp;

import android.app.Activity;
import android.content.*;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.wyx.utils.MD5;
import org.androidpn.client.Constants;
import org.androidpn.client.NotificationService;
import org.androidpn.client.ServiceManager;
import org.androidpn.client.XmppManager;
import org.androidpn.iq.SetAliasIQ;
import org.androidpn.model.User;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.jivesoftware.smack.packet.IQ;
import org.litepal.crud.DataSupport;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.*;

public class LoginActivity extends Activity {
    private EditText et_username;
    private EditText et_password;
    private String account;
    private String password;
    private Button btn_login;
    private Button btn_register;
    private ServiceManager serviceManager;
    private Context context;
    List<NameValuePair> params;
    private NotificationService notificationService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);
        context = getApplicationContext();
        btn_login = (Button) findViewById(R.id.btn_login);
        btn_register = (Button) findViewById(R.id.btn_register);
        serviceManager = new ServiceManager(this);
        serviceManager.setNotificationIcon(R.drawable.icon_main_small);
        serviceManager.startService();
        et_username = (EditText) findViewById(R.id.edit_username);
        et_password = (EditText) findViewById(R.id.edit_password);

        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent;
                notificationService = NotificationService.getNotificationService();
                String clientId = notificationService.getXmppManager().getUsername();
                Log.d("clientId",clientId);
                account = et_username.getText().toString();
                password = et_password.getText().toString();
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
                    startActivity(intent);
                }else{
                    Toast.makeText(context, context.getString(R.string.user_valid_failed), Toast.LENGTH_LONG).show();
                }
            }
        });
        btn_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this,NotificationSettingsActivity.class);
                startActivity(intent);
            }
        });
    }
}