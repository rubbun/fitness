package com.example.database;

import java.util.ArrayList;

import com.example.object.MyExercise;
import com.example.object.MyLocation;

import android.content.Context;
import android.util.Log;

//Use to get database, The application only use this class for manage database
public class MyDatabase {
	public static String DATE_FORMAT="dd.MM.yy hh:mm:ss";
	private MyDatabase(){}
	private static MyDatabase database=null;
	private Context context;
	public static MyDatabase getMyDatabase(Context context)
	{
		if(database==null)
		{
			database=new MyDatabase();
			database.context=context;
		}
		return database;
	}
	//Save detail of exercise and All of location in this exercise
	public boolean saveExercise(ArrayList<MyLocation> listLocation,MyExercise myExercise)
	{
		try{
			MyExerciseDB exerciseDB=new MyExerciseDB(context);
			String id=exerciseDB.insertExercise(myExercise);
			myExercise.setId(id);
			
			MyLocationDB locationDB=new MyLocationDB(context);
			locationDB.saveListLocation(listLocation, myExercise.getId());
			return true;
		}
		catch(Exception ex)
		{
			Log.d("ANDROID", "ERROR: "+ex.getMessage());
			return false;
		}
	}

	//Get list of exercise for show old list exercise
	public ArrayList<MyExercise> getExercise()
	{
		return new MyExerciseDB(context).getExercise();
	}
	//get list of locations for one exercise.
	public ArrayList<MyLocation> getLocation(String id_ex)
	{
		return new MyLocationDB(context).getListLocation(id_ex);
	}
}
