/**
 * this package is only responsible for listening for and sending data packages, as for how to process
 * these data packages, we don't care at all
 * 
 * every user will have one socket, each of which corresponds to one ClientActivity
 */

package com.zhbit.crs.service;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

import com.zhbit.crs.action.ClientMap;
import com.zhbit.crs.action.ServerListen;
import com.zhbit.crs.commons.GlobalMsgTypes;
import com.zhbit.crs.dao.ChatLogDao;
import com.zhbit.crs.dao.UserDao;
import com.zhbit.crs.domain.ChatPerLog;
import com.zhbit.crs.domain.ChatPerLogTemp;
import com.zhbit.crs.domain.Friend;
import com.zhbit.crs.domain.FriendId;
import com.zhbit.crs.domain.User;
import com.zhbit.crs.domain.ZSearchEntity;
import com.zhbit.crs.tools.tools;

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

		// /send online notification to all friends
		// ArrayList<User> onlineList = DBUtil.getOnlineFriendList(getUserInfo().getId());
		// for (User uup : onlineList) {
		// ServerActivity target = ClientMap.getInstance().getById(uup.getUserid());
		// if (target != null) {
		// target.sendOneObject(getUserInfo(), GlobalMsgTypes.msgFriendGoOnline);
		// }
		// }

		// onAskForUnsendMsgs();
	}

	public void startHandShake(User userTemp) { // String msg0
		mHandShake = new HandShakeThread();
		this.user = mHandShake.start(userTemp);
		mHandShake.sendHandShakeBack(this, user);
		if (user != null) {
			List<Friend> friends = userDao.selectFriend(user);
			List<User> users = new ArrayList<User>();
			for (int i = 0; i < friends.size(); i++) {
				users.add(friends.get(i).getId().getFriendid());
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
		List<ChatPerLogTemp> listOfChatPerLogTemp = chatLogDao.selectByReceiveId(user.getUserid());
		ChatPerLog chatPerLog = null;
		for (ChatPerLogTemp chatPerLogTemp : listOfChatPerLogTemp) {
			chatPerLog = new ChatPerLog(chatPerLogTemp);
			if (chatPerLog.getType() == GlobalMsgTypes.msgFriendshipRequest) {
				Friend friend = new Friend(new FriendId(chatPerLog.getUserBySenderid(), chatPerLog.getUserByReceiverid()), chatPerLog.getSendtime(), chatPerLog.getSendtext());
				sendOneObject(friend, GlobalMsgTypes.msgFriendshipRequest);// 好友请求
			} else if (chatPerLog.getType() == GlobalMsgTypes.msgFriendshipRequestResponse) {
				Friend friend = new Friend(new FriendId(chatPerLog.getUserByReceiverid(), chatPerLog.getUserBySenderid()), chatPerLog.getSendtime(), chatPerLog.getSendtext());
				sendOneObject(friend, GlobalMsgTypes.msgFriendshipRequest);// 好友请求回应
			} else {
				sendOneObject(chatPerLog, GlobalMsgTypes.msgFromFriend);// 聊天记录
			}
			chatLogDao.deleteChatPerLogTemp(chatPerLogTemp);
		}
	}

	/** notify a new message has come */
	public void receivedNewMsg(int type, ChatPerLog chatPerLog) {
		// ChatEntity ent0 = ChatEntity.Str2Ent(msg);

		if (type == GlobalMsgTypes.msgFromFriend) {
			// int recvId = ent0.getReceiverId();
			int recvId = chatPerLog.getUserByReceiverid().getUserid();
			System.out.println("receivedNewMsg-");
			ServerActivity ca = mServerListen.getClientActivityById(recvId);
			if (ca != null) {
				chatLogDao.insertChatPerLog(chatPerLog);
				ca.sendOneData(chatPerLog, GlobalMsgTypes.msgFromFriend);
			} else {
				// DBTempSaveUtil.saveUnsentChatMsg(mUsrInfo.getId(), recvId, ent0);
				chatLogDao.insertChatPerLog(chatPerLog);
				ChatPerLogTemp chatPerLogTemp = new ChatPerLogTemp(chatPerLog);
				chatLogDao.insertChatPerLogTemp(chatPerLogTemp);
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
		User requester = friend.getId().getUserid();
		User requestee = friend.getId().getFriendid();

		if (isOneIdOnline(requestee.getUserid())) {
			ServerActivity target = ClientMap.getInstance().getById(requestee.getUserid());
			target.sendOneObject(friend, GlobalMsgTypes.msgFriendshipRequest);
		} else {
			ChatPerLog chatPerLog = new ChatPerLog(requester, requestee, tools.getDate(), "", GlobalMsgTypes.msgFriendshipRequest);
			chatLogDao.insertChatPerLog(chatPerLog);
			// 插入到临时表中
			ChatPerLogTemp chatPerLogTemp = new ChatPerLogTemp(requester, requestee, tools.getDate(), "", GlobalMsgTypes.msgFriendshipRequest);
			chatLogDao.insertChatPerLogTemp(chatPerLogTemp);
		}
	}

	public void onFriendshipRequestResponse(Friend friendReq) {
		User requester = friendReq.getId().getUserid();
		User requestee = friendReq.getId().getFriendid();

		if (friendReq.getState()) { // 处理数据库表
			userDao.insertFriend(friendReq);
			Friend friend = new Friend(new FriendId(requestee, requester), friendReq.getFriendtime(), friendReq.getNote());
			userDao.insertFriend(friend);
		}

		ServerActivity ca = ClientMap.getInstance().getById(friendReq.getId().getUserid().getUserid());
		if (ca != null) {
			ca.sendOneObject(friendReq, GlobalMsgTypes.msgFriendshipRequestResponse);
		} else { // 不在线，需要处理数据库表
			ChatPerLog chatPerLog = new ChatPerLog(requestee, requester, tools.getDate(), friendReq.getNote(), GlobalMsgTypes.msgFriendshipRequestResponse);
			chatLogDao.insertChatPerLog(chatPerLog);
			// 插入到临时表中
			ChatPerLogTemp chatPerLogTemp = new ChatPerLogTemp(requestee, requester, tools.getDate(), friendReq.getNote(), GlobalMsgTypes.msgFriendshipRequestResponse);
			chatLogDao.insertChatPerLogTemp(chatPerLogTemp);
		}
	}

	public void onUpdateUser(User userTemp) {// String msg0
		user = userTemp;
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
