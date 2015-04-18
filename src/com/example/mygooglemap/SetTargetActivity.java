package com.example.mygooglemap;

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

public class SetTargetActivity extends BaseActivity{
	
	private Spinner spType;
	private Button btnNext,btnSet;
	private TextView tv_set_type;
	private EditText et_input;
	private LinearLayout ll_set;
	
	private String chosen_type;

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
			if(chosen_type.equalsIgnoreCase("Calory")){
				tv_set_type.setText("Enter Calory:");
			}else if(chosen_type.equalsIgnoreCase("kilometers")){
				tv_set_type.setText("Enter kilometers:");
			}
			break;

		case R.id.btnSet:
			
			break;
		}
	}
}
