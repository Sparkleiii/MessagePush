package org.androidpn.demoapp;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
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
        mAdapter = new NotificationHistoryAdapter(this,0,mlist);
        mlistView.setAdapter(mAdapter);
    }

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

