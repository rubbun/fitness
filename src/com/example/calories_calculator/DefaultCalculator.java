package com.example.calories_calculator;

import com.example.object.MyExercise;

public class DefaultCalculator extends Calculator{

	protected DefaultCalculator(MyExercise myExercise) {
		super(myExercise);
	}

	@Override
	public float getCaloriesBurn() {
		return 0;
	}

}
