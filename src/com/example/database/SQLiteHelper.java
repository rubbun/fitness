package com.example.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
//This class is use to create database or change database
public class SQLiteHelper extends SQLiteOpenHelper{

	public static String DB_NAME="smart_fitness.db";
	public SQLiteHelper(Context context) {
		super(context, DB_NAME, null, 1);
		//If database file was not created, it will be created. 
		context.openOrCreateDatabase(MyExerciseDB.DB_NAME,context.MODE_PRIVATE,null);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL(
				"create table "+MyExerciseDB.TABLE_EXERCISE+"("
					+MyExerciseDB.ID+" integer primary key asc AUTOINCREMENT,"       
					+MyExerciseDB.START_TIME	+" varchar not null,"       
					+MyExerciseDB.END_TIME		+" varchar not null,"       
					+MyExerciseDB.DISTANCE		+" varchar not null,"       
					+MyExerciseDB.TOTAL_TIME	+" varchar not null,"
					+MyExerciseDB.CALORIES		+" varchar,"    
					+MyExerciseDB.EXTYPE		+" varchar,"
					+MyExerciseDB.NAME			+" varchar,"
					+MyExerciseDB.COMMENT		+" varchar,"
					+MyExerciseDB.WEIGHT		+" varchar"
					+")"
				);
		
		db.execSQL(
				"create table "+MyLocationDB.TABLE_LOCATION+"("
						+MyLocationDB.ID+" integer primary key asc AUTOINCREMENT,"       
						+MyLocationDB.TIME+" varchar not null,"       
						+MyLocationDB.LATITUDE+" varchar not null,"
						+MyLocationDB.LONGITUDE+" varchar not null,"       
						+MyLocationDB.ID_EXERCISE+" varchar"
						+")"
				);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		db.execSQL("DROP TABLE IF EXISTS "+MyLocationDB.TABLE_LOCATION);
		db.execSQL("DROP TABLE IF EXISTS "+MyExerciseDB.TABLE_EXERCISE);
		onCreate(db);
	}

}
