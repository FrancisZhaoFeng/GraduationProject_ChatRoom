package com.zhbit.crs.myNetwork;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import android.content.Context;

/**
 * @author zhaoguofeng 该类为网络服务类，提供： 1.提供网络服务类对象:getInstance; 2.设置连接,新建一个连接服务端的线程类，并启动该类的线程:setupConnection；
 * 
 */
public class NetworkService {

	private static NetworkService mInstance;

	/**
	 * @return 返回网络服务类的实例
	 */
	public static NetworkService getInstance() {
		if (mInstance == null) {
			mInstance = new NetworkService();
		}
		return mInstance;
	}

	/**
	 * 查看是否存在连接服务端的socket对象
	 */
	private boolean mIsConnected;
	private NetConnect mNetCon;
	private ClientListenThread mListenThread; // 声明客户端监听线程
	private ClientSendThread mSendThread; // 声明客户端发送信息到服务端线程
	private Socket mSocket;
	private Context mContext; // 通过 Context才能识别调用者的实例

	/**
	 * here, it should always do nothing except set mIsConnected to false
	 */
	private NetworkService() {
		mIsConnected = false;
	}

	public void onInit(Context context) {
		mContext = context;
	}

	/**
	 * 设置连接： 1.新建一个连接服务端的线程类，并启动该类的线程（join）； 2.获取一个连接到服务端的socket对象； 3.调用startListen(Context context0)，启动监听
	 */
	public void setupConnection() {
		mNetCon = new NetConnect();
		mNetCon.start(); // 启动连接服务端线程
		try {
			mNetCon.join(); // join就是阻塞调用线程，直到该线程结束后，调用线程才能继续执行
							// 如在t1线程中调用t2.join(),则需要t2线程执行完后t1方能继续执行
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (mNetCon == null || !mNetCon.connectedOrNot()) {
			mIsConnected = false;
			return;
		} else {
			mSocket = mNetCon.getSocket();
			mIsConnected = true;
			startListen(mContext);
			if (mSocket != null) {
				System.out.println("socket is not null");
			} else {
				System.out.println("socket is null");
			}
		}
	}

	/**
	 * @param context0
	 *            1.新建一个客户端监听线程并启动； 2.新建一个客户端发送信息类；
	 */
	private void startListen(Context context0) {
		// try {
		// ObjectOutputStream os = new ObjectOutputStream(mSocket.getOutputStream());
		// mSendThread = new ClientSendThread(os);
		// } catch (IOException e) {
		// // TODO Auto-generated catch block
		// e.printStackTrace();
		// }
		mSendThread = new ClientSendThread();
		mListenThread = new ClientListenThread(context0, mSocket);
		mListenThread.start();

	}

	/**
	 * @return 返回当前socket对象是否连接到服务端
	 */
	public boolean getIsConnected() {
		return mIsConnected;
	}

	/**
	 * @param type
	 * @param sentence
	 *            发送信息到服务器，先发送：type，后发送：sentence
	 */
	public void sendUpload(int type, Object obj) {
		sendUpload("" + type, obj);
	}

	/* synchronized so only one send action is happening at a time */
	private synchronized void sendUpload(Object type, Object obj) {
		// buff = buff.replace("\n", GlobalStrings.replaceOfReturn);
		// mSendThread.start(type, obj);
		mSendThread.start(mSocket, type, obj);
	}

	/**
	 * 关闭连接服务端的读入流和socket连接
	 */
	public void closeConnection() {
		try {
			if (mListenThread != null) {
				mListenThread.closeBufferedReader();
			}
		} catch (Exception e) {
		}
		try {
			if (mSocket != null) {
				mSocket.close();
			}
		} catch (Exception e) {
		}
		mSocket = null;
		mIsConnected = false;
	}

}
