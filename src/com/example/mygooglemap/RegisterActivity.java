package com.example.mygooglemap;

import java.util.regex.Pattern;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.constant.Constants;
import com.example.network.KlHttpClient;

public class RegisterActivity extends BaseActivity implements OnClickListener {

	private Button btn_login, btn_register;
	private EditText et_email, et_password,et_lname,et_fname,et_weight,et_retype_password;
	private Intent mIntent;
	
	private String name, surname, username, password, weight,email,conf_password;
	private static final String EMAIL_PATTERN = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@" + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_register);

		et_email = (EditText) findViewById(R.id.et_email);
		et_password = (EditText) findViewById(R.id.et_password);
		
		et_fname = (EditText) findViewById(R.id.et_fname);
		et_lname = (EditText) findViewById(R.id.et_lname);
		et_weight = (EditText) findViewById(R.id.et_weight);
		et_retype_password = (EditText) findViewById(R.id.et_retype_password);
		

		btn_register = (Button) findViewById(R.id.btn_register);
		btn_register.setOnClickListener(this);
		
		btn_login = (Button) findViewById(R.id.btn_login);
		btn_login.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_login:
			finish();
			break;

		case R.id.btn_register:
			registerMe();
			break;
		}
	}
	
	private void registerMe() {
		if (isValid()) {
			sendValueToServer();
		}
	}
	
	private boolean isValid() {
		name = et_fname.getText().toString().trim();
		surname = et_lname.getText().toString().trim();
		weight = et_weight.getText().toString().trim();
		email = et_email.getText().toString().trim();
		conf_password = et_retype_password.getText().toString().trim();
		password = et_password.getText().toString().trim();

		boolean flag = true;
		if (name.length() == 0) {
			et_fname.setError("Please Enter your First Name");
			flag = false;
		} else if (surname.length() == 0) {
			et_lname.setError("Please Enter your Last name");
			flag = false;
		} else if (email.length() == 0) {
			et_email.setError("Please Enter your Email.");
			flag = false;
		} else if (!isvalidMailid(email)) {
			et_email.setError("Please enter valid Email id.");
			flag = false;
		}else if (weight.length() == 0) {
			et_weight.setError("Please Enter your Weight");
			flag = false;
		}  else if (password.length() == 0) {
			et_password.setError("Please Enter your password.");
			flag = false;
		} else if (conf_password.length() == 0) {
			et_retype_password.setError("Please Re-type your Password.");
			flag = false;
		} else if (!password.equalsIgnoreCase(conf_password)) {
			Toast.makeText(RegisterActivity.this, "Password mismatched..", 6000).show();
			flag = false;
		}
		return flag;
	}

	public boolean isvalidMailid(String mail) {
		return Pattern.compile(EMAIL_PATTERN).matcher(mail).matches();
	}
	
	private void sendValueToServer() {
		new CallServerForRegistration().execute();
	}

	public class CallServerForRegistration extends AsyncTask<Void, Void, Boolean> {

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			doShowLoading();
		}

		@Override
		protected Boolean doInBackground(Void... params) {
			try {
				JSONObject req = new JSONObject();
				req.put("name", (name +" "+surname));
				req.put("weight", weight);
				req.put("email", email);
				req.put("password", password);

				String response = KlHttpClient.SendHttpPost(Constants.SIGNUP, req.toString());
				if (response != null) {
					try {
						JSONObject ob = new JSONObject(response);
						if (ob.getBoolean("status")) {
							return true;
						}else{
							return false;
						}
					} catch (JSONException e) {
						e.printStackTrace();
					}
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}
			return null;
		}

		@Override
		protected void onPostExecute(Boolean result) {
			super.onPostExecute(result);
			doRemoveLoading();
			if(result){
				Toast.makeText(getApplicationContext(), "Registration Successful!!!", Toast.LENGTH_LONG).show();
				finish();
			}else{
				Toast.makeText(getApplicationContext(), "Some error occured.Please try again..", Toast.LENGTH_LONG).show();
			}
		}
	}
}
