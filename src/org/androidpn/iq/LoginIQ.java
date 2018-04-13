package org.androidpn.iq;

import org.jivesoftware.smack.packet.IQ;

public class LoginIQ extends IQ {
    private String uid;
    private String uname;
    private String upwd;

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getUname() {
        return uname;
    }
    public void setUname(String uname) {
        this.uname = uname;
    }

    public String getUpwd() {
        return upwd;
    }

    public void setUpwd(String upwd) {
        this.upwd = upwd;
    }

    @Override
    public String getChildElementXML() {
        StringBuilder buf = new StringBuilder();
        buf.append("<").append("login").append(" xmlns=\"").append(
                "androidpn:iq:login").append("\">");
        if (uid != null) {
            buf.append("<uid>").append(uname).append("</uid>");
        }
        if (uname != null) {
            buf.append("<uname>").append(uname).append("</uname>");
        }
        if (upwd != null) {
            buf.append("<upwd>").append(upwd).append("</upwd>");
        }
        buf.append("</").append("login").append("> ");
        return buf.toString();
    }

}
