package com.example.mygooglemap;


import java.text.DecimalFormat;
import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.constant.Constants;
import com.example.database.MyDatabase;
import com.example.object.ExerciseType;
import com.example.object.MyExercise;
import com.example.object.MyLocation;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMapOptions;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

public class DetailActivity extends Activity implements OnClickListener{
	public static String DETAIL_EXERCISE="result";
	TextView tvStartTime,tvEndTime,tvDistance,tvTotal,tvCalo,tvName,tvComment;
	ImageButton btnRePlay,btnViewResult,btnShare;
	ImageView ivType;

	private MapFragment fragment;
	private GoogleMap googleMap;
	MyExercise exercise;
	int position;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_detail);
		setTitle("Detail of workout");
		tvStartTime=(TextView) findViewById(R.id.txtStartTime);
		tvEndTime=(TextView) findViewById(R.id.txtEndTime);
		tvDistance=(TextView) findViewById(R.id.txtDistance);
		tvTotal=(TextView) findViewById(R.id.txtTotalTime);
		tvCalo=(TextView) findViewById(R.id.txtCalories);
		ivType=(ImageView) findViewById(R.id.ivType);
		tvName=(TextView) findViewById(R.id.tvName);
		tvComment=(TextView) findViewById(R.id.tvComment);

		btnRePlay=(ImageButton) findViewById(R.id.btnRePlay);
		btnViewResult=(ImageButton) findViewById(R.id.btnViewResult);
		btnRePlay.setOnClickListener(this);
		btnViewResult.setOnClickListener(this);
		
		btnShare = (ImageButton)findViewById(R.id.btnShare);
		btnShare.setOnClickListener(this);

		exercise= (MyExercise) getIntent().getSerializableExtra(DETAIL_EXERCISE);
		position = getIntent().getExtras().getInt("position");
		
		tvStartTime.setText(exercise.getStart_time());
		tvEndTime.setText(exercise.getEnd_time());
		tvDistance.setText((new DecimalFormat("##.####").format(exercise.getDistance()*0.000621371))+" miles");
		tvTotal.setText(((int)(exercise.getTotal_time()/60))+"m:"+exercise.getTotal_time()%60+"s");
		tvCalo.setText(new DecimalFormat("##.####").format(exercise.getCalories())+" calories");
		tvName.setText(exercise.getName());
		tvComment.setText(exercise.getComment());
		ivType.setImageResource(ExerciseType.getImageType(exercise.getEx_type()));

		GoogleMapOptions options = new GoogleMapOptions();
		options.mapType(GoogleMap.MAP_TYPE_NORMAL)
		.tiltGesturesEnabled(true)
		.rotateGesturesEnabled(true)
		.compassEnabled(true)
		.zoomControlsEnabled(true);
		fragment = MapFragment.newInstance(options);
		getFragmentManager().beginTransaction().add(R.id.container, fragment).commit();

	}

	@Override
	protected void onResume() {
		super.onResume();
		googleMap = fragment.getMap();
		ArrayList<MyLocation> listLocations;
		if(position>= 0){
			listLocations = Constants.locationList.get(position);
		}else{
			listLocations=MyDatabase.getMyDatabase(this).getLocation(exercise.getId());
		}

		int size=listLocations.size();
		if(size>0)
		{
			googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(
					listLocations.get((int)(size/2)).getLocation(),
					14)
					);
			PolylineOptions rectOptions = new PolylineOptions();
			for(int i=0;i<size;i++)
				rectOptions.add(listLocations.get(i).getLocation());
			googleMap.addPolyline(rectOptions);

			System.out.println("!!star location:"+listLocations.get(0).getLocation());
			
			googleMap.addMarker(new MarkerOptions().position(listLocations.get(0).getLocation()).title("Start Location"));
			googleMap.addMarker(new MarkerOptions().position(listLocations.get(size-1).getLocation()).title("End Location"));
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btnRePlay:
			Intent intent=new Intent(this,MapActivity.class);
			intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
			startActivity(intent);
			finish();
		
			break;
		case R.id.btnViewResult:
			Intent intent2=new Intent(this,HistoryActivity.class);
			intent2.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
			startActivity(intent2);
			finish();
			break;
			
		case R.id.btnShare:
			
			Intent sendIntent = new Intent();
			sendIntent.setAction(Intent.ACTION_SEND);
			sendIntent.putExtra(Intent.EXTRA_TEXT, "Time: "+exercise.getTotal_time()+"Distance: "+exercise.getDistance()+"Calorie: "+exercise.getCalories()+"Comment: "+exercise.getComment());
			sendIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Time: "+exercise.getTotal_time()+"\n"+"Distance: "+exercise.getDistance()+"\n"+"Calorie: "+exercise.getCalories()+"\n"+"Comment: "+exercise.getComment());
			sendIntent.setType("text/plain");
			startActivity(sendIntent);
			break;
		default:
			break;
		}
	}
}