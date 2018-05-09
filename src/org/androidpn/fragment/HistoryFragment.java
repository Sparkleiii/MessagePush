package org.androidpn.fragment;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.*;
import android.widget.*;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.android.volley.toolbox.Volley;
import org.androidpn.client.Constants;
import org.androidpn.client.LogUtil;
import org.androidpn.demoapp.ImageActivity;
import org.androidpn.demoapp.R;
import org.androidpn.model.NotificationHistory;
import org.androidpn.view.RefreshableView;
import org.litepal.crud.DataSupport;

import java.util.ArrayList;
import java.util.List;

public class HistoryFragment extends Fragment{
    private static final String LOGTAG = LogUtil
            .makeLogTag(HistoryFragment.class);
    private ListView mlistView;
    private NotificationHistoryAdapter mAdapter;
    private NetworkImageView niv_imageView;
    private List<NotificationHistory> mlist = new ArrayList<>();
    private RequestQueue mQueue;
    private String url;
    private Handler handler;
    private RefreshableView refreshableView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View view = inflater.inflate(R.layout.notification_history,container,false);
        initView(view);
        handler = new Handler();
        Runnable refreshUI = new Runnable(){
            @Override
            public void run() {
                mlist.clear();
                mlist.addAll(DataSupport.findAll(NotificationHistory.class));
                mAdapter.notifyDataSetChanged();
            }
        };

        refreshableView.setOnRefreshListener(new RefreshableView.PullToRefreshListener() {
            @Override
            public void onRefresh() {
                try {
                    handler.post(refreshUI);
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                refreshableView.finishRefreshing();
            }
        }, 1);

        /**
         * item点击事件
         * **/
        mlistView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                NotificationHistory history = mlist.get(position);
                Intent intent = new Intent(view.getContext(),
                        ImageActivity.class);
                intent.putExtra(Constants.NOTIFICATION_ID,history.getNotId());
                Log.d("notId", String.valueOf(history.getNotId()));
                intent.putExtra(Constants.NOTIFICATION_API_KEY, history.getApiKey());
                intent.putExtra(Constants.NOTIFICATION_TITLE, history.getTitle());
                intent.putExtra(Constants.NOTIFICATION_MESSAGE, history.getMessage());
                intent.putExtra(Constants.NOTIFICATION_URI, history.getUri());
                intent.putExtra(Constants.NOTIFICATION_IMAGE_URL, history.getImageUrl());
                startActivity(intent);
            }
        });
        mAdapter = new NotificationHistoryAdapter(view.getContext(),0,mlist);
        mlistView.setAdapter(mAdapter);
        registerForContextMenu(mlistView);
        return view;
    }
    //初始化控件
    private void initView(View view) {
        mlist = DataSupport.findAll(NotificationHistory.class);
        mlistView = (ListView)view.findViewById(R.id.list_view_history);
        Log.d("mlistview", String.valueOf(mlistView));
        mQueue = Volley.newRequestQueue(view.getContext());
        refreshableView = (RefreshableView) view.findViewById(R.id.refreshable_view);
    }

    //创建上下文菜单
    @Override
    public void onCreateContextMenu(ContextMenu menu, View v,
                                    ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        menu.add(0,0,0,R.string.delete);
    }

    //上下文菜单点击事件
    @Override
    public boolean onContextItemSelected(MenuItem item) {
        if(item.getItemId() == 0){
            AdapterView.AdapterContextMenuInfo menuInfo = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
            int index = menuInfo.position;
            NotificationHistory history = mlist.get(index);
            history.delete();
            mlist.remove(index);
            mAdapter.notifyDataSetChanged();
        }
        return super.onContextItemSelected(item);
    }

    /**
     * NotificationHistoryAdapter--->NotificationHistory
     * */
    class NotificationHistoryAdapter extends ArrayAdapter<NotificationHistory> {

        public NotificationHistoryAdapter(Context context, int resource,
                                          List<NotificationHistory> objects) {
            super(context, resource, objects);
        }
        public void RefreshList(List<NotificationHistory> arrayList){
            arrayList.clear();
            arrayList.addAll(DataSupport.findAll(NotificationHistory.class));
            mAdapter.notifyDataSetChanged();
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            NotificationHistory history = getItem(position);
            View view;
            if (convertView == null) {
                view = LayoutInflater.from(getContext()).inflate(R.layout.notification_history_item,null);
            } else {
                view = convertView;
            }
            TextView titleTextView = (TextView) view.findViewById(R.id.tv_titles);
            TextView timeTextView = (TextView) view.findViewById(R.id.tv_time);
            niv_imageView = (NetworkImageView) view.findViewById(R.id.niv_imageView);
            titleTextView.setText(history.getTitle());
            timeTextView.setText(history.getTime());
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
            url = history.getImageUrl();
            niv_imageView.setImageUrl(url,imageLoader);
            return view;
        }
    }


}
