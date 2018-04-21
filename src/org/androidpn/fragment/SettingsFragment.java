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
    private Spinner spinner;
    private EditText et_school;
    private EditText et_major;
    private Button btn_submit;
    private String school;
    private String major;
    private String studentResource;
    private ServiceManager serviceManager;
    private Context context;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view =inflater.inflate(R.layout.set_tags,container,false);
        context = getActivity().getApplicationContext();
        serviceManager = new ServiceManager(getActivity());
        initView(view);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                studentResource = (String) spinner.getSelectedItem();
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                studentResource = getString(R.string.student_resource);
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
                tlist.add(school);
                tlist.add(major);
                tlist.add(studentResource);
                Log.d("school",school);
                Log.d("major",major);
                Log.d("studentResource",studentResource);
                serviceManager.setTags(tlist,username);
                Toast.makeText(getActivity(),"success",Toast.LENGTH_LONG).show();
            }
        });
    }

    public void initView(View view){
        et_school = (EditText) view.findViewById(R.id.et_school);
        et_major = (EditText) view.findViewById(R.id.et_major);
        spinner = (Spinner) view.findViewById(R.id.spinner_resource);
    }
}
