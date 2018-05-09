package org.androidpn.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import org.androidpn.demoapp.LoginActivity;
import org.androidpn.demoapp.R;

public class BootFragment extends Fragment {
    private int[] bgRes = {R.drawable.guide_1,R.drawable.guide_2,R.drawable.guide_3,R.drawable.guide_4};
    private Button btn_index;
    private RelativeLayout rl;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.boot_fra_content,container,false);
        initView(view);
        int index = getArguments().getInt("index");
        rl.setBackgroundResource(bgRes[index]);
        btn_index.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(),LoginActivity.class);
                startActivity(intent);
            }
        });
        //在第四张图片显示按钮
        btn_index.setVisibility(index==3?View.VISIBLE:View.GONE);
        return view;
    }

    private void initView(View view) {
        btn_index = (Button) view.findViewById(R.id.btn_index);
        rl = (RelativeLayout) view.findViewById(R.id.boot_relativeLayout);
    }


}
