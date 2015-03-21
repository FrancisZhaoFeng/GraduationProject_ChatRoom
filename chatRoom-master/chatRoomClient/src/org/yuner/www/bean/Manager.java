package org.yuner.www.bean;

/**
 * Manager entity. @author MyEclipse Persistence Tools
 */

public class Manager implements java.io.Serializable {

	// Fields

	private Integer adminid;
	private String adminname;
	private String password;

	// Constructors

	/** default constructor */
	public Manager() {
	}

	/** full constructor */
	public Manager(String adminname, String password) {
		this.adminname = adminname;
		this.password = password;
	}

	// Property accessors

	public Integer getAdminid() {
		return this.adminid;
	}

	public void setAdminid(Integer adminid) {
		this.adminid = adminid;
	}

	public String getAdminname() {
		return this.adminname;
	}

	public void setAdminname(String adminname) {
		this.adminname = adminname;
	}

	public String getPassword() {
		return this.password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

}