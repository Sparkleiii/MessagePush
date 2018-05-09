
package org.androidpn.demoapp;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.*;
import android.widget.*;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.androidpn.client.Constants;
import org.androidpn.client.ServiceManager;
import org.androidpn.model.Comment;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.util.ArrayList;
import java.util.List;

public class ImageActivity extends Activity {
    private NetworkImageView networkImageView;
    private TextView title;
    private TextView content;
    private RequestQueue mQueue;
    private Button ibtn_back;
    private ServiceManager serviceManager;
    private Long notId = null;
    //comment_item

    private TextView tv_username;
    private TextView tv_local;
    private TextView tv_comment_content;
    private TextView tv_comment_time;
    //评论listview
    private ListView mlistView;
    private CommentAdapter mAdapter;
    private List<Comment> mlist = new ArrayList<>();
    private List<NameValuePair> params;
    private Gson gson = new Gson();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        Intent intent = getIntent();
        notId =intent.getLongExtra(Constants.NOTIFICATION_ID,0);
        String notificationTitle = intent
                .getStringExtra(Constants.NOTIFICATION_TITLE);
        String notificationMessage = intent
                .getStringExtra(Constants.NOTIFICATION_MESSAGE);
        String notificationUri = intent
                .getStringExtra(Constants.NOTIFICATION_URI);
        String url = intent
                .getStringExtra(Constants.NOTIFICATION_IMAGE_URL);
        setContentView(R.layout.comment);
        initView();
        if(mlist!=null){
            mAdapter = new CommentAdapter(this,0,mlist);
            mlistView.setAdapter(mAdapter);
            registerForContextMenu(mlistView);
            setListViewHight();
        }else{
            TextView tv_no_comment = (TextView) findViewById(R.id.tv_no_comment);
            tv_no_comment.setVisibility(View.VISIBLE);
            mlistView.setVisibility(View.GONE);
        }
        ibtn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        mQueue = Volley.newRequestQueue(this);
        ImageLoader imageLoader = new ImageLoader(mQueue, new ImageLoader.ImageCache() {
            @Override
            public Bitmap getBitmap(String s) {
                return null;
            }
            @Override
            public void putBitmap(String s, Bitmap bitmap) {
            }
        });
        title.setText(notificationTitle);
        content.setText(notificationMessage);
        networkImageView.setImageUrl(url,imageLoader);
    }


    private void initView() {
        serviceManager = new ServiceManager(this);
        title = (TextView) findViewById(R.id.tv_title);
        content = (TextView) findViewById(R.id.tv_content);
        ibtn_back = (Button) findViewById(R.id.ibtn_back);
        networkImageView = (NetworkImageView) findViewById(R.id.imageView1);
        //评论listview
        mlistView = (ListView) findViewById(R.id.list_view_comment);
        mlist = findCommentByNotId(notId+"");
        Log.d("mlist", String.valueOf(mlist));

    }

    /**
     * 通过消息编号获取该消息的评论列表
     * @param notId
     * @return
     */
    private List<Comment> findCommentByNotId(String notId) {
        serviceManager.setUrl("/manager.do?action=getCommentByNotId");
        params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("notId",notId));
        serviceManager.setParams(params);
        serviceManager = serviceManager.RequestToServer(serviceManager);
        List<Comment> NIList = gson.fromJson(serviceManager.getJsonData(),new TypeToken<List<Comment>>(){}.getType());
        Log.d("NIList", String.valueOf(NIList));
        return NIList;
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
    class CommentAdapter extends ArrayAdapter<Comment> {

        public CommentAdapter(Context context, int resource,
                                          List<Comment> objects) {
            super(context, resource, objects);
        }
        public void RefreshList(List<Comment> arrayList){
            arrayList.clear();
            arrayList.addAll(findCommentByNotId(notId+""));
            mAdapter.notifyDataSetChanged();
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            Comment comment = getItem(position);
            View view;
            if (convertView == null) {
                view = LayoutInflater.from(getContext()).inflate(R.layout.comment_item,null);
            } else {
                view = convertView;
            }
            tv_username = (TextView)view.findViewById(R.id.tv_comment_username);
            tv_local = (TextView) view.findViewById(R.id.comment_tv_local);
            tv_comment_content = (TextView) view.findViewById(R.id.tv_comment_content);
            tv_comment_time = (TextView) view.findViewById(R.id.tv_comment_time);
            tv_username.setText(comment.getAccount());
//            tv_local.setText(comment.get);
            tv_comment_content.setText(comment.getContent());
            tv_comment_time.setText(comment.getTime());
            return view;
        }
    }
    /**
     * 解决ScrollView中ListView仅显示一条
     */
    public void setListViewHight(){
        int totalHeight = 0;                                    // 定义、初始化listview总高度值
        for (int i = 0; i < mAdapter.getCount(); i++) {
            View listItem = mAdapter.getView(i, null, mlistView);          // 获取单个item
            listItem.setLayoutParams(new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));// 设置item高度为适应内容
            listItem.measure(0, 0);                                        // 测量现在item的高度
            totalHeight += listItem.getMeasuredHeight();                   // 总高度增加一个listitem的高度
        }
        ViewGroup.LayoutParams params = mlistView.getLayoutParams();
        params.height = totalHeight + (mlistView.getDividerHeight() * (mAdapter.getCount() - 1)); // 将分割线高度加上总高度作为最后listview的高度
        mlistView.setLayoutParams(params);
    }

}