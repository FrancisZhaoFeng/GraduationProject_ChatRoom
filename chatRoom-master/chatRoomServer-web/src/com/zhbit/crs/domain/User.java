package com.zhbit.crs.domain;

import java.util.HashSet;
import java.util.Set;

/**
 * User entity. @author MyEclipse Persistence Tools
 */

public class User implements java.io.Serializable {

	// Fields

	private Integer userid;
	private String username;
	private String password;
	private String telephone;
	private Integer age;
	private boolean sex;
	private boolean online;
	private boolean blacklist;
	private Set chatRoomLogTemps = new HashSet(0);
	private Set chatPerLogTempsForReceiverid = new HashSet(0);
	private Set chatPerLogTempsForSenderid = new HashSet(0);
	private Set friendsForUserid = new HashSet(0);
	private Set friendsForFriendid = new HashSet(0);
	private Set chatRoomLogs = new HashSet(0);
	private Set chatRoomMems = new HashSet(0);
	private Set chatPerLogsForReceiverid = new HashSet(0);
	private Set chatRooms = new HashSet(0);
	private Set chatPerLogsForSenderid = new HashSet(0);

	// Constructors

	/** default constructor */
	public User() {
	}

	/** minimal constructor */
	public User(String username, String password, String telephone, Integer age) {
		this.username = username;
		this.password = password;
		this.telephone = telephone;
		this.age = age;
	}

	/** full constructor */
	public User(String username, String password, String telephone,
			Integer age, boolean sex, boolean online, boolean blacklist,
			Set chatRoomLogTemps, Set chatPerLogTempsForReceiverid,
			Set chatPerLogTempsForSenderid, Set friendsForUserid,
			Set friendsForFriendid, Set chatRoomLogs, Set chatRoomMems,
			Set chatPerLogsForReceiverid, Set chatRooms,
			Set chatPerLogsForSenderid) {
		this.username = username;
		this.password = password;
		this.telephone = telephone;
		this.age = age;
		this.sex = sex;
		this.online = online;
		this.blacklist = blacklist;
		this.chatRoomLogTemps = chatRoomLogTemps;
		this.chatPerLogTempsForReceiverid = chatPerLogTempsForReceiverid;
		this.chatPerLogTempsForSenderid = chatPerLogTempsForSenderid;
		this.friendsForUserid = friendsForUserid;
		this.friendsForFriendid = friendsForFriendid;
		this.chatRoomLogs = chatRoomLogs;
		this.chatRoomMems = chatRoomMems;
		this.chatPerLogsForReceiverid = chatPerLogsForReceiverid;
		this.chatRooms = chatRooms;
		this.chatPerLogsForSenderid = chatPerLogsForSenderid;
	}

	// Property accessors

	public Integer getUserid() {
		return this.userid;
	}

	public void setUserid(Integer userid) {
		this.userid = userid;
	}

	public String getUsername() {
		return this.username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return this.password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getTelephone() {
		return this.telephone;
	}

	public void setTelephone(String telephone) {
		this.telephone = telephone;
	}

	public Integer getAge() {
		return this.age;
	}

	public void setAge(Integer age) {
		this.age = age;
	}

	public boolean getSex() {
		return this.sex;
	}

	public void setSex(boolean sex) {
		this.sex = sex;
	}

	public boolean getOnline() {
		return this.online;
	}

	public void setOnline(boolean online) {
		this.online = online;
	}

	public boolean getBlacklist() {
		return this.blacklist;
	}

	public void setBlacklist(boolean blacklist) {
		this.blacklist = blacklist;
	}

	public Set getChatRoomLogTemps() {
		return this.chatRoomLogTemps;
	}

	public void setChatRoomLogTemps(Set chatRoomLogTemps) {
		this.chatRoomLogTemps = chatRoomLogTemps;
	}

	public Set getChatPerLogTempsForReceiverid() {
		return this.chatPerLogTempsForReceiverid;
	}

	public void setChatPerLogTempsForReceiverid(Set chatPerLogTempsForReceiverid) {
		this.chatPerLogTempsForReceiverid = chatPerLogTempsForReceiverid;
	}

	public Set getChatPerLogTempsForSenderid() {
		return this.chatPerLogTempsForSenderid;
	}

	public void setChatPerLogTempsForSenderid(Set chatPerLogTempsForSenderid) {
		this.chatPerLogTempsForSenderid = chatPerLogTempsForSenderid;
	}

	public Set getFriendsForUserid() {
		return this.friendsForUserid;
	}

	public void setFriendsForUserid(Set friendsForUserid) {
		this.friendsForUserid = friendsForUserid;
	}

	public Set getFriendsForFriendid() {
		return this.friendsForFriendid;
	}

	public void setFriendsForFriendid(Set friendsForFriendid) {
		this.friendsForFriendid = friendsForFriendid;
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

	public Set getChatPerLogsForReceiverid() {
		return this.chatPerLogsForReceiverid;
	}

	public void setChatPerLogsForReceiverid(Set chatPerLogsForReceiverid) {
		this.chatPerLogsForReceiverid = chatPerLogsForReceiverid;
	}

	public Set getChatRooms() {
		return this.chatRooms;
	}

	public void setChatRooms(Set chatRooms) {
		this.chatRooms = chatRooms;
	}

	public Set getChatPerLogsForSenderid() {
		return this.chatPerLogsForSenderid;
	}

	public void setChatPerLogsForSenderid(Set chatPerLogsForSenderid) {
		this.chatPerLogsForSenderid = chatPerLogsForSenderid;
	}

}