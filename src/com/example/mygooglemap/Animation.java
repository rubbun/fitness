package com.example.mygooglemap;

import android.content.Context;
import android.view.View;
import android.view.animation.AnimationUtils;

//make animation when button is Disable and Enable
public class Animation {
	public static void setEnable(Context context,View button,boolean isEnable)
	{
		button.setEnabled(isEnable);
		
		if(isEnable)
		{
			android.view.animation.Animation animationFadeIn = AnimationUtils.loadAnimation(context, R.anim.enable_animation);
			button.startAnimation(animationFadeIn);
		}
		else
		{
			android.view.animation.Animation animationFadeIn = AnimationUtils.loadAnimation(context, R.anim.disable_animation);
			button.startAnimation(animationFadeIn);
		}
	}
}
