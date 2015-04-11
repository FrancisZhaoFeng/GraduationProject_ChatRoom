package com.zhbit.crs.friendRequest;

import android.app.Service;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;

import com.zhbit.crs.chatServices.FriendListInfo;
import com.zhbit.crs.domain.Friend;
import com.zhbit.crs.domain.User;
import com.zhbit.crs.domain.ZdbChatEntity;
import com.zhbit.crs.domain.ZdbFrdReqNotifItemEntity;
import com.zhbit.crs.domain.ZdbTabMsgItemEntity;
import com.zhbit.crs.mainBody.FrdRequestNotifActivity;
import com.zhbit.crs.mainBody.MainBodyActivity;
import com.zhbit.crs.mainBody.MainTabMsgPage;
import com.zhbit.crs.util.tools;

/**
 * @author zhaoguofeng 好友请求服务类，基础 Service
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
	 * @return 返回本类实例
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
	 *            处理 好友请求 函数
	 */
	public void processFriendRequest(Friend friend) { // String msg0
	// FrdRequestEntity reqEnt = new FrdRequestEntity(friend);
		mRequester = friend.getId().getUserid();
		mRequestee = friend.getId().getFriendid();

		MainTabMsgPage.getInstance().onUpdateById(ZdbTabMsgItemEntity.FrdReqNotifId, mRequester.getSex(), mRequester.getUsername(),
				mRequester.getUsername() + " wants to be your friend", tools.getDate(), true);
		if (MainBodyActivity.getCurPage() == MainBodyActivity.mPageMsg) {  //确保当前界面在显示界面
			MainTabMsgPage.getInstance().onUpdateView();
		}

		FrdRequestNotifActivity.newNotification(ZdbFrdReqNotifItemEntity.mTypeFrdReq, mRequester.getUserid(), mRequester.getSex(), mRequester.getUsername(),
				mRequester.getUsername() + " wants to be your friend", tools.getDate(), mRequester);
		if (FrdRequestNotifActivity.getInstance() != null && FrdRequestNotifActivity.getInstance().getIsCurPage() == true) { // 确保当前界面在“好友消息请求界面”，后调用该界面listView的内容元素
			FrdRequestNotifActivity.getInstance().onUpdateView();
		}
	}

	/**
	 * @param friend
	 *            处理 好友请求回应 函数
	 */
	public void processFriendRequestResponse(Friend friend) {
		// FrdRequestEntity reqEnt = new FrdRequestEntity(friend);
		mRequestee = friend.getId().getFriendid();

		if (friend.getState()) {
			MainTabMsgPage.getInstance().onUpdateById(ZdbTabMsgItemEntity.FrdReqNotifId, mRequestee.getSex(), mRequestee.getUsername(),
					mRequestee.getUsername() + " accepted your friend request", tools.getDate(), true);
		} else {
			MainTabMsgPage.getInstance().onUpdateById(ZdbTabMsgItemEntity.FrdReqNotifId, mRequestee.getSex(), mRequestee.getUsername(),
					mRequestee.getUsername() + " declined your friend request", tools.getDate(), true);
		}

		if (MainBodyActivity.getCurPage() == MainBodyActivity.mPageMsg) {
			MainTabMsgPage.getInstance().onUpdateView();
		}

		String msg11;
		if (!friend.getState()) {
			msg11 = mRequestee.getUsername() + " has declined your request to be friends";
		} else {
			msg11 = mRequestee.getUsername() + " has accepted your request to be friends";
			FriendListInfo.getFriendListInfo().uponMakeNewFriend(mRequestee);
		}
		FrdRequestNotifActivity.newNotification(ZdbFrdReqNotifItemEntity.mTypeFrdReqResult, mRequestee.getUserid(), mRequestee.getSex(), mRequestee.getUsername(), msg11,
				tools.getDate(), mRequestee);
		if (FrdRequestNotifActivity.getInstance() != null && FrdRequestNotifActivity.getInstance().getIsCurPage() == true) {
			FrdRequestNotifActivity.getInstance().onUpdateView();
		}
	}

}
