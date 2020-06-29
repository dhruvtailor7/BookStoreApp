package com.example.bookstoreapp.pojo;

import com.google.gson.annotations.SerializedName;

public class GoogleFacebookLogin {

	@SerializedName("loginType")
	private String loginType;

	@SerializedName("accessToken")
	private String accessToken;

	public void setLoginType(String loginType){
		this.loginType = loginType;
	}

	public String getLoginType(){
		return loginType;
	}

	public void setAccessToken(String accessToken){
		this.accessToken = accessToken;
	}

	public String getAccessToken(){
		return accessToken;
	}

	@Override
 	public String toString(){
		return 
			"GoogleFacebookLogin{" +
			"loginType = '" + loginType + '\'' + 
			",accessToken = '" + accessToken + '\'' + 
			"}";
		}
}