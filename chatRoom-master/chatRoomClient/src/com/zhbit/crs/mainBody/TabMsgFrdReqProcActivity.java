package com.zhbit.crs.mainBody;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.zhbit.crs.R;
import com.zhbit.crs.action.ConnectedApp;
import com.zhbit.crs.chatServices.FriendListInfo;
import com.zhbit.crs.commons.GlobalMsgTypes;
import com.zhbit.crs.domain.Friend;
import com.zhbit.crs.domain.FriendId;
import com.zhbit.crs.domain.ZdbFrdReqNotifItemEntity;
import com.zhbit.crs.domain.ZFrdRequestEntity;
import com.zhbit.crs.domain.User;
import com.zhbit.crs.myNetwork.NetworkService;
import com.zhbit.crs.util.tools;

/**
 * @author zhaoguofeng
 * 此界面是：accept 或 refuse 界面
 * 消息界面（点击好友请求消息）-好友请求消息列表listView形式（点击最近的消息）-accept 或 refuse 界面
 */
public class TabMsgFrdReqProcActivity extends Activity {

	private ZdbFrdReqNotifItemEntity mThisItem;

	private TextView mTvHeadName;
	private ImageView mImgIcon;
	private TextView mTvGender;
	private TextView mTvAge;
	private Button mBtnAccept;
	private Button mBtnDecline;

	private int mThisPos;

	/* (non-Javadoc)
	 * @see android.app.Activity#onCreate(android.os.Bundle)
	 * 消息界面（点击好友请求消息）-好友请求消息列表listView形式（点击最近的消息）-accept 或 refuse 界面
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		// this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.tabmsg_frd_req_proc2);

		Intent intent = getIntent();
		mThisItem = (ZdbFrdReqNotifItemEntity)intent.getSerializableExtra("itemObj");
		mThisPos = intent.getIntExtra("itemPos", 0);

		mTvHeadName = (TextView) findViewById(R.id.tabmsg_frd_req_proc_header_name);
		mImgIcon = (ImageView) findViewById(R.id.tabmsg_frd_req_proc_icon);
		mTvGender = (TextView) findViewById(R.id.tabmsg_frd_req_proc_gender);
		mTvAge = (TextView) findViewById(R.id.tabmsg_frd_req_proc_age);
		mBtnAccept = (Button) findViewById(R.id.tabmsg_frd_req_proc_btn_accept);
		mBtnDecline = (Button) findViewById(R.id.tabmsg_frd_req_proc_btn_decline);

		mTvHeadName.setText(mThisItem.getName());
		if (mThisItem.getImgId() == false) {
			mImgIcon.setImageResource(R.drawable.cb0_h001);
		} else {
			mImgIcon.setImageResource(R.drawable.cb0_h003);
		}

		User user = mThisItem.getUser();
		if (user.getSex() == false) {
			mTvGender.setText("Girl");
		} else {
			mTvGender.setText("Guy");
		}

		mTvAge.setText(user.getAge() + "");

		mBtnAccept.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(TabMsgFrdReqProcActivity.this);

				// set title
				alertDialogBuilder.setTitle(null);
				// set dialog message
				alertDialogBuilder.setMessage("are you sure to accept the request?").setCancelable(true).setPositiveButton("Yes", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						Friend friendReq = new Friend(new FriendId(mThisItem.getUser(),ConnectedApp.getInstance().getUser()),tools.getDate(),true,"");
						NetworkService.getInstance().sendObject(GlobalMsgTypes.msgFriendshipRequestResponse, friendReq); //发送Friend类数据到服务器，note字段赋予accept值
						mThisItem.setStatus(ZdbFrdReqNotifItemEntity.mAccepted);
						FrdRequestNotifActivity.getInstance().setItemStatus(mThisPos, ZdbFrdReqNotifItemEntity.mAccepted);
						FriendListInfo.getFriendListInfo().uponMakeNewFriend(mThisItem.getUser());
					}
				}).setNegativeButton("No", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						dialog.cancel();
					}
				});
				// create alert dialog
				AlertDialog alertDialog = alertDialogBuilder.create();
				// show it
				alertDialog.show();
			}
		});

		mBtnDecline.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(TabMsgFrdReqProcActivity.this);
				// set title
				alertDialogBuilder.setTitle(null);
				// set dialog message
				alertDialogBuilder.setMessage("are you sure to decline the request?").setCancelable(true).setPositiveButton("Yes", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
//						ZFrdRequestEntity reqEnt0 = new ZFrdRequestEntity(mThisItem.getUser(), ConnectedApp.getInstance().getUser());
//						reqEnt0.decline();
						Friend friendReq = new Friend(new FriendId(mThisItem.getUser(),ConnectedApp.getInstance().getUser()),tools.getDate(),false,"");
						NetworkService.getInstance().sendObject(GlobalMsgTypes.msgFriendshipRequestResponse, friendReq);
						mThisItem.setStatus(ZdbFrdReqNotifItemEntity.mDeclined);
						FrdRequestNotifActivity.getInstance().setItemStatus(mThisPos, ZdbFrdReqNotifItemEntity.mDeclined);
					}
				}).setNegativeButton("No", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						dialog.cancel();
					}
				});
				// create alert dialog
				AlertDialog alertDialog = alertDialogBuilder.create();
				// show it
				alertDialog.show();
			}
		});
	}

}
