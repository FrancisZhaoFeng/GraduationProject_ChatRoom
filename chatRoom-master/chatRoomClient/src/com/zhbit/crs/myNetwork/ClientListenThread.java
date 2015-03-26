package com.zhbit.crs.myNetwork;

import java.io.BufferedInputStream;
import java.io.ObjectInputStream;
import java.net.Socket;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.zhbit.crs.action.RegisterActivity;
import com.zhbit.crs.chatServices.InitData;
import com.zhbit.crs.commons.GlobalMsgTypes;
import com.zhbit.crs.domain.ChatPerLog;
import com.zhbit.crs.domain.User;
import com.zhbit.crs.mainBody.MainBodyActivity;

/**
 * @author zhaoguofeng
 * 客户端监听线程，用于监听服务端传输到客户端的信息，并根据信息的类型（GlobalMsgTypes）作不同处理
 *
 */
public class ClientListenThread extends Thread {
	private Context mContext0;
	private Socket mSocket0;

//	private InputStreamReader mInStrRder0;
//	private BufferedReader mBuffRder0;
	private ObjectInputStream is= null;
	private User user;
	private List<User> users;

	public ClientListenThread(Context par, Socket s) {
		this.mContext0 = par;
		this.mSocket0 = s;
	}

	/* (non-Javadoc)
	 * @see java.lang.Thread#run()
	 */
	public void run() {
		super.run();
		try {
//			mInStrRder0 = new InputStreamReader(mSocket0.getInputStream());
//			mBuffRder0 = new BufferedReader(mInStrRder0);
			is = new ObjectInputStream(new BufferedInputStream(mSocket0.getInputStream()));
			String resp = null;

			while (true) {
				if ((resp = (String)is.readObject()) != null) {   //mBuffRder0.readLine()
					int msgType = Integer.parseInt(resp); // type of message received
//					String actualMsg = mBuffRder0.readLine();
//					actualMsg = actualMsg.replace(GlobalStrings.replaceOfReturn, "\n");
					Object obj = is.readObject();
					switch (msgType) {
					case GlobalMsgTypes.msgPublicRoom:
						/* falls through */
					case GlobalMsgTypes.msgChattingRoom:
						/* falls through */
					case GlobalMsgTypes.msgFromFriend:
						/*
						 * try here to secure for the possible message with first input character as "\n"
						 */
						Log.w("GlobalMsgTypes.msgFromFriend", ""+GlobalMsgTypes.msgFromFriend);
						uponReceivedMsg();
						Log.w("message received +++","+++++++++++++++++++");
//						ChatEntity entTemp = new ChatEntity(actualMsg);
						ChatPerLog chatPerLog = (ChatPerLog)obj;
						Intent intent = new Intent("yuner.example.hello.MESSAGE_RECEIVED");
						intent.putExtra("yuner.example.hello.msg_received", chatPerLog.toString());
						intent.putExtra("yuner.example.hello.msg_type", msgType);
						mContext0.sendBroadcast(intent);
						break;
					case GlobalMsgTypes.msgHandShake:
						Log.w("GlobalMsgTypes.msgHandShake", ""+GlobalMsgTypes.msgHandShake);
						try {
//							UserInfo usrInfo = new UserInfo(actualMsg);
							user = (User)obj;
//							InitData.getInitData().msg3Arrive(usrInfo.toString());
							InitData.getInitData().msg3Arrive(user);
						} catch (Exception e) {
							e.printStackTrace();
						}
						break;
					case GlobalMsgTypes.msgHandSHakeFriendList:
						Log.w("GlobalMsgTypes.msgHandSHakeFriendList", ""+GlobalMsgTypes.msgHandSHakeFriendList);
						try {
							users = (List<User>)obj;
							InitData.getInitData().msg5Arrive(users);
						} catch (Exception e) {
							e.printStackTrace();
						}
						break;
					case GlobalMsgTypes.msgUpdateFriendList:
						Log.w("GlobalMsgTypes.msgUpdateFriendList", ""+GlobalMsgTypes.msgUpdateFriendList);
						user = (User)obj;
						Intent intentp = new Intent("yuner.example.hello.MESSAGE_RECEIVED");
						intentp.putExtra("yuner.example.hello.msg_received", user);
						intentp.putExtra("yuner.example.hello.msg_type", msgType);
						mContext0.sendBroadcast(intentp);
						break;
					case GlobalMsgTypes.msgSignUp:
						Log.w("GlobalMsgTypes.msgSignUp", ""+GlobalMsgTypes.msgSignUp);
						user = (User)obj;
						RegisterActivity.getInstance().uponRegister(user);
						break;
					case GlobalMsgTypes.msgSearchPeople:
						Log.w("GlobalMsgTypes.msgSearchPeople", ""+GlobalMsgTypes.msgSearchPeople);
						users = (List<User>)obj;
						MainBodyActivity.getInstance().onReceiveSearchList(users);
						break;
					case GlobalMsgTypes.msgFriendshipRequest:
						Log.w("GlobalMsgTypes.msgFriendshipRequest", ""+GlobalMsgTypes.msgFriendshipRequest);
						user = (User)obj;
						uponReceivedMsg();
						Intent intent2 = new Intent("yuner.example.hello.FRIEND_REQUEST_MSGS");
						intent2.putExtra("yuner.example.hello.msg_received", user);
						intent2.putExtra("yuner.example.hello.msg_type", msgType);
						mContext0.sendBroadcast(intent2);
						break;
					case GlobalMsgTypes.msgFriendshipRequestResponse: 
						Log.w("GlobalMsgTypes.msgFriendshipRequestResponse", ""+GlobalMsgTypes.msgFriendshipRequestResponse);
						User user = (User)obj;
						uponReceivedMsg();
						Log.d("response comes", "+++" + "++++++++++++++++++");
						Intent intent3 = new Intent("yuner.example.hello.FRIEND_REQUEST_MSGS");
						intent3.putExtra("yuner.example.hello.msg_received", user);
						intent3.putExtra("yuner.example.hello.msg_type", msgType);
						mContext0.sendBroadcast(intent3);
						break;
					case GlobalMsgTypes.msgFriendGoOnline:
						// FriendListInfo.getFriendListInfo().friendGoOnAndOffline(actualMsg,
						// 1);
						Log.w("GlobalMsgTypes.msgFriendGoOnline", ""+GlobalMsgTypes.msgFriendGoOnline);
						user = (User)obj;
						Intent intent6 = new Intent("yuner.example.hello.MESSAGE_RECEIVED");
						intent6.putExtra("yuner.example.hello.msg_received", user);
						intent6.putExtra("yuner.example.hello.msg_type", msgType);
						mContext0.sendBroadcast(intent6);
						break;
					case GlobalMsgTypes.msgFriendGoOffline:
						Log.w("GlobalMsgTypes.msgFriendGoOffline", ""+GlobalMsgTypes.msgFriendGoOffline);
						user = (User)obj;
						Intent intent7 = new Intent("yuner.example.hello.MESSAGE_RECEIVED");
						intent7.putExtra("yuner.example.hello.msg_received", user);
						intent7.putExtra("yuner.example.hello.msg_type", msgType);
						mContext0.sendBroadcast(intent7);
						break;
					default:
						break;
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 告知服务器，客户端接收者，已经接受此信息
	 */
	public void uponReceivedMsg() {  
		NetworkService.getInstance().sendUpload(GlobalMsgTypes.msgMsgReceived, "xxxxx");
	}

	/**
	 * 关闭读入流
	 */
	public void closeBufferedReader() {
		try {
			is = null;
		} catch (Exception e) {
		}
	}
}
