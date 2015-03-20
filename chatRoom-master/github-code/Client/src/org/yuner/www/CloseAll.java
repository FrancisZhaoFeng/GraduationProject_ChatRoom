package org.yuner.www;

import org.yuner.www.chatServices.ChatServiceData;
import org.yuner.www.chatServices.FriendListInfo;
import org.yuner.www.chatServices.InitData;
import org.yuner.www.myNetwork.NetConnect;
import org.yuner.www.myNetwork.NetworkService;

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
