package org.androidpn.fragment;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.androidpn.client.Constants;
import org.androidpn.client.ServiceManager;
import org.androidpn.demoapp.ImageActivity;
import org.androidpn.demoapp.R;
import org.androidpn.model.NotInformation;
import org.androidpn.view.RefreshableView;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment{
    private Button btn_filter;
    private ListView mlistView;
    private EditText et_type;
    private NotInformationAdapter mAdapter;
    private NetworkImageView niv_imageView;
    private ServiceManager serviceManager;
    private List<NotInformation> mlist = new ArrayList<>();
    private RequestQueue mQueue;
    private String url;
    private String type;
    private Handler handler;
    private RefreshableView refreshableView;
    private Context context;
    private List<NameValuePair> params;
    private Gson gson = new Gson();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.tab_home,container,false);
        context = getActivity().getApplicationContext();
        initView(view);
        handler = new Handler();
        Runnable refreshAllUI = new Runnable(){
            @Override
            public void run() {
                mlist.clear();
                mlist.addAll(findAllNotInformation());
                mAdapter.notifyDataSetChanged();
            }
        };
        Runnable refreshUIByType = new Runnable(){
            @Override
            public void run() {
                mlist.clear();
                mlist.addAll(findNotInformationByType());
                mAdapter.notifyDataSetChanged();
            }
        };

        btn_filter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handler.post(refreshUIByType);
            }
        });

        refreshableView.setOnRefreshListener(new RefreshableView.PullToRefreshListener() {
            @Override
            public void onRefresh() {
                try {
                    //重绘listview
                    handler.post(refreshAllUI);
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                refreshableView.finishRefreshing();
            }
        }, 0);

        /**
         * item点击事件
         * **/
        mlistView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                NotInformation information = mlist.get(position);
                Intent intent = new Intent(view.getContext(),
                        ImageActivity.class);
                intent.putExtra(Constants.NOTIFICATION_ID,information.getNotId());
                intent.putExtra(Constants.NOTIFICATION_API_KEY, "");
                intent.putExtra(Constants.NOTIFICATION_TITLE, information.getTitle());
                intent.putExtra(Constants.NOTIFICATION_MESSAGE, information.getMessage());
                intent.putExtra(Constants.NOTIFICATION_URI, information.getUri());
                intent.putExtra(Constants.NOTIFICATION_IMAGE_URL, information.getImageUrl());
                startActivity(intent);
            }
        });
        mAdapter = new NotInformationAdapter(view.getContext(),0,mlist);
        Log.d("mAdapter", String.valueOf(mAdapter));
        mlistView.setAdapter(mAdapter);
        registerForContextMenu(mlistView);
        return view;
    }


    public List<NotInformation> findAllNotInformation(){
        serviceManager.setUrl("/user.do?action=getNotInformation");
        serviceManager = serviceManager.RequestToServer(serviceManager);
        List<NotInformation> NIList = gson.fromJson(serviceManager.getJsonData(),new TypeToken<List<NotInformation>>(){}.getType());
        Log.d("NIList", String.valueOf(NIList));
        return NIList;
    }

    public List<NotInformation> findNotInformationByType(){
        params = new ArrayList<NameValuePair>();
        serviceManager.setUrl("/user.do?action=getNotInformationByType");
        type = et_type.getText().toString();
        params.add(new BasicNameValuePair("type",type));
        serviceManager.setParams(params);
        serviceManager = serviceManager.RequestToServer(serviceManager);
        List<NotInformation> NIList = gson.fromJson(serviceManager.getJsonData(),new TypeToken<List<NotInformation>>(){}.getType());
        return NIList;
    }

    private void initView(View view) {
        serviceManager = new ServiceManager(context);
        mlist = findAllNotInformation();
        Log.d("mlist", String.valueOf(mlist));
        mlistView = (ListView) view.findViewById(R.id.list_view_information);
        mQueue = Volley.newRequestQueue(view.getContext());
        refreshableView = (RefreshableView) view.findViewById(R.id.refreshable_infor_view);
        btn_filter = (Button) view.findViewById(R.id.btn_filter);
        et_type = (EditText) view.findViewById(R.id.et_type);
    }

    /**
     * NotInformationAdapter--->NotInformation
     **/
    class NotInformationAdapter extends ArrayAdapter<NotInformation> {

        public NotInformationAdapter(Context context, int resource,
                                          List<NotInformation> objects) {
            super(context, resource, objects);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            NotInformation information = getItem(position);
            View view;
            if (convertView == null) {
                view = LayoutInflater.from(getContext()).inflate(R.layout.ni_information_item,null);
            } else {
                view = convertView;
            }
            TextView titleTextView = (TextView) view.findViewById(R.id.tv_infor_titles);
            TextView timeTextView = (TextView) view.findViewById(R.id.tv_infor_time);
            niv_imageView = (NetworkImageView) view.findViewById(R.id.niv_infor_image);
            TextView contentTextView = (TextView) view.findViewById(R.id.tv_infor_content);
            titleTextView.setText(information.getTitle());
            timeTextView.setText(information.getTime());
            contentTextView.setText(information.getMessage());
            /**
             * 加载网络图片
             */
            ImageLoader imageLoader = new ImageLoader(mQueue, new ImageLoader.ImageCache() {
                @Override
                public Bitmap getBitmap(String s) {
                    return null;
                }
                @Override
                public void putBitmap(String s, Bitmap bitmap) {
                }
            });
            url = information.getImageUrl();
            niv_imageView.setImageUrl(url,imageLoader);
            return view;
        }
    }
}
