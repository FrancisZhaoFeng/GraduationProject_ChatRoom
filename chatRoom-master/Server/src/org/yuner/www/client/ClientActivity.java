/**
 * this package is only responsible for listening for and sending data packages, as for how to process
 * these data packages, we don't care at all
 * 
 * every user will have one socket, each of which corresponds to one ClientActivity
 */

package org.yuner.www.client;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.util.*;

import org.yuner.www.ServerListen;
import org.yuner.www.ClientMap;

import org.yuner.www.commons.*;
import org.yuner.www.beans.*;
import org.yuner.www.database.DBUtil;
import org.yuner.www.database.DBTempSaveUtil;

public class ClientActivity {

	private Socket mSocket;
	private BufferedReader mBuffRder;
	private BufferedWriter mBuffWter;
	private ServerListen mServerListen;
	
	private HandShakeThread mHandShake;
	private ClientListenThread mClientListen;
	private ClientSendThread mClientSend;
	
	private UserInfo mUsrInfo;

	private boolean mConnectSuccessfully;

	private boolean mIsClosingAndSaving;
	
	/**
	 * public constructor, major tasks here include : 
	 * 1 : initialize the BufferedReader and OutputStreamWriter
	 * 2 : read in the user information and close this connection if input format is not correct
	 */
	public ClientActivity(Socket socket, ServerListen serverListen)
	{
		this.mSocket=socket;
		mConnectSuccessfully = true;
		mIsClosingAndSaving = false;
		try{
			mBuffRder = new BufferedReader(new InputStreamReader(mSocket.getInputStream() ) );
			mBuffWter = new BufferedWriter(new OutputStreamWriter(	mSocket.getOutputStream() ));
			mServerListen = serverListen;
			
			mClientListen = new ClientListenThread(this,mBuffRder);
			mClientListen.start();
			mClientSend = new ClientSendThread(this, mBuffWter);
			mClientSend.start();
		}catch(Exception e){
			System.out.println("error occurs for creating");
			e.printStackTrace();
		}
	}

	public void trySignup(String msg0) {
		String[] strArr0 = msg0.split(GlobalStrings.signupDivider);

		String userStr = strArr0[0];
		String password = strArr0[1];

		UserInfo uu0 = new UserInfo(userStr);

		UserInfo uux = DBUtil.signUp(uu0, password);

		sendOneString(uux.toString(), GlobalMsgTypes.msgSignUp);
	}

	public void backOnline(String str0) {
		mUsrInfo = new UserInfo(str0);

		ClientActivity preceder = mServerListen.getClientActivityById(mUsrInfo.getId());
		if(preceder != null) {
			preceder.goOffLine();
		}

		DBUtil.setOnAndOffLine(getUserInfo().getId(), 1);
		mServerListen.updateFriendList(this);

		// send online notification to all friends
		ArrayList<UserInfo> onlineList = DBUtil.getOnlineFriendList(getUserInfo().getId());
		for(UserInfo uup : onlineList) {
			ClientActivity target0 = ClientMap.getInstance().getById(uup.getId());
			if(target0 != null) {
				target0.sendOneString(getUserInfo().toString(),GlobalMsgTypes.msgFriendGoOnline);
			}
		}

		onAskForUnsendMsgs();
	}

	public void startHandShake(String msg0) {
		mHandShake = new HandShakeThread();
		mUsrInfo = mHandShake.start(msg0);
		if(mUsrInfo != null) {
			mHandShake.sendHandShakeBack(this, mUsrInfo);
			if(mUsrInfo.getId() >= 0) {
				mHandShake.sendFriendList(this, DBUtil.loginToGetFriendList(mUsrInfo.getId()));
			} else {
				unableToConnect();
				return;
			}
		} else {
			unableToConnect();
			return;
		}

		ClientActivity preceder = mServerListen.getClientActivityById(mUsrInfo.getId());
		if(preceder != null) {
			preceder.goOffLine();
		}

		DBUtil.setOnAndOffLine(getUserInfo().getId(), 1);
		mServerListen.updateFriendList(this);

		// send online notification to all friends
		ArrayList<UserInfo> onlineList = DBUtil.getOnlineFriendList(getUserInfo().getId());
		for(UserInfo uup : onlineList) {
			ClientActivity target0 = ClientMap.getInstance().getById(uup.getId());
			if(target0 != null) {
				target0.sendOneString(getUserInfo().toString(),GlobalMsgTypes.msgFriendGoOnline);
			}
		}
	}

	public void startSearchPeople(String msg0) {
		SearchEntity s_ent0 = new SearchEntity(msg0);
		ArrayList<UserInfo> listU;
		if(s_ent0.getType() == SearchEntity.SEARCH_BY_NAME) {
			listU = DBUtil.searchForPeopleWithName(s_ent0);
		} else {
			listU = DBUtil.searchForPeopleList(s_ent0);
		}
		if(listU == null) {
			listU = new ArrayList<UserInfo>();
		}

		String toSend = listU.size() + GlobalStrings.searchListDivider;
		for(UserInfo uu : listU) {
			toSend += uu.toString() + GlobalStrings.searchListDivider;
		}
		sendOneString(toSend, GlobalMsgTypes.msgSearchPeople);
	}
	
