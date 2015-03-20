package org.yuner.www.myNetwork;

import org.yuner.www.ConnectedApp;
import org.yuner.www.bean.UserInfo;
import org.yuner.www.commons.GlobalMsgTypes;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

public class NetStateReceiver extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
		NetworkInfo currentNetworkInfo = (NetworkInfo) intent.getParcelableExtra(ConnectivityManager.EXTRA_NETWORK_INFO);
		if (currentNetworkInfo.isConnected()) {
			if (!NetworkService.getInstance().getIsConnected()) {
				System.out.println("connected again");
				NetworkService.getInstance().setupConnection();
				UserInfo uu0 = ConnectedApp.getInstance().getUserInfo();
				NetworkService.getInstance().sendUpload(GlobalMsgTypes.msgBackOnline, uu0.toString());
			}
		} else {
			Log.w("closeConnection", "关闭连接服务端的读入流和socket连接");
			NetworkService.getInstance().closeConnection();
		}
	}
}