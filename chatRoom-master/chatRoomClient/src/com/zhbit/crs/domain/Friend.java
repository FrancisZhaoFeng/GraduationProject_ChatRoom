package com.zhbit.crs.domain;

import java.util.Date;

/**
 * Friend entity. @author MyEclipse Persistence Tools
 */

public class Friend implements java.io.Serializable {

	// Fields

	/**
	 * 
	 */
	private static final long serialVersionUID = -6107034331421216387L;
	private FriendId id;
	private Date friendtime;
	private boolean state;
	private String note;

	// Constructors

	/** default constructor */
	public Friend() {
	}

	/** minimal constructor */
	public Friend(FriendId id, Date friendtime) {
		this.id = id;
		this.friendtime = friendtime;
	}
	
	public Friend(FriendId id, Date friendtime, String note) {
		this.id = id;
		this.friendtime = friendtime;
		this.note = note;
	}

	/** full constructor */
	public Friend(FriendId id, Date friendtime, boolean state, String note) {
		this.id = id;
		this.friendtime = friendtime;
		this.state = state;
		this.note = note;
	}

	// Property accessors

	public FriendId getId() {
		return this.id;
	}

	public void setId(FriendId id) {
		this.id = id;
	}

	public Date getFriendtime() {
		return this.friendtime;
	}

	public void setFriendtime(Date friendtime) {
		this.friendtime = friendtime;
	}

	public boolean getState() {
		return this.state;
	}

	public void setState(boolean state) {
		this.state = state;
	}

	public String getNote() {
		return this.note;
	}

	public void setNote(String note) {
		this.note = note;
	}

}