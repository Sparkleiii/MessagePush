package org.androidpn.demoapp;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.*;
import org.androidpn.client.LogUtil;
import org.androidpn.client.ServiceManager;

import java.util.ArrayList;
import java.util.List;

public class InformationSettingsActivity extends Activity {
    private static final String LOGTAG = LogUtil
            .makeLogTag(InformationSettingsActivity.class);
    private Spinner spinner_exam_type;
    private Spinner spinner_english_type;
    private EditText et_school;
    private EditText et_major;
    private EditText et_pro_course1;
    private EditText et_pro_course2;
    private Button btn_submit;
    private String school;
    private String major;
    private String exam_type;
    private String pro_course1;
    private String pro_course2;
    private String english_type;
    private Button btn_back;
    private Context context;
    private ServiceManager serviceManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = getApplicationContext();
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.set_tags);
        serviceManager = new ServiceManager(this);
        initView();
        btn_submit = (Button)findViewById(R.id.btn_submit);
        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = getIntent().getStringExtra("username");
                Log.d(LOGTAG,"usernameInfo"+username);
                List<String> tlist = new ArrayList<String>();
                school = et_school.getText().toString();
                major = et_major.getText().toString();
                pro_course1 = et_pro_course1.getText().toString();
                pro_course2 = et_pro_course2.getText().toString();
                tlist.add(school);
                tlist.add(major);
                tlist.add(exam_type);
                tlist.add(pro_course1);
                tlist.add(pro_course2);
                Log.d("school",school);
                Log.d("major",major);
                Log.d("exam_type",exam_type);
                Log.d("pro_course1",pro_course1);
                serviceManager.setTags(tlist,username);
                Toast.makeText(context,getString(R.string.modify_success),Toast.LENGTH_LONG).show();
            }
        });

        spinner_exam_type.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                exam_type = (String) spinner_exam_type.getSelectedItem();
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                exam_type = getString(R.string.exam_default_type);
            }
        });
        spinner_english_type.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                english_type = (String) spinner_english_type.getSelectedItem();
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                english_type = getString(R.string.english_default);
            }
        });
        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    public void initView(){
        et_school = (EditText)findViewById(R.id.et_school);
        et_major = (EditText)findViewById(R.id.et_major);
        et_pro_course1 = (EditText)findViewById(R.id.professional_course1);
        et_pro_course2 = (EditText)findViewById(R.id.professional_course2);
        spinner_exam_type = (Spinner)findViewById(R.id.spinner_exam_type);
        spinner_english_type = (Spinner)findViewById(R.id.spinner_english_type);
        btn_back = (Button) findViewById(R.id.ibtn_back);
    }
}
