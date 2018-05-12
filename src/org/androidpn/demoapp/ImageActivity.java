
package org.androidpn.demoapp;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.*;
import android.view.inputmethod.InputMethodManager;
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
import org.androidpn.view.ListViewForScrollView;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ImageActivity extends Activity implements View.OnClickListener {
    //帖子属性
    private String notificationTitle;
    private String notificationMessage;
    private String notificationUri;
    private String url;
    private Long notId;
    private String account;
    //
    private View mView;
    private NetworkImageView networkImageView;
    private TextView title;
    private TextView content;
    private RequestQueue mQueue;
    private Button ibtn_back;
    private ServiceManager serviceManager;
    //comment_item
    private TextView tv_username;
    private TextView tv_local;
    private TextView tv_comment_content;
    private TextView tv_comment_time;
    //评论listview
    private ListViewForScrollView mlistView;
    private CommentAdapter mAdapter;
    private List<Comment> mlist = new ArrayList<>();
    private List<NameValuePair> params;
    private List<NameValuePair> params2;
    private Gson gson = new Gson();
    //TextView点击控件
    private TextView tv_support;
    private TextView tv_comment;
    private LinearLayout ll_info;
    private LinearLayout ll_comment;
    private EditText et_comment;
    private Button btn_comment_save;
    private LinearLayout ll_comment_list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        Intent intent = getIntent();
        initParams(intent);
        setContentView(R.layout.comment);
        initView();
        initSupport();
        initAdapter();
        initImage(notificationTitle,notificationMessage,url);
    }

    /**
     * 初始化帖子参数
     * @param intent
     */
    private void initParams(Intent intent) {
        notId =intent.getLongExtra(Constants.NOTIFICATION_ID,0);
        notificationTitle = intent
                .getStringExtra(Constants.NOTIFICATION_TITLE);
        notificationMessage = intent
                .getStringExtra(Constants.NOTIFICATION_MESSAGE);
        notificationUri = intent
                .getStringExtra(Constants.NOTIFICATION_URI);
        url = intent
                .getStringExtra(Constants.NOTIFICATION_IMAGE_URL);
        account = intent.getStringExtra("username");
    }

    private void initImage(String notificationTitle,String notificationMessage,String url) {
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

    /**
     *  ListView
     */
    private void initAdapter() {
        TextView tv_no_comment = (TextView) findViewById(R.id.tv_no_comment);
        if(mlist!=null){
            tv_no_comment.setVisibility(View.GONE);
            mlistView.setVisibility(View.VISIBLE);
            mAdapter = new CommentAdapter(this,0,mlist);
            mlistView.setAdapter(mAdapter);
            registerForContextMenu(mlistView);
            setListViewHight();
        }else{
            //设置LL的高度
            ViewGroup.LayoutParams params = ll_comment_list.getLayoutParams();
            params.height = 200;
            Log.d("height", String.valueOf(params.height));
            ll_comment_list.setLayoutParams(params);
            tv_no_comment.setVisibility(View.VISIBLE);
            mlistView.setVisibility(View.GONE);
        }
    }

    /**
     * 初始化控件
     */
    private void initView() {
        serviceManager = new ServiceManager(this);
        title = (TextView) findViewById(R.id.tv_title);
        content = (TextView) findViewById(R.id.tv_content);
        ibtn_back = (Button) findViewById(R.id.ibtn_back);
        networkImageView = (NetworkImageView) findViewById(R.id.imageView1);
        //评论listview
        mlistView = (ListViewForScrollView) findViewById(R.id.list_view_comment);
        mlist = findCommentByNotId(notId+"");
        Log.d("mlist", String.valueOf(mlist));
        //
        tv_support = (TextView) findViewById(R.id.tv_support);
        tv_comment = (TextView) findViewById(R.id.tv_comment);
        ll_info = (LinearLayout) findViewById(R.id.am_ll_info);
        ll_comment = (LinearLayout) findViewById(R.id.ll_leave_message);
        et_comment = (EditText) findViewById(R.id.am_et_msg);
        btn_comment_save = (Button) findViewById(R.id.am_b_save);
        ll_comment_list = (LinearLayout) findViewById(R.id.ll_comment_list);
        ibtn_back.setOnClickListener(this);
        btn_comment_save.setOnClickListener(this);
        tv_support.setOnClickListener(this);
        tv_comment.setOnClickListener(this);


    }

    public void refresh() {
        if(mlist!=null&&!mlist.isEmpty()){
            mlist.clear();
            mlist.addAll(findCommentByNotId(notId+""));
            mAdapter.notifyDataSetChanged();
        }else{
            mlist = findCommentByNotId(notId+"");
            Log.d("mlist find", String.valueOf(mlist));
            initAdapter();
        }
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
     * 显示或隐藏输入法
     */
    private void onFocusChange(boolean hasFocus) {
        final boolean isFocus = hasFocus;
        (new Handler()).postDelayed(new Runnable() {
            public void run() {
                InputMethodManager imm = (InputMethodManager)
                        getApplicationContext().getSystemService(INPUT_METHOD_SERVICE);
                if (isFocus) {
                    // 弹出输入法
                    imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
                    et_comment.setFocusable(true);
                    et_comment.requestFocus();
                } else {
                    //隐藏输入法
                    imm.hideSoftInputFromWindow(et_comment.getWindowToken(), 0);
                }
            }
        }, 100);
    }

    //评论弹出输入法
    private void comment(boolean flag) {
        if(flag){
            ll_info.setVisibility(View.GONE);
            ll_comment.setVisibility(View.VISIBLE);
            onFocusChange(flag);
        }else{
            ll_info.setVisibility(View.VISIBLE);
            ll_comment.setVisibility(View.GONE);
            onFocusChange(flag);
        }
    }
    //点赞
    private void support() {
        boolean flag = isSupport();
        if(flag){
            tv_support.setText(R.string.support);
            Drawable drawable = getResources().getDrawable(R.drawable.icon_support);
            drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
            tv_support.setCompoundDrawables(drawable,null,null,null);
            cancelSupport();
        }else{
            tv_support.setText(R.string.hasSupport);
            Drawable drawable = getResources().getDrawable(R.drawable.icon_support_pressed);
            drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
            tv_support.setCompoundDrawables(drawable,null,null,null);
            sendSupport();
        }
    }

    /**
     * 点击事件
     * @param v
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            //点赞
            case R.id.tv_support:
                support();
                break;
             //评论
            case R.id.tv_comment:
                comment(true);

                break;
             //发表评论
            case R.id.am_b_save:
                sendComment(et_comment.getText().toString());
                refresh();
                comment(false);
                Toast.makeText(this,R.string.comment_success,Toast.LENGTH_SHORT).show();
                break;
             //返回按钮
            case R.id.ibtn_back:
                finish();
                break;
            default:
                break;
        }
    }


    /**
     * 通过消息编号获取该消息的评论列表
     * @param notId
     * @return List<Comment>
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

    /**
     * 发送评论消息至服务器端
     * @param content 评论内容
     */
    private void sendComment(String content) {
        serviceManager.setUrl("/manager.do?action=sendComment");
        params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("notId",notId+""));
        params.add(new BasicNameValuePair("account",account));
        params.add(new BasicNameValuePair("content",content));
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String time = sdf.format(date);
        Log.d("notId", String.valueOf(notId));
        Log.d("account",account);
        Log.d("content",content);
        Log.d("time",time);
        Comment comment = new Comment();
        params.add(new BasicNameValuePair("time",time));
        serviceManager.setParams(params);
        serviceManager = serviceManager.RequestToServer(serviceManager);
        String result = serviceManager.getJsonData();
        Log.d("发表评论结果",result);
    }

    /**
     * 点赞
     */
    public void sendSupport(){
        serviceManager.setUrl("/manager.do?action=support");
        params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("notId",notId+""));
        params.add(new BasicNameValuePair("account",account));
        serviceManager.setParams(params);
        serviceManager = serviceManager.RequestToServer(serviceManager);
        String result = serviceManager.getJsonData();
        Log.d("点赞结果",result);
    }

    //初始化点赞按钮
    public void initSupport(){
        boolean flag = isSupport();
        if(flag){
            tv_support.setText(R.string.hasSupport);
            Drawable drawable = getResources().getDrawable(R.drawable.icon_support_pressed);
            drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
            tv_support.setCompoundDrawables(drawable,null,null,null);
        }else{
            tv_support.setText(R.string.support);
            Drawable drawable = getResources().getDrawable(R.drawable.icon_support);
            drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
            tv_support.setCompoundDrawables(drawable,null,null,null);
        }
    }
    /**
     * 取消点赞
     */
    public void cancelSupport(){
        serviceManager.setUrl("/manager.do?action=cancelSupport");
        params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("notId",notId+""));
        params.add(new BasicNameValuePair("account",account));
        serviceManager.setParams(params);
        serviceManager = serviceManager.RequestToServer(serviceManager);
        String result = serviceManager.getJsonData();
        Log.d("取消结果",result);
    }

    /**
     * 是否已点赞
     * @return
     */
    public boolean isSupport(){
        boolean flag = true;
        serviceManager.setUrl("/manager.do?action=isSupport");
        params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("notId",notId+""));
        params.add(new BasicNameValuePair("account",account));
        Log.d("notId", String.valueOf(notId));
        Log.d("account",account);
        serviceManager.setParams(params);
        serviceManager = serviceManager.RequestToServer(serviceManager);
        String result = serviceManager.getJsonData();
        Log.d("cancelResult",result);
        if(result.equals("no")){
                flag = false;
        }
        Log.d("flag", String.valueOf(flag));
        return flag;
    }

    /**
     * 获取点赞数
     * @return
     */
    public int getSupportNum(){
        serviceManager.setUrl("/manager.do?action=getSupportNum");
        params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("notId",notId+""));
        Log.d("notId", String.valueOf(notId));
        serviceManager.setParams(params);
        serviceManager = serviceManager.RequestToServer(serviceManager);
        int num = gson.fromJson(serviceManager.getJsonData(),new TypeToken<Integer>(){}.getType());
        return num;
    }

    /**
     * CommentAdapter--->Comment
     * */
    class CommentAdapter extends ArrayAdapter<Comment> {

        public CommentAdapter(Context context, int resource,
                                          List<Comment> objects) {
            super(context, resource, objects);
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
            tv_comment_content.setText(comment.getContent());
            tv_comment_time.setText(comment.getTime());
            return view;
        }
    }
    /**
     * 解决ScrollView中ListView仅显示一条
     */
    public void setListViewHight(){
        int totalHeight = 0;
        // 定义、初始化listview总高度值
        for (int i = 0; i < mAdapter.getCount(); i++) {
            View listItem = mAdapter.getView(i, null, mlistView);
            // 获取单个item
            listItem.setLayoutParams(new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
            // 设置item高度为适应内容
            listItem.measure(0, 0);
            // 测量现在item的高度
            totalHeight += listItem.getMeasuredHeight();
            // 总高度增加一个listitem的高度
        }
        ViewGroup.LayoutParams params = mlistView.getLayoutParams();
        params.height = totalHeight + (mlistView.getDividerHeight() * (mAdapter.getCount() - 1));
        // 将分割线高度加上总高度作为最后listview的高度
        mlistView.setLayoutParams(params);
    }

}