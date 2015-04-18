package com.example.mygooglemap;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

public class SplashActivity extends BaseActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_splash);

		// after 3s, MainActivity will be started and this SlashActivity is
		// closed
		if(app.getLogininfo().isLogin){
			Intent i = new Intent(SplashActivity.this,StartActivity.class);
			startActivity(i);
			finish();
		}else{
			int secondsDelayed = 3;
			new Handler().postDelayed(new Runnable() {
				public void run() {
					SplashActivity.this.startActivity(new Intent(SplashActivity.this, LoginActivity.class));
					finish();
				}
			}, secondsDelayed * 1000);
		}
	}
}