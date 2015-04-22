package com.zhbit.crs.domain;

import java.util.Date;

/**
 * ChatPerLogTemp entity. @author MyEclipse Persistence Tools
 */

public class ChatPerLogTemp implements java.io.Serializable {

	// Fields

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
	public ChatPerLogTemp() {
	}

	/** minimal constructor */
	public ChatPerLogTemp(ChatPerLog chatPerLog) {
		this.userBySenderid = chatPerLog.getUserBySenderid();
		this.userByReceiverid = chatPerLog.getUserByReceiverid();
		this.sendtime = chatPerLog.getSendtime();
		this.sendtext = chatPerLog.getSendtext();
		this.sendimage = chatPerLog.getSendimage();
		this.sendvoice = chatPerLog.getSendvoice();
		this.type = chatPerLog.getType();
	}

	public ChatPerLogTemp(Date sendtime, Integer type) {
		this.sendtime = sendtime;
		this.type = type;
	}

	public ChatPerLogTemp(User userBySenderid, User userByReceiverid, String sendtext, Integer type) {
		this.userBySenderid = userBySenderid;
		this.userByReceiverid = userByReceiverid;
		this.sendtext = sendtext;
		this.type = type;
	}

	public ChatPerLogTemp(User userBySenderid, User userByReceiverid, Date sendtime, String sendtext, Integer type) {
		this.userBySenderid = userBySenderid;
		this.userByReceiverid = userByReceiverid;
		this.sendtime = sendtime;
		this.sendtext = sendtext;
		this.type = type;
	}

	/** full constructor */
	public ChatPerLogTemp(User userBySenderid, User userByReceiverid, Date sendtime, String sendtext, byte[] sendimage, byte[] sendvoice, Integer type) {
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