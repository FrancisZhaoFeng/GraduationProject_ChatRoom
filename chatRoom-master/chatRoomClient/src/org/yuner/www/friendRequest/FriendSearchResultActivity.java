package org.yuner.www.friendRequest;

import java.util.ArrayList;

import org.yuner.www.ConnectedApp;
import org.yuner.www.R;
import org.yuner.www.bean.FrdRequestEntity;
import org.yuner.www.bean.UserInfo;
import org.yuner.www.chatServices.FriendListInfo;
import org.yuner.www.commons.GlobalMsgTypes;
import org.yuner.www.commons.GlobalStrings;
import org.yuner.www.myNetwork.NetConnect;
import org.yuner.www.myNetwork.NetworkService;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ListView;

public class FriendSearchResultActivity extends Activity{

	private static FriendSearchResultActivity mInstance;
	
	private ListView mListviewOfResults;
	
	private ArrayList<UserInfo> mListU;
	private UserInfo requestee;
	
	public static FriendSearchResultActivity getInstance() {
		return mInstance;
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
//		this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);	
		setContentView(R.layout.cc0_friend_search_result);
		
		ConnectedApp.getInstance().addItemIntoListActivity(this);
		
		mInstance = this;
		
		mListviewOfResults = (ListView)findViewById(R.id.cc0_friend_search_result_listview);
		
		Intent intent0 = getIntent();
		String searchedResult = intent0.getStringExtra("searchResult");
		String[] strArr0 = searchedResult.split(GlobalStrings.searchListDivider);
		int num = Integer.parseInt(strArr0[0]);
		
		mListU = new ArrayList<UserInfo>();
		for(int p = 1;p <= num;p++) {
			UserInfo uux = new UserInfo(strArr0[p]);
			mListU.add(uux);
		}
		mListviewOfResults.setAdapter(new FriendSearchResultAdapter(this,mListU));
		
		mListviewOfResults.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				UserInfo mySelf = ConnectedApp.getInstance().getUserInfo();
				requestee = mListU.get(position);
				
				AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
						FriendSearchResultActivity.this);
				// set title
				alertDialogBuilder.setTitle(null);
				// set dialog message
				
				if(mySelf.getId() == requestee.getId()) {
					alertDialogBuilder
					.setMessage("sorry, you cannot send yourself friend request!")
					.setCancelable(true)
					.setPositiveButton("Ok",new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog,int id) {
							dialog.cancel();
						}
					});
				}
				else if(!FriendListInfo.getFriendListInfo().isIdFriend(requestee.getId())) {
					alertDialogBuilder
					.setMessage("are you sure to send friend request?")
					.setCancelable(true)
					.setPositiveButton("Yes",new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog,int id) {
							UserInfo myUser = ConnectedApp.getInstance().getUserInfo();
							FrdRequestEntity reqEnt0 = new FrdRequestEntity(myUser, requestee);
							reqEnt0.accept();
							NetworkService.getInstance().sendUpload(GlobalMsgTypes.msgFriendshipRequest, reqEnt0.toString());
						}
					  })
					.setNegativeButton("No",new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog,int id) {
							dialog.cancel();
					}
					});
				} else {
					alertDialogBuilder
					.setMessage("sorry, you are friends already!")
					.setCancelable(true)
					.setPositiveButton("Ok",new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog,int id) {
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
