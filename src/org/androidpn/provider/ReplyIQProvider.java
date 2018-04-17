package org.androidpn.provider;

import android.util.Log;
import org.jivesoftware.smack.packet.IQ;
import org.jivesoftware.smack.provider.IQProvider;
import org.androidpn.client.LogUtil;
import org.androidpn.client.ReplyPacketListener;
import org.androidpn.iq.ReplyResultIQ;
import org.xmlpull.v1.XmlPullParser;

public class ReplyIQProvider implements IQProvider {
	private static final String LOGTAG = LogUtil
			.makeLogTag(ReplyPacketListener.class);

	@Override
	public IQ parseIQ(XmlPullParser parser) throws Exception {
		Log.d(LOGTAG, "ReplyIQProvider.parseIQ()...");
		ReplyResultIQ reply = new ReplyResultIQ();
		for (boolean done = false; !done;) {
			int eventType = parser.next();
			if (eventType == 2) {
				if ("method".equals(parser.getName())) {
					reply.setMethod(parser.nextText());
					Log.d(LOGTAG,"method");
				}
				if ("ecode".equals(parser.getName())) {
					reply.setEcode(parser.nextText());
					Log.d(LOGTAG,"ecode");
				}
				if ("emsg".equals(parser.getName())) {
					reply.setEmsg(parser.nextText());
					Log.d(LOGTAG,"emsg");
				}
			} else if (eventType == 3
					&& reply.getMethod().equals(parser.getName())) {
				done = true;
			}
		}
		return reply;
	}

}
