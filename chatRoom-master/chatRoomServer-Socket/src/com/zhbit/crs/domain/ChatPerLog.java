package com.zhbit.crs.domain;

import java.util.Date;

/**
 * ChatPerLog entity. @author MyEclipse Persistence Tools
 */

public class ChatPerLog implements java.io.Serializable {

	// Fields

	/**
	 * 
	 */
	private static final long serialVersionUID = -7386063188858556377L;
	private Integer logid;
	private User userBySenderid;
	private User userByReceiverid;
	private Date sendtime;
	private String sendtext;
	private byte[] sendimage;
	private byte[] sendvoice;
	private Integer type;

	// Constructors

	/** default constructor */
	public ChatPerLog() {
	}

	/** minimal constructor */
	public ChatPerLog(ChatPerLogTemp chatPerLogTemp) {
		this.userBySenderid = chatPerLogTemp.getUserBySenderid();
		this.userByReceiverid = chatPerLogTemp.getUserByReceiverid();
		this.sendtime = chatPerLogTemp.getSendtime();
		this.sendtext = chatPerLogTemp.getSendtext();
		this.sendimage = chatPerLogTemp.getSendimage();
		this.sendvoice = chatPerLogTemp.getSendvoice();
		this.type = chatPerLogTemp.getType();
	}
	
	public ChatPerLog(Date sendtime, Integer type) {
		this.sendtime = sendtime;
		this.type = type;
	}

	public ChatPerLog(User userBySenderid, User userByReceiverid, String sendtext, Integer type) {
		this.userBySenderid = userBySenderid;
		this.userByReceiverid = userByReceiverid;
		this.sendtext = sendtext;
		this.type = type;
	}
	
	public ChatPerLog(User userBySenderid, User userByReceiverid, Date sendtime, String sendtext, Integer type) {
		this.userBySenderid = userBySenderid;
		this.userByReceiverid = userByReceiverid;
		this.sendtime = sendtime;
		this.sendtext = sendtext;
		this.type = type;
	}
	

	/** full constructor */
	public ChatPerLog(User userBySenderid, User userByReceiverid, Date sendtime, String sendtext, byte[] sendimage, byte[] sendvoice, Integer type) {
		this.userBySenderid = userBySenderid;
		this.userByReceiverid = userByReceiverid;
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

	public User getUserBySenderid() {
		return this.userBySenderid;
	}

	public void setUserBySenderid(User userBySenderid) {
		this.userBySenderid = userBySenderid;
	}

	public User getUserByReceiverid() {
		return this.userByReceiverid;
	}

	public void setUserByReceiverid(User userByReceiverid) {
		this.userByReceiverid = userByReceiverid;
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