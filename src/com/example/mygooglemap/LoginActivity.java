package com.example.mygooglemap;

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

public class LoginActivity extends BaseActivity implements OnClickListener {

	private Button btn_login, btn_register;
	private EditText et_username, et_password;

	private Intent mIntent;
	private String username,password;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);

		et_username = (EditText) findViewById(R.id.et_email);
		et_password = (EditText) findViewById(R.id.et_password);

		btn_login = (Button) findViewById(R.id.btn_login);
		btn_login.setOnClickListener(this);

		btn_register = (Button) findViewById(R.id.btn_register);
		btn_register.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_login:
			if(isValid()){
				new CallServerForLogin().execute();
			}
			break;

		case R.id.btn_register:
			mIntent = new Intent(LoginActivity.this, RegisterActivity.class);
			startActivity(mIntent);
			break;
		}
	}

	private boolean isValid() {
		boolean flag = true;
		username = et_username.getText().toString().trim();
		password = et_password.getText().toString().trim();
		if(username.length() == 0){
			et_username.setError("Please enter your email id..");
			flag = false;
		}else if(password.length() == 0){
			et_password.setError("Please enter your password..");
			flag = false;
		}
		return flag;
	}
	
	public class CallServerForLogin extends AsyncTask<Void, Void, Boolean>{

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			doShowLoading();
		}
		
		@Override
		protected Boolean doInBackground(Void... params) {

			String username = et_username.getText().toString().trim();
			String password = et_password.getText().toString().trim();
			try {
				JSONObject req = new JSONObject();
				req.put("email", username);
				req.put("password", password);
				String response = KlHttpClient.SendHttpPost(Constants.SIGNIN, req.toString());
				if (response != null) {
					JSONObject ob = new JSONObject(response);
					if (ob.getBoolean("status")) {
						String user_id = ob.getString("id");
						String name = ob.getString("name");
						String email = ob.getString("email");
						String weight = ob.getString("weight");
						
						app.getUserinfo().setUserInfo(user_id, name, weight, email);
						app.getLogininfo().setLoginInfo(username, password,true);
						
						return ob.getBoolean("status");
					}
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}
			return false;
		}
		
		@Override
		protected void onPostExecute(Boolean result) {
			super.onPostExecute(result);
			doRemoveLoading();
			if(result){
				mIntent = new Intent(LoginActivity.this,StartActivity.class);
				startActivity(mIntent);
				finish();
			}else{
				Toast.makeText(getApplicationContext(), "Email id or Password mismatched.Please try again..", Toast.LENGTH_LONG).show();
			}
		}
	}
}
