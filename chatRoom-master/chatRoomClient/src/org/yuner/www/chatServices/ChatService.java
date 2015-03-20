/**
 * the background Service that process the message sending by sendMyMessage()
 * and coming message by newMsgArrive()
 * 
 * it also manage the message queue for ChatActivity
 */

package org.yuner.www.chatServices;

import java.util.List;

import org.yuner.www.ConnectedApp;
import org.yuner.www.bean.ChatEntity;
import org.yuner.www.bean.UserInfo;
import org.yuner.www.chatter.ChatActivity;
import org.yuner.www.commons.GlobalMsgTypes;
import org.yuner.www.mainBody.FriendListPage;
import org.yuner.www.mainBody.MainBodyActivity;
import org.yuner.www.mainBody.MainTabMsgPage;
import org.yuner.www.myNetwork.NetConnect;
import org.yuner.www.myNetwork.NetworkService;
import org.yuner.www.util.UnsavedChatMsg;

import android.app.Service;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Binder;
import android.os.IBinder;

public class ChatService extends Service {
	
	private static ChatService mInstance;
	
	private ChatActivity mChatActv = null;
	private ChatMsgReceiver mMsgReceiver = null;
	private ChatBinder mBinder = new ChatBinder();
	
	private int mCurType = 0; // 0 for public room (default), 1 for group room, 2 for friend chatting
	private UserInfo mMyUserInfo;
	
	private int mFriendId;
	
	private NetConnect mNetCon;
	
	private boolean mEnterFromNotification;
	private int mEnterFromNotificationId;
	
	public static ChatService getInstance() {
		return mInstance;
	}
	
	public class ChatBinder extends Binder
	{
		public ChatService getService()
		{
			return ChatService.this;
		}
	}
	
	@Override
	public IBinder onBind(Intent intent)
	{
		return mBinder;
	}
	
	@Override
	public void onCreate()
	{
		super.onCreate();
		
		mFriendId = -1;
		
		mInstance = this;
		
		IntentFilter ifter=new IntentFilter("yuner.example.hello.MESSAGE_RECEIVED");
		mMsgReceiver=new ChatMsgReceiver(this);
		ChatService.this.registerReceiver(mMsgReceiver,ifter);

		mMyUserInfo = ConnectedApp.getInstance().getUserInfo();
	}
	
	@Override
	public void onDestroy()
	{
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
	
	public void setBoundChatActivity(ChatActivity act0)
	{
		mChatActv=act0;
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
	
	public void sendMyMessage(String st0)
	{
		if(mCurType == GlobalMsgTypes.msgFromFriend) {
			ChatEntity ent0 = new ChatEntity(mCurType, mMyUserInfo, 
				mFriendId, st0);
			NetworkService.getInstance().sendUpload(mCurType, ent0.toString());	
	    	newMsgArrive(ent0.toString(),true);
		}
	}
	
	public void newMsgArrive(String str0, boolean isSelf)
	{
		ChatEntity msgEntity=new ChatEntity(str0);
		
		int type = msgEntity.getType();
		// id is the id of the one client is talking to, regardless of him/her being sender or receiver
		int id = msgEntity.getSenderId();   
		if(isSelf) {
			id = mFriendId;
		}
		
		ChatServiceData.getInstance().getCurMsg(type, id).add(msgEntity);		
		ChatServiceData.getInstance().getCurIsSelf(type, id).add(isSelf);
		chatActivityDisplayHistory();	
		
		UnsavedChatMsg.getInstance().newMsg(type, id, msgEntity, isSelf);
		
		if(!ChatActivity.getIsActive() || mFriendId != id) {
			ChatServiceData.getInstance().increUnreadMsgs(id);
		}
		
		if(!ChatActivity.getIsActive() || mFriendId != id) {
		MainTabMsgPage.getInstance().onUpdateByUserinfo(FriendListInfo.getFriendListInfo().getUserFromId(id), 
				msgEntity.getContent(), msgEntity.getTime(), true);
		} else {
			MainTabMsgPage.getInstance().onUpdateByUserinfo(FriendListInfo.getFriendListInfo().getUserFromId(id), 
					msgEntity.getContent(), msgEntity.getTime(), false);
		}
		
		if(MainBodyActivity.getCurPage() == MainBodyActivity.mPageMsg) {
			MainTabMsgPage.getInstance().onUpdateView();
		}
	}
	
	public void chatActivityDisplayHistory() {
		if(mChatActv == null) {
			return;
		}
		if(ChatActivity.getIsActive()) {
			List<ChatEntity> msgs = ChatServiceData.getInstance().getCurMsg(mCurType, mFriendId);
			List<Boolean> isSelf = ChatServiceData.getInstance().getCurIsSelf(mCurType, mFriendId);
			
			if(mCurType == GlobalMsgTypes.msgFromFriend) {
				mChatActv.updateListviewHistory(msgs,isSelf,mCurType,FriendListInfo.getFriendListInfo().
						getNameFromId(mFriendId));
			}
		}		
	}

}
