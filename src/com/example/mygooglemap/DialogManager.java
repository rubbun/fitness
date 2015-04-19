package com.example.mygooglemap;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.adapter.ExerciseTypeAdapter;
import com.example.constant.Constants;
import com.example.database.MyExerciseDB;
import com.example.network.KlHttpClient;
import com.example.object.ExerciseType;
import com.example.object.MyExercise;

public class DialogManager {
	//content of dialog Prepare
	static boolean flag = false;
	public static MapActivity activity;
	public static String weight;
	
	public static void showDialogPrepare(final Context context, final MyExercise myExercise)
	{		
		final Dialog dialog = new Dialog(context);

		dialog.setContentView(R.layout.popup_start);
		dialog.setTitle("Prepare....");

		Button btnsave=(Button)dialog.findViewById(R.id.btnSave);
		Button btnCancel=(Button) dialog.findViewById(R.id.btnCancel);
		final EditText edName=(EditText) dialog.findViewById(R.id.edName);
		final EditText comment=(EditText) dialog.findViewById(R.id.edComment);
		final EditText edWeight=(EditText) dialog.findViewById(R.id.edWeight);

		final SharedPreferences sharedpreferences =context.getSharedPreferences(MapActivity.MyPREFERENCES,StartActivity.MODE_PRIVATE);

		if(sharedpreferences.contains(MyExerciseDB.WEIGHT))
		{
			edWeight.setText(sharedpreferences.getString(MyExerciseDB.WEIGHT, "0"));
		}

		if(sharedpreferences.contains(MyExerciseDB.NAME))
		{
			edName.setText(sharedpreferences.getString(MyExerciseDB.NAME, ""));
		}

		btnCancel.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				dialog.dismiss();
			}
		});
		dialog.show();

		btnsave.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				//validation
				if(edName.getText().toString().equals(""))
				{
					Toast.makeText(context, "Please input your name", Toast.LENGTH_LONG).show();
					return;
				}
				else if(edWeight.getText().toString().equals(""))
				{
					Toast.makeText(context, "Please input your weight", Toast.LENGTH_LONG).show();
					return;
				}

				myExercise.setName(edName.getText().toString()+"");
				myExercise.setComment(comment.getText().toString()+"");
				myExercise.setWeight(edWeight.getText().toString());

				sharedpreferences.edit().putString(MyExerciseDB.WEIGHT, myExercise.getWeight()).commit();
				sharedpreferences.edit().putString(MyExerciseDB.NAME, myExercise.getName()).commit();

				dialog.dismiss();
			}
		});
	}
	
	//content of dialog choose exercise and start
	public static void showDialogStartNow(final MapActivity context, final MyExercise myExercise,final View btnPause,final View btnFinish)
	{
		final Dialog dialog2 = new Dialog(context);
		dialog2.setContentView(R.layout.popup_choose);
		dialog2.setTitle("choose workout");

		Button btnStartNow=(Button)dialog2.findViewById(R.id.btnSave);
		Button btnCancel2=(Button) dialog2.findViewById(R.id.btnCancel);
		final Spinner spType2=(Spinner) dialog2.findViewById(R.id.spType);

		ExerciseTypeAdapter exerciseAdapter2=new ExerciseTypeAdapter(context, R.layout.item_exercise_type, ExerciseType.getListExerciseType());
		spType2.setAdapter(exerciseAdapter2);

		btnCancel2.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				dialog2.dismiss();
			}
		});
		dialog2.show();
		btnStartNow.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				myExercise.setEx_type(((ExerciseType) spType2.getSelectedItem()).getType());
				dialog2.dismiss();
				context.startGetLocation();
				Animation.setEnable(context, btnPause, true);
				Animation.setEnable(context, btnFinish, true);
			}
		});
	}
	
	//content of dialog choose exercise and start
		public static void showDialogForProfile(final MapActivity context)
		{
			activity = context;
			final Dialog dialog2 = new Dialog(context);
			dialog2.setContentView(R.layout.userinfo_dialog);
			dialog2.setTitle("Profile");
			
			Button btnOk=(Button)dialog2.findViewById(R.id.btnOk);
			TextView tv_name = (TextView)dialog2.findViewById(R.id.tv_name);
			TextView tv_email = (TextView)dialog2.findViewById(R.id.tv_email);
			final EditText ed_weight = (EditText)dialog2.findViewById(R.id.et_weight);
			
			tv_name.setText(context.app.getUserinfo().name);
			tv_email.setText(context.app.getUserinfo().email);
			ed_weight.setText(context.app.getUserinfo().weight);
			ed_weight.setEnabled(false);
			
			final Button btn_edit=(Button) dialog2.findViewById(R.id.btn_edit);
			btn_edit.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					if(!flag){
						ed_weight.setEnabled(true);
						btn_edit.setText("Done");
						flag = true;
					}else{
						flag = false;
						ed_weight.setEnabled(false);
						btn_edit.setText("Edit");
						if(!ed_weight.getText().toString().equalsIgnoreCase("0")){
							weight = ed_weight.getText().toString();
							updateWeight();
						}else{
							Toast.makeText(context, "Please enter a valid weight", Toast.LENGTH_LONG).show();
						}
					}
				}
			});
			
			dialog2.show();
			btnOk.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					
					dialog2.dismiss();
				}
			});
		}
		
		private static void updateWeight() {
			new UpdateWeight().execute();
		}
		
		public static class UpdateWeight extends AsyncTask<Void, Void, Boolean>{

			@Override
			protected void onPreExecute() {
				super.onPreExecute();
				activity.doShowLoading();
			}
			@Override
			protected Boolean doInBackground(Void... params) {
				
				try {
					JSONObject ob = new JSONObject();
					ob.put("weight", weight);
					ob.put("user_id", activity.app.getUserinfo().user_id);
					
					String response = KlHttpClient.SendHttpPost(Constants.UPDATE_WEIGHT, ob.toString());
					if(response!= null){
						JSONObject obj = new JSONObject(response);
						if(obj.getBoolean("status")){
							activity.app.getUserinfo().setWeight(weight);
							return true;
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
				activity.doRemoveLoading();
				if(result){
					Toast.makeText(activity, "Weight successfully updated!!", Toast.LENGTH_LONG).show();
				}else{
					Toast.makeText(activity, "Some Error occured.Plese try again..", Toast.LENGTH_LONG).show();
				}
			}
		}
}
