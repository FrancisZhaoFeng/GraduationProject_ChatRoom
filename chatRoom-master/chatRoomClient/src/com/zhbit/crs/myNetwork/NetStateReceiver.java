package com.zhbit.crs.myNetwork;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

import com.zhbit.crs.action.ConnectedApp;
import com.zhbit.crs.commons.GlobalMsgTypes;
import com.zhbit.crs.domain.User;

/**
 * @author zhaoguofeng
 * 检查客户端与服务端的连接状态，连接：发送msgBackOnline信息到服务端，不在线：关闭socket连接
 *
 */
public class NetStateReceiver extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
		NetworkInfo currentNetworkInfo = (NetworkInfo) intent.getParcelableExtra(ConnectivityManager.EXTRA_NETWORK_INFO);
		if (currentNetworkInfo.isConnected()) {
			if (!NetworkService.getInstance().getIsConnected()) {
				System.out.println("connected again");
				NetworkService.getInstance().setupConnection();
				User user = ConnectedApp.getInstance().getUser();
				NetworkService.getInstance().sendUpload(GlobalMsgTypes.msgBackOnline, user);
			}
		} else {
			Log.w("closeConnection", "关闭连接服务端的读入流和socket连接");
			NetworkService.getInstance().closeConnection();
		}
	}
}