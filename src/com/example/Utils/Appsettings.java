package com.example.Utils;

import android.app.Application;

public class Appsettings extends Application{

	public boolean init = false;
	
	public LoginInfo logininfo;
	public UserInfo userinfo;
	
	public LoginInfo getLogininfo() {
		return logininfo;
	}
	public void setLogininfo(LoginInfo logininfo) {
		this.logininfo = logininfo;
	}
	public UserInfo getUserinfo() {
		return userinfo;
	}
	public void setUserinfo(UserInfo userinfo) {
		this.userinfo = userinfo;
	}
}
