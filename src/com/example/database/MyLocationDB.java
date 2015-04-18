package com.example.database;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

import android.content.Context;
import android.database.Cursor;

import com.example.object.MyLocation;
import com.google.android.gms.maps.model.LatLng;

public class MyLocationDB extends SQLiteHelper{
	
	public static String TABLE_LOCATION="Location";
	public static String ID				="id";
	public static String TIME			="time";
	public static String LATITUDE		="latitude";
	public static String LONGITUDE		="longitude";
	public static String ID_EXERCISE		="id_exercise";
	
	public MyLocationDB(Context context) 
	{
		super(context);
	}

	public void saveListLocation(ArrayList<MyLocation> list,String id_ex)
	{
		String sql="INSERT INTO "+TABLE_LOCATION
				+"("+TIME+","+LATITUDE+","+LONGITUDE+","+ID_EXERCISE+") values ";
		
		for(int i=0;i<list.size();i++)
		{
			sql+=(i==0?"":",")+"('"+new SimpleDateFormat(MyDatabase.DATE_FORMAT).format(list.get(i).getDate())
					+"','"+list.get(i).getLocation().latitude
					+"','"+list.get(i).getLocation().longitude
					+"','"+id_ex+"')";
		}
		getWritableDatabase().execSQL(sql);
	}
	public ArrayList<MyLocation> getListLocation(String id_ex)
	{
		ArrayList<MyLocation> listMyLocations=new ArrayList<MyLocation>();
		String sql="select * from "+TABLE_LOCATION+" where "+ID_EXERCISE+"="+"'"+id_ex+"'";
		Cursor cursor=getReadableDatabase().rawQuery(sql, null);
		if(cursor.moveToFirst())
			do
			{
				MyLocation myLocation=new MyLocation();
				myLocation.setLocation(new LatLng(
						cursor.getFloat(cursor.getColumnIndex(LATITUDE)),
						cursor.getFloat(cursor.getColumnIndex(LONGITUDE))
						));
				listMyLocations.add(myLocation);
			}while(cursor.moveToNext());
		
		return listMyLocations;
	}

}
