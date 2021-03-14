package com.thinkgenie.news.Banner;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class NewsGalleryModel {

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("nid")
    @Expose
    private Integer nid;
    @SerializedName("contentid")
    @Expose
    private Integer contentid;
    @SerializedName("contenturl")
    @Expose
    private String contenturl;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getNid() {
        return nid;
    }

    public void setNid(Integer nid) {
        this.nid = nid;
    }

    public Integer getContentid() {
        return contentid;
    }

    public void setContentid(Integer contentid) {
        this.contentid = contentid;
    }

    public String getContenturl() {
        return contenturl;
    }

    public void setContenturl(String contenturl) {
        this.contenturl = contenturl;
    }

}