package com.example.object;

import java.util.Date;

import android.location.Location;

import com.google.android.gms.maps.model.LatLng;

public class MyLocation {
	
	public MyLocation() {
	}
	
	public MyLocation(LatLng location,Date date) {
		this.location=location;
		this.date=date;
	}
	
	private LatLng location;
	private Date date;
	private float myDistance;
	
	public float getMyDistance() {
		return myDistance;
	}

	public void setMyDistance(float myDistance) {
		this.myDistance = myDistance;
	}

	public LatLng getLocation() {
		return location;
	}
	public void setLocation(LatLng location) {
		this.location = location;
	}
	public Date getDate() {
		return date;
	}
	public void setDate(Date date) {
		this.date = date;
	}
	
	public float getDistance(MyLocation myLocation)
	{
		float result[]=new float[1];
		Location.distanceBetween(this.location.latitude,this.location.longitude,myLocation.getLocation().latitude,myLocation.getLocation().longitude,result);
		return result[0];
	}
}
