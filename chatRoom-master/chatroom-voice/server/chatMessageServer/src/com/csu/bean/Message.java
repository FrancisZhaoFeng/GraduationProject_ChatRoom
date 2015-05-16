package com.csu.bean;

/**
 *消息pojo
 * @author Administrator
 *
 */
public class Message {
	private String send_ctn;
	private String send_person;
	private String send_date;
	private boolean ifyuyin = false; // 是否是语音消息
	private long recordTime; // 语音消息持续的时间
	public Message(String send_ctn, String send_person, String send_date)
			 {
		super();
		this.send_ctn = send_ctn;
		this.send_person = send_person;
		this.send_date = send_date;
	}
	public String getSend_ctn() {
		return send_ctn;
	}
	public void setSend_ctn(String send_ctn) {
		this.send_ctn = send_ctn;
	}
	public String getSend_person() {
		return send_person;
	}
	public void setSend_person(String send_person) {
		this.send_person = send_person;
	}
	public String getSend_date() {
		return send_date;
	}
	public void setSend_date(String send_date) {
		this.send_date = send_date;
	}
	public boolean isIfyuyin() {
		return ifyuyin;
	}
	public void setIfyuyin(boolean ifyuyin) {
		this.ifyuyin = ifyuyin;
	}
	public long getRecordTime() {
		return recordTime;
	}
	public void setRecordTime(long recordTime) {
		this.recordTime = recordTime;
	}
	
}
