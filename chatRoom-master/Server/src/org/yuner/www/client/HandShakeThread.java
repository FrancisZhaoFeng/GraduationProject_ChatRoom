package org.yuner.www.client;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.util.*;

import org.yuner.www.ServerListen;
import org.yuner.www.commons.*;
import org.yuner.www.beans.*;
import org.yuner.www.database.DBUtil;

/**
 * thread used to conduct handshake with client
 */
public class HandShakeThread {
	boolean legitimateHandShaker = true;
	
	public UserInfo start(String msg0)
	{
		try {
			String[] strArr0 = msg0.split(GlobalStrings.signinDivider);
			UserInfo uu = new UserInfo(strArr0[0]);
			String password = strArr0[1];

			// set id
			UserInfo userInfo = DBUtil.loginWithUsername(uu, password);
			userInfo.setIsOnline(1);

			return userInfo;
		} catch (Exception e) {
			legitimateHandShaker = false;
			return null;
		}
	}
	
	/**
	 * send a handShake message back to the client
	 */
	public boolean sendHandShakeBack(ClientActivity cliActv, UserInfo usrInfo)
	{
		try {
			cliActv.sendOneString(usrInfo.toString(), usrInfo.msgType);
		} catch (Exception e) {
			return false;
		}
		
		return true;
	}

	public void sendFriendList(ClientActivity cliActv, ArrayList<UserInfo> userV)
	{
		String type56 = GlobalStrings.friendListDivider;
		int n = userV.size();
		String str = n+type56;
		for(UserInfo uu0 : userV) {
			str += uu0.toString() + type56;
		} 
//		str += UserInfo.strSplitter;
		try {
			cliActv.sendOneString(str, GlobalMsgTypes.msgHandSHakeFriendList);
		} catch (Exception e) {}
	}
}
