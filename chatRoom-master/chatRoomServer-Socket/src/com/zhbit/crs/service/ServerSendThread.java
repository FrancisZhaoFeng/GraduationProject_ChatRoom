package com.zhbit.crs.service;

import java.io.BufferedWriter;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;

import com.zhbit.crs.commons.*;
import com.zhbit.crs.domain.ChatEntity;
import com.zhbit.crs.domain.SendStackItem;
import com.zhbit.crs.domain.User;

public class ServerSendThread extends Thread {

	private ServerActivity mClientActivity;

	// private BufferedWriter mBuffWter;
	private ObjectOutputStream mBuffWter;
	private ArrayList<SendStackItem> mSendList;

	private boolean mIsRunning;
	private boolean mReceived; // the heartbeat message from client verifying they have received the message you send
	
	private Socket mSocket;

	public ServerSendThread(ServerActivity ca0, ObjectOutputStream bufw) {
		mClientActivity = ca0;
		mBuffWter = bufw;
		mSendList = new ArrayList<SendStackItem>();
	}
	
	public ServerSendThread(ServerActivity ca0, Socket socket) {
		mClientActivity = ca0;
		mSocket = socket;
		mSendList = new ArrayList<SendStackItem>();
	}

	public void run() {

		super.run();
		mIsRunning = true;

		while (mIsRunning) {
			if (mSendList.size() == 0) {
				try {
					sleep(60);
				} catch (Exception eer) {
					eer.printStackTrace();
				}
			} else {
				SendStackItem item0 = mSendList.get(0); // get one at the head

				if (item0 != null) {
					send(item0.getmType() + "", item0.getmObj());
				} else {
					System.out.println("error, item0 = " + item0);
					continue;
				}

				mReceived = false;
				while (!mReceived) {
					try {
						sleep(3);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}

				mSendList.remove(0);
			}
		}

	}

	public void close() {
		mIsRunning = false;
	}

	public synchronized void insert(int type, Object obj) {
		switch (type) {
		case GlobalMsgTypes.msgFromFriend:
		case GlobalMsgTypes.msgFriendshipRequest:
		case GlobalMsgTypes.msgFriendshipRequestResponse:
			break;
		default:
			send(type + "", obj);
			return;
		}

		SendStackItem item0 = new SendStackItem(type, obj);
		mSendList.add(item0); // add this item to the end of stack
	}

	private synchronized void send(Object type, Object obj) {
		try {
			mBuffWter = new ObjectOutputStream(mSocket.getOutputStream());
			mBuffWter.writeObject(type);
			mBuffWter.flush();
			mBuffWter.writeObject(obj);
			mBuffWter.flush();
			System.out.println("here is send object function\n");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void setIsReceived() {
		mReceived = true;
	}

	public void saveUnsends() {
		int senderId = GlobalInts.idPlaceholder; // since it's useless, so just a placeholder, we don't care who send the message
		int receiverId = mClientActivity.getUserInfo().getUserid();
		for (SendStackItem item0 : mSendList) {
			// int type = item0.getType();
			// String msg = item0.getStr();
			int type = item0.getmType();
			Object msg = item0.getmObj();
			switch (type) {
			case GlobalMsgTypes.msgFromFriend:
				// DBTempSaveUtil.saveUnsentChatMsg(senderId, receiverId,
				// ChatEntity.Str2Ent(msg));
				break;
			case GlobalMsgTypes.msgFriendshipRequest:
				// DBTempSaveUtil.saveUnsentFrdReqs(senderId, receiverId, msg);
				break;
			case GlobalMsgTypes.msgFriendshipRequestResponse:
				// DBTempSaveUtil.saveUnsentFrdReqResponse(receiverId, senderId,
				// msg);
				break;
			default:
				break;
			}
		}
	}
}
