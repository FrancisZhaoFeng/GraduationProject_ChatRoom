package com.zhbit.crs.mainBody;

import com.zhbit.crs.R;

import android.content.Context;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

/**
 * ChooseRoom is used to present a listView for app-user to choose which chatting room he/she wants to
 * go in, be it public room, group chatting room or friend chatting *
 */
public class ChooseRoomPage {
	
	private static ChooseRoomPage mInstance;
	
	private View mViewOfPage;
	private ListView mListview;
	private Context mContext0;
	
	public static ChooseRoomPage getInstance() {
		if(mInstance == null) {
			mInstance = new ChooseRoomPage();
		}
		return mInstance;
	}
	
	private ChooseRoomPage() {
	}
	
	public void onInit(View viewOfPage, Context context0) {
		mViewOfPage = viewOfPage;
		mContext0 = context0;
	}
	
	public void onCreate() {
		
		mListview = (ListView)mViewOfPage.findViewById(R.id.cb0ChooseRoomList);
		mListview.setAdapter(new ChooseRoomAdapter(mContext0));
		mListview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				switch (position) {
				case 0:
					break;
				case 1:
					MainBodyActivity.getInstance().gotoPage(MainBodyActivity.mTabNumContacts, 
							MainBodyActivity.mPageFriendList);
				default :
					break;
				}
			}
		});
	}	
	
}
