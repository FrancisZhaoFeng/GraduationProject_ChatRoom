package com.zhbit.crs.chatServices;

import java.util.List;

import android.util.SparseArray;

import com.zhbit.crs.commons.GlobalStrings;
import com.zhbit.crs.domain.User;
import com.zhbit.crs.mainBody.FriendListPage;
import com.zhbit.crs.mainBody.MainBodyActivity;
import com.zhbit.crs.util.UnsavedChatMsg;

public class FriendListInfo {

	public static final String strSplitter = GlobalStrings.friendListDivider;

	/**
	 * single instance
	 */
	private static FriendListInfo mFriendListInfo;

	/**
	 * list stores all users
	 */
	// private List<UserInfo> mListOfFriends;
	private List<User> mListOfFriends;
	/**
	 * this is to facilitate the search
	 */
	// private SparseArray<UserInfo> mSparseArrayOfFriends;
	private SparseArray<User> mSparseArrayOfFriends;

	/**
	 * @return retrieve single instance
	 */
	public static FriendListInfo getFriendListInfo() {
		if (mFriendListInfo == null) {
			mFriendListInfo = new FriendListInfo();
		}
		return mFriendListInfo;
	}

	/** private constructor */
	private FriendListInfo() {
		// mListOfFriends = InitData.getInitData().getListOfFriends();

		// mSparseArrayOfFriends = new SparseArray<UserInfo>();
		// for(UserInfo uu0 : mListOfFriends) {
		// mSparseArrayOfFriends.put(uu0.getId(), uu0);
		// }
		mListOfFriends = InitData.getInitData().getListOfFriends();
		mSparseArrayOfFriends = new SparseArray<User>();
		for (User uu0 : mListOfFriends) {
			mSparseArrayOfFriends.put(uu0.getUserid(), uu0);
		}
	}

	/*
	 * public void updateFriendList(String str0) { String[] strArr0 =
	 * str0.split(strSplitter); int type = Integer.parseInt(strArr0[0]);
	 * 
	 * UserInfo userInfo = new UserInfo(strArr0[1]); if(type==1) {
	 * mListOfFriends.add(userInfo);
	 * ChatServiceData.getInstance().newUser(userInfo);
	 * UnsavedChatMsg.getInstance().newUser(userInfo); } else if(type==-1) {
	 * for(UserInfo ui0 : mListOfFriends) { if(ui0.getId() == userInfo.getId())
	 * { mListOfFriends.remove(mListOfFriends.indexOf(ui0));
	 * ChatServiceData.getInstance().offLineUser(ui0); break; } } }
	 * 
	 * if(MainBodyActivity.getCurPage() == MainBodyActivity.mPageFriendList) {
	 * FriendListPage.getInstance().onFriendListUpdate(); } }
	 */

	/**
	 * @param user
	 */
	public void uponMakeNewFriend(User user) {//UserInfo uu0
		int newId = user.getUserid();
		if (mSparseArrayOfFriends.get(newId) != null) {
			return; // we need to be careful to avoid double insert
		}

		mListOfFriends.add(user);
		mSparseArrayOfFriends.put(user.getUserid(), user);

		ChatServiceData.getInstance().newUser(user);
		UnsavedChatMsg.getInstance().newUser(user);
		if (MainBodyActivity.getCurPage() == MainBodyActivity.mPageFriendList) {
			FriendListPage.getInstance().onFriendListUpdate();
		}
	}

	public void friendGoOnAndOffline(User user, boolean onOff) { //String msg0
//		UserInfo uup = new UserInfo(msg0);
//		int id = uup.getId();
		int id = user.getUserid();

//		UserInfo uux = mSparseArrayOfFriends.get(id);
		User userTemp = mSparseArrayOfFriends.get(id);
//		uux.setIsOnline(onOff);
		userTemp.setOnline(onOff);

		if (MainBodyActivity.getCurPage() == MainBodyActivity.mPageFriendList) {
			FriendListPage.getInstance().onFriendListUpdate();
		}
	}

	public User getUserFromId(int id) {
		return mSparseArrayOfFriends.get(id);
	}

	/**
	 * @return
	 * 获取好友列表
	 */
	public List<User> getFriendList() {
		return mListOfFriends;
	}

	public String getNameFromId(int id) {
		return mSparseArrayOfFriends.get(id).getUsername();
	}

	public static void closeFriendListInfo() {
		mFriendListInfo = null;
	}

	public boolean isIdFriend(int id) {
		if (mSparseArrayOfFriends.get(id) != null) {
			return true;
		} else {
			return false;
		}
	}

}
