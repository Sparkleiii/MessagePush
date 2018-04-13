package org.androidpn.iq;

import org.jivesoftware.smack.packet.IQ;

import java.util.ArrayList;
import java.util.List;

public class SetTagsIQ extends IQ {
    private String username;
    private List<String> tagList = new ArrayList<String>();

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public List<String> getTagList() {
        return tagList;
    }

    public void setTagList(List<String> tagList) {
        this.tagList = tagList;
    }

    @Override
    public String getChildElementXML() {
        StringBuilder buf = new StringBuilder();
        buf.append("<").append("settags").append(" xmlns=\"").append(
                "androidpn:iq:settags").append("\">");
        if (username != null) {
            buf.append("<username>").append(username).append("</username>");
        }
        if(tagList !=null && !tagList.isEmpty()){
            buf.append("<tags>");
            for(int i=0;i<tagList.size();i++){
                if(i!=tagList.size()-1){
                    buf.append(tagList.get(i)).append(",");
                }else
                    buf.append(tagList.get(i));
            }
            buf.append("</tags>");
        }
        buf.append("</").append("settags").append("> ");
        return buf.toString();
    }

}
