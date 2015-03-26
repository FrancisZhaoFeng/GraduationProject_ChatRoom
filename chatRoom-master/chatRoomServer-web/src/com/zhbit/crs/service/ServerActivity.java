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

import com.zhbit.crs.action.ClientMap;
import com.zhbit.crs.action.ServerListen;
import com.zhbit.crs.commons.GlobalInts;
import com.zhbit.crs.commons.GlobalMsgTypes;
import com.zhbit.crs.commons.GlobalStrings;
import com.zhbit.crs.dao.ChatLogDao;
import com.zhbit.crs.dao.UserDao;
import com.zhbit.crs.domain.ChatPerLog;
import com.zhbit.crs.domain.Friend;
import com.zhbit.crs.domain.SearchEntity;
import com.zhbit.crs.domain.User;
import com.zhbit.crs.domain.UserInfo;

/**
* @author zhaoguofeng
* public constructor, major tasks here include : 
* 1 : initialize the BufferedReader and OutputStreamWriter 
* 2 : read in the user information and close this connection if input format is not correct
*/
public class ServerActivity {

	private Socket mSocket;
	private ObjectInputStream mBuffRder;
	private ObjectOutputStream mBuffWter;
	private ServerListen mServerListen;

	private HandShakeThread mHandShake;
	private ServerListenThread mClientListen;
	private ServerSendThread mClientSend;

	private UserInfo mUsrInfo;

	private boolean mConnectSuccessfully;

	private boolean mIsClosingAndSaving;
	@Resource
	private UserDao userDao;
	@Resource
	private ChatLogDao chatLogDao;
	private User user;

	/**
	 * public constructor, major tasks here include : 
	 * 1 : initialize the BufferedReader and OutputStreamWriter 
	 * 2 : read in the user information and close this connection if input format is not correct
	 */
	public ServerActivity(Socket socket, ServerListen serverListen) {
		this.mSocket = socket;
		mConnectSuccessfully = true;
		mIsClosingAndSaving = false;
		try {
			mBuffRder = new ObjectInputStream(new BufferedInputStream(mSocket.getInputStream()));
			mBuffWter = new ObjectOutputStream(mSocket.getOutputStream());
			mServerListen = serverListen;

			mClientListen = new ServerListenThread(this, mBuffRder);
			mClientListen.start();
			mClientSend = new ServerSendThread(this, mBuffWter);
			mClientSend.start();
		} catch (Exception e) {
			System.out.println("error occurs for creating");
			e.printStackTrace();
		}
	}

	public void trySignup(User user) {
//		String[] strArr0 = msg0.split(GlobalStrings.signupDivider);
//
//		String userStr = strArr0[0];
//		String password = strArr0[1];
//
//		UserInfo uu0 = new UserInfo(userStr);
//
//		UserInfo uux = DBUtil.signUp(uu0, password);
		userDao.signUp(user);

		sendOneString(user, GlobalMsgTypes.msgSignUp);
	}

	public void backOnline(String str0) {
		mUsrInfo = new UserInfo(str0);

		ServerActivity preceder = mServerListen.getClientActivityById(mUsrInfo.getId());
		if (preceder != null) {
			preceder.goOffLine();
		}

//		DBUtil.setOnAndOffLine(getUserInfo().getId(), 1);
		mServerListen.updateFriendList(this);

		// send online notification to all friends
//		ArrayList<UserInfo> onlineList = DBUtil.getOnlineFriendList(getUserInfo().getId());
//		for (UserInfo uup : onlineList) {
//			ServerActivity target0 = ClientMap.getInstance().getById(uup.getId());
//			if (target0 != null) {
//				target0.sendOneString(getUserInfo().toString(), GlobalMsgTypes.msgFriendGoOnline);
//			}
//		}
//
//		onAskForUnsendMsgs();
	}

	public void startHandShake(User user) {  //String msg0
		mHandShake = new HandShakeThread();
//		mUsrInfo = mHandShake.start(msg0);
		this.user = mHandShake.start(user);
		if (user != null) {
			mHandShake.sendHandShakeBack(this, user);
			if (user.getUserid() >= 0 ) {  //mUsrInfo.getId() >= 0
				List<Friend> friends = userDao.selectFriend(user);
//				mHandShake.sendFriendList(this, DBUtil.loginToGetFriendList(mUsrInfo.getId()));
				List <User> users = null;
				for(int i = 0 ; i < friends.size();i++){
					users.add(userDao.selectUser(friends.get(i).getUserByFriendid()).get(0));
				}
				mHandShake.sendFriendList(this, users);
			} else {
				unableToConnect();
				return;
			}
		} else {
			unableToConnect();
			return;
		}

		ServerActivity preceder = mServerListen.getClientActivityById(mUsrInfo.getId());
		if (preceder != null) {
			preceder.goOffLine();
		}

//		DBUtil.setOnAndOffLine(getUserInfo().getId(), 1);
//		mServerListen.updateFriendList(this);

		// send online notification to all friends
//		ArrayList<UserInfo> onlineList = DBUtil.getOnlineFriendList(getUserInfo().getId());
//		for (UserInfo uup : onlineList) {
//			ServerActivity target0 = ClientMap.getInstance().getById(uup.getId());
//			if (target0 != null) {
//				target0.sendOneString(getUserInfo().toString(), GlobalMsgTypes.msgFriendGoOnline);
//			}
//		}
	}

