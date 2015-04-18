package com.example.Utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

import com.example.constant.Constants;

public class LoginInfo {

	public String username,password,user_id = null;
	public boolean isLogin = false;
	public SharedPreferences pref;
	
	public LoginInfo(Context context){
	
		pref = context.getSharedPreferences(Constants.values.LOGININFO.name(), Context.MODE_PRIVATE);
		username = pref.getString(Constants.values.USERNAME.name(), null);
		password = pref.getString(Constants.values.PASSWORD.name(), null);
		isLogin = pref.getBoolean(Constants.values.IS_SIGNIN_STATUS.name(), false);
		user_id = pref.getString(Constants.values.USERID.name(), null);
	}
	
	public void setLoginInfo(String username,String password,boolean status){
		this.username = username;
		this.password = password;
		
		Editor edit = pref.edit();
		edit.putString(Constants.values.USERNAME.name(), username);
		edit.putString(Constants.values.PASSWORD.name(), password);
		edit.putBoolean(Constants.values.IS_SIGNIN_STATUS.name(), status);
		edit.putString(Constants.values.USERID.name(), user_id);
		
		edit.commit();
	}
	
	public boolean getLoginStatus(){
		return pref.getBoolean(Constants.values.IS_SIGNIN_STATUS.name(), false);
	}
	
	public String getUserid(){
		return pref.getString(Constants.values.USERID.name(), null);
	}

}
