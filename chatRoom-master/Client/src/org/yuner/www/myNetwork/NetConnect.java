/**
 * myNetwork block provides interface for basic network operations,
 * --> NetConnect provides interface to establish a socket connect to a server
 * --> ClientListen establish a watcher to listen to this socket
 * --> one thing to notice, only one socket is established to our server
 * 
 *  InetAddress ia0=InetAddress.getByName("javalobby.org");
 *  InetSocketAddress ist0=new InetSocketAddress(HostIp,HostPort);
 *  just an example to tell you how to use domain name
 */

package org.yuner.www.myNetwork;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;

import org.yuner.www.bean.ChatEntity;
import org.yuner.www.bean.UserInfo;
import org.yuner.www.commons.GlobalMsgTypes;
import org.yuner.www.commons.GlobalStrings;

import android.content.Context;
import android.os.Parcel;
import android.os.Parcelable;

public class NetConnect extends Thread{
	
	/*  information about Server, ip address and portal number  */
//	private String mHostIp="54.251.178.242";
	private String mHostIp="172.16.15.101";

	private int mHostPort=8888;
	
	/*  socket  */
	private Socket mSocket0=null;
	
	private boolean connectedAlready=false;
	
	/*  singleton retrieval  
	public static NetConnect getnetConnect() {
		if(netConnect == null) {
			netConnect = new NetConnect();
		}
		return netConnect;
	}
	*/
	
	public NetConnect()
	{
		
	}
	
	public void run()
	{
		try {
			sleep(100); // to wait for the connection to be stable
		} catch(Exception e) {}
		try{		
			// initialization and establish connection
			InetAddress ia0=InetAddress.getByName(mHostIp);
			InetSocketAddress ist0=new InetSocketAddress(ia0,mHostPort);
			mSocket0=new Socket();
			mSocket0.connect(ist0,2000);
			
			if(mSocket0.isConnected()) {
				connectedAlready = true;
			} else {
				connectedAlready = false;
			}
		}catch(IOException e){
			System.out.println("error occured");
		}
	}
	
	public Socket getSocket() {
		return mSocket0;
	}
	
	public boolean connectedOrNot()
	{
		return connectedAlready;
	}
	
	/*
	public void startListen(Context context0) {
		mClientListen0 = new ClientListenThread(context0,mSocket0);
		mClientListen0.start();
		
		mClientSend0 = new ClientSendThread();
	}
	
	public void sendUpload(int type, String sentence)
	{
		sendUpload(type + "");
		sendUpload(sentence);
	}
	
	private synchronized void sendUpload(String buff)
	{
		buff = buff.replace("\n", GlobalStrings.replaceOfReturn);
		mClientSend0.start(mSocket0,buff);
	}
	
	
	
	public void closeNetConnect()
	{
		try{
			if(mClientListen0 != null) {
				mClientListen0.closeBufferedReader();
			}
		} catch (Exception e) {}
		try{
			if(mSocket0 != null) {
				mSocket0.close();
			}
		} catch (Exception e) {}
		netConnect=null;
	}
	*/
}