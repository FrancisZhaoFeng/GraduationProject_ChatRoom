package com.zhbit.crs.mainBody;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.NotificationManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;

import com.zhbit.crs.R;
import com.zhbit.crs.action.ConnectedApp;
import com.zhbit.crs.chatServices.ChatService;
import com.zhbit.crs.chatServices.ChatServiceData;
import com.zhbit.crs.chatServices.FriendListInfo;
import com.zhbit.crs.chatServices.InitData;
import com.zhbit.crs.chatter.ChatActivity;
import com.zhbit.crs.commons.GlobalMsgTypes;
import com.zhbit.crs.domain.SearchEntity;
import com.zhbit.crs.domain.User;
import com.zhbit.crs.friendRequest.FriendRequestService;
import com.zhbit.crs.friendRequest.FriendSearchResultActivity;
import com.zhbit.crs.myNetwork.NetStateReceiver;
import com.zhbit.crs.myNetwork.NetworkService;
import com.zhbit.crs.util.DbSaveOldMsg;

public class MainBodyActivity extends Activity {

	private static MainBodyActivity mInstance;
	private static int mCurPage;
	private static int mCurPageOnTab[];

	/* page definition */
	public static final int mPageMsg = 0;
	public static final int mPageChooseRoom = 1;
	public static final int mPageFriendList = 2;
	public static final int mPageFriendSearch = 3;
	public static final int mPageSettings = 4;

	/* tab number definition */
	public static final int mTabNumMsg = 0;
	public static final int mTabNumContacts = 1;
	public static final int mTabNumFriends = 2;
	public static final int mTabNumSettings = 3;

	private CustomViewPager mMainPager;
	private ImageView mTabMsgs;
	private ImageView mTabContacts;
	private ImageView mTabFriends;
	private ImageView mTabSettings;

	/* friend search string/boolean definition */
	boolean mMsg9Received;
	private List<User> users;

	private NetStateReceiver mNetStateReceiver;

	/*************************** static access functions ***********************************/
	public static MainBodyActivity getInstance() {
		return mInstance;
	}

	public static int getCurPage() {
		return mCurPage;
	}

	public static void setCurPage(int page) {
		mCurPage = page;
	}

	public static int getCurPageForTab(int tab) {
		return mCurPageOnTab[tab];
	}

	public static void setCurPageForTab(int tab, int page) {
		mCurPageOnTab[tab] = page;
	}

	/*************************** static access functions ***********************************/

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		// this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.main_body_main_container);

		mInstance = this;
		mCurPage = mPageMsg;
		mCurPageOnTab = new int[4];
		mCurPageOnTab[mTabNumMsg] = mPageMsg;
		mCurPageOnTab[mTabNumContacts] = mPageChooseRoom;
		mCurPageOnTab[mTabNumFriends] = mPageFriendSearch;
		mCurPageOnTab[mTabNumSettings] = mPageSettings;

