package org.androidpn.model;

import org.litepal.crud.DataSupport;

public class Tags extends DataSupport{

    private String tag_name;

    public String getTag_name() {
        return tag_name;
    }

    public void setTag_name(String tag_name) {
        this.tag_name = tag_name;
    }
}
