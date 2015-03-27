package com.zhbit.crs.domain;

import java.util.Date;

import com.zhbit.crs.commons.GlobalStrings;

public class FrdReqNotifItemEntity {

	public static final int mTypeFrdReq = 1;
	public static final int mTypeFrdReqResult = 2;
	
	public static final int mUnanswer = 1;
	public static final int mAccepted = 2;
	public static final int mDeclined = 3;
	
	private int mType;
//	private String mStrOfUser;  // the String of other end user
	private User mStrOfUser;
	
	private int mNotifId;
	private boolean mImgId;
	private String mName;
	private String mContent;
	private Date mTime;
	
	private int mIsRead = 0;
	private int mStatus = mUnanswer;
	
	public FrdReqNotifItemEntity(int type, int id, boolean imgId, String name, String content, Date time, User strOfUser) {
		mType = type;
		mNotifId = id;
		mImgId = imgId;
		mName = name;
		mContent = content;
		mTime = time;
		mStrOfUser = strOfUser;
	}
	
	public FrdReqNotifItemEntity(String in) {
		String[] strArr0 = in.split(GlobalStrings.entityDivider2);
		
		mType = Integer.parseInt(strArr0[0]);
		mNotifId = Integer.parseInt(strArr0[1]);
		mImgId = Boolean.parseBoolean(strArr0[2]);
		mName = strArr0[3];
		mContent = strArr0[4];
//		mTime = strArr0[5];
		mIsRead = Integer.parseInt(strArr0[6]);
		mStatus = Integer.parseInt(strArr0[7]);
	}
	
	public String toString() {
		String out = mType + GlobalStrings.entityDivider2;
		out += mNotifId + GlobalStrings.entityDivider2;
		out += mImgId + GlobalStrings.entityDivider2;
		out += mName + GlobalStrings.entityDivider2;
		out += mContent + GlobalStrings.entityDivider2;
		out += mTime + GlobalStrings.entityDivider2;
		out += mIsRead + GlobalStrings.entityDivider2;
		out += mStatus + GlobalStrings.entityDivider2;
		out += mStrOfUser + GlobalStrings.entityDivider2;
		
		return out;
	}
	
	public int getType() {
		return mType;
	}
	
	public int getNotifId() {
		return mNotifId;
	}
	
	public Boolean getImgId() {
		return mImgId;
	}
	
	public String getName() {
		return mName;
	}
	
	public String getContent() {
		return mContent;
	}
	
	public Date getTime() {
		return mTime;
	}
	
	public User getStrOfUser() {
		return mStrOfUser;
	}
	
	public void setIsRead(int isRead) {
		mIsRead = isRead;
	}
	
	public int getIsRead() {
		return mIsRead;
	}
	
	public void setStatus(int status) {
		mStatus = status;
	}
	
	public int getStatus() {
		return mStatus;
	}
	
}
