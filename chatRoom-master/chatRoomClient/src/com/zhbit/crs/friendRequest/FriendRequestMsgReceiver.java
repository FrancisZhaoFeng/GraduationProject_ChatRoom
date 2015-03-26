package com.zhbit.crs.friendRequest;

import java.util.List;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.zhbit.crs.commons.GlobalMsgTypes;
import com.zhbit.crs.domain.User;

public class FriendRequestMsgReceiver extends BroadcastReceiver{

	@Override
	public void onReceive(Context context, Intent intent)
	{
		Log.d("receiver received","++++++++++++++++++++++++++++" +
				"++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++");
		
		int msgType = intent.getIntExtra("yuner.example.hello.msg_type",0);
//		String msgStr = intent.getStringExtra("yuner.example.hello.msg_received");
		List<User> users = (List<User>)intent.getSerializableExtra("yuner.example.hello.msg_received");
		
		switch(msgType) {
		case GlobalMsgTypes.msgFriendshipRequest:
			FriendRequestService.getInstance().processFriendRequest(users);
			break;
		case GlobalMsgTypes.msgFriendshipRequestResponse:
			FriendRequestService.getInstance().processFriendRequestResponse(users);
			break;
		default:
			break;
		}
	}
	
}
