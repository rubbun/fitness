package com.example.mygooglemap;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;

public class StartActivity extends Activity implements OnClickListener{
	Button btnStart,btnViewResult,btnSetTarget;
	ImageView ivRunning;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		btnStart=(Button) findViewById(R.id.btnStart);
		btnViewResult=(Button) findViewById(R.id.btnViewResult);
		btnStart.setOnClickListener(this);
		btnViewResult.setOnClickListener(this);
		btnSetTarget = (Button)findViewById(R.id.btnSetTarget);
		btnSetTarget.setOnClickListener(this);
		ivRunning=(ImageView) findViewById(R.id.ivRunning);
		
		//run animation
		AnimationDrawable animationDrawable=((AnimationDrawable) ivRunning.getBackground());
		animationDrawable.start();
	}
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btnStart:
			Intent intent=new Intent(this,MapActivity.class);
			intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
			startActivity(intent);
			break;
		case R.id.btnViewResult:
			Intent intent2=new Intent(this,HistoryActivity.class);
			intent2.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
			startActivity(intent2);
			break;
		case R.id.btnSetTarget:
			Intent intent3=new Intent(this,SetTargetActivity.class);
			intent3.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
			startActivity(intent3);
			break;
		}
	}
}