package com.zhbit.crs.domain;

import com.zhbit.crs.commons.GlobalStrings;

public class ZFrdRequestEntity {

	private static final int idRefuseFriendship = 22;
	private static final int idAcceptFriendship = 23;
	
	private int mStatus;
//	private UserInfo mRequester;
//	private UserInfo mRequestee;
	private User mRequester;
	private User mRequestee;
	
	public ZFrdRequestEntity(User requester, User requestee) {//UserInfo requester, UserInfo requestee
		mStatus = idRefuseFriendship;
		mRequester = requester;
		mRequestee = requestee;
	}
	
	public ZFrdRequestEntity(Friend friend) {
//		String[] arr0 = str0.split(GlobalStrings.friendshipRequestDivider);
//		mStatus = Integer.parseInt(arr0[0]);
//		mRequester = new UserInfo(arr0[1]);
//		mRequestee = new UserInfo(arr0[2]);
		mRequester = friend.getUserByUserid();
		mRequestee = friend.getUserByFriendid();
	}
	
	public String toString() {
		String str0 = mStatus + GlobalStrings.friendshipRequestDivider;
		str0 += mRequester.toString() + GlobalStrings.friendshipRequestDivider;
		str0 += mRequestee.toString() + GlobalStrings.friendshipRequestDivider;
		return str0;
	}
	
	public boolean isAceepted() {
		if(mStatus == idAcceptFriendship) {
			return true;
		}
		return false;
	}
	
	public void accept() {
		mStatus = idAcceptFriendship;
	}
	
	public void decline() {
		mStatus = idRefuseFriendship;
	}
	
	public User getRequester() {
		return mRequester;
	}
	
	public User getRequestee() {
		return mRequestee;
	}
}
