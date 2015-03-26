package com.zhbit.crs.action;

import com.zhbit.crs.chatServices.ChatServiceData;
import com.zhbit.crs.chatServices.FriendListInfo;
import com.zhbit.crs.chatServices.InitData;
import com.zhbit.crs.myNetwork.NetworkService;

public class CloseAll {
	
	public static void closeAll() {
		ConnectedApp.getInstance().clearAll();
		
		try {
			NetworkService.getInstance().closeConnection();
		} catch (Exception e) {}
		try {
			InitData.closeInitData();
			FriendListInfo.closeFriendListInfo();
			ChatServiceData.closeChatServiceData();
		} catch (Exception e) {}
	}
	
}
