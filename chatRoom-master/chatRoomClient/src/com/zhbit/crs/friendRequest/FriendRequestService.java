package com.zhbit.crs.friendRequest;

import java.util.List;

import android.app.Service;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;

import com.zhbit.crs.chatServices.FriendListInfo;
import com.zhbit.crs.domain.ZdbChatEntity;
import com.zhbit.crs.domain.ZdbFrdReqNotifItemEntity;
import com.zhbit.crs.domain.ZFrdRequestEntity;
import com.zhbit.crs.domain.Friend;
import com.zhbit.crs.domain.ZdbTabMsgItemEntity;
import com.zhbit.crs.domain.User;
import com.zhbit.crs.mainBody.FrdRequestNotifActivity;
import com.zhbit.crs.mainBody.MainBodyActivity;
import com.zhbit.crs.mainBody.MainTabMsgPage;

/**
 * @author zhaoguofeng
 * 好友请求服务类，基础 Service
 *
 */
public class FriendRequestService extends Service {

	private static FriendRequestService mInstance;

	// private UserInfo mRequester;
	// private UserInfo mRequestee;
	private User mRequester;
	private User mRequestee;
	private FriendRequestMsgReceiver mFriendRequestReceiver;

	/**
	 * @return
	 * 返回本类实例
	 */
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

	/**
	 * @param friend
	 * 处理 好友请求 函数
	 */
	public void processFriendRequest(Friend friend) { // String msg0
//		FrdRequestEntity reqEnt = new FrdRequestEntity(friend);
		mRequester = friend.getUserByUserid();
		mRequestee = friend.getUserByFriendid();

		MainTabMsgPage.getInstance().onUpdateById(ZdbTabMsgItemEntity.FrdReqNotifId, mRequester.getSex(), mRequester.getUsername(),
				mRequester.getUsername() + " wants to be your friend", ZdbChatEntity.genDate(), true);
		if (MainBodyActivity.getCurPage() == MainBodyActivity.mPageMsg) {
			MainTabMsgPage.getInstance().onUpdateView();
		}

		FrdRequestNotifActivity.newNotification(ZdbFrdReqNotifItemEntity.mTypeFrdReq, mRequester.getUserid(), mRequester.getSex(), mRequester.getUsername(),
				mRequester.getUsername() + " wants to be your friend", ZdbChatEntity.genDate(), mRequester);
		if (FrdRequestNotifActivity.getInstance() != null && FrdRequestNotifActivity.getInstance().getIsCurPage() == true) {
			FrdRequestNotifActivity.getInstance().onUpdateView();
		}
	}

	/**
	 * @param friend
	 * 处理 好友请求回应 函数
	 */
	public void processFriendRequestResponse(Friend friend) {
//		FrdRequestEntity reqEnt = new FrdRequestEntity(friend);
		mRequestee = friend.getUserByFriendid();

		if (friend.getNote().equals("accept")) {
			MainTabMsgPage.getInstance().onUpdateById(ZdbTabMsgItemEntity.FrdReqNotifId, mRequestee.getSex(), mRequestee.getUsername(),
					mRequestee.getUsername() + " accepted your friend request", ZdbChatEntity.genDate(), true);
		} else {
			MainTabMsgPage.getInstance().onUpdateById(ZdbTabMsgItemEntity.FrdReqNotifId, mRequestee.getSex(), mRequestee.getUsername(),
					mRequestee.getUsername() + " declined your friend request", ZdbChatEntity.genDate(), true);
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
		FrdRequestNotifActivity.newNotification(ZdbFrdReqNotifItemEntity.mTypeFrdReqResult, mRequestee.getUserid(), mRequestee.getSex(), mRequestee.getUsername(), msg11,
				ZdbChatEntity.genDate(), mRequestee);
		if (FrdRequestNotifActivity.getInstance() != null && FrdRequestNotifActivity.getInstance().getIsCurPage() == true) {
			FrdRequestNotifActivity.getInstance().onUpdateView();
		}
	}

}
