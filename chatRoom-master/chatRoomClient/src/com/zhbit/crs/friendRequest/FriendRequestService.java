package com.zhbit.crs.friendRequest;

import java.util.List;

import android.app.Service;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;

import com.zhbit.crs.chatServices.FriendListInfo;
import com.zhbit.crs.domain.ChatEntity;
import com.zhbit.crs.domain.FrdReqNotifItemEntity;
import com.zhbit.crs.domain.FrdRequestEntity;
import com.zhbit.crs.domain.Friend;
import com.zhbit.crs.domain.TabMsgItemEntity;
import com.zhbit.crs.domain.User;
import com.zhbit.crs.mainBody.FrdRequestNotifActivity;
import com.zhbit.crs.mainBody.MainBodyActivity;
import com.zhbit.crs.mainBody.MainTabMsgPage;

public class FriendRequestService extends Service {

	private static FriendRequestService mInstance;

	// private UserInfo mRequester;
	// private UserInfo mRequestee;
	private User mRequester;
	private User mRequestee;
	private FriendRequestMsgReceiver mFriendRequestReceiver;

	public static FriendRequestService getInstance() {
		return mInstance;
	}

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	@Override
	public void onCreate() {
		super.onCreate();
		mInstance = this;
		IntentFilter ifter = new IntentFilter("zhbit.example.hello.FRIEND_REQUEST_MSGS");
		mFriendRequestReceiver = new FriendRequestMsgReceiver();
		registerReceiver(mFriendRequestReceiver, ifter);
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		unregisterReceiver(mFriendRequestReceiver);
	}

	public void processFriendRequest(Friend friend) { // String msg0
//		FrdRequestEntity reqEnt = new FrdRequestEntity(friend);
		mRequester = friend.getUserByUserid();
		mRequestee = friend.getUserByFriendid();

		MainTabMsgPage.getInstance().onUpdateById(TabMsgItemEntity.FrdReqNotifId, mRequester.getSex(), mRequester.getUsername(),
				mRequester.getUsername() + " wants to be your friend", ChatEntity.genDate(), true);
		if (MainBodyActivity.getCurPage() == MainBodyActivity.mPageMsg) {
			MainTabMsgPage.getInstance().onUpdateView();
		}

		FrdRequestNotifActivity.newNotification(FrdReqNotifItemEntity.mTypeFrdReq, mRequester.getUserid(), mRequester.getSex(), mRequester.getUsername(),
				mRequester.getUsername() + " wants to be your friend", ChatEntity.genDate(), mRequester);
		if (FrdRequestNotifActivity.getInstance() != null && FrdRequestNotifActivity.getInstance().getIsCurPage() == true) {
			FrdRequestNotifActivity.getInstance().onUpdateView();
		}
	}

	public void processFriendRequestResponse(Friend friend) {
//		FrdRequestEntity reqEnt = new FrdRequestEntity(friend);
		mRequestee = friend.getUserByFriendid();

		if (friend.getNote().equals("accept")) {
			MainTabMsgPage.getInstance().onUpdateById(TabMsgItemEntity.FrdReqNotifId, mRequestee.getSex(), mRequestee.getUsername(),
					mRequestee.getUsername() + " accepted your friend request", ChatEntity.genDate(), true);
		} else {
			MainTabMsgPage.getInstance().onUpdateById(TabMsgItemEntity.FrdReqNotifId, mRequestee.getSex(), mRequestee.getUsername(),
					mRequestee.getUsername() + " declined your friend request", ChatEntity.genDate(), true);
		}

		if (MainBodyActivity.getCurPage() == MainBodyActivity.mPageMsg) {
			MainTabMsgPage.getInstance().onUpdateView();
		}

		String msg11;
		if (!friend.getNote().equals("accept")) {
			msg11 = mRequestee.getUsername() + " has declined your request to be friends";
		} else {
			msg11 = mRequestee.getUsername() + " has accepted your request to be friends";
			FriendListInfo.getFriendListInfo().uponMakeNewFriend(mRequestee);
		}
		FrdRequestNotifActivity.newNotification(FrdReqNotifItemEntity.mTypeFrdReqResult, mRequestee.getUserid(), mRequestee.getSex(), mRequestee.getUsername(), msg11,
				ChatEntity.genDate(), mRequestee);
		if (FrdRequestNotifActivity.getInstance() != null && FrdRequestNotifActivity.getInstance().getIsCurPage() == true) {
			FrdRequestNotifActivity.getInstance().onUpdateView();
		}
	}

}
