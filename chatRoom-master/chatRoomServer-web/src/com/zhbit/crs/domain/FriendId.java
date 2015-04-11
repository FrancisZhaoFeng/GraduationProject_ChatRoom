package com.zhbit.crs.domain;

/**
 * FriendId entity. @author MyEclipse Persistence Tools
 */

public class FriendId implements java.io.Serializable {

	// Fields

	private User userid;
	private User friendid;

	// Constructors

	/** default constructor */
	public FriendId() {
	}

	/** full constructor */
	public FriendId(User user, User friendid) {
		this.userid = user;
		this.friendid = friendid;
	}

	public User getUserid() {
		return userid;
	}

	public void setUserid(User userid) {
		this.userid = userid;
	}

	public User getFriendid() {
		return friendid;
	}

	public void setFriendid(User friendid) {
		this.friendid = friendid;
	}

	// Property accessors

}