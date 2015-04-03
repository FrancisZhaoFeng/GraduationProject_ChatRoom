package com.zhbit.crs.mainBody;

import com.zhbit.crs.commons.GlobalMsgTypes;
import com.zhbit.crs.myNetwork.NetworkService;

public class AskForUnsendThread extends Thread{
	
	@Override
	public void run() {
		try {
			sleep(500);
		} catch(Exception e) {}
		// to ask for unsend msgs
//		NetworkService.getInstance().sendUpload(GlobalMsgTypes.msgAskForUnsendMsgs, "xxxxxx"); // because here this msg is not used
	}

}
