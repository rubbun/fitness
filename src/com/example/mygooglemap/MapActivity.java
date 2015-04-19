package com.example.mygooglemap;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.constant.Constants;
import com.example.database.MyDatabase;
import com.example.network.KlHttpClient;
import com.example.object.MyExercise;
import com.example.object.MyLocation;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMapOptions;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

public class MapActivity extends BaseActivity implements OnClickListener, LocationListener{
	private MapFragment fragment;
	private GoogleMap googleMap;
	private ArrayList<MyLocation> listLocation;
	private Marker marker=null;
	
	private int total_time=0;

	private float distance=0;
	private long  pauseTime=0;
	private float calories=0;
	private boolean isPause=false;

	public static String MyPREFERENCES="SMART_FITNESS_PRE";
	private int TIME_TO_CHECK_LOCATION=500;

	MyExercise myExercise = new MyExercise();
	private LinearLayout btnProfile,btnFinish,btnRestart,btnChoose;
	TextView tvReport;
	MyLocationManager myLocationManager;
	Timer timer;
	
	String comment = "";
	String value = "";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_map1);
		setTitle("Start workout");

		GoogleMapOptions options = new GoogleMapOptions();
		options.mapType(GoogleMap.MAP_TYPE_NORMAL)
		.tiltGesturesEnabled(true)
		.rotateGesturesEnabled(true)
		.compassEnabled(true)
		.zoomControlsEnabled(true);

		fragment = MapFragment.newInstance(options);
		getFragmentManager().beginTransaction().add(R.id.container, fragment).commit();

		listLocation=new ArrayList<MyLocation>();

		btnProfile=(LinearLayout) findViewById(R.id.btnProfile);
		btnFinish=(LinearLayout) findViewById(R.id.btnFinish);
		btnRestart=(LinearLayout) findViewById(R.id.btnRestart);
		btnChoose=(LinearLayout) findViewById(R.id.btnChoose);
		tvReport=(TextView) findViewById(R.id.tvReport);
		
		//reset status of buttons and some value before start
		reset();

		btnProfile.setOnClickListener(this);
		btnFinish.setOnClickListener(this);
		btnRestart.setOnClickListener(this);
		btnChoose.setOnClickListener(this);
		myLocationManager=new MyLocationManager(this);
	}
	/*//setting value for new exercise
	private void reset()
	{
		Animation.setEnable(getApplicationContext(), btnStart, true);
		Animation.setEnable(getApplicationContext(), btnPause, false);
		Animation.setEnable(getApplicationContext(), btnFinish, false);
		Animation.setEnable(getApplicationContext(), btnChoose, false);
		
		total_time=0;
		distance=0;
		isPause=false;
	}*/
	
	//setting value for new exercise
		private void reset()
		{
			btnProfile.setEnabled(true);
			btnRestart.setEnabled(false);
			btnFinish.setEnabled(false);
			btnRestart.setEnabled(false);
			distance=0;
			pauseTime=0;
			calories=0;
			total_time = 0;
			isPause=false;
		}
	
	final Handler handler=new Handler()
	{
		@Override
		public void handleMessage(Message msg) {
			
			if(msg.what==0)
			{
				tvReport.setText("Time: "+((int)(total_time/60))+"m:"+total_time%60+"s"
						+",Distance: "+(new DecimalFormat("##.####").format(distance*0.000621371))+" miles");
			}
			super.handleMessage(msg);
		}
	};
	/*
	 * If networkProvider is enable, app will use network provider. (it will faster gps and consume less energy)
	 * else if gps provider is enable, app will use gps provider.	(emulator for free is not support network provider, so it can use gps_provider)
	 * else if passive provider is enable, app will use passive provider.
	 */
	public void startGetLocation()
	{
		myLocationManager.startLocationUpdates();
		timer=new Timer();
		timer.scheduleAtFixedRate(new TimerTask() {
			@Override
			public void run() {
				total_time++;
				handler.sendEmptyMessage(0);
			}
		}, 1000, 1000);
	}
	public void stopLocationUpdate()
	{
		myLocationManager.stopLocationUpdates();
		if(timer!=null)
			timer.cancel();
	}
	@Override
	protected void onDestroy() {
		super.onDestroy();
		stopLocationUpdate();
		myLocationManager.discontected();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	protected void onResume() {
		super.onResume();
		googleMap = fragment.getMap();
		myLocationManager.prepare();
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btnProfile://start this exercise
			Animation.setEnable(getApplicationContext(), btnProfile, true);
			DialogManager.showDialogForProfile(this);

			break;
		case R.id.btnRestart://pause this exercise
			Animation.setEnable(getApplicationContext(), btnRestart, false);
			Animation.setEnable(getApplicationContext(), btnProfile, true);
			Animation.setEnable(getApplicationContext(), btnFinish, true);
			Animation.setEnable(getApplicationContext(), btnChoose, true);
			isPause=true;
			stopLocationUpdate();
			Toast.makeText(getApplicationContext(), "Pause", Toast.LENGTH_SHORT).show();
			break;
			
		case R.id.btnChoose://restart this exercise
			if (isPause) {
				startGetLocation();
				Animation.setEnable(getApplicationContext(), btnRestart, true);
				Toast.makeText(getApplicationContext(), "Continue!", Toast.LENGTH_SHORT).show();
			} 
			else
				DialogManager.showDialogStartNow(this, myExercise, btnFinish, btnRestart);
			break;

		case R.id.btnFinish://app will save exercise and new location into database and show detail of exercise
			
			stopLocationUpdate();
			
			if(listLocation.size()>0)
			{
				
				final Dialog dialog = new Dialog(MapActivity.this);
				dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
				dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
				dialog.setContentView(R.layout.comment_dilog);
				dialog.setCancelable(false);
				
				final EditText et_comment = (EditText)dialog.findViewById(R.id.et_comment);
				Button btnSkip = (Button)dialog.findViewById(R.id.btnSkip);
				btnSkip.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						comment = "";
						setvalueForServer();
						dialog.dismiss();
					}
				});
				
				Button btnSubmit = (Button)dialog.findViewById(R.id.btnSubmit);
				btnSubmit.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						if(et_comment.getText().toString().trim().length()> 0){
							comment = et_comment.getText().toString().trim();
							setvalueForServer();
						}else{
							Toast.makeText(MapActivity.this, "Please enter your comment", Toast.LENGTH_LONG).show();
						}
						dialog.dismiss();
					}
				});
				
				dialog.show();
			}
			else
				Toast.makeText(getApplicationContext(), "Exercise hasn't started, can't finish!!", Toast.LENGTH_SHORT).show();
			break;
		}
	}
	
	
	@SuppressLint("SimpleDateFormat")
	private void setvalueForServer() {
		SimpleDateFormat format=new SimpleDateFormat(MyDatabase.DATE_FORMAT);

		this.myExercise.setStart_time(format.format(listLocation.get(0).getDate()));
		this.myExercise.setEnd_time(format.format(listLocation.get(listLocation.size()-1).getDate()));
		this.myExercise.setDistance(distance);
		this.myExercise.setTotal_time(total_time);
		this.myExercise.setWeight(app.getUserinfo().weight);
		this.myExercise.setId(app.getUserinfo().user_id);
		this.myExercise.setCalories();
		this.myExercise.setComment(comment);

		new sendDataToserver().execute();
		
		//calculator calories
		if(listLocation.size()>1)
			this.myExercise.setCalories();

		MyDatabase.getMyDatabase(MapActivity.this).saveExercise(listLocation,myExercise);
		//show result
	}
	
	/*
	 * 
	 * this function will be call if new location is found
	 */
	@Override
	public void onLocationChanged(Location location) {
		LatLng latLng=new LatLng(location.getLatitude(), location.getLongitude());
		listLocation.add(new MyLocation(latLng, new Date()));
		if(listLocation.size()==1)
			googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng,
					16));
		else
			googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng,
					googleMap.getCameraPosition().zoom));

		int size=listLocation.size();
		if(listLocation.size()>1)
		{
			if(isPause)
			{
				isPause=false;
			}
			else{
				PolylineOptions rectOptions = new PolylineOptions()
				.add(listLocation.get(size-2).getLocation())
				.add(listLocation.get(size-1).getLocation());
				googleMap.addPolyline(rectOptions);
				distance+=listLocation.get(size-1).getDistance(listLocation.get(size-2));
			}
		}
		else googleMap.addMarker(new MarkerOptions().position(latLng).title("Start location"));

		if(marker!=null)
		{
			marker.remove();
		}
		marker=googleMap.addMarker(new MarkerOptions().position(latLng).title("Your location"));
	}
	
	public class sendDataToserver extends AsyncTask<Void, Void, Boolean>{

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			doShowLoading();
		}
		
		@Override
		protected Boolean doInBackground(Void... params) {
			
			try {
				JSONObject jobj = new JSONObject();
				
				JSONObject obj = new JSONObject();
				obj.put("id", app.getUserinfo().user_id);
				obj.put("start_time", myExercise.getStart_time());
				obj.put("end_time", myExercise.getEnd_time());
				obj.put("distance", myExercise.getDistance());
				obj.put("total_time", myExercise.getTotal_time());
				obj.put("calories", myExercise.getCalories());
				obj.put("ex_type", myExercise.getEx_type());
				obj.put("name", app.getUserinfo().name);
				obj.put("comment", myExercise.getComment());
				obj.put("weight", app.getUserinfo().weight);
				
				JSONArray arr = new JSONArray();
				for(int i = 0; i < listLocation.size(); i++){
					
					System.out.println("!!reach here");
					JSONObject object = new JSONObject();
					object.put("latitude", listLocation.get(i).getLocation().latitude);
					object.put("longitude", listLocation.get(i).getLocation().longitude);
					arr.put(i, object);
				}
				
				obj.put("locations", arr);
				jobj.put("points", obj.toString());
				jobj.put("user_id",app.getUserinfo().user_id);
				if(app.getUserinfo().getTarget_type().equalsIgnoreCase("m")){
					jobj.put("value",new DecimalFormat("##.####").format(myExercise.getDistance() * 0.000621371));
				}else if(app.getUserinfo().getTarget_type().equalsIgnoreCase("c")){
					jobj.put("value", myExercise.getCalories());
				}else{
					jobj.put("value", "");
				}
				
				String response = KlHttpClient.SendHttpPost(Constants.INSERT_DATA, jobj.toString());
				if(response != null){
					JSONObject ob = new JSONObject(response);
					if(ob.getBoolean("status")){
						value = ob.getString("value");
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
			doRemoveLoading();
			if(result){
				if(value.length()<=0){
					updateUi();
				}else if(Float.parseFloat(value)<= 0.0f){
					new AlertDialog.Builder(MapActivity.this)
					.setTitle("Alert!!")
					.setMessage("Good Job.You have fulfill your target!!")
					.setNeutralButton("Ok", new DialogInterface.OnClickListener() {
						
						@Override
						public void onClick(DialogInterface dialog, int which) {
							app.getUserinfo().setTarget_type("");
							app.getUserinfo().setTarget_value(0.0f);
							updateUi();
							dialog.dismiss();
						}
					})
					.show();
				}else{
					new AlertDialog.Builder(MapActivity.this)
					.setTitle("Alert!!")
					.setMessage(value+" "+(app.getUserinfo().getTarget_type().equalsIgnoreCase("c")? "calories to burn" : "miles to go"))
					.setNeutralButton("Ok", new DialogInterface.OnClickListener() {
						
						@Override
						public void onClick(DialogInterface dialog, int which) {
							updateUi();
							dialog.dismiss();
						}
					})
					.show();
				}
			}else{
				Toast.makeText(getApplicationContext(), "Some error occured", 6000).show();
			}
		}
	}
	
	public void updateUi(){
		//reset value
		reset();
		listLocation.clear();
		googleMap.clear();
		
		myLocationManager.discontected();
		tvReport.setText("");
		Intent intent=new Intent(MapActivity.this,DetailActivity.class);
		intent.putExtra(DetailActivity.DETAIL_EXERCISE, myExercise);
		intent.putExtra("position", -1);
		startActivity(intent);
	}
}