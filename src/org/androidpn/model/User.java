package org.androidpn.model;

import org.litepal.crud.DataSupport;

import java.io.Serializable;
import java.util.List;

public class User extends DataSupport implements Serializable{

    private String username;
    private String password;
    private List<String> tagsList;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public List<String> getTagsList() {
        return tagsList;
    }

    public void setTagsList(List<String> tagsList) {
        this.tagsList = tagsList;
    }

}
