package org.yuner.www.beans;

/**
  *	the wrapper for a message stored in the ClientSendThread stack
  */

public class SendStackItem {

	private int mType;
	private String mStr;

	public SendStackItem(int type, String str) {
		mType = type;
		mStr = str;
	}

	public int getType() {
		return mType;
	}

	public String getStr() {
		return mStr;
	}

}
