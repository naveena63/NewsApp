package com.thinkgenie.news.OTP;

import com.google.gson.annotations.SerializedName;
import com.thinkgenie.news.Login.LoginActivity;

@SuppressWarnings("unused")
public class OtpResponse  {

	@SerializedName("ErrorCode")
	private String errorCode;

	@SerializedName("ErrorMessage")
	private String errorMessage;

	@SerializedName("MessageOTP")
	private String messageOTP;

	public void setErrorCode(String errorCode){
		this.errorCode = errorCode;
	}

	public String getErrorCode(){
		return errorCode;
	}

	public void setErrorMessage(String errorMessage){
		this.errorMessage = errorMessage;
	}

	public String getErrorMessage(){
		return errorMessage;
	}

	public void setMessageOTP(String messageOTP){
		this.messageOTP = messageOTP;
	}

	public String getMessageOTP(){
		return messageOTP;
	}
}