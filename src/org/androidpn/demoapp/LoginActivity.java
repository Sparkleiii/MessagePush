package org.androidpn.demoapp;

import android.app.Activity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import org.apache.http.NameValuePair;

import java.util.List;

public class LoginActivity extends Activity{
    List<NameValuePair> params;
    private EditText et_username;
    private EditText et_password;
    private Button btn_login;
    private Button btn_register;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);
        init();
    }

    public void init(){
        et_username = (EditText) findViewById(R.id.edit_username);
        et_password = (EditText) findViewById(R.id.edit_password);
        btn_login = (Button) findViewById(R.id.btn_login);
        btn_register = (Button) findViewById(R.id.btn_register);
    }


}