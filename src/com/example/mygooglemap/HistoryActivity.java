package com.example.mygooglemap;


import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONObject;

import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.example.adapter.MyExerciseAdapter;
import com.example.constant.Constants;
import com.example.network.KlHttpClient;
import com.example.object.MyExercise;
import com.example.object.MyLocation;
import com.google.android.gms.maps.model.LatLng;
//show list of exercise
public class HistoryActivity extends BaseActivity{
	
	Button btnStart,btnViewResult;
	private ArrayList<MyExercise> listExercises = new ArrayList<MyExercise>();
	private ArrayList<MyLocation> listLocation = new ArrayList<MyLocation>();;
	private ArrayList<ArrayList<MyLocation>> locationList = Constants.locationList;;
	ListView listView;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_result);
		listView=(ListView) findViewById(R.id.lstResult);
		setTitle("History");
		new fetchDataFromserver().execute();
	}
	
	public class fetchDataFromserver extends AsyncTask<Void, Void, Boolean>{

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			doShowLoading();
		}
		
		@Override
		protected Boolean doInBackground(Void... params) {
			try{
			
			JSONObject jobj = new JSONObject();
			jobj.put("user_id", app.getUserinfo().user_id);
			
			String response = KlHttpClient.SendHttpPost(Constants.HISTORY, jobj.toString());
			if(response!=null){
				JSONObject jRes = new JSONObject(response);
				if(jRes.getBoolean("status")){
					JSONArray jarr = jRes.getJSONArray("value");
					listExercises.clear();
					locationList.clear();
					for(int i = 0; i <jarr.length(); i++){
						JSONObject obj = jarr.getJSONObject(i);
						String strObj = obj.getString("points");
						JSONObject ob = new JSONObject(strObj);
						MyExercise myExercise=new MyExercise();
						myExercise.setId(ob.getString("id"));
						myExercise.setCalories(Float.parseFloat(ob.getString("calories")));
						myExercise.setDistance(Float.parseFloat(ob.getString("distance")));
						myExercise.setEnd_time(ob.getString("end_time"));
						myExercise.setEx_type(Integer.parseInt(ob.getString("ex_type")));
						myExercise.setStart_time(ob.getString("start_time"));
						myExercise.setTotal_time(Long.parseLong(ob.getString("total_time")));
						myExercise.setName(ob.getString("name"));
						myExercise.setComment(ob.getString("comment"));
						myExercise.setWeight(ob.getString("weight"));
						listExercises.add(myExercise);
						
						String location = ob.getString("locations");
						JSONArray arr = new JSONArray(location);
						
						for(int j = 0; j <arr.length(); j++){
							JSONObject object = arr.getJSONObject(j);
							MyLocation myLocations = new MyLocation();
							LatLng latLng=new LatLng(object.getDouble("latitude"),object.getDouble("longitude"));
							myLocations.setLocation(latLng);
							listLocation.add(myLocations);
						}
						locationList.add(listLocation);
					}
					return true;
				}else{
					return false;
				}
			}
			
			}catch(Exception e){
				e.printStackTrace();
			}
			return false;
		}
		
		@Override
		protected void onPostExecute(Boolean result) {
			super.onPostExecute(result);
			doRemoveLoading();
			if(result){
				if(listExercises.size()==0)
				{
					Toast.makeText(HistoryActivity.this, "You don't have any exercise!!", Toast.LENGTH_SHORT).show();
				}
				else
				{
					MyExerciseAdapter adapter=new MyExerciseAdapter(HistoryActivity.this, R.layout.item_exercise, listExercises);
					listView.setAdapter(adapter);
				}
				
			}else{
				Toast.makeText(getApplicationContext(), "Some error occured", 6000).show();
			}
		}
	}
}