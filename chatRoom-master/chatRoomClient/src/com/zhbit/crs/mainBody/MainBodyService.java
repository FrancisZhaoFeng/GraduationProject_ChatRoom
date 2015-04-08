package com.zhbit.crs.mainBody;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.util.SparseArray;

import com.zhbit.crs.action.ConnectedApp;
import com.zhbit.crs.chatServices.ChatServiceData;
import com.zhbit.crs.chatServices.FriendListInfo;
import com.zhbit.crs.domain.ChatPerLog;
import com.zhbit.crs.domain.ZdbFrdReqNotifItemEntity;
import com.zhbit.crs.domain.ZdbTabMsgItemEntity;
import com.zhbit.crs.domain.User;
import com.zhbit.crs.util.DbSaveOldMsg;
import com.zhbit.crs.util.UnsavedChatMsg;

public class MainBodyService {

	private static MainBodyService mInstance;

	public static MainBodyService getInstance() {
		if (mInstance == null) {
			mInstance = new MainBodyService();
		}
		return mInstance;
	}

	private MainBodyService() {
	}

	private Context mContext;

	public void onInit(Context context) {
		mContext = context;
	}

	public void onReadMsg() {
		DbSaveOldMsg.onInit(mContext);
		DbSaveOldMsg.getInstance();

		List<User> friendList = FriendListInfo.getFriendListInfo().getFriendList();
		int myId = ConnectedApp.getInstance().getUserInfo().getUserid();
		for (User user : friendList) {
			int id = user.getUserid();
			ArrayList<ChatPerLog> mapFriendsEntity = (ArrayList<ChatPerLog>) ChatServiceData.getInstance().getCurMsg(2, id);
			ArrayList<Boolean> mapFriendsSelf = (ArrayList<Boolean>) ChatServiceData.getInstance().getCurIsSelf(2, id);
			DbSaveOldMsg.getInstance().getMsg(mapFriendsEntity, mapFriendsSelf, myId, id);
		}
	}

	public void onReadFrdReqNotif() {
		ArrayList<ZdbFrdReqNotifItemEntity> list = FrdRequestNotifActivity.getListOfNotif();

		DbSaveOldMsg.onInit(mContext);
		DbSaveOldMsg.getInstance().getFrdReqNotif(ConnectedApp.getInstance().getUserInfo().getUserid(), list);
	}

	public void onReadUnreadMsgAm() {
		DbSaveOldMsg.onInit(mContext);

		List<User> friendList = FriendListInfo.getFriendListInfo().getFriendList();
		int myId = ConnectedApp.getInstance().getUserInfo().getUserid();
		for (User uu0 : friendList) {
			int friendId = uu0.getUserid();
			int num = DbSaveOldMsg.getInstance().getUnreadMsgs(myId, friendId);
			ChatServiceData.getInstance().setUnreadMsgs(friendId, num);
		}
	}

	/**
	 * 保存，消息界面-已读消息项，ChatPerLog
	 */
	public void saveOldMsgs() {
		ArrayList<User> listOfUsers = (ArrayList<User>) FriendListInfo.getFriendListInfo().getFriendList();
		SparseArray<ArrayList<ChatPerLog>> mapOfEntity = UnsavedChatMsg.getInstance().getMapFriendsEntity();
		SparseArray<ArrayList<Boolean>> mapOfIsSelf = UnsavedChatMsg.getInstance().getMapFriendsSelf();

		DbSaveOldMsg.onInit(mContext);
		DbSaveOldMsg.getInstance();

		int myId = ConnectedApp.getInstance().getUserInfo().getUserid();
		for (User user : listOfUsers) {
			int id = user.getUserid();
			ArrayList<ChatPerLog> listOfEntity = mapOfEntity.get(id);
			ArrayList<Boolean> listOfIsSelf = mapOfIsSelf.get(id);
			int size = listOfEntity.size();
			for (int i = 0; i < size; i++) {
				ChatPerLog ent = listOfEntity.get(i);
				boolean isSelf = listOfIsSelf.get(i);
				DbSaveOldMsg.getInstance().saveMsg(myId, id, isSelf, ent);
			}
		}
	}

	/**
	 * 保存，消息界面-未读消息数量，（userid,friendid,num）
	 */
	public void saveUnreadMsgAm() {
		DbSaveOldMsg.onInit(mContext);

		List<User> friendList = FriendListInfo.getFriendListInfo().getFriendList();
		int myId = ConnectedApp.getInstance().getUserInfo().getUserid();
		for (User user : friendList) {
			int friendId = user.getUserid();
			int num = ChatServiceData.getInstance().getUnreadMsgs(friendId);
			DbSaveOldMsg.getInstance().saveUnreadMsgs(myId, friendId, num);
		}
	}

	/**			
	 * 保存，消息界面-消息项,TabMsgItemEntity
	 */
	public void saveTabMsgItems() {
		List<ZdbTabMsgItemEntity> listOfEntity = MainTabMsgPage.getInstance().getListOfEntity();
		DbSaveOldMsg.onInit(mContext);
		DbSaveOldMsg.getInstance().saveTabMsgItem(ConnectedApp.getInstance().getUserInfo().getUserid(), listOfEntity);
	}

	/**
	 * 保存，消息界面好友请求-提示项,FrdReqNotifItemEntity
	 */
	public void saveFrdNotifItems() {
		DbSaveOldMsg.getInstance().saveFrdReqNotif(ConnectedApp.getInstance().getUserInfo().getUserid(), FrdRequestNotifActivity.getListOfNotif());
	}

}
