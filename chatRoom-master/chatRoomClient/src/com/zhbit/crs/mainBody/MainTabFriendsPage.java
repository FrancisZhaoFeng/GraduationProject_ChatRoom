package com.zhbit.crs.mainBody;

import com.zhbit.crs.R;

import android.content.Context;
import android.view.View;
import android.widget.RelativeLayout;

public class MainTabFriendsPage {

	private static MainTabFriendsPage mInstance;
	
	private View mViewOfPage;
	private Context mContext;
	
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
		mContext = cont;
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
