package com.zhbit.crs.chatServices;

import java.util.ArrayList;
import java.util.List;

import com.zhbit.crs.domain.User;
import com.zhbit.crs.util.UnsavedChatMsg;


public class InitData extends Thread{

	/* singleton instance */
	private static InitData mInitData;
	
//	private UserInfo mUserInfo;
	private User user;
	private boolean msg3Recev;
	
//	private List<UserInfo> mListOfFriends;
	private List<User> mListOfFriends;
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
		mListOfFriends = new ArrayList<User>();
	}
	
	@Override
	public void run() {
		while(!(msg3Recev && msg5Recev)){
//			Log.w("while(!(msg3Recev && msg5Recev))", ""+msg3Recev+"-"+msg5Recev);
		}
	}
	
	public void msg3Arrive(User user) {//String str
//		mUserInfo = new UserInfo(str);
		this.user = user;
		msg3Recev = true;
		if(user == null) {  //mUserInfo.getId() < 0    //invalid username or password, no friendlist will be sent
			msg5Recev = true;
		}
	}
	
	public void msg5Arrive(List<User> users) {//String str
//		String strSplitter = FriendListInfo.strSplitter;
//		String[] strArr0 = str.split(strSplitter);
//		int count = Integer.parseInt(strArr0[0]);
		
		ChatServiceData mChatServiceData = ChatServiceData.getInstance();
		if(users != null){
			mListOfFriends = users;
			for(int i = 1 ; i <= users.size() ; i++) {
//				UserInfo userInfo = new UserInfo(strArr0[p]);
				mChatServiceData.newUser(users.get(i));
				UnsavedChatMsg.getInstance().newUser(users.get(i));
			}
		}
		msg5Recev = true;
	}
	
//	public UserInfo getUserInfo() {
//		return mUserInfo;
//	}
	
	public User getUser(){
		return user;
	}
	
//	public List<UserInfo> getListOfFriends() {
//		return mListOfFriends;
//	}
	
	public List<User> getListOfFriends() {
		return mListOfFriends;
	}
	
	// to close mInitData in order to avoid same thread being started twice
	public static void closeInitData() {
		mInitData = null;
	}
}
