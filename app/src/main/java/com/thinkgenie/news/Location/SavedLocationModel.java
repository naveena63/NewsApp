package com.thinkgenie.news.Location;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class SavedLocationModel {


    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("pincode")
    @Expose
    private String pincode;
    @SerializedName("villageid")
    @Expose
    private Integer villageid;
    @SerializedName("userid")
    @Expose
    private Integer userid;
    @SerializedName("status")
    @Expose
    private Boolean status;
    @SerializedName("createdon")
    @Expose
    private String createdon;
    @SerializedName("createdby")
    @Expose
    private Integer createdby;
    @SerializedName("modifiedby")
    @Expose
    private Integer modifiedby;
    @SerializedName("modifiedon")
    @Expose
    private String modifiedon;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPincode() {
        return pincode;
    }

    public void setPincode(String pincode) {
        this.pincode = pincode;
    }

    public Integer getVillageid() {
        return villageid;
    }

    public void setVillageid(Integer villageid) {
        this.villageid = villageid;
    }

    public Integer getUserid() {
        return userid;
    }

    public void setUserid(Integer userid) {
        this.userid = userid;
    }

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }

    public String getCreatedon() {
        return createdon;
    }

    public void setCreatedon(String createdon) {
        this.createdon = createdon;
    }

    public Integer getCreatedby() {
        return createdby;
    }

    public void setCreatedby(Integer createdby) {
        this.createdby = createdby;
    }

    public Integer getModifiedby() {
        return modifiedby;
    }

    public void setModifiedby(Integer modifiedby) {
        this.modifiedby = modifiedby;
    }

    public String getModifiedon() {
        return modifiedon;
    }

    public void setModifiedon(String modifiedon) {
        this.modifiedon = modifiedon;
    }


}
