package org.androidpn.demoapp;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import org.androidpn.model.User;
import org.apache.http.NameValuePair;
import org.litepal.crud.DataSupport;

import java.io.Serializable;
import java.util.List;

public class LoginActivity extends Activity {
    private EditText et_username;
    private EditText et_password;
    private Button btn_login;
    private Button btn_register;
    private Context context;
    List<NameValuePair> params;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);
        context =getApplicationContext();
        init();
        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = et_username.getText().toString();
                String password = et_password.getText().toString();
                List<User> userList = DataSupport.findAll(User.class);
                boolean flag = false;
                for(User user:userList){
                    if(user.getUsername().equals(username)&&user.getPassword().equals(password)){
                        flag = true;
                        Intent intent = new Intent(LoginActivity.this,DemoAppActivity.class);
                        intent.putExtra("user", (Serializable)user);
                        startActivity(intent);
                    }
                }
                if(!flag){
                    Toast.makeText(context,context.getString(R.string.User_valid_false),Toast.LENGTH_SHORT).show();
                }
            }
        });

    }


    private void init() {
        et_username = (EditText) findViewById(R.id.edit_username);
        et_password = (EditText) findViewById(R.id.edit_password);
        btn_login = (Button) findViewById(R.id.btn_login);
        btn_register = (Button) findViewById(R.id.btn_register);
    }
}