		Intent intentTemp = new Intent(MainBodyActivity.this, ChatService.class);
		startService(intentTemp);
		intentTemp = new Intent(MainBodyActivity.this, FriendRequestService.class);
		startService(intentTemp);
		MainBodyService.getInstance().onInit(this);
		mNetStateReceiver = new NetStateReceiver();
		registerReceiver(mNetStateReceiver, new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));

		mMainPager = (CustomViewPager) findViewById(R.id.main_body_main_container_tabpager);

		mTabMsgs = (ImageView) findViewById(R.id.main_body_main_container_msgs);
		mTabContacts = (ImageView) findViewById(R.id.main_body_main_container_contacts);
		mTabFriends = (ImageView) findViewById(R.id.main_body_main_container_friends);
		mTabSettings = (ImageView) findViewById(R.id.main_body_main_container_settings);
		mTabMsgs.setOnClickListener(new MainBodyOnClickListener(mTabNumMsg));
		mTabContacts.setOnClickListener(new MainBodyOnClickListener(mTabNumContacts));
		mTabFriends.setOnClickListener(new MainBodyOnClickListener(mTabNumFriends));
		mTabSettings.setOnClickListener(new MainBodyOnClickListener(mTabNumSettings));

		LayoutInflater li = LayoutInflater.from(this); // Obtains the
														// LayoutInflater from
														// the given context.
		View viewPastMsgs = li.inflate(R.layout.tabmsg_main, null);
		View viewChooseRoom = li.inflate(R.layout.cc0_choose_room, null);
		View viewFriendList = li.inflate(R.layout.cc0_friend_list, null);
		View viewFriendSearch = li.inflate(R.layout.main_tab_friends, null);
		View viewSettings = li.inflate(R.layout.tabsettings_main, null);

		/***************************** set the looks of the pager initial ***********************************************/
		final ArrayList<View> listOfViews = new ArrayList<View>();
		listOfViews.add(viewPastMsgs);
		listOfViews.add(viewChooseRoom);
		listOfViews.add(viewFriendList);
		listOfViews.add(viewFriendSearch);
		listOfViews.add(viewSettings);

		PagerAdapter pagerAdapter0 = new PagerAdapter() {
			@Override
			public boolean isViewFromObject(View arg0, Object arg1) {
				return arg0 == arg1;
			}

			@Override
			public int getCount() {
				return listOfViews.size();
			}

			@Override
			public void destroyItem(View container, int position, Object object) {
				((ViewPager) container).removeView(listOfViews.get(position));
			}

			@Override
			public Object instantiateItem(View container, int position) {
				((ViewPager) container).addView(listOfViews.get(position));
				return listOfViews.get(position);
			}
		};

		mMainPager.setAdapter(pagerAdapter0);
		/***************************** set the looks of the pager **********************************************/
		ChooseRoomPage.getInstance().onInit(viewChooseRoom, this); // 聊天室列表界面
		ChooseRoomPage.getInstance().onCreate();
		FriendListPage.getInstance().onInit(viewFriendList, this); // 好友列表界面
		FriendListPage.getInstance().onCreate();
		MainTabFriendsPage.getInstance().onInit(viewFriendSearch, this); // search界面
		MainTabFriendsPage.getInstance().onCreate();
		MainTabMsgPage.getInstance().onInit(viewPastMsgs, this); // 过去信息界面
		MainTabMsgPage.getInstance().onCreate();
		MainTabSettingsPage.getInstance().onInit(viewSettings, this); // 设置界面
		MainTabSettingsPage.getInstance().onCreate();

		for (int i = 0; i < 2; i++) {
			MainBodyService.getInstance().onReadMsg();
		}
		MainBodyService.getInstance().onReadFrdReqNotif();
		MainBodyService.getInstance().onReadUnreadMsgAm();
		// the msgItem read in is completed inside MainTabMsgPage
		new AskForUnsendThread().start();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		mInstance = null;
		Intent intentTemp = new Intent(MainBodyActivity.this, ChatService.class);
		stopService(intentTemp);
		intentTemp = new Intent(MainBodyActivity.this, FriendRequestService.class);
		stopService(intentTemp);
		try {
			unregisterReceiver(mNetStateReceiver);
		} catch (Exception e) {
		}
		((NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE)).cancelAll();
	}

	@Override
	public void onResume() {
		super.onResume();
		ConnectedApp.getInstance().setCurActivity(this);

		if (mCurPage == mPageFriendList) {
			FriendListPage.getInstance().onFriendListUpdate();
		}
		if (mCurPage == mPageMsg) {
			MainTabMsgPage.getInstance().onUpdateView();
		}
	}

	@Override
	public void onBackPressed() {
		AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
		alertDialogBuilder.setTitle(null);

		alertDialogBuilder.setMessage("are you sure to leave?").setCancelable(false).setPositiveButton("Yes", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int id) {
				MainBodyService.getInstance().saveOldMsgs();
				MainBodyService.getInstance().saveTabMsgItems();
				MainBodyService.getInstance().saveFrdNotifItems();
				MainBodyService.getInstance().saveUnreadMsgAm();
				FrdRequestNotifActivity.clearListOfNotif();
				DbSaveOldMsg.getInstance().close();
				MainBodyActivity.this.superOnBackPressed();
			}
		}).setNegativeButton("No", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int id) {
				dialog.cancel();
			}
		});

		AlertDialog alertDialog = alertDialogBuilder.create();
		alertDialog.show();
	}

	public void superOnBackPressed() {
		super.onBackPressed();
		((NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE)).cancelAll();
	}

	public class MainBodyOnClickListener implements View.OnClickListener {
		private int index = 0;

		public MainBodyOnClickListener(int i) {
			index = i;
		}

		@Override
		public void onClick(View v) {
			int page = mCurPageOnTab[index];
			gotoPage(index, page);
		}
	};

	/*** search friends ***/
	public void startSearch(SearchEntity sEnt0) {
		NetworkService.getInstance().sendUpload(GlobalMsgTypes.msgSearchPeople, sEnt0);
		mMsg9Received = false;
		while (!mMsg9Received) {
		}
		gotoFriendSearchResult(users);
	}

	public void onReceiveSearchList(List<User> users) { // String msg0
		// mSearchedString = msg0;
		this.users = users;
		mMsg9Received = true;
	}

	/*** search friends ***/

	public void gotoPage(int tab, int page) {
		mMainPager.setCurrentItem(page, true); // 切换界面，第二个参数：是否滑动的效果切换界面
		mCurPage = page;
		mCurPageOnTab[tab] = page;
		setBottomImageview(tab);

		if (page == mPageFriendList) {
			FriendListPage.getInstance().onFriendListUpdate();
		}
		if (page == mPageMsg) {
			MainTabMsgPage.getInstance().onUpdateView();
		}
	}

	public void setBottomImageview(int tab) {
		switch (tab) {
		case mTabNumMsg:
			mTabMsgs.setImageDrawable(getResources().getDrawable(R.drawable.tab_weixin_pressed));
			mTabContacts.setImageDrawable(getResources().getDrawable(R.drawable.tab_address_normal));
			mTabFriends.setImageDrawable(getResources().getDrawable(R.drawable.tab_find_frd_normal));
			mTabSettings.setImageDrawable(getResources().getDrawable(R.drawable.tab_settings_normal));
			break;
		case mTabNumContacts:
			mTabMsgs.setImageDrawable(getResources().getDrawable(R.drawable.tab_weixin_normal));
			mTabContacts.setImageDrawable(getResources().getDrawable(R.drawable.tab_address_pressed));
			mTabFriends.setImageDrawable(getResources().getDrawable(R.drawable.tab_find_frd_normal));
			mTabSettings.setImageDrawable(getResources().getDrawable(R.drawable.tab_settings_normal));
			break;
		case mTabNumFriends:
			mTabMsgs.setImageDrawable(getResources().getDrawable(R.drawable.tab_weixin_normal));
			mTabContacts.setImageDrawable(getResources().getDrawable(R.drawable.tab_address_normal));
			mTabFriends.setImageDrawable(getResources().getDrawable(R.drawable.tab_find_frd_pressed));
			mTabSettings.setImageDrawable(getResources().getDrawable(R.drawable.tab_settings_normal));
			break;
		case mTabNumSettings:
			mTabMsgs.setImageDrawable(getResources().getDrawable(R.drawable.tab_weixin_normal));
			mTabContacts.setImageDrawable(getResources().getDrawable(R.drawable.tab_address_normal));
			mTabFriends.setImageDrawable(getResources().getDrawable(R.drawable.tab_find_frd_normal));
			mTabSettings.setImageDrawable(getResources().getDrawable(R.drawable.tab_settings_pressed));
			break;
		default:
			break;
		}
	}

	public void gotoChatActivity() {
		Intent intent = new Intent(MainBodyActivity.this, ChatActivity.class);
		startActivity(intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT));
		overridePendingTransition(R.anim.my_slide_right_in, R.anim.my_slide_left_out);
	}

	public void gotoTabMsgFrdReqNotifActivity() {
		Intent intent = new Intent(MainBodyActivity.this, FrdRequestNotifActivity.class);
		startActivity(intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT));
		overridePendingTransition(R.anim.my_slide_right_in, R.anim.my_slide_left_out);
	}

	public void gotoFriendSearchResult(List<User> users) { // String searchedString
		Intent intent = new Intent(MainBodyActivity.this, FriendSearchResultActivity.class);
		intent.putExtra("searchResult", (Serializable) users);
		startActivity(intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT));
		overridePendingTransition(R.anim.my_slide_right_in, R.anim.my_slide_left_out);
	}

	public void gotoSearchBynameActivity() {
		Intent intent = new Intent(MainBodyActivity.this, SearchFriendByNameActivity.class);
		startActivity(intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT));
		overridePendingTransition(R.anim.my_slide_right_in, R.anim.my_slide_left_out);
	}

	public void gotoSearchByelseActivity() {
		Intent intent = new Intent(MainBodyActivity.this, SearchFriendByElseActivity.class);
		startActivity(intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT));
		overridePendingTransition(R.anim.my_slide_right_in, R.anim.my_slide_left_out);
	}

	public void terminateAll() {
		ConnectedApp.getInstance().clearListActivity();
		ConnectedApp.getInstance().clearAll();
		InitData.closeInitData();
		FriendListInfo.closeFriendListInfo();
		ChatServiceData.closeChatServiceData();
	}

}
