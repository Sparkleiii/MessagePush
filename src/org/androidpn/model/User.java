package org.androidpn.model;

import org.litepal.crud.DataSupport;

import java.util.List;

public class User extends DataSupport{

    private String username;
    private String password;
    private List<Tags> tagsList;

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

    public List<Tags> getTagsList() {
        return tagsList;
    }

    public void setTagsList(List<Tags> tagsList) {
        this.tagsList = tagsList;
    }

}
