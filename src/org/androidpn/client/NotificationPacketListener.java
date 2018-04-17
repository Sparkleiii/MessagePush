
package org.androidpn.client;

import org.androidpn.demoapp.DemoAppActivity;
import org.androidpn.iq.DeliverConfirmIQ;
import org.androidpn.iq.NotificationIQ;
import org.androidpn.model.NotificationHistory;
import org.jivesoftware.smack.PacketListener;
import org.jivesoftware.smack.packet.IQ;
import org.jivesoftware.smack.packet.Packet;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.Date;

/** 
 *
 * 客户端接收解析服务器端到达的消息
 * 接收到消息后向服务器提交回执
 *
 */
public class NotificationPacketListener implements PacketListener {

    private static final String LOGTAG = LogUtil
            .makeLogTag(NotificationPacketListener.class);

    private final XmppManager xmppManager;

    private Context context;

    public NotificationPacketListener(XmppManager xmppManager) {
        this.xmppManager = xmppManager;
    }

    @Override
    public void processPacket(Packet packet) {
        Log.d(LOGTAG, "NotificationPacketListener.processPacket()...");
        Log.d(LOGTAG, "packet.toXML()=" + packet.toXML());

        if (packet instanceof NotificationIQ) {
            NotificationIQ notification = (NotificationIQ) packet;

            if (notification.getChildElementXML().contains(
                    "androidpn:iq:notification")) {
                String notificationId = notification.getId();
                String notificationApiKey = notification.getApiKey();
                String notificationTitle = notification.getTitle();
                String notificationMessage = notification.getMessage();
                //                String notificationTicker = notification.getTicker();
                String notificationUri = notification.getUri();
                String notificationImageUrl = notification.getImageUrl();

                Intent intent = new Intent(Constants.ACTION_SHOW_NOTIFICATION);
                intent.putExtra(Constants.NOTIFICATION_ID, notificationId);
                intent.putExtra(Constants.NOTIFICATION_API_KEY,
                        notificationApiKey);
                intent
                        .putExtra(Constants.NOTIFICATION_TITLE,
                                notificationTitle);
                intent.putExtra(Constants.NOTIFICATION_MESSAGE,
                        notificationMessage);
                intent.putExtra(Constants.NOTIFICATION_URI, notificationUri);
                intent.putExtra(Constants.NOTIFICATION_IMAGE_URL, notificationImageUrl);

                //保存消息至历史消息
                NotificationHistory history = new NotificationHistory();
                history.setApiKey(notificationApiKey);
                history.setImageUrl(notificationImageUrl);
                history.setMessage(notificationMessage);
                history.setTitle(notificationTitle);
                history.setUri(notificationUri);
                SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm");
                String time = df.format(new Date());
                history.setTime(time);
                history.save();

                xmppManager.getContext().sendBroadcast(intent);
                //发送已收到回执
                DeliverConfirmIQ deliverConfirmIQ = new DeliverConfirmIQ();
                deliverConfirmIQ.setUuid(notificationId);
                deliverConfirmIQ.setType(IQ.Type.SET);
                xmppManager.getConnection().sendPacket(deliverConfirmIQ);
            }
        }
    }

}
