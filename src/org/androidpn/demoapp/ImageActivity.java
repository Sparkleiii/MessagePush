
package org.androidpn.demoapp;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.android.volley.toolbox.Volley;
import org.androidpn.client.Constants;
public class ImageActivity extends Activity {
    private NetworkImageView networkImageView;
    private TextView title;
    private TextView content;
    private RequestQueue mQueue;
    private Button btn_ok;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getActionBar().setDisplayHomeAsUpEnabled(true);
        Intent intent = getIntent();
        String notificationTitle = intent
                .getStringExtra(Constants.NOTIFICATION_TITLE);
        String notificationMessage = intent
                .getStringExtra(Constants.NOTIFICATION_MESSAGE);
        String notificationUri = intent
                .getStringExtra(Constants.NOTIFICATION_URI);
        String url = intent
                .getStringExtra(Constants.NOTIFICATION_IMAGE_URL);
        setContentView(R.layout.image);
        title = (TextView) findViewById(R.id.tv_title);
        content = (TextView) findViewById(R.id.tv_content);
        networkImageView = (NetworkImageView) findViewById(R.id.imageView1);
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

        btn_ok = (Button) findViewById(R.id.btn_image_ok);
        btn_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1;
                if (notificationUri != null
                        && notificationUri.length() > 0
                        && (notificationUri.startsWith("http:") || notificationUri.startsWith("https:")
                        || notificationUri.startsWith("tel:") || notificationUri
                        .startsWith("geo:"))) {
                    intent1 = new Intent(Intent.ACTION_VIEW, Uri.parse(notificationUri));
                    startActivity(intent1);
                } else {
                    finish();
                }
            }
        });

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
}