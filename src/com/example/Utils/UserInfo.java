package com.example.Utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

import com.example.constant.Constants;

public class UserInfo {

	public String user_id = null;
	public String name = null;
	public String surname = null;
	public String email = null;
	public String weight = null;
	public String target_type = null;
	public float target_value = 0.0f;
	public SharedPreferences pref;
	
	public UserInfo(Context context){
		pref = context.getSharedPreferences(Constants.values.USERINFO.name(), Context.MODE_PRIVATE);
		user_id = pref.getString(Constants.values.USERID.name(), null);
		name = pref.getString(Constants.values.NAME.name(), null);
		surname = pref.getString(Constants.values.SURNAME.name(), null);
		weight = pref.getString(Constants.values.WEIGHT.name(), null);
		email = pref.getString(Constants.values.EMAIL.name(), null);
		
	}

	public void setUserInfo(String user_id, String name, String weight,
			String email) {
		
		this.user_id = user_id;
		this.name = name;
		this.weight = weight;
		this.email = email;
		
		Editor edit = pref.edit();
		edit.putString(Constants.values.USERID.name(), user_id);
		edit.putString(Constants.values.NAME.name(), name);
		edit.putString(Constants.values.WEIGHT.name(), weight);
		edit.putString(Constants.values.EMAIL.name(), email);
		
		edit.commit();
	}

	public String getUser_id() {
		return pref.getString(Constants.values.USERID.name(), null);
	}

	public String getName() {
		return pref.getString(Constants.values.NAME.name(), null);
	}

	public String getSurname() {
		return pref.getString(Constants.values.SURNAME.name(), null);
	}

	public String getEmail() {
		return pref.getString(Constants.values.EMAIL.name(), null);
	}
	
	public String getWeight() {
		return pref.getString(Constants.values.WEIGHT.name(), null);
	}

	public void setWeight(String weight) {
		this.weight = weight;
		Editor edit = pref.edit();
		edit.putString(Constants.values.WEIGHT.name(), weight);
		edit.commit();
	}

	public String getTarget_type() {
		return pref.getString(Constants.values.TARGET_TYPE.name(), "");
	}

	public void setTarget_type(String target_type) {
		this.target_type = target_type;
		Editor edit = pref.edit();
		edit.putString(Constants.values.TARGET_TYPE.name(), target_type);
		edit.commit();
	}

	public float getTarget_value() {
		return pref.getFloat(Constants.values.TARGET_VALUE.name(), 0.0f);
	}

	public void setTarget_value(float target_value) {
		this.target_value = target_value;
		Editor edit = pref.edit();
		edit.putFloat(Constants.values.TARGET_VALUE.name(), target_value);
		edit.commit();
	}
}
