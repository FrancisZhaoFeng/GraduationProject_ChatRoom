package com.zhbit.crs.service;

import java.util.List;

import com.zhbit.crs.commons.GlobalMsgTypes;
import com.zhbit.crs.dao.UserDao;
import com.zhbit.crs.domain.User;

/**
 * thread used to conduct handshake with client
 */
public class HandShakeThread {
	private UserDao userDao;
	boolean legitimateHandShaker = true;

	public User start(User user) { // String msg0
		try {
			userDao = new UserDao();
			List<User> users = userDao.login(user);
			User usert = null;
			if (users.size() != 0) {
				usert = users.get(0);
				usert.setOnline(true);
				userDao.updateUser(usert);
			}
			// userInfo.setIsOnline(1);
			return usert;
		} catch (Exception e) {
			e.printStackTrace();
			legitimateHandShaker = false;
			return null;
		}
	}

	/**
	 * send a handShake message back to the client
	 */
	public boolean sendHandShakeBack(ServerActivity cliActv, User user) {
		try {
			cliActv.sendOneObject(user, 3); // usrInfo.msgType 的值为 3
		} catch (Exception e) {
			return false;
		}

		return true;
	}

	public void sendFriendList(ServerActivity cliActv, List<User> users) {// ArrayList<UserInfo>
																			// userV
	// String type56 = GlobalStrings.friendListDivider;
	// int n = users.size();
	// String str = n + type56;
	// for (UserInfo uu0 : userV) {
	// str += uu0.toString() + type56;
	// }
		// str += UserInfo.strSplitter;
		try {
			// cliActv.sendOneString(str,
			// GlobalMsgTypes.msgHandSHakeFriendList);
			cliActv.sendOneObject(users, GlobalMsgTypes.msgHandSHakeFriendList);
		} catch (Exception e) {
		}
	}
}
