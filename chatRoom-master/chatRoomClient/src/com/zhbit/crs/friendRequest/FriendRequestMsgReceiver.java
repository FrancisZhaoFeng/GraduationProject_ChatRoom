package com.zhbit.crs.friendRequest;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.zhbit.crs.commons.GlobalMsgTypes;
import com.zhbit.crs.domain.Friend;

/**
 * @author zhaoguofeng
 * 继承广播 BroadcastReceiver 当客户端监听线程ClientListenThread，接收到好友请求消息后发出广播，会被此广播接收器接收
 */
public class FriendRequestMsgReceiver extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
		Log.d("receiver received", "++++++++++++++++++++++++++++" + "++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++");

		int msgType = intent.getIntExtra("zhbit.example.hello.msg_type", 0);
		// String msgStr = intent.getStringExtra("zhbit.example.hello.msg_received");
//		List<User> users = (List<User>) intent.getSerializableExtra("zhbit.example.hello.msg_received");
		Friend friend = (Friend)intent.getSerializableExtra("zhbit.example.hello.msg_received");

		switch (msgType) {
		case GlobalMsgTypes.msgFriendshipRequest:
			FriendRequestService.getInstance().processFriendRequest(friend);
			break;
		case GlobalMsgTypes.msgFriendshipRequestResponse:
			FriendRequestService.getInstance().processFriendRequestResponse(friend);
			break;
		default:
			break;
		}
	}

}