	public UserInfo getUserInfo()
	{
		return mUsrInfo;
	}

	public boolean isConnectedSuccessfully() {
		return mConnectSuccessfully;
	}

	public void onAskForUnsendMsgs() {
		System.out.println("start to send all unsends");

		// for chatMsgs
		ArrayList<ChatEntity> listOfChatMsg = DBTempSaveUtil.getUnsentChatMsg(mUsrInfo.getId());
		for(ChatEntity ent0 : listOfChatMsg) {
			sendOneData(ent0, 2);
		}

		// for frdReqs
		ArrayList<String> listOfFrdReqs = DBTempSaveUtil.getUnsentFrdReqs(mUsrInfo.getId());
		for(String str0 : listOfFrdReqs) {
			sendOneString(str0,GlobalMsgTypes.msgFriendshipRequest);
		}

		// for frdReqResponses
		ArrayList<String> listOfFrdReqResp = DBTempSaveUtil.getUnsentFrdReqResponse(mUsrInfo.getId());
		for(String str0 : listOfFrdReqResp) {
			sendOneString(str0, GlobalMsgTypes.msgFriendshipRequestResponse);
		}
	}
	
	/**  notify a new message has come  */
	public void receivedNewMsg(int type,String msg)
	{
		ChatEntity ent0 = ChatEntity.Str2Ent(msg);
		if(type == 2) {
			int recvId = ent0.getReceiverId();
			ClientActivity ca0 = mServerListen.getClientActivityById(recvId);
			if(ca0 != null) {
				ca0.sendOneData(ent0, 2);
			} else {
				DBTempSaveUtil.saveUnsentChatMsg(mUsrInfo.getId(), recvId, ent0);
			}
		}
	}
	
	/* send one String */
	public void sendOneString(String str0, int type) {
		try {
			mClientSend.insert(type, str0);
		} catch(Exception e) { e.printStackTrace(); }
	}

	/**  send one ChatEntity to this client  */
	public void sendOneData(ChatEntity entPackage, int sendType)
	{
		System.out.println("Receiver is "+getUserInfo().getName());
		
		String toSend;

		toSend = entPackage.toString();
		sendOneString(toSend, sendType);
	}
	
	public void unableToConnect()
	{
		mConnectSuccessfully = false;
		closeConnect();
	}

	public boolean isOneIdOnline(int id) {
		return ClientMap.getInstance().containsOneId(id);
	}

	/* friendship request and response are always integer(response) + userinfo(requester) + userinfo(requestee) */
	public void startFriendshipRequest(String msg0) {
		System.out.println("start the request processing");

		String[] strArr0 = msg0.split(GlobalStrings.friendshipRequestDivider);
		UserInfo requester = new UserInfo(strArr0[1]);
		UserInfo uu0 = new UserInfo(strArr0[2]);

		if(isOneIdOnline(uu0.getId())) {
			ClientActivity target0 = ClientMap.getInstance().getById(uu0.getId());
			target0.sendOneString(msg0,GlobalMsgTypes.msgFriendshipRequest);
		} else {
			DBTempSaveUtil.saveUnsentFrdReqs(requester.getId(), uu0.getId(), msg0);
		}
	}

	public void onFriendshipRequestResponse(String msg0) {
		String[] strArr0 = msg0.split(GlobalStrings.friendshipRequestDivider);
		int response0 = Integer.parseInt(strArr0[0]);
		UserInfo requester = new UserInfo(strArr0[1]);
		UserInfo requestee = new UserInfo(strArr0[2]);

		if(response0 == GlobalInts.idAcceptFriendship) {
			DBUtil.makeFriends(requester.getId(), requestee.getId());
		}

		ClientActivity ca0 = ClientMap.getInstance().getById(requester.getId());
		if(ca0 != null) {
			ca0.sendOneString(msg0, GlobalMsgTypes.msgFriendshipRequestResponse);
		} else {
			DBTempSaveUtil.saveUnsentFrdReqResponse(requester.getId(), requestee.getId(), msg0);
		}
	}

	public void onUpdateUserInfo(String msg0) {
		UserInfo uu0 = new UserInfo(msg0);
		UserInfo uux = DBUtil.updateUserInfomaton(uu0);
	}

	public void responsedOfMsgReceived() {
		try {
			mClientSend.setIsReceived();
		} catch(Exception e) { e.printStackTrace(); }
	}

	public void goOffLine() {
		if(mIsClosingAndSaving) {
			return;
		}
		mIsClosingAndSaving = true;

		closeConnect();
		mServerListen.removeOneClient(this);

		ArrayList<UserInfo> onlineList = DBUtil.getOnlineFriendList(getUserInfo().getId());

		for(UserInfo uup : onlineList) {
			ClientActivity target0 = ClientMap.getInstance().getById(uup.getId());
			if(target0 != null) {
				target0.sendOneString(getUserInfo().toString(),GlobalMsgTypes.msgFriendGoOffline);
			}
		}

		mClientSend.saveUnsends();

		mIsClosingAndSaving = false;
	}
	
	private void closeConnect()
	{
		DBUtil.setOnAndOffLine(getUserInfo().getId(), 0);
		try {
			mSocket.close();
		} catch (Exception e) {}
		try {
			mClientListen.closeBufferedReader();
		} catch (Exception e) {}
	}
}
