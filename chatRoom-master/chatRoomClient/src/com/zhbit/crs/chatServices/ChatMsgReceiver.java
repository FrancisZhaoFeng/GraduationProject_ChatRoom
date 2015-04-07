/**
 * the registered BroadcastReceiver to which the ClientListener send arrived message
 * the only thing it did here is to unwrap the String contained in Intent and send it to
 * Chatting Service by calling its newMsgArrive function
 */

package com.zhbit.crs.chatServices;

import java.io.Serializable;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.zhbit.crs.commons.GlobalMsgTypes;
import com.zhbit.crs.domain.ChatPerLog;
import com.zhbit.crs.domain.User;

public class ChatMsgReceiver extends BroadcastReceiver {

	private ChatService mParService;

	public ChatMsgReceiver(ChatService mParService0) {
		mParService = mParService0;
	}

	@Override
	public void onReceive(Context context, Intent intent) {
		int msgType = intent.getIntExtra("zhbit.example.hello.msg_type", 0);
		// String msgStr = intent.getStringExtra("zhbit.example.hello.msg_received");
		Serializable temp = intent.getSerializableExtra("zhbit.example.hello.msg_received");

		switch (msgType) {
		case GlobalMsgTypes.msgPublicRoom:
		case GlobalMsgTypes.msgChattingRoom:
		case GlobalMsgTypes.msgFromFriend:
			mParService.newMsgArrive((ChatPerLog) temp, false);
			break;
		case GlobalMsgTypes.msgUpdateFriendList:
			// FriendListInfo.getFriendListInfo().updateFriendList(msgStr);
			break;
		case GlobalMsgTypes.msgFriendGoOnline:
			FriendListInfo.getFriendListInfo().friendGoOnAndOffline((User) temp, true);
			break;
		case GlobalMsgTypes.msgFriendGoOffline:
			FriendListInfo.getFriendListInfo().friendGoOnAndOffline((User) temp, false);
			break;
		default:
			break;
		}
	}

	/*
	 * PublicActivity par;
	 * 
	 * public PublicMsgReceiver(PublicActivity par0) { par=par0; }
	 * 
	 * @Override public void onReceive(Context context, Intent intent) { String msgStr=intent.getStringExtra
	 * ("zhbit.example.hello.msg_received");
	 * 
	 * ChatEntity msgEntity=ChatEntity.Str2Ent(msgStr); par.msgs.add(msgEntity); par.Content.setAdapter(new
	 * PublicAdapter(par,par,par.msgs)); par.Content.setSelection(par.msgs.size()-1); }
	 */

	/*
	 * public void sendMyMessage(String st0) { String div=ChatEntity.strSplitter; ChatEntity
	 * ent0=ChatEntity.Str2Ent(par.type+div+0+div+ 0+div+par.Name+div+ par.Sex+div+ChatEntity.genDate()+div+ st0+div+0);
	 * par.netCon.sendUpload(par.type, ent0);
	 * 
	 * st0=ent0.toString(); Intent intent=new Intent("zhbit.example.hello.MESSAGE_RECEIVED"); intent.putExtra
	 * ("zhbit.example.hello.msg_received", st0); par.sendBroadcast(intent); }
	 */
}