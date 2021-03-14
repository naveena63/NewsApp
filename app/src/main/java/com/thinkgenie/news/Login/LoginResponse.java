
package com.thinkgenie.news.Login;


import com.google.gson.annotations.SerializedName;


@SuppressWarnings("unused")
public class LoginResponse {

    @SerializedName("confirmcode")
    private String mConfirmcode;
    @SerializedName("email")
    private String mEmail;
    @SerializedName("id")
    private Long mId;
    @SerializedName("imageurl")
    private Object mImageurl;
    @SerializedName("isverified")
    private Boolean mIsverified;
    @SerializedName("message")
    private String mMessage;
    @SerializedName("mobile")
    private String mMobile;
    @SerializedName("name")
    private String mName;
    @SerializedName("password")
    private Object mPassword;
    @SerializedName("result")
    private Boolean mResult;
    @SerializedName("status")
    private Boolean mStatus;
    @SerializedName("usertype")
    private Long mUsertype;
    @SerializedName("tagname")
    private String tagname;

    public String getConfirmcode() {
        return mConfirmcode;
    }

    public void setConfirmcode(String confirmcode) {
        mConfirmcode = confirmcode;
    }

    public String getEmail() {
        return mEmail;
    }

    public void setEmail(String email) {
        mEmail = email;
    }

    public Long getId() {
        return mId;
    }

    public String getTagname() {
        return tagname;
    }

    public void setTagname(String tagname) {
        this.tagname = tagname;
    }

    public void setId(Long id) {
        mId = id;
    }

    public Object getImageurl() {
        return mImageurl;
    }

    public void setImageurl(Object imageurl) {
        mImageurl = imageurl;
    }

    public Boolean getIsverified() {
        return mIsverified;
    }

    public void setIsverified(Boolean isverified) {
        mIsverified = isverified;
    }

    public String getMessage() {
        return mMessage;
    }

    public void setMessage(String message) {
        mMessage = message;
    }

    public String getMobile() {
        return mMobile;
    }

    public void setMobile(String mobile) {
        mMobile = mobile;
    }

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        mName = name;
    }

    public Object getPassword() {
        return mPassword;
    }

    public void setPassword(Object password) {
        mPassword = password;
    }

    public Boolean getResult() {
        return mResult;
    }

    public void setResult(Boolean result) {
        mResult = result;
    }

    public Boolean getStatus() {
        return mStatus;
    }

    public void setStatus(Boolean status) {
        mStatus = status;
    }

    public Long getUsertype() {
        return mUsertype;
    }

    public void setUsertype(Long usertype) {
        mUsertype = usertype;
    }

}
