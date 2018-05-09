package org.androidpn.model;

import org.litepal.crud.DataSupport;

import java.io.Serializable;

public class Comment extends DataSupport implements Serializable{

    private long commentId;

    private  long notId;

    private String content;

    private String account;

    private String time;

    public long getCommentId() {
        return commentId;
    }

    public void setCommentId(long commentId) {
        this.commentId = commentId;
    }

    public long getNotId() {
        return notId;
    }

    public void setNotId(long notId) {
        this.notId = notId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
