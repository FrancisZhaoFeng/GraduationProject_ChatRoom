package com.zhbit.crs.service;

import java.io.BufferedReader;
import java.lang.String;

import com.zhbit.crs.commons.*;

/**
 * @author zhaoguofeng 监听线程类，用于监听客户端发送到服务端的信息
 */
public class ServerListenThread extends Thread {

	private ServerActivity mServerActivity;
	private BufferedReader mBuffRder;

	public ServerListenThread(ServerActivity ca0, BufferedReader br0) {
		mServerActivity = ca0;
		mBuffRder = br0;
	}

	@Override
	public void run() {
		super.run();
		while (true) {
			try {
				String received = mBuffRder.readLine();
				if (received == null) { // if received == null, meaning the
										// socket has been closed
										// remove the class/threads etc.
										// associated with this
										// specific client
					mServerActivity.goOffLine();
					return;
				} else {
					int msgType = Integer.parseInt(received); // type of message
																// received

					String actualMsg = mBuffRder.readLine();
					actualMsg = actualMsg.replace(GlobalStrings.replaceOfReturn, "\n");

					try {
						System.out.println("a message with type " + msgType + " received from " + mServerActivity.getUserInfo().getName());
					} catch (Exception e) {
					}

					switch (msgType) {
					case GlobalMsgTypes.msgPublicRoom:
						ChatEntity msg = ChatEntity.Str2Ent(actualMsg);
						mServerActivity.receivedNewMsg(msgType, msg.toString());
						// add this ChatEntity data package into the system
						// stack
						break;
					case GlobalMsgTypes.msgChattingRoom:
						/* to be added later */
						break;
					case GlobalMsgTypes.msgFromFriend:
						ChatEntity msg2 = ChatEntity.Str2Ent(actualMsg);
						mServerActivity.receivedNewMsg(msgType, msg2.toString());
						/* to be added later */
						break;
					case GlobalMsgTypes.msgHandShake:
						mServerActivity.startHandShake(actualMsg);
						break;
					case GlobalMsgTypes.msgSignUp:
						mServerActivity.trySignup(actualMsg);
						break;
					case GlobalMsgTypes.msgSearchPeople:
						mServerActivity.startSearchPeople(actualMsg);
						break;
					case GlobalMsgTypes.msgFriendshipRequest:
						System.out.println("one friend request comes");
						mServerActivity.startFriendshipRequest(actualMsg);
						break;
					case GlobalMsgTypes.msgFriendshipRequestResponse:
						mServerActivity.onFriendshipRequestResponse(actualMsg);
						break;
					case GlobalMsgTypes.msgUpdateUserInfo:
						mServerActivity.onUpdateUserInfo(actualMsg);
						break;
					case GlobalMsgTypes.msgAskForUnsendMsgs:
						mServerActivity.onAskForUnsendMsgs();
						break;
					case GlobalMsgTypes.msgMsgReceived:
						mServerActivity.responsedOfMsgReceived();
						break;
					case GlobalMsgTypes.msgBackOnline:
						mServerActivity.backOnline(actualMsg);
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
