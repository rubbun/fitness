package com.example.object;

import java.util.ArrayList;

import com.example.mygooglemap.R;

public class ExerciseType {
	public static final int WALKING=0;
	public static final int RUNNING=3;
	public static final int MORTORBIKE=1;
	public static final int CYCLING=2;

	private String typeName;
	private int type;
	
	
	//return list of type exercise
	public static ArrayList<ExerciseType> getListExerciseType()
	{
		ArrayList<ExerciseType> list=new ArrayList<ExerciseType>();
		
		list.add(new ExerciseType("Walking", WALKING));
		list.add(new ExerciseType("Running", RUNNING));
		list.add(new ExerciseType("Cycling", CYCLING));
				
		return list;
	}
	
	//return image for every type of exercise
	public static int getImageType(int type)
	{
		if(type==CYCLING)
			return R.drawable.cycle;
		if(type==WALKING)
			return R.drawable.walking;
		if(type==RUNNING)
			return R.drawable.running;
		
		return R.drawable.ic_launcher;
	}
	public ExerciseType(){}
	
	public ExerciseType(String typeName, int type) {
		super();
		this.typeName = typeName;
		this.type = type;
	}
	public int getImageId() {
		return getImageType(type);
	}

	public String getTypeName() {
		return typeName;
	}
	public void setTypeName(String typeName) {
		this.typeName = typeName;
	}
	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
	}
	
}
