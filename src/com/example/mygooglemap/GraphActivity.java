package com.example.mygooglemap;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import org.achartengine.ChartFactory;
import org.achartengine.chart.PointStyle;
import org.achartengine.model.XYMultipleSeriesDataset;
import org.achartengine.model.XYSeries;
import org.achartengine.renderer.BasicStroke;
import org.achartengine.renderer.XYMultipleSeriesRenderer;
import org.achartengine.renderer.XYSeriesRenderer;
import org.json.JSONArray;
import org.json.JSONObject;

import android.graphics.Color;
import android.graphics.Paint.Align;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.constant.Constants;
import com.example.network.KlHttpClient;

public class GraphActivity extends BaseActivity {

	private LinearLayout calorieChart, milesChart;

	private View mChart;
	private ArrayList<String> mDate = new ArrayList<String>();
	private ArrayList<String> mCalorie = new ArrayList<String>();
	private ArrayList<String> mDistance = new ArrayList<String>();

	private LinearLayout ll_calory, ll_distance;

	private String[] mMonth;

	private float total_distance = 0;
	private float total_calorie_burn = 0;
	private String start_time = "";
	private String holding_value = "";

	private Set<String> set = new HashSet<String>();
	private JSONArray jarr;

	@Override
	protected void onCreate(Bundle arg) {
		super.onCreate(arg);
		setContentView(R.layout.activity_graph);

		calorieChart = (LinearLayout) findViewById(R.id.calorieChart);
		ll_calory = (LinearLayout) findViewById(R.id.ll_calory);
		ll_calory.setOnClickListener(this);
		ll_distance = (LinearLayout) findViewById(R.id.ll_distance);
		ll_distance.setOnClickListener(this);

		new fetchDataFromserver().execute();
		// openChart();
	}

	@Override
	public void onClick(View v) {
		super.onClick(v);
		switch (v.getId()) {
		case R.id.ll_calory:
			calorieChart.removeAllViews();
			calorieChart.addView(openChartCalory());
			break;

		case R.id.ll_distance:
			calorieChart.removeAllViews();
			calorieChart.addView(openChartDistance());
			break;
		}
	}

	public class fetchDataFromserver extends AsyncTask<Void, Void, Boolean> {

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			doShowLoading();
		}

		@Override
		protected Boolean doInBackground(Void... params) {
			try {

				JSONObject jobj = new JSONObject();
				jobj.put("user_id", app.getUserinfo().user_id);

				String response = KlHttpClient.SendHttpPost(Constants.HISTORY, jobj.toString());
				if (response != null) {
					JSONObject jRes = new JSONObject(response);
					if (jRes.getBoolean("status")) {
						jarr = jRes.getJSONArray("value");
						for (int i = 0; i < jarr.length(); i++) {
							JSONObject obj = jarr.getJSONObject(i);
							String strObj = obj.getString("points");
							JSONObject ob = new JSONObject(strObj);

							set.add(ob.getString("start_time").split(" ")[0]);

						}

						Iterator<String> ite = set.iterator();
						System.out.println("!!set size:" + set.size());
						while (ite.hasNext()) {
							start_time = ite.next();
							total_calorie_burn = 0.0f;
							total_distance = 0.0f;
							mDate.add(start_time);
							for (int i = 0; i < jarr.length(); i++) {
								JSONObject obj = jarr.getJSONObject(i);
								String strObj = obj.getString("points");
								JSONObject ob = new JSONObject(strObj);
								if (ob.getString("start_time").split(" ")[0].equalsIgnoreCase(start_time)) {
									total_calorie_burn = total_calorie_burn + Float.parseFloat(ob.getString("calories"));
									total_distance = total_distance + Float.parseFloat(ob.getString("distance"));
								}
							}
							mCalorie.add(Float.toString(total_calorie_burn));
							mDistance.add(Float.toString(total_distance));
						}

						return true;
					} else {
						return false;
					}
				}

			} catch (Exception e) {
				e.printStackTrace();
			}
			return false;
		}

