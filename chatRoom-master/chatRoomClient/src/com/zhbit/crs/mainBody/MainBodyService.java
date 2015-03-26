package com.zhbit.crs.mainBody;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.util.SparseArray;

import com.zhbit.crs.action.ConnectedApp;
import com.zhbit.crs.chatServices.ChatServiceData;
import com.zhbit.crs.chatServices.FriendListInfo;
import com.zhbit.crs.domain.ChatPerLog;
import com.zhbit.crs.domain.FrdReqNotifItemEntity;
import com.zhbit.crs.domain.TabMsgItemEntity;
import com.zhbit.crs.domain.User;
import com.zhbit.crs.util.DbSaveOldMsg;
import com.zhbit.crs.util.UnsavedChatMsg;

public class MainBodyService {

	private static MainBodyService mInstance;
	
	public static MainBodyService getInstance() {
		if(mInstance == null) {
			mInstance = new MainBodyService();
		}
		return mInstance;
	}
	
	private MainBodyService() {	}
	
	private Context mContext0;
	
	public void onInit(Context context) {
		mContext0 = context;
	}
	
	public void onReadMsg() {
		DbSaveOldMsg.onInit(mContext0);
		DbSaveOldMsg.getInstance();
		
		List<User> friendList = FriendListInfo.getFriendListInfo().getFriendList();
		int myId = ConnectedApp.getInstance().getUserInfo().getUserid();
		for(User uu0 : friendList) {
			int id = uu0.getUserid();
			ArrayList<ChatPerLog> mapFriendsEntity = (ArrayList<ChatPerLog>) ChatServiceData.getInstance().getCurMsg(2, id);
			ArrayList<Boolean> mapFriendsSelf = (ArrayList<Boolean>) ChatServiceData.getInstance().getCurIsSelf(2, id);
			DbSaveOldMsg.getInstance().getMsg(mapFriendsEntity, mapFriendsSelf, myId, id);
		}
	}
	
	public void onReadFrdReqNotif() {
		ArrayList<FrdReqNotifItemEntity> list0 = FrdRequestNotifActivity.getListOfNotif();
		
		DbSaveOldMsg.onInit(mContext0);
		DbSaveOldMsg.getInstance().getFrdReqNotif(ConnectedApp.getInstance().getUserInfo().getUserid(), list0);
	}
	
	public void onReadUnreadMsgAm() {
		DbSaveOldMsg.onInit(mContext0);
		
		List<User> friendList = FriendListInfo.getFriendListInfo().getFriendList();
		int myId = ConnectedApp.getInstance().getUserInfo().getUserid();
		for(User uu0 : friendList) {
			int friendId = uu0.getUserid();
			int num = DbSaveOldMsg.getInstance().getUnreadMsgs(myId, friendId);
			ChatServiceData.getInstance().setUnreadMsgs(friendId, num);
		}
	}
	
	public void saveOldMsgs() {
		ArrayList<User> listOfUsers = (ArrayList<User>) FriendListInfo.getFriendListInfo().getFriendList();
		SparseArray<ArrayList<ChatPerLog>> mapOfEntity = UnsavedChatMsg.getInstance().getMapFriendsEntity();
		SparseArray<ArrayList<Boolean>> mapOfIsSelf = UnsavedChatMsg.getInstance().getMapFriendsSelf();
		
		DbSaveOldMsg.onInit(mContext0);
		DbSaveOldMsg.getInstance();
		
		int myId = ConnectedApp.getInstance().getUserInfo().getUserid();
		for(User uu0 : listOfUsers) {
			int id = uu0.getUserid();
			ArrayList<ChatPerLog> listOfEntity = mapOfEntity.get(id);
			ArrayList<Boolean> listOfIsSelf = mapOfIsSelf.get(id);
			int size = listOfEntity.size();
			for(int i = 0;i < size;i++) {
				ChatPerLog ent0 = listOfEntity.get(i);
				boolean isSelf = listOfIsSelf.get(i);
				DbSaveOldMsg.getInstance().saveMsg(myId, id, isSelf, ent0);
			}
		}
	}
	
	public void saveUnreadMsgAm() {
		DbSaveOldMsg.onInit(mContext0);
		
		List<User> friendList = FriendListInfo.getFriendListInfo().getFriendList();
		int myId = ConnectedApp.getInstance().getUserInfo().getUserid();
		for(User uu0 : friendList) {
			int friendId = uu0.getUserid();
			int num = ChatServiceData.getInstance().getUnreadMsgs(friendId);
			DbSaveOldMsg.getInstance().saveUnreadMsgs(myId, friendId, num);
		}
	}
	
	public void saveTabMsgItems() {
		List<TabMsgItemEntity> listOfEntity = MainTabMsgPage.getInstance().getListOfEntity();
		DbSaveOldMsg.onInit(mContext0);
		DbSaveOldMsg.getInstance().saveTabMsgItem(ConnectedApp.getInstance().getUserInfo().getUserid(), 
				listOfEntity);
	}
	
	public void saveFrdNotifItems() {
		DbSaveOldMsg.getInstance().saveFrdReqNotif(ConnectedApp.getInstance().getUserInfo().getUserid(), 
				FrdRequestNotifActivity.getListOfNotif());
	}
	
}
