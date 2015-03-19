package org.yuner.www.friendRequest;

import org.yuner.www.commons.GlobalMsgTypes;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class FriendRequestMsgReceiver extends BroadcastReceiver{

	@Override
	public void onReceive(Context context, Intent intent)
	{
		Log.d("receiver received","++++++++++++++++++++++++++++" +
				"++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++");
		
		int msgType = intent.getIntExtra("yuner.example.hello.msg_type",0);
		String msgStr = intent.getStringExtra("yuner.example.hello.msg_received");
		
		switch(msgType) {
		case GlobalMsgTypes.msgFriendshipRequest:
			FriendRequestService.getInstance().processFriendRequest(msgStr);
			break;
		case GlobalMsgTypes.msgFriendshipRequestResponse:
			FriendRequestService.getInstance().processFriendRequestResponse(msgStr);
			break;
		default:
			break;
		}
	}
	
}
