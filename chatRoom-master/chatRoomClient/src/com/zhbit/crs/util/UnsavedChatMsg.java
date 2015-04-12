package com.zhbit.crs.util;

import java.util.ArrayList;

import android.util.SparseArray;

import com.zhbit.crs.domain.ChatPerLog;
import com.zhbit.crs.domain.User;

/**
 * @author zhaoguofeng 到目前为止，还没有保存的消息
 */
public class UnsavedChatMsg {

	private static UnsavedChatMsg mInstance;

	/* List for friend chatting */
	private SparseArray<ArrayList<ChatPerLog>> mMapFriendsEntity;
	private SparseArray<ArrayList<Boolean>> mMapFriendsSelf;

	public static UnsavedChatMsg getInstance() {
		if (mInstance == null) {
			mInstance = new UnsavedChatMsg();
		}
		return mInstance;
	}

	private UnsavedChatMsg() {
		mMapFriendsEntity = new SparseArray<ArrayList<ChatPerLog>>();
		mMapFriendsSelf = new SparseArray<ArrayList<Boolean>>();
	}

	public void newUser(User user) {// UserInfo userInfo
	// int id = userInfo.getId();
		int id = user.getUserid();

		mMapFriendsEntity.put(id, new ArrayList<ChatPerLog>());
		mMapFriendsSelf.put(id, new ArrayList<Boolean>());
	}

	public void newMsg(int type, int id, ChatPerLog entity0, boolean isSelf0) {
		mMapFriendsEntity.get(id).add(entity0);
		mMapFriendsSelf.get(id).add(isSelf0);
	}

	/**
	 * @return
	 * 获取与好友的聊天记录
	 */
	public SparseArray<ArrayList<ChatPerLog>> getMapFriendsEntity() {
		return mMapFriendsEntity;
	}

	/**
	 * @return
	 * 获取聊天记录中，用户自己发送的消息为true
	 */
	public SparseArray<ArrayList<Boolean>> getMapFriendsSelf() {
		return mMapFriendsSelf;
	}

	public static void closeChatServiceData() {
		mInstance = null;
	}
}