		@Override
		protected void onPostExecute(Boolean result) {
			super.onPostExecute(result);
			doRemoveLoading();

			System.out.println("!!the date value is:" + mDate.size());
			System.out.println("!!the calorie value is:" + mCalorie.size());
			System.out.println("!!the distance value is:" + mDistance.size());

			if (result) {
				calorieChart.removeAllViews();
				calorieChart.addView(openChartCalory());
			} else {
				Toast.makeText(getApplicationContext(), "Some error occured", 6000).show();
			}
		}
	}

	private View openChartCalory() {

		XYSeries calorySeries = new XYSeries("Calory");
		
		// Adding data to Income and Expense Series

		for (int i = 0; i < mDate.size(); i++) {
			System.out.println(mCalorie.get(i) + "   " + mDistance.get(i));
			calorySeries.add(i, Double.parseDouble(mCalorie.get(i)));
			
		}
		
		XYMultipleSeriesDataset dataset = new XYMultipleSeriesDataset();
		dataset.addSeries(calorySeries);
		
		XYSeriesRenderer incomeRenderer = new XYSeriesRenderer();
		incomeRenderer.setColor(Color.CYAN);
		incomeRenderer.setFillPoints(true);
		incomeRenderer.setLineWidth(2f);
		incomeRenderer.setDisplayChartValues(true);
		incomeRenderer.setDisplayChartValuesDistance(10);
		incomeRenderer.setPointStyle(PointStyle.CIRCLE);
		incomeRenderer.setStroke(BasicStroke.SOLID);

		
		

		XYMultipleSeriesRenderer multiRenderer = new XYMultipleSeriesRenderer();
		multiRenderer.setXLabels(0);
		multiRenderer.setChartTitle("Calory  Chart");
		multiRenderer.setXTitle("Workout Graph");
		multiRenderer.setYTitle("Calory");

		

		multiRenderer.setChartTitleTextSize(28);

		// setting text size of the axis title

		multiRenderer.setAxisTitleTextSize(24);

		// setting text size of the graph lable

		multiRenderer.setLabelsTextSize(24);

		// setting zoom buttons visiblity

		multiRenderer.setZoomButtonsVisible(false);

		// setting pan enablity which uses graph to move on both axis

		multiRenderer.setPanEnabled(false, false);

		// setting click false on graph

		multiRenderer.setClickEnabled(false);

		// setting zoom to false on both axis

		multiRenderer.setZoomEnabled(false, false);

		// setting lines to display on y axis

		multiRenderer.setShowGridY(true);

		// setting lines to display on x axis

		multiRenderer.setShowGridX(true);

		// setting legend to fit the screen size

		multiRenderer.setFitLegend(true);

		// setting displaying line on grid

		multiRenderer.setShowGrid(true);

		// setting zoom to false

		multiRenderer.setZoomEnabled(false);

		// setting external zoom functions to false

		multiRenderer.setExternalZoomEnabled(false);

		// setting displaying lines on graph to be formatted(like using
		// graphics)

		multiRenderer.setAntialiasing(true);

		// setting to in scroll to false

		multiRenderer.setInScroll(false);

		// setting to set legend height of the graph

		multiRenderer.setLegendHeight(30);

		// setting x axis label align

		multiRenderer.setXLabelsAlign(Align.CENTER);

		// setting y axis label to align

		multiRenderer.setYLabelsAlign(Align.LEFT);

		// setting text style

		multiRenderer.setTextTypeface("sans_serif", Typeface.NORMAL);

		// setting no of values to display in y axis

		// multiRenderer.setYLabels(20);

		// setting y axis max value, Since i'm using static values inside the
		// graph so i'm setting y max value to .

		// if you use dynamic values then get the max y value and set here

		// multiRenderer.setYAxisMax(100);

		// setting used to move the graph on xaxiz to . to the right

		multiRenderer.setXAxisMin(-0.5);

		// setting used to move the graph on xaxiz to . to the right

		// multiRenderer.setXAxisMax(11);

		// setting bar size or space between two bars

		// multiRenderer.setBarSpacing(.);

		// Setting background color of the graph to transparent

		multiRenderer.setBackgroundColor(Color.TRANSPARENT);

		// Setting margin color of the graph to transparent

		multiRenderer.setMarginsColor(getResources().getColor(android.R.color.transparent));

		multiRenderer.setApplyBackgroundColor(true);

		multiRenderer.setScale(2f);

		// setting x axis point size

		multiRenderer.setPointSize(4f);

		// setting the margin size for the graph in the order top, left, bottom,
		// right

		multiRenderer.setMargins(new int[] { 30, 30, 30, 30 });

		for (int i = 0; i < mDate.size(); i++) {

			multiRenderer.addXTextLabel(i, mDate.get(i));

		}

		multiRenderer.addSeriesRenderer(incomeRenderer);

		mChart = ChartFactory.getLineChartView(GraphActivity.this, dataset, multiRenderer);

		// adding the view to the linearlayout

		//calorieChart.addView(mChart);
		return mChart;
	}
	
	private View openChartDistance() {

		XYSeries distaneSeries = new XYSeries("Distance");

		// Adding data to Income and Expense Series

		for (int i = 0; i < mDate.size(); i++) {
			System.out.println(mCalorie.get(i) + "   " + mDistance.get(i));
			distaneSeries.add(i, Double.parseDouble(mDistance.get(i)));
		}
		
		XYMultipleSeriesDataset dataset = new XYMultipleSeriesDataset();
		dataset.addSeries(distaneSeries);

		
		XYSeriesRenderer expenseRenderer = new XYSeriesRenderer();
		expenseRenderer.setColor(Color.CYAN);
		expenseRenderer.setFillPoints(true);
		expenseRenderer.setLineWidth(2f);
		expenseRenderer.setDisplayChartValues(true);
		expenseRenderer.setPointStyle(PointStyle.SQUARE);
		expenseRenderer.setStroke(BasicStroke.SOLID);

		

		XYMultipleSeriesRenderer multiRenderer = new XYMultipleSeriesRenderer();
		multiRenderer.setXLabels(0);
		multiRenderer.setChartTitle("Distance Chart");
		multiRenderer.setXTitle("Workout Graph");
		multiRenderer.setYTitle("Distance");

		

		multiRenderer.setChartTitleTextSize(28);

		// setting text size of the axis title

		multiRenderer.setAxisTitleTextSize(24);

		// setting text size of the graph lable

		multiRenderer.setLabelsTextSize(24);

		// setting zoom buttons visiblity

		multiRenderer.setZoomButtonsVisible(false);

		// setting pan enablity which uses graph to move on both axis

		multiRenderer.setPanEnabled(false, false);

		// setting click false on graph

		multiRenderer.setClickEnabled(false);

		// setting zoom to false on both axis

		multiRenderer.setZoomEnabled(false, false);

		// setting lines to display on y axis

		multiRenderer.setShowGridY(true);

		// setting lines to display on x axis

		multiRenderer.setShowGridX(true);

		// setting legend to fit the screen size

		multiRenderer.setFitLegend(true);

		// setting displaying line on grid

		multiRenderer.setShowGrid(true);

		// setting zoom to false

		multiRenderer.setZoomEnabled(false);

		// setting external zoom functions to false

		multiRenderer.setExternalZoomEnabled(false);

		// setting displaying lines on graph to be formatted(like using
		// graphics)

		multiRenderer.setAntialiasing(true);

		// setting to in scroll to false

		multiRenderer.setInScroll(false);

		// setting to set legend height of the graph

		multiRenderer.setLegendHeight(30);

		// setting x axis label align

		multiRenderer.setXLabelsAlign(Align.CENTER);

		// setting y axis label to align

		multiRenderer.setYLabelsAlign(Align.LEFT);

		// setting text style

		multiRenderer.setTextTypeface("sans_serif", Typeface.NORMAL);

		// setting no of values to display in y axis

		// multiRenderer.setYLabels(20);

		// setting y axis max value, Since i'm using static values inside the
		// graph so i'm setting y max value to .

		// if you use dynamic values then get the max y value and set here

		// multiRenderer.setYAxisMax(100);

		// setting used to move the graph on xaxiz to . to the right

		multiRenderer.setXAxisMin(-0.5);

		// setting used to move the graph on xaxiz to . to the right

		// multiRenderer.setXAxisMax(11);

		// setting bar size or space between two bars

		// multiRenderer.setBarSpacing(.);

		// Setting background color of the graph to transparent

		multiRenderer.setBackgroundColor(Color.TRANSPARENT);

		// Setting margin color of the graph to transparent

		multiRenderer.setMarginsColor(getResources().getColor(android.R.color.transparent));

		multiRenderer.setApplyBackgroundColor(true);

		multiRenderer.setScale(2f);

		// setting x axis point size

		multiRenderer.setPointSize(4f);

		// setting the margin size for the graph in the order top, left, bottom,
		// right

		multiRenderer.setMargins(new int[] { 30, 30, 30, 30 });

		for (int i = 0; i < mDate.size(); i++) {

			multiRenderer.addXTextLabel(i, mDate.get(i));

		}

		
		multiRenderer.addSeriesRenderer(expenseRenderer);

		mChart = ChartFactory.getLineChartView(GraphActivity.this, dataset, multiRenderer);

		// adding the view to the linearlayout

		//calorieChart.addView(mChart);
		return mChart;
	}
}
