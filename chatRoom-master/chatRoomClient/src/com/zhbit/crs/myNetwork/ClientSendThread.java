package com.zhbit.crs.myNetwork;

import java.io.ObjectOutputStream;

import android.util.Log;

/**
 * @author zhaoguofeng 客户端发送消息到服务端，任何发送到服务端的信息都经过此类的start函数
 */
public class ClientSendThread {

	private ObjectOutputStream os;
	
	public ClientSendThread(ObjectOutputStream os){
		this.os = os;
	}
	
	public synchronized void start(Object type, Object obj) { // 声明synchronized，原因：只有一个线程可以使用此方法
		try {
			os.writeObject(type);
			os.flush();
			os.writeObject(obj);
			os.flush();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
