package com.zhbit.crs.service;

import java.io.BufferedWriter;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;

import com.zhbit.crs.commons.*;
import com.zhbit.crs.domain.ZdbChatEntity;
import com.zhbit.crs.domain.ZSendStackItem;
import com.zhbit.crs.domain.User;

public class ServerSendThread extends Thread {

	private ServerActivity mClientActivity;

	// private BufferedWriter mBuffWter;
	private ObjectOutputStream mBuffWter;
	private ArrayList<ZSendStackItem> mSendList;

	private boolean mIsRunning;
	private boolean mReceived; // the heartbeat message from client verifying they have received the message you send
	
	private Socket mSocket;

	public ServerSendThread(ServerActivity ca, ObjectOutputStream bufw) {
		mClientActivity = ca;
		mBuffWter = bufw;
		mSendList = new ArrayList<ZSendStackItem>();
	}
	
	public ServerSendThread(ServerActivity ca, Socket socket) {
		mClientActivity = ca;
		mSocket = socket;
		mSendList = new ArrayList<ZSendStackItem>();
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
				ZSendStackItem item = mSendList.get(0); // get one at the head

				if (item != null) {
					send(item.getmType() + "", item.getmObj());
				} else {
					System.out.println("error, item = " + item);
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

		ZSendStackItem item = new ZSendStackItem(type, obj);
		mSendList.add(item); // add this item to the end of stack
	}

	private synchronized void send(Object type, Object obj) {
		try {
			this.sleep(300);
			mBuffWter = new ObjectOutputStream(mSocket.getOutputStream());
			mBuffWter.writeObject(type);
			mBuffWter.flush();
			mBuffWter.writeObject(obj);
			mBuffWter.flush();
			System.out.println("here is send object function------------："+(String)type);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void setIsReceived() {
		mReceived = true;
	}

	public void saveUnsends() {
		int senderId = GlobalInts.idPlaceholder; // since it's useless, so just a placeholder, we don't care who send the message
		int receiverId = mClientActivity.getUser().getUserid();
		for (ZSendStackItem item : mSendList) {
			// int type = item.getType();
			// String msg = item.getStr();
			int type = item.getmType();
			Object msg = item.getmObj();
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
