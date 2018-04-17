package org.androidpn.fragment;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.*;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import org.androidpn.client.Constants;
import org.androidpn.client.LogUtil;
import org.androidpn.demoapp.ImageActivity;
import org.androidpn.demoapp.R;
import org.androidpn.model.NotificationHistory;
import org.litepal.crud.DataSupport;

import java.util.ArrayList;
import java.util.List;

public class HistoryFragment extends Fragment{
    private static final String LOGTAG = LogUtil
            .makeLogTag(HistoryFragment.class);
    private ListView mlistView;
    private NotificationHistoryAdapter mAdapter;
    private List<NotificationHistory> mlist = new ArrayList<NotificationHistory>();
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View view = inflater.inflate(R.layout.notification_history,container,false);
        mlist = DataSupport.findAll(NotificationHistory.class);
        mlistView = (ListView)view.findViewById(R.id.list_view_history);
//        requestWindowFeature(Window.FEATURE_NO_TITLE);
        /**
         * item点击事件
         * **/
        mlistView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                NotificationHistory history = mlist.get(position);
                /**
                 * 可能错
                 */
                Intent intent = new Intent(view.getContext(),
                        ImageActivity.class);
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
