package com.zhbit.crs.domain;

import java.io.Serializable;
import java.util.Date;

import com.zhbit.crs.commons.GlobalStrings;

/**
 * @author zhaoguofeng
 * 消息显示实体类
 */
public class ZdbTabMsgItemEntity implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -7097886725710787357L;

	public static final int FrdReqNotifId = -1;

	/* for friend request processed result */
	public static final int FrdReqUnread = 0;   //好友请求信息  未读
	public static final int FrdReqAccepted = 1; //好友请求信息   接收
	public static final int FrdReqDeclined = 2; //好友请求信息  拒接

	public static final int NotReadYet = 0;    //消息 未读
	public static final int ReadAlready = 1;   //消息 已读

	private int mTalkerId;   //对方的id
	private boolean mImgId;
	private String mName;    //对方的名字
	private String mSentence; //聊天内容
	private Date mTime;      //聊天日期

	private int mIsRead = NotReadYet;
	private int mFrdReqStatus = FrdReqUnread;

	public ZdbTabMsgItemEntity(int talkerId, boolean imgId, String name, String sentence, Date time) {
		mTalkerId = talkerId;
		mImgId = imgId;
		mName = name;
		mSentence = sentence;
		mTime = time;
	}

	public ZdbTabMsgItemEntity(String str0) {
		String[] arr0 = str0.split(GlobalStrings.entityDivider);

		mTalkerId = Integer.parseInt(arr0[0]);
		mImgId = Boolean.parseBoolean(arr0[1]);
		mName = arr0[2];
		mSentence = arr0[3];
		// mTime = String(arr0[4]);
		mIsRead = Integer.parseInt(arr0[5]);
		mFrdReqStatus = Integer.parseInt(arr0[6]);
	}

	public String toString() {
		String out = mTalkerId + GlobalStrings.entityDivider;
		out += mImgId + GlobalStrings.entityDivider;
		out += mName + GlobalStrings.entityDivider;
		out += mSentence + GlobalStrings.entityDivider;
		out += mTime + GlobalStrings.entityDivider;
		out += mIsRead + GlobalStrings.entityDivider;
		out += mFrdReqStatus + GlobalStrings.entityDivider;

		return out;
	}

	public int getTalkerId() {
		return mTalkerId;
	}

	public Boolean getImgId() {
		return mImgId;
	}

	public String getSentence() {
		return mSentence;
	}

	public String getName() {
		return mName;
	}

	public Date getTime() {
		return mTime;
	}

	public int getIsRead() {
		return mIsRead;
	}

	public void setIsRead(int isRead) {
		mIsRead = isRead;
	}

	public int getFrdReqStatus() {
		return mFrdReqStatus;
	}

	public void setFrdReqStatus(int status) {
		mFrdReqStatus = status;
	}

}
