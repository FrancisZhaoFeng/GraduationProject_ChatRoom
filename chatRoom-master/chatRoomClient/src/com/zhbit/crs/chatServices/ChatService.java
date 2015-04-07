/**
 * the background Service that process the message sending by sendMyMessage()
 * and coming message by newMsgArrive()
 * 
 * it also manage the message queue for ChatActivity
 */

package com.zhbit.crs.chatServices;

import java.util.List;

import android.app.Service;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Binder;
import android.os.IBinder;

import com.zhbit.crs.action.ConnectedApp;
import com.zhbit.crs.chatter.ChatActivity;
import com.zhbit.crs.commons.GlobalMsgTypes;
import com.zhbit.crs.domain.ChatPerLog;
import com.zhbit.crs.domain.User;
import com.zhbit.crs.mainBody.MainBodyActivity;
import com.zhbit.crs.mainBody.MainTabMsgPage;
import com.zhbit.crs.myNetwork.NetConnect;
import com.zhbit.crs.myNetwork.NetworkService;
import com.zhbit.crs.util.UnsavedChatMsg;

public class ChatService extends Service {

	private static ChatService mInstance;

	private ChatActivity mChatActv = null;
	private ChatMsgReceiver mMsgReceiver = null;
	private ChatBinder mBinder = new ChatBinder();

	private int mCurType = 0; // 0 for public room (default), 1 for group room,
								// 2 for friend chatting
								// private UserInfo mMyUserInfo;
	private User mMyUserInfo;

	private int mFriendId;

	private NetConnect mNetCon;

	private boolean mEnterFromNotification;
	private int mEnterFromNotificationId;

	public static ChatService getInstance() {
		return mInstance;
	}

	public class ChatBinder extends Binder {
		public ChatService getService() {
			return ChatService.this;
		}
	}

	@Override
	public IBinder onBind(Intent intent) {
		return mBinder;
	}

	@Override
	public void onCreate() {
		super.onCreate();

		mFriendId = -1;

		mInstance = this;

		IntentFilter ifter = new IntentFilter("zhbit.example.hello.MESSAGE_RECEIVED");
		mMsgReceiver = new ChatMsgReceiver(this);
		ChatService.this.registerReceiver(mMsgReceiver, ifter);

		// mMyUserInfo = ConnectedApp.getInstance().getUserInfo();
		mMyUserInfo = ConnectedApp.getInstance().getUserInfo();
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		ChatService.this.unregisterReceiver(mMsgReceiver);
		NetworkService.getInstance().closeConnection();
		mInstance = null;
	}

	public boolean getEnterFromNotification() {
		return mEnterFromNotification;
	}

	public void setEnterFromNotification(boolean b) {
		mEnterFromNotification = b;
	}

	public int getEnterFromNotificationId() {
		return mEnterFromNotificationId;
	}

	public void setEnterFromNotificationId(int id) {
		mEnterFromNotificationId = id;
	}

	public void setBoundChatActivity(ChatActivity act0) {
		mChatActv = act0;
	}

	public void setType(int type) {
		mCurType = type;
	}

	public void setId(int id) {
		mFriendId = id;
	}

	public int getId() {
		return mFriendId;
	}

	public void sendMyMessage(String st0) {
		if (mCurType == GlobalMsgTypes.msgFromFriend) {
			ChatPerLog ent0 = new ChatPerLog(mCurType, mMyUserInfo, mFriendId, st0);
			NetworkService.getInstance().sendUpload(mCurType, ent0.toString());
			newMsgArrive(ent0, true);
		}
	}

	public void newMsgArrive(ChatPerLog chatPerLog, boolean isSelf) {
		// ChatEntity msgEntity = new ChatEntity(str0);

		int type = chatPerLog.getType();
		// id is the id of the one client is talking to, regardless of him/her
		// being sender or receiver
		int id = chatPerLog.getUserBySenderid().getUserid();
		if (isSelf) {
			id = mFriendId;
		}

		ChatServiceData.getInstance().getCurMsg(type, id).add(chatPerLog);
		ChatServiceData.getInstance().getCurIsSelf(type, id).add(isSelf);
		chatActivityDisplayHistory();

		UnsavedChatMsg.getInstance().newMsg(type, id, chatPerLog, isSelf);

		if (!ChatActivity.getIsActive() || mFriendId != id) {
			ChatServiceData.getInstance().increUnreadMsgs(id);
		}

		if (!ChatActivity.getIsActive() || mFriendId != id) {
			MainTabMsgPage.getInstance().onUpdateByUserinfo(FriendListInfo.getFriendListInfo().getUserFromId(id), chatPerLog.getSendtext(), chatPerLog.getSendtime(),
					true);
		} else {
			MainTabMsgPage.getInstance().onUpdateByUserinfo(FriendListInfo.getFriendListInfo().getUserFromId(id), chatPerLog.getSendtext(), chatPerLog.getSendtime(),
					false);
		}

		if (MainBodyActivity.getCurPage() == MainBodyActivity.mPageMsg) {
			MainTabMsgPage.getInstance().onUpdateView();
		}
	}

	public void chatActivityDisplayHistory() {
		if (mChatActv == null) {
			return;
		}
		if (ChatActivity.getIsActive()) {
			List<ChatPerLog> msgs = ChatServiceData.getInstance().getCurMsg(mCurType, mFriendId);
			List<Boolean> isSelf = ChatServiceData.getInstance().getCurIsSelf(mCurType, mFriendId);

			if (mCurType == GlobalMsgTypes.msgFromFriend) {
				mChatActv.updateListviewHistory(msgs, isSelf, mCurType, FriendListInfo.getFriendListInfo().getNameFromId(mFriendId));
			}
		}
	}

}
