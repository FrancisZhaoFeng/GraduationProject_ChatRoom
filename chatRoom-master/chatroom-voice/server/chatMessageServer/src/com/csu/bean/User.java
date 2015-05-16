package com.csu.bean;

/**
 * 用户pojo
 * 
 * @author Administrator
 * 
 */
public class User {
	private long id; // 主键ID
	private String ip; // 连接服务器的ip地址
	private String port; // 连接服务器的port号
	private String name; // 用户登录名
	private String img; // 上传的头像
	private int flag; // 当前用户标识

	public String getIp() {
		return ip;
	}
	public void setIp(String ip) {
		this.ip = ip;
	}
	public String getPort() {
		return port;
	}
	public void setPort(String port) {
		this.port = port;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getImg() {
		return img;
	}
	public void setImg(String img) {
		this.img = img;
	}
	public int getFlag() {
		return flag;
	}
	public void setFlag(int flag) {
		this.flag = flag;
	}
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
}
