package org.yuner.www.bean;

/**
 * Mkey entity. @author MyEclipse Persistence Tools
 */

public class Mkey implements java.io.Serializable {

	// Fields

	private Integer mid;
	private boolean used;

	// Constructors

	/** default constructor */
	public Mkey() {
	}

	/** full constructor */
	public Mkey(boolean used) {
		this.used = used;
	}

	// Property accessors

	public Integer getMid() {
		return this.mid;
	}

	public void setMid(Integer mid) {
		this.mid = mid;
	}

	public boolean getUsed() {
		return this.used;
	}

	public void setUsed(boolean used) {
		this.used = used;
	}

}