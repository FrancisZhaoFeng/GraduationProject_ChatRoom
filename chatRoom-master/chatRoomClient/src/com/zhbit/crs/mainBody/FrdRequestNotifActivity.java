package com.zhbit.crs.mainBody;

import java.util.ArrayList;
import java.util.Date;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ListView;

import com.zhbit.crs.R;
import com.zhbit.crs.action.ConnectedApp;
import com.zhbit.crs.domain.ZdbFrdReqNotifItemEntity;
import com.zhbit.crs.domain.User;

/**
 * @author zhaoguofeng 
 * 此页面为好友申请消息，以及，好友申请消息回应页面，listView形式布局， 
 * 场景： 
 * 1.A是请求者，请求B为好友，B（可以点击该消息进入：accept，rejust界面）拒绝，此时 A 和 B 的该界面分别存在1条消息（B再次点击该消息不能进入accept界面）；
 * 2.A是请求者，再次请求B为好友，B（同上）拒绝，此时 A 和 B 的该界面分别存在2条消息（B再次点击该消息不能进入accept界面）；
 * 3.A是请求者，再次请求B为好友，B（同上）接收，此时 A 和 B 的该界面分别存在3条消息（B再次点击该消息不能进入accept界面）；
 */
public class FrdRequestNotifActivity extends Activity {

	private static FrdRequestNotifActivity mInstance;
	private static ArrayList<ZdbFrdReqNotifItemEntity> mListOfNotif;

	public static FrdRequestNotifActivity getInstance() {
		return mInstance;
	}

	public static ArrayList<ZdbFrdReqNotifItemEntity> getListOfNotif() {
		if (mListOfNotif == null) {
			mListOfNotif = new ArrayList<ZdbFrdReqNotifItemEntity>();
		}
		return mListOfNotif;
	}

	public static void clearListOfNotif() {
		mListOfNotif = null;
	}

	public static void newNotification(int type, int id, boolean imgId, String name, String content, Date time, User user) {
		ZdbFrdReqNotifItemEntity entity = new ZdbFrdReqNotifItemEntity(type, id, imgId, name, content, time, user);

		if (mListOfNotif == null) {
			mListOfNotif = new ArrayList<ZdbFrdReqNotifItemEntity>();
		}
		mListOfNotif.add(entity);
	}

	private boolean mIsCurPage = false;
	private ListView mListView;
	private ZdbFrdReqNotifItemEntity mEntity;
	private Dialog mDialog;

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.app.Activity#onCreate(android.os.Bundle) 
	 * 此页面为好友申请消息，以及，好友申请消息回应页面，listView形式布局， 
	 * 场景： 
	 * 1.A是请求者，请求B为好友，B（可以点击该消息进入：accept，rejust界面）拒绝，此时 A 和 B 的该界面分别存在1条消息（B再次点击该消息不能进入accept界面）；
	 * 2.A是请求者，再次请求B为好友，B（同上）拒绝，此时 A 和 B 的该界面分别存在2条消息（B再次点击该消息不能进入accept界面）；
	 * 3.A是请求者，再次请求B为好友，B（同上）接收，此时 A 和 B 的该界面分别存在3条消息（B再次点击该消息不能进入accept界面）；
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		// this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.tabmsg_frd_req_notif_page);

		mInstance = this;
		if (mListOfNotif == null) {
			mListOfNotif = new ArrayList<ZdbFrdReqNotifItemEntity>();
		}

		mListView = (ListView) findViewById(R.id.tabmsg_frd_req_notif_page_listview);

		mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
			}
		});
	}

	@Override
	public void onResume() {
		super.onResume();
		mIsCurPage = true;
		onUpdateView();

		try {
			FrdRequestNotifActivity preced = ConnectedApp.getInstance().getFrdRequestNotifActivity();
			preced.finish();
		} catch (Exception e) {
		}

		ConnectedApp.getInstance().setFrdRequestNotifActivity(this);
	}

	@Override
	public void onPause() {
		super.onPause();
		mIsCurPage = false;
	}

	/**
	 * @param position
	 * 调用accept，refuse界面
	 */
	public void onPopupForResponse(int position) {
		Intent intent = new Intent(FrdRequestNotifActivity.this, TabMsgFrdReqProcActivity.class); //TabMsgFrdReqProcActivity 是accept，refuse界面
		mEntity = mListOfNotif.get(position);

		if (mEntity.getType() == ZdbFrdReqNotifItemEntity.mTypeFrdReq && mEntity.getStatus() == ZdbFrdReqNotifItemEntity.mUnanswer) {
			intent.putExtra("itemObj", mEntity);
			intent.putExtra("itemPos", position);
			startActivity(intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT));
			overridePendingTransition(R.anim.my_slide_right_in, R.anim.my_slide_left_out);
		}
	}

	public void setItemStatus(int pos, int status) {
		mEntity = mListOfNotif.get(pos);
		mEntity.setStatus(status);
	}

	public boolean getIsCurPage() {
		return mIsCurPage;
	}

	/**
	 * 进入“好友消息请求界面listView”，必须调用此函数，因：listView元素由FrdRequestNotifAdapter布局组成
	 */
	public void onUpdateView() {
		mListView.setAdapter(new FrdRequestNotifAdapter(this, mListOfNotif));
	}

}
