package com.example.mygooglemap;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.GoogleApiClient.ConnectionCallbacks;
import com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;

public class MyLocationManager implements ConnectionCallbacks,
		OnConnectionFailedListener {

	private final static int PLAY_SERVICES_RESOLUTION_REQUEST = 1000;

	// Google client to interact with Google API
	private GoogleApiClient mGoogleApiClient;

	private LocationRequest mLocationRequest;

	// Location updates intervals in sec
	private static int UPDATE_INTERVAL = 1000; // 1 sec
	private static int FATEST_INTERVAL = 1000; // 1 sec
	private static int DISPLACEMENT = 3; // 3 meters


	Activity context;
	public MyLocationManager(Activity context) {
		this.context=context;
	}
	public void prepare()
	{
		if (checkPlayServices()) {
			// Building the GoogleApi client
			buildGoogleApiClient();
			createLocationRequest();
			contected();
		}
	}
	/**
	 * Creating google api client object
	 * */
	protected synchronized void buildGoogleApiClient() {
		mGoogleApiClient = new GoogleApiClient.Builder(context)
				.addConnectionCallbacks(this)
				.addOnConnectionFailedListener(this)
				.addApi(LocationServices.API).build();
	}

	/**
	 * Creating location request object
	 * */
	protected void createLocationRequest() {
		mLocationRequest = new LocationRequest();
		mLocationRequest.setInterval(UPDATE_INTERVAL);
		mLocationRequest.setFastestInterval(FATEST_INTERVAL);
		mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
		mLocationRequest.setSmallestDisplacement(DISPLACEMENT);
	}

	/**
	 * Method to verify google play services on the device
	 * */
	private boolean checkPlayServices() {
		int resultCode = GooglePlayServicesUtil
				.isGooglePlayServicesAvailable(context);
		if (resultCode != ConnectionResult.SUCCESS) {
			if (GooglePlayServicesUtil.isUserRecoverableError(resultCode)) {
				GooglePlayServicesUtil.getErrorDialog(resultCode, context,
						PLAY_SERVICES_RESOLUTION_REQUEST).show();
			} else {
				Toast.makeText(context,
						"This device is not supported.", Toast.LENGTH_LONG)
						.show();
			}
			return false;
		}
		return true;
	}

	/**
	 * Starting the location updates
	 * */
	protected void startLocationUpdates() {
		contected();
		LocationServices.FusedLocationApi.requestLocationUpdates(
				mGoogleApiClient, mLocationRequest, (LocationListener)context);
	}

	/**
	 * Stopping location updates
	 */
	protected void stopLocationUpdates() {
		LocationServices.FusedLocationApi.removeLocationUpdates(
				mGoogleApiClient, (LocationListener)context);
	}

	/**
	 * Google api callback methods
	 */
	@Override
	public void onConnectionFailed(ConnectionResult result) {
		Log.d("ANDROID", "faild contected");
	}

	@Override
	public void onConnected(Bundle arg0) {
		
	}

	@Override
	public void onConnectionSuspended(int arg0) {
		mGoogleApiClient.connect();
	}
	
	public void discontected()
	{
		if (mGoogleApiClient.isConnected()) {
			mGoogleApiClient.disconnect();
		}
	}
	
	public void contected()
	{
		if (!mGoogleApiClient.isConnected()) {
			mGoogleApiClient.connect();
		}
	}
}
