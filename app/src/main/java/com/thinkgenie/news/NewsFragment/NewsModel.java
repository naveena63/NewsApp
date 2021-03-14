package com.thinkgenie.news.NewsFragment;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class NewsModel {
        @SerializedName("id")
        @Expose
        private Integer id;
        @SerializedName("title")
        @Expose
        private String title;
        @SerializedName("date")
        @Expose
        private String date;
        @SerializedName("description")
        @Expose
        private String description;
        @SerializedName("articletypeid")
        @Expose
        private Integer articletypeid;
        @SerializedName("userid")
        @Expose
        private Integer userid;
        @SerializedName("villageid")
        @Expose
        private Integer villageid;
        @SerializedName("newstypeid")
        @Expose
        private Integer newstypeid;
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

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getDate() {
            return date;
        }

        public void setDate(String date) {
            this.date = date;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public Integer getArticletypeid() {
            return articletypeid;
        }

        public void setArticletypeid(Integer articletypeid) {
            this.articletypeid = articletypeid;
        }

        public Integer getUserid() {
            return userid;
        }

        public void setUserid(Integer userid) {
            this.userid = userid;
        }

        public Integer getVillageid() {
            return villageid;
        }

        public void setVillageid(Integer villageid) {
            this.villageid = villageid;
        }

        public Integer getNewstypeid() {
            return newstypeid;
        }

        public void setNewstypeid(Integer newstypeid) {
            this.newstypeid = newstypeid;
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

