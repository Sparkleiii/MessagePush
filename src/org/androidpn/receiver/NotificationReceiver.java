
package org.androidpn.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import org.androidpn.client.Constants;
import org.androidpn.client.LogUtil;
import org.androidpn.client.NotificationService;
import org.androidpn.client.Notifier;
import org.androidpn.iq.UserLoginIQ;
import org.androidpn.iq.UserRegisterIQ;
import org.jivesoftware.smack.packet.IQ;

public final class NotificationReceiver extends BroadcastReceiver {

    private static final String LOGTAG = LogUtil
            .makeLogTag(NotificationReceiver.class);

    private NotificationService notificationService;


    public NotificationReceiver(NotificationService notificationService) {
        this.notificationService = notificationService;
    }


    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d(LOGTAG, "NotificationReceiver.onReceive()...");
        String action = intent.getAction();
        Log.d(LOGTAG, "action=" + action);

        if (Constants.ACTION_SHOW_NOTIFICATION.equals(action)) {
            String notificationId = intent
                    .getStringExtra(Constants.NOTIFICATION_ID);
            String notificationApiKey = intent
                    .getStringExtra(Constants.NOTIFICATION_API_KEY);
            String notificationTitle = intent
                    .getStringExtra(Constants.NOTIFICATION_TITLE);
            String notificationMessage = intent
                    .getStringExtra(Constants.NOTIFICATION_MESSAGE);
            String notificationUri = intent
                    .getStringExtra(Constants.NOTIFICATION_URI);
            String notificationImageUrl = intent.
                    getStringExtra(Constants.NOTIFICATION_IMAGE_URL);

            Log.d(LOGTAG, "notificationId=" + notificationId);
            Log.d(LOGTAG, "notificationApiKey=" + notificationApiKey);
            Log.d(LOGTAG, "notificationTitle=" + notificationTitle);
            Log.d(LOGTAG, "notificationMessage=" + notificationMessage);
            Log.d(LOGTAG, "notificationUri=" + notificationUri);
            Log.d(LOGTAG, "notificationImageUrl=" + notificationImageUrl);

            Notifier notifier = new Notifier(context);
            notifier.notify(notificationId, notificationApiKey,
                    notificationTitle, notificationMessage, notificationUri,notificationImageUrl);
        } else if (Constants.ACTION_USER_LOGIN.equals(action)) {
            // ..用户登录事件
            String account = intent.getStringExtra("account");
            String password = intent.getStringExtra("password");
            String timestamp = intent.getStringExtra("timestamp");
            String clientId = notificationService.getXmppManager()
                    .getUsername();
            Log.d("TAG", "clientId = " + clientId);

            UserLoginIQ userLoginIQ = new UserLoginIQ();
            userLoginIQ.setAccount(account);
            userLoginIQ.setPassword(password);
            userLoginIQ.setTimestamp(timestamp);
            userLoginIQ.setClientId(clientId);
            Log.d("TAG", "clientId = " + clientId);

            userLoginIQ.setType(IQ.Type.SET);
            notificationService.getXmppManager().getConnection()
                    .sendPacket(userLoginIQ);
        } else if (Constants.ACTION_USER_REGISTER.equals(action)) {
            // .. 用户注册事件

            String account = intent.getStringExtra("account");
            String password = intent.getStringExtra("password");

            UserRegisterIQ userRegisterIQ = new UserRegisterIQ();
            userRegisterIQ.setAccount(account);
            userRegisterIQ.setPassword(password);

            userRegisterIQ.setType(IQ.Type.SET);
            notificationService.getXmppManager().getConnection()
                    .sendPacket(userRegisterIQ);

        }

    }

}
