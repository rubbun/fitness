package com.example.mygooglemap;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Intent;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
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
			isPause=false;
		}
	
	final Handler handler=new Handler()
	{
		@Override
		public void handleMessage(Message msg) {
			
			if(msg.what==0)
			{
				tvReport.setText("Time: "+((int)(total_time/60))+"m:"+total_time%60+"s"
						+",Distance: "+(distance*0.000621371)+" miles");
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
				SimpleDateFormat format=new SimpleDateFormat(MyDatabase.DATE_FORMAT);

				this.myExercise.setStart_time(format.format(listLocation.get(0).getDate()));
				this.myExercise.setEnd_time(format.format(listLocation.get(listLocation.size()-1).getDate()));
				this.myExercise.setDistance(distance);
				this.myExercise.setTotal_time(total_time);
				this.myExercise.setWeight(app.getUserinfo().weight);

				new sendDataToserver().execute();
				
				//calculator calories
				if(listLocation.size()>1)
					this.myExercise.setCalories();

				MyDatabase.getMyDatabase(MapActivity.this).saveExercise(listLocation,myExercise);
				//show result
			}
			else
				Toast.makeText(getApplicationContext(), "Exercise hasn't started, can't finish!!", Toast.LENGTH_SHORT).show();
			break;
		}
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
				obj.put("comment", "Hello");
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
				
				String response = KlHttpClient.SendHttpPost(Constants.INSERT_DATA, jobj.toString());
				if(response != null){
					JSONObject ob = new JSONObject(response);
					if(ob.getBoolean("status")){
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
				Intent intent=new Intent(MapActivity.this,DetailActivity.class);
				intent.putExtra(DetailActivity.DETAIL_EXERCISE, myExercise);
				startActivity(intent);
				//reset value
				reset();
				listLocation.clear();
				googleMap.clear();
				
				myLocationManager.discontected();
				tvReport.setText("");
				
			}else{
				Toast.makeText(getApplicationContext(), "Some error occured", 6000).show();
			}
		}
	}
}