	public void startSearchPeople(SearchEntity searchEntity) {//String msg0
//		SearchEntity s_ent0 = new SearchEntity(msg0);
		List<User> users = null;
		if (searchEntity.getType() == SearchEntity.SEARCH_BY_NAME) {
//			listU = DBUtil.searchForPeopleWithName(s_ent0);
			users = userDao.searchUserByName(searchEntity);
		} else {
//			listU = DBUtil.searchForPeopleList(s_ent0);
		}
		if (users == null) {
			users = new ArrayList<User>();
		}

//		String toSend = user.size() + GlobalStrings.searchListDivider;
//		for (UserInfo uu : listU) {
//			toSend += uu.toString() + GlobalStrings.searchListDivider;
//		}
		sendOneString(users, GlobalMsgTypes.msgSearchPeople);
	}

	public UserInfo getUserInfo() {
		return mUsrInfo;
	}

	public boolean isConnectedSuccessfully() {
		return mConnectSuccessfully;
	}

	public void onAskForUnsendMsgs() {
		System.out.println("start to send all unsends");

		// for chatMsgs
//		ArrayList<ChatPerLog> listOfChatPerLog = DBTempSaveUtil.getUnsentChatMsg(mUsrInfo.getId());
		List<ChatPerLog> listOfChatPerLog =  chatLogDao.selectByReceiveId(mUsrInfo.getId());
		for (ChatPerLog ent0 : listOfChatPerLog) {
			sendOneData(ent0, 2);
		}

		// for frdReqs
//		ArrayList<String> listOfFrdReqs = DBTempSaveUtil.getUnsentFrdReqs(mUsrInfo.getId());
//		for (String str0 : listOfFrdReqs) {
//			sendOneString(str0, GlobalMsgTypes.msgFriendshipRequest);
//		}

		// for frdReqResponses
//		ArrayList<String> listOfFrdReqResp = DBTempSaveUtil.getUnsentFrdReqResponse(mUsrInfo.getId());
//		for (String str0 : listOfFrdReqResp) {
//			sendOneString(str0, GlobalMsgTypes.msgFriendshipRequestResponse);
//		}
	}

	/** notify a new message has come */
	public void receivedNewMsg(int type, ChatPerLog chatPerLog) {
//		ChatEntity ent0 = ChatEntity.Str2Ent(msg);
		if (type == 2) {
//			int recvId = ent0.getReceiverId();
			int recvId = chatPerLog.getUserByReceiverid().getUserid();
			System.out.println("receivedNewMsg-");
			ServerActivity ca0 = mServerListen.getClientActivityById(recvId);
			if (ca0 != null) {
				ca0.sendOneData(chatPerLog, 2);
			} else {
//				DBTempSaveUtil.saveUnsentChatMsg(mUsrInfo.getId(), recvId, ent0);
				new ChatLogDao().insertChatPerLog(chatPerLog);
			}
		}
	}

	/* send one String */
	public void sendOneString(Object obj, int type) {
		try {
			mClientSend.insert(type, obj);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/** send one ChatEntity to this client */
	public void sendOneData(ChatPerLog chatPerLog, int sendType) {//ChatEntity entPackage
		System.out.println("Receiver is " + getUserInfo().getName());

//		String toSend;
//
//		toSend = entPackage.toString();
//		sendOneString(toSend, sendType);
		sendOneString(chatPerLog, sendType);
	}

	public void unableToConnect() {
		mConnectSuccessfully = false;
		closeConnect();
	}

	public boolean isOneIdOnline(int id) {
		return ClientMap.getInstance().containsOneId(id);
	}

	/*
	 * friendship request and response are always integer(response) +
	 * userinfo(requester) + userinfo(requestee)
	 */
	public void startFriendshipRequest(String msg0) {
		System.out.println("start the request processing");

		String[] strArr0 = msg0.split(GlobalStrings.friendshipRequestDivider);
		UserInfo requester = new UserInfo(strArr0[1]);
		UserInfo uu0 = new UserInfo(strArr0[2]);

		if (isOneIdOnline(uu0.getId())) {
			ServerActivity target0 = ClientMap.getInstance().getById(uu0.getId());
			target0.sendOneString(msg0, GlobalMsgTypes.msgFriendshipRequest);
		} else {
//			DBTempSaveUtil.saveUnsentFrdReqs(requester.getId(), uu0.getId(), msg0);
		}
	}

	public void onFriendshipRequestResponse(String msg0) {
		String[] strArr0 = msg0.split(GlobalStrings.friendshipRequestDivider);
		int response0 = Integer.parseInt(strArr0[0]);
		UserInfo requester = new UserInfo(strArr0[1]);
		UserInfo requestee = new UserInfo(strArr0[2]);

		if (response0 == GlobalInts.idAcceptFriendship) {
//			DBUtil.makeFriends(requester.getId(), requestee.getId());
		}

		ServerActivity ca0 = ClientMap.getInstance().getById(requester.getId());
		if (ca0 != null) {
			ca0.sendOneString(msg0, GlobalMsgTypes.msgFriendshipRequestResponse);
		} else {
//			DBTempSaveUtil.saveUnsentFrdReqResponse(requester.getId(), requestee.getId(), msg0);
		}
	}

	public void onUpdateUserInfo(User user) {//String msg0
//		UserInfo uu0 = new UserInfo(msg0);
//		UserInfo uux = DBUtil.updateUserInfomaton(uu0);
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

//		List<User> onlineList = DBUtil.getOnlineFriendList(getUserInfo().getId());
//
//		for (UserInfo uup : onlineList) {
//			ServerActivity target0 = ClientMap.getInstance().getById(uup.getId());
//			if (target0 != null) {
//				target0.sendOneString(getUserInfo().toString(), GlobalMsgTypes.msgFriendGoOffline);
//			}
//		}
//
//		mClientSend.saveUnsends();
//
//		mIsClosingAndSaving = false;
	}

	private void closeConnect() {
//		DBUtil.setOnAndOffLine(getUserInfo().getId(), 0);
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
