package org.androidpn.demoapp;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.*;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import org.androidpn.client.Constants;
import org.androidpn.client.LogUtil;
import org.androidpn.model.NotificationHistory;
import org.litepal.crud.DataSupport;

import java.util.ArrayList;
import java.util.List;

public class NotificationHistoryActivity extends Activity{
    private static final String LOGTAG = LogUtil
            .makeLogTag(NotificationHistoryActivity.class);
    private ListView mlistView;
    private NotificationHistoryAdapter mAdapter;
    private List<NotificationHistory>  mlist = new ArrayList<NotificationHistory>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.notification_history);
        mlist = DataSupport.findAll(NotificationHistory.class);
        mlistView = (ListView)findViewById(R.id.list_view_history);
        getActionBar().setDisplayHomeAsUpEnabled(true);
        /**
         * item点击事件
         * **/
        mlistView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                NotificationHistory history = mlist.get(position);
                Intent intent = new Intent(NotificationHistoryActivity.this,
                        ImageActivity.class);
                intent.putExtra(Constants.NOTIFICATION_API_KEY, history.getApiKey());
                intent.putExtra(Constants.NOTIFICATION_TITLE, history.getTitle());
                intent.putExtra(Constants.NOTIFICATION_MESSAGE, history.getMessage());
                intent.putExtra(Constants.NOTIFICATION_URI, history.getUri());
                intent.putExtra(Constants.NOTIFICATION_IMAGE_URL, history.getImageUrl());
                startActivity(intent);
            }
        });

        mAdapter = new NotificationHistoryAdapter(this,0,mlist);
        mlistView.setAdapter(mAdapter);
        registerForContextMenu(mlistView);
    }

    //创建上下文菜单
    @Override
    public void onCreateContextMenu(ContextMenu menu, View v,
                                    ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        menu.add(0,0,0,"Remove");
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

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                finish();
                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * NotificationHistoryAdapter--->NotificationHistory
     * */
    class NotificationHistoryAdapter extends ArrayAdapter<NotificationHistory> {

        public NotificationHistoryAdapter(Context context, int resource,
                                          List<NotificationHistory> objects) {
            super(context, resource, objects);
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
            titleTextView.setText(history.getTitle());
            timeTextView.setText(history.getTime());
            return view;
        }
    }
}

