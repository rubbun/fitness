package com.example.adapter;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.mygooglemap.R;
import com.example.object.ExerciseType;

public class ExerciseTypeAdapter extends BaseAdapter{

	private ArrayList<ExerciseType> list;
	private Context context;
	private int layoutResourceId;
	public ExerciseTypeAdapter(Context context, int resource,
			ArrayList<ExerciseType> list) {
		this.context=context;
		this.layoutResourceId=resource;
		this.list=list;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder=null;
		if(convertView==null)
		{
			convertView=LayoutInflater.from(context).inflate(layoutResourceId, null);
			holder=new ViewHolder();
			holder.ivImage=(ImageView) convertView.findViewById(R.id.ivImageType);
			holder.tvType=(TextView) convertView.findViewById(R.id.tvType);
			convertView.setTag(holder);
		}
		else holder=(ViewHolder) convertView.getTag();
		holder.ivImage.setImageResource(list.get(position).getImageId());
		holder.tvType.setText(list.get(position).getTypeName());
		return convertView;
	}
	@Override
	public ExerciseType getItem(int position) {
		return list.get(position);
	}
	class ViewHolder{
		TextView tvType;
		ImageView ivImage;
	}
	@Override
	public int getCount() {
		return list.size();
	}

	@Override
	public long getItemId(int position) {
		return 0;
	}
}
