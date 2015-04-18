package com.example.calories_calculator;

import com.example.object.ExerciseType;
import com.example.object.MyExercise;


//All of class in this package use for calculator calories
//Use Method Factory in design pattern

public abstract class Calculator {

	protected  MyExercise myExercise;
	protected Calculator(MyExercise myExercise) {
		this.myExercise=myExercise;
	}

	abstract public float getCaloriesBurn();

	//Every type of exercise, this function will return object to calculator calories for this type
	public static Calculator getInstance(MyExercise myExercise)
	{
		switch (myExercise.getEx_type()) {
		case ExerciseType.WALKING:
			return new Walking(myExercise);
		case ExerciseType.MORTORBIKE:
			return new Motorbike(myExercise);
		case ExerciseType.RUNNING:
			new Running(myExercise);
		case ExerciseType.CYCLING:
			return new Cycling(myExercise);
		default:
			return new DefaultCalculator(myExercise);
		}
	}
}
