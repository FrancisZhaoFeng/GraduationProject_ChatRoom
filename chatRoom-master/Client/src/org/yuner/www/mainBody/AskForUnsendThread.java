package org.yuner.www.mainBody;

import org.yuner.www.commons.GlobalMsgTypes;
import org.yuner.www.myNetwork.NetConnect;
import org.yuner.www.myNetwork.NetworkService;

public class AskForUnsendThread extends Thread{
	
	@Override
	public void run() {
		try {
			sleep(500);
		} catch(Exception e) {}
		// to ask for unsend msgs
		NetworkService.getInstance().sendUpload(GlobalMsgTypes.msgAskForUnsendMsgs, "xxxxxx"); // because here this msg is not used
	}

}
