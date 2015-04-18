package com.example.database;

import java.util.ArrayList;

import com.example.object.MyExercise;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class MyExerciseDB extends SQLiteHelper {
	
	public static String TABLE_EXERCISE	="Exercise";
	public static String ID				="id";
	public static String START_TIME		="start_time";
	public static String END_TIME		="end_time";
	public static String DISTANCE		="distance";
	public static String TOTAL_TIME		="total_time";
	public static String CALORIES		="calories";
	public static String EXTYPE			="ex_type";
	public static String NAME			="name";
	public static String COMMENT		="comment";
	public static String WEIGHT			="weight";
	
	public MyExerciseDB(Context context) {
		super(context);
	}

	public String insertExercise(MyExercise myExercise)
	{
		String sql="INSERT INTO "+TABLE_EXERCISE
				+" ("+START_TIME+","+END_TIME+","+DISTANCE+","+TOTAL_TIME+","+CALORIES+","+EXTYPE+","+NAME+","+COMMENT+","+WEIGHT+")"
				+"values('"+myExercise.getStart_time()
						+"','"+myExercise.getEnd_time()
						+"','"+myExercise.getDistance()
						+"','"+myExercise.getTotal_time()
						+"','"+myExercise.getCalories()
						+"','"+myExercise.getEx_type()
						+"','"+myExercise.getName()
						+"','"+myExercise.getComment()
						+"','"+myExercise.getWeight()
						+"')";
		
		this.getWritableDatabase().execSQL(sql);
		return getLastExerciseId();
	}
	
	public String getLastExerciseId()
	{
		SQLiteDatabase db=this.getReadableDatabase();
		Cursor cursor=db.rawQuery("select "+ID+" from "+TABLE_EXERCISE+" order by "+ID+" desc limit 1"
				, null);
		if(cursor.moveToFirst())
			return cursor.getString(cursor.getColumnIndex(ID));
		return "-1";
	}
	public ArrayList<MyExercise> getExercise()
	{
		ArrayList<MyExercise> listExercises=new ArrayList<MyExercise>();
		String sql="select * from "+TABLE_EXERCISE;
		
		SQLiteDatabase db=this.getReadableDatabase();
		Cursor cursor= db.rawQuery(sql, null);
		if(cursor.moveToFirst())
			do
			{
				MyExercise myExercise=new MyExercise();
				myExercise.setId(cursor.getString(cursor.getColumnIndex(ID)));
				myExercise.setCalories(cursor.getFloat(cursor.getColumnIndex(CALORIES)));
				myExercise.setDistance(cursor.getFloat(cursor.getColumnIndex(DISTANCE)));
				myExercise.setEnd_time(cursor.getString(cursor.getColumnIndex(END_TIME)));
				myExercise.setEx_type(cursor.getInt(cursor.getColumnIndex(EXTYPE)));
				myExercise.setStart_time(cursor.getString(cursor.getColumnIndex(START_TIME)));
				myExercise.setTotal_time(cursor.getLong(cursor.getColumnIndex(TOTAL_TIME)));
				myExercise.setName(cursor.getString(cursor.getColumnIndex(NAME)));
				myExercise.setComment(cursor.getString(cursor.getColumnIndex(COMMENT)));
				myExercise.setWeight(cursor.getString(cursor.getColumnIndex(WEIGHT)));
				listExercises.add(myExercise);
			}while(cursor.moveToNext());
		return listExercises;
	}
}
