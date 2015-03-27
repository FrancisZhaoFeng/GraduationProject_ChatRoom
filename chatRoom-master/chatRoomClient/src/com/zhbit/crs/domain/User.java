package com.zhbit.crs.domain;

import java.util.HashSet;
import java.util.Set;

/**
 * User entity. @author MyEclipse Persistence Tools
 */

public class User implements java.io.Serializable {

	// Fields

	private static final long serialVersionUID = -3286564461647015367L;
	private Integer userid;
	private String username;
	private String password;
	private String telephone;
	private Integer age;
	private boolean sex;
	private boolean online;
	private boolean blacklist;
	// Constructors

	/** default constructor */
	public User() {
	}

	public User(String username, String password) {
		this.username = username;
		this.password = password;
	}

	/** minimal constructor */
	public User(String username, String password, String telephone, Integer age) {
		this.username = username;
		this.password = password;
		this.telephone = telephone;
		this.age = age;
	}

	/** minimal constructor */
	public User(String username, String password, String telephone, Integer age, Boolean sex) {
		this.username = username;
		this.password = password;
		this.telephone = telephone;
		this.age = age;
		this.sex = sex;
	}

	/** full constructor */
	public User(String username, String password, String telephone, Integer age, boolean sex, boolean online, boolean blacklist) {
		this.username = username;
		this.password = password;
		this.telephone = telephone;
		this.age = age;
		this.sex = sex;
		this.online = online;
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
}