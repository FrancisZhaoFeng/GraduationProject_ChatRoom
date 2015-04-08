package com.zhbit.crs.domain;

/**
  *	the wrapper for a message stored in the ClientSendThread stack
  */

public class ZSendStackItem {

	private int mType;
//	private String mStr;
	private Object mObj;

	public ZSendStackItem(int type, Object obj) {
		mType = type;
//		mStr = str;
		mObj = obj;
	}

	public int getmType() {
		return mType;
	}

	public void setmType(int mType) {
		this.mType = mType;
	}

	public Object getmObj() {
		return mObj;
	}

	public void setmObj(Object mObj) {
		this.mObj = mObj;
	}
}
