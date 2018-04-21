package org.androidpn.fragment;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import org.androidpn.client.LogUtil;
import org.androidpn.client.ServiceManager;
import org.androidpn.demoapp.R;
import java.util.ArrayList;
import java.util.List;

public class SettingsFragment extends Fragment{
    private static final String LOGTAG = LogUtil
            .makeLogTag(SettingsFragment.class);
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
    private Context context;
    private ServiceManager serviceManager;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view =inflater.inflate(R.layout.set_tags,container,false);
        context = getActivity().getApplicationContext();
        serviceManager = new ServiceManager(getActivity());
        initView(view);
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
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Log.d("onActivityCreated","onActivityCreated");
        btn_submit = (Button) getActivity().findViewById(R.id.btn_submit);
        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = getActivity().getIntent().getStringExtra("username");
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
                serviceManager.setTags(tlist,username);
                Toast.makeText(getActivity(),getString(R.string.modify_success),Toast.LENGTH_LONG).show();
            }
        });
    }

    public void initView(View view){
        et_school = (EditText) view.findViewById(R.id.et_school);
        et_major = (EditText) view.findViewById(R.id.et_major);
        et_pro_course1 = (EditText) view.findViewById(R.id.professional_course1);
        et_pro_course2 = (EditText) view.findViewById(R.id.professional_course2);
        spinner_exam_type = (Spinner) view.findViewById(R.id.spinner_exam_type);
        spinner_english_type = (Spinner) view.findViewById(R.id.spinner_english_type);
    }
}
