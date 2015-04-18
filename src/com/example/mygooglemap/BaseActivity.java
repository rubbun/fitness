package com.example.mygooglemap;

import android.app.ProgressDialog;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import com.example.Utils.Appsettings;
import com.example.Utils.LoginInfo;
import com.example.Utils.UserInfo;

public class BaseActivity extends FragmentActivity implements OnClickListener {

	public Appsettings app;
	private ProgressDialog dialog;
	public DisplayMetrics metrics;
	public int width, height;

	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		app = (Appsettings) getApplication();
		metrics = getResources().getDisplayMetrics();
		width = metrics.widthPixels;
		height = metrics.heightPixels;

		if (!app.init) {
			app.init = true;
			app.setLogininfo(new LoginInfo(this));
			app.setUserinfo(new UserInfo(this));
		}
	}

	@Override
	public void onClick(View arg0) {

	}

	public void hideKeyBoard(EditText et) {
		InputMethodManager im = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
		im.hideSoftInputFromWindow(et.getWindowToken(), 0);
	}

	public void doShowLoading() {
		dialog = new ProgressDialog(BaseActivity.this);
		dialog.setMessage("Please wait..........");
		dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		dialog.setIndeterminate(true);
		dialog.setCancelable(false);
		dialog.show();

	}

	public void doRemoveLoading() {
		dialog.dismiss();

	}

	public boolean isConnectingToInternet() {
		ConnectivityManager connectivity = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
		if (connectivity != null) {
			NetworkInfo[] info = connectivity.getAllNetworkInfo();
			if (info != null)
				for (int i = 0; i < info.length; i++)
					if (info[i].getState() == NetworkInfo.State.CONNECTED) {
						return true;
					}

		}
		return false;
	}
}
