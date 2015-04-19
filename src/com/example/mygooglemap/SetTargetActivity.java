package com.example.mygooglemap;

import org.json.JSONException;
import org.json.JSONObject;

import com.example.constant.Constants;
import com.example.network.KlHttpClient;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class SetTargetActivity extends BaseActivity{
	
	private Spinner spType;
	private Button btnNext,btnSet;
	private TextView tv_set_type;
	private EditText et_input;
	private LinearLayout ll_set;
	
	private String chosen_type = "calorie";

	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		setContentView(R.layout.activity_settarget);
		
		spType = (Spinner)findViewById(R.id.spType);
		btnNext = (Button)findViewById(R.id.btnNext);
		btnSet = (Button)findViewById(R.id.btnSet);
		et_input = (EditText)findViewById(R.id.et_input);
		tv_set_type = (TextView)findViewById(R.id.tv_set_type);
		
		ll_set = (LinearLayout)findViewById(R.id.ll_set);
		ll_set.setVisibility(View.GONE);
		
		String[] array = getResources().getStringArray(R.array.target_option);
		
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(SetTargetActivity.this
				, android.R.layout.simple_dropdown_item_1line, array);
		spType.setAdapter(adapter);
		
		spType.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> parent, View arg1, int position, long arg3) {
				chosen_type = parent.getItemAtPosition(position).toString();
				if(chosen_type.equalsIgnoreCase("Calory")){
					tv_set_type.setText("Enter Calory:");
				}else if(chosen_type.equalsIgnoreCase("kilometers")){
					tv_set_type.setText("Enter kilometers:");
				}
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				
			}
		});
		
		btnNext.setOnClickListener(this);
		btnSet.setOnClickListener(this);
	}
	
	@Override
	public void onClick(View arg0) {
		super.onClick(arg0);
		switch (arg0.getId()) {
		case R.id.btnNext:
			ll_set.setVisibility(View.VISIBLE);
			if(chosen_type.equalsIgnoreCase("Calorie")){
				tv_set_type.setText("Enter Calorie:");
			}else if(chosen_type.equalsIgnoreCase("Miles")){
				tv_set_type.setText("Enter Miles:");
			}
			break;

		case R.id.btnSet:
			if(et_input.getText().toString().trim().length() > 0 && !et_input.getText().toString().trim().equalsIgnoreCase("0")){
				new SetTargetToserver().execute();
			}else{
				Toast.makeText(SetTargetActivity.this, "Please enter a valid input", Toast.LENGTH_LONG).show();;
			}
			break;
		}
	}
	
	public class SetTargetToserver extends AsyncTask<Void, Void, Boolean>{

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			doShowLoading();
		}
		
		@Override
		protected Boolean doInBackground(Void... params) {
			try {
				JSONObject ob = new JSONObject();
				ob.put("user_id", app.getUserinfo().user_id);
				if(chosen_type.equalsIgnoreCase("Calorie")){
					ob.put("type", "c");
				}else if(chosen_type.equalsIgnoreCase("Miles")){
					ob.put("type", "m");
				}
				ob.put("value", et_input.getText().toString().trim());
				
				String response = KlHttpClient.SendHttpPost(Constants.SET_TARGET, ob.toString());
				if(response != null){
					JSONObject obj = new JSONObject(response);
					if(obj.getBoolean("status")){
						return true;
					}else{
						return false;
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
				Toast.makeText(SetTargetActivity.this, "Target successfully setted!!", Toast.LENGTH_LONG).show();
				if(chosen_type.equalsIgnoreCase("Calorie")){
					app.getUserinfo().setTarget_type("c");
				}else if(chosen_type.equalsIgnoreCase("Miles")){
					app.getUserinfo().setTarget_type("m");
				}
				app.getUserinfo().setTarget_value(Float.parseFloat(et_input.getText().toString().trim()));
				finish();
			}else{
				Toast.makeText(SetTargetActivity.this, "Some error occured.Please try again..", Toast.LENGTH_LONG).show();
			}
		}
	}
}
