package com.example.calories_calculator;

import com.example.object.MyExercise;

public class Motorbike extends Calculator{

	protected Motorbike(MyExercise myExercise) {
		super(myExercise);
	}

	@Override
	public float getCaloriesBurn() {
		return 0;
	}

}
