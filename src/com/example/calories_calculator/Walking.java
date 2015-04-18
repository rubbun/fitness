package com.example.calories_calculator;

import com.example.object.MyExercise;

public class Walking extends Calculator{
	
	public Walking(MyExercise myExercise) {
		super(myExercise);
	}

	@Override
	public float getCaloriesBurn() {
		float mph=(float) (myExercise.getDistance()/myExercise.getTotal_time()*3.6/1.60934);
		float space=0;
		if(mph<=2) 
			space=(float) 2.5;
		else if(mph>2 && mph <= 2.5)
			space=(float) 2.8;
		else if(mph>2.5 && mph <= 3.0)
			space=(float) 3;
		else if(mph>3 && mph <= 3.5)
			space=(float) 3.3;
		else if(mph>3.5 && mph <= 4)
			space=(float) 5;
		else if(mph>4 && mph <= 4.5)
			space=(float) 6.3;
		else if(mph>4.5 && mph <= 5)
			space=(float) 8;
		else space=(float) (mph*1.5);
		return Float.parseFloat(myExercise.getWeight())*space*myExercise.getTotal_time()/3600;
	}
}
