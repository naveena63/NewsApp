package com.thinkgenie.news.compose;

public class SelectNewstypemodel {

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    int id;
    String type;

    public SelectNewstypemodel(int id, String type) {
        this.id = id;
        this.type = type;
    }

}
