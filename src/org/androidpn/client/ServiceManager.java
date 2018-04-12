
package org.androidpn.client;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.text.TextUtils;
import android.util.Log;
import org.jivesoftware.smack.packet.IQ;

import java.util.List;
import java.util.Properties;


public final class ServiceManager {

    private static final String LOGTAG = LogUtil
            .makeLogTag(ServiceManager.class);

    private Context context;

    private SharedPreferences sharedPrefs;

    private Properties props;

    private String version = "0.5.0";

    private String apiKey;

    private String xmppHost;

    private String xmppPort;

    private String callbackActivityPackageName;

    private String callbackActivityClassName;

    public ServiceManager(Context context) {
        this.context = context;

        if (context instanceof Activity) {
            Log.i(LOGTAG, "Callback Activity...");
            Activity callbackActivity = (Activity) context;
            callbackActivityPackageName = callbackActivity.getPackageName();
            callbackActivityClassName = callbackActivity.getClass().getName();
        }

        props = loadProperties();
        apiKey = props.getProperty("apiKey", "");
        xmppHost = props.getProperty("xmppHost", "127.0.0.1");
        xmppPort = props.getProperty("xmppPort", "5222");
        Log.i(LOGTAG, "apiKey=" + apiKey);
        Log.i(LOGTAG, "xmppHost=" + xmppHost);
        Log.i(LOGTAG, "xmppPort=" + xmppPort);

        sharedPrefs = context.getSharedPreferences(
                Constants.SHARED_PREFERENCE_NAME, Context.MODE_PRIVATE);
        Editor editor = sharedPrefs.edit();
        editor.putString(Constants.API_KEY, apiKey);
        editor.putString(Constants.VERSION, version);
        editor.putString(Constants.XMPP_HOST, xmppHost);
        editor.putInt(Constants.XMPP_PORT, Integer.parseInt(xmppPort));
        editor.putString(Constants.CALLBACK_ACTIVITY_PACKAGE_NAME,
                callbackActivityPackageName);
        editor.putString(Constants.CALLBACK_ACTIVITY_CLASS_NAME,
                callbackActivityClassName);
        editor.commit();
        // Log.i(LOGTAG, "sharedPrefs=" + sharedPrefs.toString());
    }

    //启动服务
    public void startService() {
        Thread serviceThread = new Thread(new Runnable() {
            @Override
            public void run() {
                Intent intent = NotificationService.getIntent();
                context.startService(intent);
            }
        });
        serviceThread.start();
    }

    public void stopService() {
        Intent intent = NotificationService.getIntent();
        context.stopService(intent);
    }


    //加载配置文件
    private Properties loadProperties() {
        Properties props = new Properties();
        try {
            int id = context.getResources().getIdentifier("androidpn", "raw",
                    context.getPackageName());
            props.load(context.getResources().openRawResource(id));
        } catch (Exception e) {
            Log.e(LOGTAG, "Could not find the properties file.", e);
            // e.printStackTrace();
        }
        return props;
    }

    public void setAlias(String alias) {
        String username = sharedPrefs.getString(Constants.XMPP_USERNAME, "");
        if (TextUtils.isEmpty(alias) || TextUtils.isEmpty(username)) {
            return;
        }
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                NotificationService notificationService = NotificationService.getNotificationService();
                XmppManager xmppManager = notificationService.getXmppManager();
                if (xmppManager!=null){
                    if(!xmppManager.isAuthenticated()){
                        try {
                            synchronized (xmppManager){
                                xmppManager.wait();
                            }
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                    SetAliasIQ iq = new SetAliasIQ();
                    iq.setType(IQ.Type.SET);
                    iq.setUsername(username);
                    iq.setAlias(alias);
                    xmppManager.getConnection().sendPacket(iq);
                }


            }
        }).start();

    }
    public void setTags(List<String> tagList){
        final String username = sharedPrefs.getString(Constants.XMPP_USERNAME, "");
        if (TextUtils.isEmpty(username) || tagList.isEmpty()|| tagList==null) {
            return;
        }
        new Thread(new Runnable() {
            @Override
            public void run() {
                try{
                    Thread.sleep(1000);
                }catch (Exception e){
                    e.printStackTrace();
                }
                NotificationService notificationService = NotificationService.getNotificationService();
                XmppManager xmppManager = notificationService.getXmppManager();
                if(xmppManager!=null){
                    if(!xmppManager.isAuthenticated()){
                        try {
                            synchronized (xmppManager){
                                Log.d(LOGTAG,"wating for auth...");
                                xmppManager.wait();
                            }
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                    Log.d(LOGTAG,"SetTagsIQ...");
                    SetTagsIQ iq = new SetTagsIQ();
                    iq.setType(IQ.Type.SET);
                    iq.setTagList(tagList);
                    iq.setUsername(username);
                    Log.d(LOGTAG,"username+++++"+username);
                    xmppManager.getConnection().sendPacket(iq);
                }
            }
        }).start();
    }

    public void setNotificationIcon(int iconId) {
        Editor editor = sharedPrefs.edit();
        editor.putInt(Constants.NOTIFICATION_ICON, iconId);
        editor.commit();
    }


    public static void viewNotificationSettings(Context context) {
        Intent intent = new Intent().setClass(context,
                NotificationSettingsActivity.class);
        context.startActivity(intent);
    }

}
