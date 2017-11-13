package com.example.qingchen.vrmr;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

/**
 * @author qingchen
 * @date 17-11-10
 */
@Entity(primaryKeys = {"ctime","picUrl"})
public class NewsBean {
    /**
     * ctime : 2017-06-06 16:00
     * title : 模式思考：为什么星巴克成了印钞机？
     * description : 创业新闻
     * picUrl : http://pic.chinaz.com/thumb/2017/0606/2017060614515724.jpg
     * url : http://www.chinaz.com/start/2017/0606/720004.shtml
     */

    @NonNull
    private String ctime;
    private String title;
    private String description;
    @NonNull
    private String picUrl;
    private String url;

    public String getCtime() {
        return ctime;
    }

    public void setCtime(@NonNull String ctime) {
        this.ctime = ctime;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPicUrl() {
        return picUrl;
    }

    public void setPicUrl(@NonNull String picUrl) {
        this.picUrl = picUrl;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
