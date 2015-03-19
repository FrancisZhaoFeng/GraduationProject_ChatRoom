package org.yuner.www.mainBody;

import java.util.ArrayList;

import org.yuner.www.ConnectedApp;
import org.yuner.www.R;
import org.yuner.www.commons.GlobalMsgTypes;
import org.yuner.www.commons.GlobalStrings;
import org.yuner.www.myNetwork.NetConnect;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.Spinner;

public class MainTabFriendsPage {

	private static MainTabFriendsPage mInstance;
	
	private View mViewOfPage;
	private Context mContext0;
	
	private RelativeLayout mLayoutByName;
	private RelativeLayout mLayoutByElse;
	
	public static MainTabFriendsPage getInstance() {
		if(mInstance == null) {
			mInstance = new MainTabFriendsPage();
		}
		return mInstance;
	}
	
	private MainTabFriendsPage() {
	}
	
	public void onInit(View view, Context cont) {
		mViewOfPage = view;
		mContext0 = cont;
	}
	
	public void onCreate() {
		
		mInstance = this;
		
		mLayoutByName = (RelativeLayout)mViewOfPage.findViewById(R.id.main_tab_friends_find_by_name);
		mLayoutByElse = (RelativeLayout)mViewOfPage.findViewById(R.id.main_tab_friends_find_by_else);
		
		mLayoutByName.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				MainBodyActivity.getInstance().gotoSearchBynameActivity();
			}
		});
		
		mLayoutByElse.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				MainBodyActivity.getInstance().gotoSearchByelseActivity();
			}
		});
	}
}
