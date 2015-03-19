package org.yuner.www.chatServices;

import java.util.ArrayList;
import java.util.List;

import org.yuner.www.bean.UserInfo;
import org.yuner.www.commons.GlobalStrings;
import org.yuner.www.mainBody.FriendListPage;
import org.yuner.www.mainBody.MainBodyActivity;
import org.yuner.www.util.UnsavedChatMsg;

import android.util.Log;
import android.util.SparseArray;


public class FriendListInfo {

	public static final String strSplitter = GlobalStrings.friendListDivider;
	
	/* single instance */
	private static FriendListInfo mFriendListInfo;
	
	/* list stores all users */
	private List<UserInfo> mListOfFriends;
	private SparseArray<UserInfo> mSparseArrayOfFriends;   // this is to facilitate the search
	
	/* retrieve single instance */
	public static FriendListInfo getFriendListInfo() {
		if(mFriendListInfo == null) {
			mFriendListInfo = new FriendListInfo();
		}
		return mFriendListInfo;
	}
	
	/* private constructor */
	private FriendListInfo() {
		mListOfFriends = InitData.getInitData().getListOfFriends();
		
		mSparseArrayOfFriends = new SparseArray<UserInfo>();
		for(UserInfo uu0 : mListOfFriends) {
			mSparseArrayOfFriends.put(uu0.getId(), uu0);
		}
	}
	
	/*
	public void updateFriendList(String str0) {
		String[] strArr0 = str0.split(strSplitter);
		int type = Integer.parseInt(strArr0[0]);
		
		UserInfo userInfo = new UserInfo(strArr0[1]);
		if(type==1) {
			mListOfFriends.add(userInfo);
			ChatServiceData.getInstance().newUser(userInfo);
			UnsavedChatMsg.getInstance().newUser(userInfo);
		} else if(type==-1) {
			for(UserInfo ui0 : mListOfFriends) {
				if(ui0.getId() == userInfo.getId()) {
					mListOfFriends.remove(mListOfFriends.indexOf(ui0));
					ChatServiceData.getInstance().offLineUser(ui0);
					break;
				}
			}
		}
		
		if(MainBodyActivity.getCurPage() == MainBodyActivity.mPageFriendList) {
			FriendListPage.getInstance().onFriendListUpdate();
		}
	}
	*/
	
	public void uponMakeNewFriend(UserInfo uu0) {
		int newId = uu0.getId();
		if(mSparseArrayOfFriends.get(newId) != null) {
			return;      // we need to be careful to avoid double insert
		}
		
		mListOfFriends.add(uu0);
		mSparseArrayOfFriends.put(uu0.getId(), uu0);
		
		ChatServiceData.getInstance().newUser(uu0);
		UnsavedChatMsg.getInstance().newUser(uu0);
		if(MainBodyActivity.getCurPage() == MainBodyActivity.mPageFriendList) {
			FriendListPage.getInstance().onFriendListUpdate();
		}
	}
	
	public void friendGoOnAndOffline(String msg0, int onOff) {
		UserInfo uup = new UserInfo(msg0);
		int id = uup.getId();
		
		UserInfo uux = mSparseArrayOfFriends.get(id);
		uux.setIsOnline(onOff);
		
		if(MainBodyActivity.getCurPage() == MainBodyActivity.mPageFriendList) {
			FriendListPage.getInstance().onFriendListUpdate();
		}
	}
	
	public UserInfo getUserFromId(int id) {
		
		return mSparseArrayOfFriends.get(id);
	}
	
	public List<UserInfo> getFriendList() {
		return mListOfFriends;
	}
	
	public String getNameFromId(int id) {
		return mSparseArrayOfFriends.get(id).getName();
	}
	
	public static void closeFriendListInfo() {
		mFriendListInfo = null;
	}
	
	public boolean isIdFriend(int id) {
		if(mSparseArrayOfFriends.get(id) != null) {
			return true;
		} else {
			return false;
		}
	}
	
}
