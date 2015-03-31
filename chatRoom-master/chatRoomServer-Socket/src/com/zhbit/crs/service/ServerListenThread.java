package com.zhbit.crs.service;

import java.io.ObjectInputStream;

import com.zhbit.crs.commons.GlobalMsgTypes;
import com.zhbit.crs.domain.ChatPerLog;
import com.zhbit.crs.domain.ChatRoomLog;
import com.zhbit.crs.domain.Friend;
import com.zhbit.crs.domain.SearchEntity;
import com.zhbit.crs.domain.User;

/**
 * @author zhaoguofeng 监听线程类，用于监听客户端发送到服务端的信息
 */
public class ServerListenThread extends Thread {

	private ServerActivity mServerActivity;
	private ObjectInputStream mBuffRder;
	private User user;

	public ServerListenThread(ServerActivity ca0, ObjectInputStream br0) {
		mServerActivity = ca0;
		mBuffRder = br0;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Thread#run()
	 */
	@Override
	public void run() {
		super.run();
		while (true) {
			try {
				Object obj = mBuffRder.readObject();
				System.out.println("mBuffRder.readObject()");
				int msgType = Integer.parseInt((String)obj);
				System.out.println("msgType:"+msgType);
				// if received == null, meaning the socket has been closed
				// remove the class/threads etc.associated with this specific
				// client
				if ( obj == null) {   //received == null 
					System.out.println("obj is null");
					mServerActivity.goOffLine();
					return;
				} else {
					// type of message received
					obj = mBuffRder.readObject();
					try {
						System.out.println("a message with type " + msgType + " received from " + mServerActivity.getUserInfo().getUsername());
					} catch (Exception e) {
					}
					switch (msgType) {
					case GlobalMsgTypes.msgPublicRoom:
						System.out.println("GlobalMsgTypes.msgPublicRoom"+GlobalMsgTypes.msgPublicRoom);
//						ChatEntity msg = ChatEntity.Str2Ent(actualMsg);
						ChatRoomLog chatRoomLog = (ChatRoomLog)obj;
//						mServerActivity.receivedNewMsg(msgType, msg.toString());
//						mServerActivity.receivedNewMsg(msgType, chatRoomLog);
						// add this ChatEntity data package into the system
						// stack
						break;
					case GlobalMsgTypes.msgChattingRoom:
						System.out.println("GlobalMsgTypes.msgChattingRoom"+GlobalMsgTypes.msgChattingRoom);
						/* to be added later */
						break;
					case GlobalMsgTypes.msgFromFriend:
						System.out.println("GlobalMsgTypes.msgFromFriend"+GlobalMsgTypes.msgFromFriend);
						ChatPerLog chatPerLog = (ChatPerLog)obj;
//						ChatEntity msg2 = ChatEntity.Str2Ent(actualMsg);
//						mServerActivity.receivedNewMsg(msgType, msg2.toString());
						mServerActivity.receivedNewMsg(msgType, chatPerLog);
						/* to be added later */
						break;
					case GlobalMsgTypes.msgHandShake:
						System.out.println("GlobalMsgTypes.msgHandShake"+GlobalMsgTypes.msgHandShake);
						mServerActivity.startHandShake((User)obj);
						break;
					case GlobalMsgTypes.msgSignUp:
						System.out.println("GlobalMsgTypes.msgSignUp"+GlobalMsgTypes.msgSignUp);
						mServerActivity.trySignup((User)obj);
						break;
					case GlobalMsgTypes.msgSearchPeople:
						System.out.println("GlobalMsgTypes.msgSearchPeople"+GlobalMsgTypes.msgSearchPeople);
						SearchEntity searchEntity = (SearchEntity)obj;
						mServerActivity.startSearchPeople(searchEntity);
						break;
					case GlobalMsgTypes.msgFriendshipRequest:
						Friend friend = (Friend)obj;
						System.out.println("GlobalMsgTypes.msgFriendshipRequest"+GlobalMsgTypes.msgFriendshipRequest);
						System.out.println("one friend request comes");
//						mServerActivity.startFriendshipRequest(friend);
						break;
					case GlobalMsgTypes.msgFriendshipRequestResponse:
						System.out.println("GlobalMsgTypes.msgFriendshipRequestResponse"+GlobalMsgTypes.msgFriendshipRequestResponse);
//						mServerActivity.onFriendshipRequestResponse(actualMsg);
						break;
					case GlobalMsgTypes.msgUpdateUserInfo:
						System.out.println("GlobalMsgTypes.msgUpdateUserInfo"+GlobalMsgTypes.msgUpdateUserInfo);
//						mServerActivity.onUpdateUserInfo(actualMsg);
						break;
					case GlobalMsgTypes.msgAskForUnsendMsgs:
						System.out.println("GlobalMsgTypes.msgAskForUnsendMsgs"+GlobalMsgTypes.msgAskForUnsendMsgs);
						mServerActivity.onAskForUnsendMsgs();
						break;
					case GlobalMsgTypes.msgMsgReceived:
						System.out.println("GlobalMsgTypes.msgMsgReceived"+GlobalMsgTypes.msgMsgReceived);
						mServerActivity.responsedOfMsgReceived();
						break;
					case GlobalMsgTypes.msgBackOnline:
						System.out.println("GlobalMsgTypes.msgBackOnline"+GlobalMsgTypes.msgBackOnline);
//						mServerActivity.backOnline(actualMsg);
						break;
					default:
						break;
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
				System.out.println("Client Listen Shutting Down!!!");
				// remove the class/threads etc. associated with this specific
				// client
				mServerActivity.goOffLine();
				return;
			}
		}
	}

	public void closeBufferedReader() {
		try {
			mBuffRder.close();
		} catch (Exception e) {
		}
	}
}
