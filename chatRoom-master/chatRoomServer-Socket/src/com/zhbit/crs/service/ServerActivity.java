/**
 * this package is only responsible for listening for and sending data packages, as for how to process
 * these data packages, we don't care at all
 * 
 * every user will have one socket, each of which corresponds to one ClientActivity
 */

package com.zhbit.crs.service;

import java.io.BufferedInputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;

import com.zhbit.crs.action.ClientMap;
import com.zhbit.crs.action.ServerListen;
import com.zhbit.crs.commons.GlobalInts;
import com.zhbit.crs.commons.GlobalMsgTypes;
import com.zhbit.crs.commons.GlobalStrings;
import com.zhbit.crs.dao.ChatLogDao;
import com.zhbit.crs.dao.UserDao;
import com.zhbit.crs.domain.ChatPerLog;
import com.zhbit.crs.domain.Friend;
import com.zhbit.crs.domain.ZSearchEntity;
import com.zhbit.crs.domain.User;
import com.zhbit.crs.domain.ZUserInfo;
import com.zhbit.crs.md5.MD5;

/**
 * @author zhaoguofeng public constructor, major tasks here include : 1 : initialize the BufferedReader and OutputStreamWriter 2 : read in the user information and close this
 *         connection if input format is not correct
 */
public class ServerActivity {

	private Socket mSocket;
	private ObjectInputStream mBuffRder;
	private ObjectOutputStream mBuffWter;
	private ServerListen mServerListen;

	private HandShakeThread mHandShake;
	private ServerListenThread mClientListen;
	private ServerSendThread mClientSend;

	// private UserInfo mUsrInfo;

	private boolean mConnectSuccessfully;

	private boolean mIsClosingAndSaving;
	private UserDao userDao = new UserDao();
	private ChatLogDao chatLogDao = new ChatLogDao();
	private User user;

	/**
	 * public constructor, major tasks here include : 1 : initialize the BufferedReader and OutputStreamWriter 2 : read in the user information and close this connection if
	 * input format is not correct
	 */
	public ServerActivity(Socket socket, ServerListen serverListen) {
		this.mSocket = socket;
		mConnectSuccessfully = true;
		mIsClosingAndSaving = false;
		try {
			// mBuffRder = new ObjectInputStream(new BufferedInputStream(mSocket.getInputStream()));
			// mBuffWter = new ObjectOutputStream(mSocket.getOutputStream());
			mServerListen = serverListen;

			// mClientListen = new ServerListenThread(this, mBuffRder);
			mClientListen = new ServerListenThread(this, socket);
			mClientListen.start();
			// mClientSend = new ServerSendThread(this, mBuffWter);
			mClientSend = new ServerSendThread(this, socket);
			mClientSend.start();
		} catch (Exception e) {
			System.out.println("error occurs for creating");
			e.printStackTrace();
		}
	}

	public void trySignup(User user) {
		if (!userDao.signUp(user)) {
			user.setUserid(-10); // 与前端约定注册失败信息
		}
		sendOneObject(user, GlobalMsgTypes.msgSignUp);
	}

	public void backOnline(User userTemp) {
		this.user = userTemp;
		ServerActivity preceder = mServerListen.getClientActivityById(user.getUserid());
		if (preceder != null) {
			preceder.goOffLine();
		}
		userTemp.setOnline(true);
		userDao.updateUser(userTemp);
		mServerListen.updateFriendList(this);

		///send online notification to all friends
//		ArrayList<User> onlineList = DBUtil.getOnlineFriendList(getUserInfo().getId());
//		for (User uup : onlineList) {
//			ServerActivity target = ClientMap.getInstance().getById(uup.getUserid());
//			if (target != null) {
//				target.sendOneObject(getUserInfo(), GlobalMsgTypes.msgFriendGoOnline);
//			}
//		}

//		onAskForUnsendMsgs();
	}

	@SuppressWarnings("null")
	public void startHandShake(User userTemp) { // String msg0
		mHandShake = new HandShakeThread();
		this.user = mHandShake.start(userTemp);
		mHandShake.sendHandShakeBack(this, user);
		if (user != null) {
			List<Friend> friends = userDao.selectFriend(user);
			// mHandShake.sendFriendList(this,DBUtil.loginToGetFriendList(mUsrInfo.getId()));
			List<User> users = null;
			for (int i = 0; i < friends.size(); i++) {
//				users.add(userDao.selectUser(friends.get(i).getUserByFriendid()).get(0));
				users.add(friends.get(i).getUserByFriendid());
			}
			mHandShake.sendFriendList(this, users);
		} else {
			unableToConnect();
			return;
		}

		ServerActivity preceder = mServerListen.getClientActivityById(user.getUserid());
		if (preceder != null) {
			preceder.goOffLine();
		}
		mServerListen.updateFriendList(this);
	}

	public void startSearchPeople(ZSearchEntity searchEntity) {// String msg0
		List<User> users = null;
		users = userDao.searchUser(searchEntity);
		sendOneObject(users, GlobalMsgTypes.msgSearchPeople);
	}

	public User getUser() {
		return user;
	}

