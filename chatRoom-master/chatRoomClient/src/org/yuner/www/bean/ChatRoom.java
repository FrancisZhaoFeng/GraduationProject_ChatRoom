package org.yuner.www.bean;

import java.util.HashSet;
import java.util.Set;

/**
 * ChatRoom entity. @author MyEclipse Persistence Tools
 */

public class ChatRoom implements java.io.Serializable {

	// Fields

	private Integer chatroomid;
	private User user;
	private String chatroomname;
	private String note;
	private boolean isdelete;
	private Set chatRoomLogs = new HashSet(0);
	private Set chatRoomMems = new HashSet(0);

	// Constructors

	/** default constructor */
	public ChatRoom() {
	}

	/** minimal constructor */
	public ChatRoom(String chatroomname) {
		this.chatroomname = chatroomname;
	}

	/** full constructor */
	public ChatRoom(User user, String chatroomname, String note, boolean isdelete, Set chatRoomLogs, Set chatRoomMems) {
		this.user = user;
		this.chatroomname = chatroomname;
		this.note = note;
		this.isdelete = isdelete;
		this.chatRoomLogs = chatRoomLogs;
		this.chatRoomMems = chatRoomMems;
	}

	// Property accessors

	public Integer getChatroomid() {
		return this.chatroomid;
	}

	public void setChatroomid(Integer chatroomid) {
		this.chatroomid = chatroomid;
	}

	public User getUser() {
		return this.user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public String getChatroomname() {
		return this.chatroomname;
	}

	public void setChatroomname(String chatroomname) {
		this.chatroomname = chatroomname;
	}

	public String getNote() {
		return this.note;
	}

	public void setNote(String note) {
		this.note = note;
	}

	public boolean getIsdelete() {
		return this.isdelete;
	}

	public void setIsdelete(boolean isdelete) {
		this.isdelete = isdelete;
	}

	public Set getChatRoomLogs() {
		return this.chatRoomLogs;
	}

	public void setChatRoomLogs(Set chatRoomLogs) {
		this.chatRoomLogs = chatRoomLogs;
	}

	public Set getChatRoomMems() {
		return this.chatRoomMems;
	}

	public void setChatRoomMems(Set chatRoomMems) {
		this.chatRoomMems = chatRoomMems;
	}

}