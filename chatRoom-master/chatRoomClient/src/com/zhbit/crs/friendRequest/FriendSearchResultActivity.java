package com.zhbit.crs.friendRequest;

import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ListView;

import com.zhbit.crs.R;
import com.zhbit.crs.action.ConnectedApp;
import com.zhbit.crs.chatServices.FriendListInfo;
import com.zhbit.crs.commons.GlobalMsgTypes;
import com.zhbit.crs.domain.ZFrdRequestEntity;
import com.zhbit.crs.domain.Friend;
import com.zhbit.crs.domain.User;
import com.zhbit.crs.myNetwork.NetworkService;

public class FriendSearchResultActivity extends Activity {

	private static FriendSearchResultActivity mInstance;

	private ListView mListviewOfResults;

	// private ArrayList<User> mListU;
	private List<User> users;
	private User requestee;

	public static FriendSearchResultActivity getInstance() {
		return mInstance;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		// this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.cc0_friend_search_result);

		ConnectedApp.getInstance().addItemIntoListActivity(this);

		mInstance = this;

		mListviewOfResults = (ListView) findViewById(R.id.cc0_friend_search_result_listview);

		Intent intent = getIntent();
		// String searchedResult = intent.getStringExtra("searchResult");
		// String[] strArr0 = searchedResult.split(GlobalStrings.searchListDivider);
		users = (List<User>) intent.getSerializableExtra("searchResult");
		// int num = Integer.parseInt(strArr0[0]);

		// mListU = new ArrayList<User>();
		// for (int p = 1; p <= users.size(); p++) {
		// // User uux = new User(strArr0[p]);
		// mListU.add(users.get(p));
		// }
		mListviewOfResults.setAdapter(new FriendSearchResultAdapter(this, users)); // mListU

		mListviewOfResults.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				User mySelf = ConnectedApp.getInstance().getUser();
				requestee = users.get(position);

				AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(FriendSearchResultActivity.this);
				// set title
				alertDialogBuilder.setTitle(null);
				// set dialog message
//				Log.e("mark", "mark:"+mySelf.getUserid()+"-"+requestee.getUserid());

				if (mySelf.getUserid().intValue() == requestee.getUserid().intValue()) {
					alertDialogBuilder.setMessage("sorry, you cannot send yourself friend request!").setCancelable(true)
							.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog, int id) {
									dialog.cancel();
								}
							});
				} else if (!FriendListInfo.getFriendListInfo().isIdFriend(requestee.getUserid())) {
					alertDialogBuilder.setMessage("are you sure to send friend request?").setCancelable(true)
							.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog, int id) {
									User myUser = ConnectedApp.getInstance().getUser();
//									FrdRequestEntity reqEnt0 = new FrdRequestEntity(myUser, requestee);
//									reqEnt0.accept();
									NetworkService.getInstance().sendUpload(GlobalMsgTypes.msgFriendshipRequest, new Friend(requestee,myUser,""));  //reqEnt0.toString()
								}
							}).setNegativeButton("No", new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog, int id) {
									dialog.cancel();
								}
							});
				} else {
					alertDialogBuilder.setMessage("sorry, you are friends already!").setCancelable(true).setPositiveButton("Ok", new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int id) {
							dialog.cancel();
						}
					});
				}

				AlertDialog alertDialog = alertDialogBuilder.create();
				alertDialog.show();
			}
		});
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		mInstance = null;
	}

	@Override
	public void onResume() {
		super.onResume();
		ConnectedApp.getInstance().setCurActivity(this);
	}
}
