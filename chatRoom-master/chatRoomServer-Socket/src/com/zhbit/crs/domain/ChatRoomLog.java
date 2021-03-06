package com.zhbit.crs.domain;

import java.util.Date;

/**
 * ChatRoomLog entity. @author MyEclipse Persistence Tools
 */

public class ChatRoomLog implements java.io.Serializable {

	// Fields
	private static final long serialVersionUID = -4777197771783394088L;
	private Integer logid;
	private User user;
	private ChatRoom chatRoom;
	private Date sendtime;
	private String sendtext;
	private byte[] sendimage;
	private byte[] sendvoice;
	private Integer type;

	// Constructors

	/** default constructor */
	public ChatRoomLog() {
	}

	/** minimal constructor */
	public ChatRoomLog(Date sendtime, Integer type) {
		this.sendtime = sendtime;
		this.type = type;
	}

	/** full constructor */
	public ChatRoomLog(User user, ChatRoom chatRoom, Date sendtime,
			String sendtext, byte[] sendimage, byte[] sendvoice, Integer type) {
		this.user = user;
		this.chatRoom = chatRoom;
		this.sendtime = sendtime;
		this.sendtext = sendtext;
		this.sendimage = sendimage;
		this.sendvoice = sendvoice;
		this.type = type;
	}

	// Property accessors

	public Integer getLogid() {
		return this.logid;
	}

	public void setLogid(Integer logid) {
		this.logid = logid;
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

	public Date getSendtime() {
		return this.sendtime;
	}

	public void setSendtime(Date sendtime) {
		this.sendtime = sendtime;
	}

	public String getSendtext() {
		return this.sendtext;
	}

	public void setSendtext(String sendtext) {
		this.sendtext = sendtext;
	}

	public byte[] getSendimage() {
		return this.sendimage;
	}

	public void setSendimage(byte[] sendimage) {
		this.sendimage = sendimage;
	}

	public byte[] getSendvoice() {
		return this.sendvoice;
	}

	public void setSendvoice(byte[] sendvoice) {
		this.sendvoice = sendvoice;
	}

	public Integer getType() {
		return this.type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

}