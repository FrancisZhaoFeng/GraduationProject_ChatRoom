package org.yuner.www.chatServices;

import java.util.ArrayList;
import java.util.List;

import org.yuner.www.bean.UserInfo;
import org.yuner.www.util.UnsavedChatMsg;


public class InitData extends Thread{

	/* singleton instance */
	private static InitData mInitData;
	
	private UserInfo mUserInfo;
	private boolean msg3Recev;
	
	private List<UserInfo> mListOfFriends;
	private boolean msg5Recev;
	
	public static InitData getInitData() {
		if(mInitData == null) {
			mInitData = new InitData();
		}
		return mInitData;
	}
	
	private InitData() {
		msg3Recev = false;
		msg5Recev = false;
		mListOfFriends = new ArrayList<UserInfo>();
	}
	
	@Override
	public void run() {
		while(!(msg3Recev && msg5Recev));
	}
	
	public void msg3Arrive(String str) {
		mUserInfo = new UserInfo(str);
		msg3Recev = true;
		if(mUserInfo.getId() < 0) {  // invalid username or password, no friendlist will be sent
			msg5Recev = true;
		}
	}
	
	public void msg5Arrive(String str) {
		String strSplitter = FriendListInfo.strSplitter;
		String[] strArr0 = str.split(strSplitter);
		int count = Integer.parseInt(strArr0[0]);
		
		ChatServiceData mChatServiceData = ChatServiceData.getInstance();
		for(int p=1;p<=count;p++) {
			UserInfo userInfo = new UserInfo(strArr0[p]);
			mListOfFriends.add(userInfo);
			mChatServiceData.newUser(userInfo);
			UnsavedChatMsg.getInstance().newUser(userInfo);
		}
		msg5Recev = true;
	}
	
	public UserInfo getUserInfo() {
		return mUserInfo;
	}
	
	public List<UserInfo> getListOfFriends() {
		return mListOfFriends;
	}
	
	// to close mInitData in order to avoid same thread being started twice
	public static void closeInitData() {
		mInitData = null;
	}
}
