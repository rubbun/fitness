package com.example.calories_calculator;

import com.example.object.MyExercise;

//All of class in this package use for calculator calories
//Use Method Factory in design pattern

public class Cycling extends Calculator{

	protected Cycling(MyExercise myExercise) {
		super(myExercise);
	}

	@Override
	public float getCaloriesBurn() {
		float mph=(float) (myExercise.getDistance()/myExercise.getTotal_time()*3.6/1.60934);
		float space=0;
		if(mph<10)
			space=4;
		else if(mph>=10 && mph <=12)
			space=6;
		else if(mph>12 && mph<=14)
			space=8;
		else if(mph>14 && mph<=16)
			space=10;
		else if(mph>16 && mph <=20)
			space=12;
		else space=16;
		
		
		return Integer.parseInt(myExercise.getWeight())*myExercise.getTotal_time()/3600*space;
	}

}
