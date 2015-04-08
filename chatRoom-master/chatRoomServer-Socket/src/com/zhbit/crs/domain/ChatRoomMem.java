package com.zhbit.crs.domain;

/**
 * ChatRoomMem entity. @author MyEclipse Persistence Tools
 */

public class ChatRoomMem implements java.io.Serializable {

	// Fields
	private static final long serialVersionUID = 1580889167043775388L;
	private Integer memid;
	private User user;
	private ChatRoom chatRoom;

	// Constructors

	/** default constructor */
	public ChatRoomMem() {
	}

	/** full constructor */
	public ChatRoomMem(User user, ChatRoom chatRoom) {
		this.user = user;
		this.chatRoom = chatRoom;
	}

	// Property accessors

	public Integer getMemid() {
		return this.memid;
	}

	public void setMemid(Integer memid) {
		this.memid = memid;
	}

	public User getUser() {
		return this.user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public ChatRoom getChatRoom() {
		return this.chatRoom;
	}

	public void setChatRoom(ChatRoom chatRoom) {
		this.chatRoom = chatRoom;
	}

}