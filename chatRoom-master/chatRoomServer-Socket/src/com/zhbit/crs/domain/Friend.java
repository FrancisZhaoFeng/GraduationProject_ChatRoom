package com.zhbit.crs.domain;

/**
 * Friend entity. @author MyEclipse Persistence Tools
 */

public class Friend implements java.io.Serializable {

	// Fields
	private static final long serialVersionUID = 8511348746671571563L;
	private Integer id;
	private User userByFriendid;
	private User userByUserid;
	private String note;

	// Constructors

	/** default constructor */
	public Friend() {
	}

	/** full constructor */
	public Friend(User userByFriendid, User userByUserid, String note) {
		this.userByFriendid = userByFriendid;
		this.userByUserid = userByUserid;
		this.note = note;
	}

	// Property accessors

	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public User getUserByFriendid() {
		return this.userByFriendid;
	}

	public void setUserByFriendid(User userByFriendid) {
		this.userByFriendid = userByFriendid;
	}

	public User getUserByUserid() {
		return this.userByUserid;
	}

	public void setUserByUserid(User userByUserid) {
		this.userByUserid = userByUserid;
	}

	public String getNote() {
		return this.note;
	}

	public void setNote(String note) {
		this.note = note;
	}

}