package com.example.calories_calculator;

import com.example.object.MyExercise;

//All of class in this package use for calculator calories
//Use Method Factory in design pattern

public class Running extends Calculator{

	protected Running(MyExercise myExercise) {
		super(myExercise);
	}

	@Override
	public float getCaloriesBurn() {
		return (float) (myExercise.getDistance()/1000*3.6*Integer.parseInt(myExercise.getWeight()));
	}

}
