package com.example.object;

import java.io.Serializable;

import com.example.calories_calculator.Calculator;

public class MyExercise implements Serializable{
	private String id;
	private String start_time;
	private String end_time;
	private float distance;
	private long total_time;
	private float calories=0;
	private int ex_type;
	private String name;
	private String weight;
	private String comment;
	
	public MyExercise() {
		
	}
	
	
	public MyExercise(String start_time, String end_time, float distance,
			long total_time, float calories, int ex_type) {
		super();
		this.start_time = start_time;
		this.end_time = end_time;
		this.distance = distance;//met
		this.total_time = total_time;//second
		this.calories = calories;
		this.ex_type = ex_type;
	}
	
	public String getWeight() {
		return weight;
	}


	public void setWeight(String weight) {
		this.weight = weight;
	}


	public String getName() {
		return name;
	}


	public void setName(String name) {
		this.name = name;
	}


	public String getComment() {
		return comment;
	}


	public void setComment(String comment) {
		this.comment = comment;
	}


	public String getId() {
		return id;
	}


	public void setId(String id) {
		this.id = id;
	}


	public String getStart_time() {
		return start_time;
	}
	public void setStart_time(String start_time) {
		this.start_time = start_time;
	}
	public String getEnd_time() {
		return end_time;
	}
	public void setEnd_time(String end_time) {
		this.end_time = end_time;
	}
	public float getDistance() {
		return distance;
	}
	public void setDistance(float distance) {
		this.distance = distance;
	}
	public float getTotal_time() {
		return total_time;
	}
	public void setTotal_time(long total_time) {
		this.total_time = total_time;
	}
	public float getCalories() {
		return calories;
	}
	public void setCalories(float calories) {
		this.calories = calories;
	}
	public void setCalories() {
		calories=Calculator.getInstance(this).getCaloriesBurn();
	}
	public int getEx_type() {
		return ex_type;
	}
	public void setEx_type(int ex_type) {
		this.ex_type = ex_type;
	}
	@Override
	public String toString() {
		return "Weight:"+weight+",TotalTime:"+total_time+",distance:"+distance+",Type:"+ex_type;
	}
	
}
