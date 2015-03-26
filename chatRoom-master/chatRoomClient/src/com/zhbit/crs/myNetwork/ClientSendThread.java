package com.zhbit.crs.myNetwork;

import java.io.ObjectOutputStream;
import java.net.Socket;

import com.zhbit.crs.domain.User;

import android.util.Log;

/**
 * @author zhaoguofeng
 * 客户端发送消息到服务端，任何发送到服务端的信息都经过此类的start函数
 */
public class ClientSendThread{   

	private Socket mSocket;
//	private String mStrToSend;
	private Object obj;
	private ObjectOutputStream os = null;
	
	public synchronized void start(Socket socket, Object type,Object obj) {  // 声明synchronized，原因：只有一个线程可以使用此方法
		this.mSocket = socket;
		this.obj = obj;   //发送到服务端的内容
		
		try {
//			OutputStreamWriter ost0 = new OutputStreamWriter(mSocket.getOutputStream());  //新建一个输出流
			os = new ObjectOutputStream(mSocket.getOutputStream());
			os.writeObject(type);
			os.flush();
			os.writeObject(obj);
			os.flush();
			Log.d("mStrToSend = " +obj.toString(), "+++" + "++++++++++++");
		} catch(Exception e){
			Log.d("cannot send to ","+++++++");
		}
	}
}
