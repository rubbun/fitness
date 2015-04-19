package com.example.adapter;

import java.util.ArrayList;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.mygooglemap.DetailActivity;
import com.example.mygooglemap.R;
import com.example.object.ExerciseType;
import com.example.object.MyExercise;

public class MyExerciseAdapter extends ArrayAdapter<MyExercise>{
	Context context;
	int layoutRourceId;
	ArrayList<MyExercise> listExercises;

	public MyExerciseAdapter(Context context, int resource,
			ArrayList<MyExercise> listExercise) {
		super(context, resource, listExercise);
		this.context=context;
		this.layoutRourceId=resource;
		this.listExercises=listExercise;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		ViewHolder holder=null;
		if(convertView==null)
		{
			convertView=LayoutInflater.from(context).inflate(layoutRourceId, null);
			
			holder=new ViewHolder();
			holder.ivItem=(ImageView) convertView.findViewById(R.id.ivItemShow);
			holder.tvStartTime=(TextView) convertView.findViewById(R.id.tvStartTime);
			holder.tvName=(TextView) convertView.findViewById(R.id.tvName);
			holder.tvPosition=(TextView) convertView.findViewById(R.id.tvPosition);
			convertView.setTag(holder);
		}
		else
			holder=(ViewHolder) convertView.getTag();
		
		final MyExercise myExercise=listExercises.get(listExercises.size()-position-1);
		
		holder.tvName.setText("Name: "+myExercise.getName());
		holder.tvStartTime.setText("Date: "+myExercise.getStart_time().split(" ")[0]);
		holder.ivItem.setImageResource(ExerciseType.getImageType(myExercise.getEx_type()));
		//holder.tvPosition.setText((position+1)+"");
		holder.tvPosition.setText("");
		convertView.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent=new Intent(context,DetailActivity.class);
				intent.putExtra(DetailActivity.DETAIL_EXERCISE, myExercise);
				intent.putExtra("position", position);
				context.startActivity(intent);
			}
		});
		return convertView;
	}

	class ViewHolder{
		ImageView ivItem;
		TextView tvStartTime,tvName, tvPosition;
	}
}
