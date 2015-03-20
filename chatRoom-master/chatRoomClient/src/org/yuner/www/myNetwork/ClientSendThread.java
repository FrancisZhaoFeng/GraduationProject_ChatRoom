package org.yuner.www.myNetwork;

import java.io.OutputStreamWriter;
import java.net.Socket;

import android.util.Log;

/**
 * @author zhaoguofeng
 * 客户端发送消息到服务端，任何发送到服务端的信息都经过此类的start函数
 */
public class ClientSendThread{   

	private Socket mSocket;
	private String mStrToSend;
	
	public synchronized void start(Socket socket, String str0) {  // 声明synchronized，原因：只有一个线程可以使用此方法
		this.mSocket = socket;
		this.mStrToSend = str0;   //发送到服务端的内容
		
		try {
			OutputStreamWriter ost0 = new OutputStreamWriter(mSocket.getOutputStream());  //新建一个输出流
			ost0.write(mStrToSend+"\n");
			ost0.flush();
			Log.d("mStrToSend = " +mStrToSend, "+++" + "++++++++++++");
		} catch(Exception e){
			Log.d("cannot send to ","+++++++");
		}
	}
}
