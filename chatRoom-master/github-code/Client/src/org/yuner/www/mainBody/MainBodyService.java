package org.yuner.www.mainBody;

import java.util.ArrayList;
import java.util.List;

import org.yuner.www.ConnectedApp;
import org.yuner.www.bean.ChatEntity;
import org.yuner.www.bean.FrdReqNotifItemEntity;
import org.yuner.www.bean.TabMsgItemEntity;
import org.yuner.www.bean.UserInfo;
import org.yuner.www.chatServices.ChatServiceData;
import org.yuner.www.chatServices.FriendListInfo;
import org.yuner.www.util.DbSaveOldMsg;
import org.yuner.www.util.UnsavedChatMsg;

import android.content.Context;
import android.util.SparseArray;

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
		
		List<UserInfo> friendList = FriendListInfo.getFriendListInfo().getFriendList();
		int myId = ConnectedApp.getInstance().getUserInfo().getId();
		for(UserInfo uu0 : friendList) {
			int id = uu0.getId();
			ArrayList<ChatEntity> mapFriendsEntity = (ArrayList<ChatEntity>) ChatServiceData.getInstance().getCurMsg(2, id);
			ArrayList<Boolean> mapFriendsSelf = (ArrayList<Boolean>) ChatServiceData.getInstance().getCurIsSelf(2, id);
			DbSaveOldMsg.getInstance().getMsg(mapFriendsEntity, mapFriendsSelf, myId, id);
		}
	}
	
	public void onReadFrdReqNotif() {
		ArrayList<FrdReqNotifItemEntity> list0 = FrdRequestNotifActivity.getListOfNotif();
		
		DbSaveOldMsg.onInit(mContext0);
		DbSaveOldMsg.getInstance().getFrdReqNotif(ConnectedApp.getInstance().getUserInfo().getId(), list0);
	}
	
	public void onReadUnreadMsgAm() {
		DbSaveOldMsg.onInit(mContext0);
		
		List<UserInfo> friendList = FriendListInfo.getFriendListInfo().getFriendList();
		int myId = ConnectedApp.getInstance().getUserInfo().getId();
		for(UserInfo uu0 : friendList) {
			int friendId = uu0.getId();
			int num = DbSaveOldMsg.getInstance().getUnreadMsgs(myId, friendId);
			ChatServiceData.getInstance().setUnreadMsgs(friendId, num);
		}
	}
	
	public void saveOldMsgs() {
		ArrayList<UserInfo> listOfUsers = (ArrayList<UserInfo>) FriendListInfo.getFriendListInfo().getFriendList();
		SparseArray<ArrayList<ChatEntity>> mapOfEntity = UnsavedChatMsg.getInstance().getMapFriendsEntity();
		SparseArray<ArrayList<Boolean>> mapOfIsSelf = UnsavedChatMsg.getInstance().getMapFriendsSelf();
		
		DbSaveOldMsg.onInit(mContext0);
		DbSaveOldMsg.getInstance();
		
		int myId = ConnectedApp.getInstance().getUserInfo().getId();
		for(UserInfo uu0 : listOfUsers) {
			int id = uu0.getId();
			ArrayList<ChatEntity> listOfEntity = mapOfEntity.get(id);
			ArrayList<Boolean> listOfIsSelf = mapOfIsSelf.get(id);
			int size = listOfEntity.size();
			for(int i = 0;i < size;i++) {
				ChatEntity ent0 = listOfEntity.get(i);
				boolean isSelf = listOfIsSelf.get(i);
				DbSaveOldMsg.getInstance().saveMsg(myId, id, isSelf, ent0);
			}
		}
	}
	
	public void saveUnreadMsgAm() {
		DbSaveOldMsg.onInit(mContext0);
		
		List<UserInfo> friendList = FriendListInfo.getFriendListInfo().getFriendList();
		int myId = ConnectedApp.getInstance().getUserInfo().getId();
		for(UserInfo uu0 : friendList) {
			int friendId = uu0.getId();
			int num = ChatServiceData.getInstance().getUnreadMsgs(friendId);
			DbSaveOldMsg.getInstance().saveUnreadMsgs(myId, friendId, num);
		}
	}
	
	public void saveTabMsgItems() {
		List<TabMsgItemEntity> listOfEntity = MainTabMsgPage.getInstance().getListOfEntity();
		DbSaveOldMsg.onInit(mContext0);
		DbSaveOldMsg.getInstance().saveTabMsgItem(ConnectedApp.getInstance().getUserInfo().getId(), 
				listOfEntity);
	}
	
	public void saveFrdNotifItems() {
		DbSaveOldMsg.getInstance().saveFrdReqNotif(ConnectedApp.getInstance().getUserInfo().getId(), 
				FrdRequestNotifActivity.getListOfNotif());
	}
	
}