	public boolean isConnectedSuccessfully() {
		return mConnectSuccessfully;
	}

	/**
	 * 发送没有发送的消息给好友
	 */
	public void onAskForUnsendMsgs() {
		System.out.println("start to send all unsends");
		List<ChatPerLog> listOfChatPerLog = chatLogDao.selectByReceiveId(user.getUserid());
		for (ChatPerLog ent : listOfChatPerLog) {
			sendOneData(ent, 2);
		}
	}

	/** notify a new message has come */
	public void receivedNewMsg(int type, ChatPerLog chatPerLog) {
		// ChatEntity ent0 = ChatEntity.Str2Ent(msg);

		if (type == 2) {
			// int recvId = ent0.getReceiverId();
			int recvId = chatPerLog.getUserByReceiverid().getUserid();
			System.out.println("receivedNewMsg-");
			ServerActivity ca0 = mServerListen.getClientActivityById(recvId);
			if (ca0 != null) {
				ca0.sendOneData(chatPerLog, 2);
			} else {
				// DBTempSaveUtil.saveUnsentChatMsg(mUsrInfo.getId(), recvId,
				// ent0);
				chatLogDao.insertChatPerLog(chatPerLog);
			}
		}
	}

	/* send one String */
	public void sendOneObject(Object obj, int type) {
		try {
			mClientSend.insert(type, obj);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/** send one ChatEntity to this client */
	public void sendOneData(ChatPerLog chatPerLog, int sendType) {// ChatEntity entPackage
		System.out.println("Receiver is " + getUser().getUsername());
		sendOneObject(chatPerLog, sendType);
	}

	public void unableToConnect() {
		mConnectSuccessfully = false;
		closeConnect();
	}

	public boolean isOneIdOnline(int id) {
		return ClientMap.getInstance().containsOneId(id);
	}

	/*
	 * friendship request and response are always integer(response) + userinfo(requester) + userinfo(requestee)
	 */
	public void startFriendshipRequest(Friend friend) { // String msg0
		System.out.println("start the request processing");

		// String[] strArr0 = msg0.split(GlobalStrings.friendshipRequestDivider);
		User requester = friend.getUserByUserid();
		User requestee = friend.getUserByFriendid();

		if (isOneIdOnline(requestee.getUserid())) {
			ServerActivity target = ClientMap.getInstance().getById(requestee.getUserid());
			target.sendOneObject(friend, GlobalMsgTypes.msgFriendshipRequest);
		} else {
			// DBTempSaveUtil.saveUnsentFrdReqs(requester.getId(), requestee.getId(),msg0);
		}
	}

	public void onFriendshipRequestResponse(Friend friendReq) {
//		String[] strArr0 = msg0.split(GlobalStrings.friendshipRequestDivider);
//		int response0 = Integer.parseInt(strArr0[0]);
//		ZUserInfo requester = new ZUserInfo(strArr0[1]);
//		ZUserInfo requestee = new ZUserInfo(strArr0[2]);

		if (friendReq.getNote().equals(GlobalInts.idAcceptFriendshipStr)) {   //处理数据库表
			// DBUtil.makeFriends(requester.getId(), requestee.getId());
			userDao.insertFriend(friendReq);
			Friend friend = friendReq;
			friend.setUserByFriendid(friendReq.getUserByUserid());
			friend.setUserByUserid(friendReq.getUserByFriendid());
			userDao.insertFriend(friend);
		}

		ServerActivity ca = ClientMap.getInstance().getById(friendReq.getUserByUserid().getUserid());
		if (ca != null) {
			ca.sendOneObject(friendReq, GlobalMsgTypes.msgFriendshipRequestResponse);
		} else {  //不在线，需要处理数据库表
			// DBTempSaveUtil.saveUnsentFrdReqResponse(requester.getId(), requestee.getId(), msg0);
		}
	}

	public void onUpdateUser(User user) {// String msg0
		// UserInfo uu0 = new UserInfo(msg0);
		// UserInfo uux = DBUtil.updateUserInfomaton(uu0);
		userDao.updateUser(user);
	}

	public void responsedOfMsgReceived() {
		try {
			mClientSend.setIsReceived();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void goOffLine() {
		if (mIsClosingAndSaving) {
			return;
		}
		mIsClosingAndSaving = true;

		closeConnect();
		mServerListen.removeOneClient(this);

		user.setOnline(false);
		userDao.updateUser(user);

		// List<User> onlineList = DBUtil.getOnlineFriendList(getUserInfo().getId());
		// for (UserInfo uup : onlineList) {
		// ServerActivity target0 = ClientMap.getInstance().getById(uup.getId());
		// if (target0 != null) {
		// target0.sendOneString(getUserInfo().toString(), GlobalMsgTypes.msgFriendGoOffline);
		// }
		// }
		// mClientSend.saveUnsends();
		// mIsClosingAndSaving = false;
	}

	private void closeConnect() {
		// DBUtil.setOnAndOffLine(getUserInfo().getId(), 0);
		try {
			mSocket.close();
		} catch (Exception e) {
		}
		try {
			mClientListen.closeBufferedReader();
		} catch (Exception e) {
		}
	}
}
