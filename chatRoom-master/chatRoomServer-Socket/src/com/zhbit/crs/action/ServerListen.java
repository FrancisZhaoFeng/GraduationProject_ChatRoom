package com.zhbit.crs.action;

import java.net.ServerSocket;
import java.net.Socket;

import com.zhbit.crs.service.ServerActivity;

public class ServerListen {

	/* the PORT number for this application */
	public static final int PORT = 8888;

	public static void main(String args[]) {
		new ServerListen().begin();
	}

	public void begin() {
		try {
			ServerSocket ss = new ServerSocket(PORT);
			System.out.println("server waiting\n");
			while (true) {
				Socket sock = ss.accept();
				new ServerActivity(sock, this);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void updateFriendList(ServerActivity ca0) {
		ClientMap.getInstance().insert(ca0.getUserInfo().getUserid(), ca0);
	}

	public void removeOneClient(ServerActivity client0) {
		ClientMap.getInstance().remove(client0.getUserInfo().getUserid());
	}

	public ServerActivity getClientActivityById(int id) {
		return ClientMap.getInstance().getById(id);
	}
}