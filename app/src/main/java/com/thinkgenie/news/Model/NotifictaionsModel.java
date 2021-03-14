package com.thinkgenie.news.Model;

public class NotifictaionsModel {
    String name;
    String likePostText;
    String timing;

    public NotifictaionsModel(String name, String likepostText, String timing) {
        this.name=name;
        this.likePostText=likepostText;
        this.timing=timing;

    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLikePostText() {
        return likePostText;
    }

    public void setLikePostText(String likePostText) {
        this.likePostText = likePostText;
    }

    public String getTiming() {
        return timing;
    }

    public void setTiming(String timing) {
        this.timing = timing;
    }
}
