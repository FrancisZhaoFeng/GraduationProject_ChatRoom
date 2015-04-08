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
import com.zhbit.crs.domain.ZdbFrdReqNotifItemEntity;
import com.zhbit.crs.domain.ZFrdRequestEntity;
import com.zhbit.crs.domain.User;
import com.zhbit.crs.myNetwork.NetworkService;

public class TabMsgFrdReqProcActivity extends Activity {

	private ZdbFrdReqNotifItemEntity mThisItem;

	private TextView mTvHeadName;
	private ImageView mImgIcon;
	private TextView mTvGender;
	private TextView mTvAge;
	private Button mBtnAccept;
	private Button mBtnDecline;

	private int mThisPos;

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		// this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.tabmsg_frd_req_proc2);

		Intent intent0 = getIntent();
		String in = intent0.getStringExtra("itemStr");
		mThisPos = intent0.getIntExtra("itemPos", 0);
		mThisItem = new ZdbFrdReqNotifItemEntity(in);

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

		User uu0 = mThisItem.getStrOfUser();
		if (uu0.getSex() == false) {
			mTvGender.setText("Girl");
		} else {
			mTvGender.setText("Guy");
		}

		mTvAge.setText(uu0.getAge() + "");

		mBtnAccept.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(TabMsgFrdReqProcActivity.this);

				// set title
				alertDialogBuilder.setTitle(null);
				// set dialog message
				alertDialogBuilder.setMessage("are you sure to accept the request?").setCancelable(true).setPositiveButton("Yes", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						ZFrdRequestEntity reqEnt0 = new ZFrdRequestEntity(mThisItem.getStrOfUser(), ConnectedApp.getInstance().getUserInfo());
						reqEnt0.accept();
						NetworkService.getInstance().sendUpload(GlobalMsgTypes.msgFriendshipRequestResponse, reqEnt0.toString());

						mThisItem.setStatus(ZdbFrdReqNotifItemEntity.mAccepted);
						FrdRequestNotifActivity.getInstance().setItemStatus(mThisPos, ZdbFrdReqNotifItemEntity.mAccepted);

						FriendListInfo.getFriendListInfo().uponMakeNewFriend(mThisItem.getStrOfUser());
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
						ZFrdRequestEntity reqEnt0 = new ZFrdRequestEntity(mThisItem.getStrOfUser(), ConnectedApp.getInstance().getUserInfo());
						reqEnt0.decline();
						NetworkService.getInstance().sendUpload(GlobalMsgTypes.msgFriendshipRequestResponse, reqEnt0.toString());

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
