package org.yuner.www.myNetwork;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.Socket;

import org.yuner.www.RegisterActivity;
import org.yuner.www.bean.ChatEntity;
import org.yuner.www.bean.UserInfo;
import org.yuner.www.chatServices.FriendListInfo;
import org.yuner.www.chatServices.InitData;
import org.yuner.www.commons.GlobalMsgTypes;
import org.yuner.www.commons.GlobalStrings;
import org.yuner.www.friendRequest.FriendSearchResultActivity;
import org.yuner.www.mainBody.MainBodyActivity;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

public class ClientListenThread extends Thread{
	private Context mContext0;
	private Socket mSocket0;
	
	private InputStreamReader mInStrRder0;
	private BufferedReader mBuffRder0;
	
	public ClientListenThread(Context par,Socket s)
	{
		this.mContext0=par;
		this.mSocket0=s;
	}

	public void run()
	{
		super.run();
		try{
			mInStrRder0=new InputStreamReader(mSocket0.getInputStream());
			mBuffRder0=new BufferedReader(mInStrRder0);
			String resp=null;
			
			while (true) {
				if ((resp = mBuffRder0.readLine()) != null) {
					int msgType = Integer.parseInt(resp);			// type of message received
					
					String actualMsg = mBuffRder0.readLine();
					actualMsg = actualMsg.replace(GlobalStrings.replaceOfReturn, "\n");
							
					switch (msgType) {
					case GlobalMsgTypes.msgPublicRoom:
						/* falls through */
					case GlobalMsgTypes.msgChattingRoom:
						/* falls through */
					case GlobalMsgTypes.msgFromFriend:	
						/* try here to secure for the possible message with first input character as 
						   "\n"	 */
						uponReceivedMsg();
						Log.d("message received" + actualMsg,"++++++++++++++++++++" +
								"+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++");
						
							ChatEntity entTemp = new ChatEntity(actualMsg);
							Intent intent = new Intent("yuner.example.hello.MESSAGE_RECEIVED");
							intent.putExtra("yuner.example.hello.msg_received", entTemp.toString());
							intent.putExtra("yuner.example.hello.msg_type", msgType);
							mContext0.sendBroadcast(intent);
						break;
					case GlobalMsgTypes.msgHandShake:
						try {
							UserInfo usrInfo = new UserInfo(actualMsg);								
							InitData.getInitData().msg3Arrive(usrInfo.toString());
						} catch(Exception e) { e.printStackTrace(); }
						break;
					case GlobalMsgTypes.msgHandSHakeFriendList:
						try {
							InitData.getInitData().msg5Arrive(actualMsg);
						} catch(Exception e) { e.printStackTrace(); }
						break;
					case GlobalMsgTypes.msgUpdateFriendList:		
						Intent intentp = new Intent("yuner.example.hello.MESSAGE_RECEIVED");
						intentp.putExtra("yuner.example.hello.msg_received", actualMsg);
						intentp.putExtra("yuner.example.hello.msg_type", msgType);
						mContext0.sendBroadcast(intentp);
						break;
					case GlobalMsgTypes.msgSignUp:
						RegisterActivity.getInstance().uponRegister(actualMsg);
						break;
					case GlobalMsgTypes.msgSearchPeople:
						MainBodyActivity.getInstance().onReceiveSearchList(actualMsg);
						break;
					case GlobalMsgTypes.msgFriendshipRequest:
						uponReceivedMsg();
						Intent intent2 = new Intent("yuner.example.hello.FRIEND_REQUEST_MSGS");
						intent2.putExtra("yuner.example.hello.msg_received", actualMsg);
						intent2.putExtra("yuner.example.hello.msg_type", msgType);
						mContext0.sendBroadcast(intent2);
						break;
					case GlobalMsgTypes.msgFriendshipRequestResponse:
						uponReceivedMsg();
						Log.d("response comes","+++++++++++++++++++++++++++++" +
								"++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++");
						
						Intent intent3 = new Intent("yuner.example.hello.FRIEND_REQUEST_MSGS");
						intent3.putExtra("yuner.example.hello.msg_received", actualMsg);
						intent3.putExtra("yuner.example.hello.msg_type", msgType);
						mContext0.sendBroadcast(intent3);
						break;
					case GlobalMsgTypes.msgFriendGoOnline:
					//	FriendListInfo.getFriendListInfo().friendGoOnAndOffline(actualMsg, 1);
						Intent intent6 = new Intent("yuner.example.hello.MESSAGE_RECEIVED");
						intent6.putExtra("yuner.example.hello.msg_received", actualMsg);
						intent6.putExtra("yuner.example.hello.msg_type", msgType);
						mContext0.sendBroadcast(intent6);
						break;
					case GlobalMsgTypes.msgFriendGoOffline:
						Intent intent7 = new Intent("yuner.example.hello.MESSAGE_RECEIVED");
						intent7.putExtra("yuner.example.hello.msg_received", actualMsg);
						intent7.putExtra("yuner.example.hello.msg_type", msgType);
						mContext0.sendBroadcast(intent7);
						break;
					default:
						break;
					}
				}
			}
		}catch(Exception e){}
	}
	
	public void uponReceivedMsg() {
		NetworkService.getInstance().sendUpload(GlobalMsgTypes.msgMsgReceived, "xxxxx");
	}
	
	public void closeBufferedReader()
	{
		try {
			mBuffRder0 = null;
		} catch(Exception e) {}
	}
}
