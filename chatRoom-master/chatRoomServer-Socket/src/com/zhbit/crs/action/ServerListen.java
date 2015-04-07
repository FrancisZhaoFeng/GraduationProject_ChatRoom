package com.zhbit.crs.action;

import java.net.ServerSocket;
import java.net.Socket;

import com.zhbit.crs.dao.UserDao;
import com.zhbit.crs.domain.User;
import com.zhbit.crs.service.ServerActivity;

public class ServerListen {

	/* the PORT number for this application */
	public static final int PORT = 8887;

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

	public void updateFriendList(ServerActivity ca) {
		ClientMap.getInstance().insert(ca.getUserInfo().getUserid(), ca);
	}

	public void removeOneClient(ServerActivity client) {
		ClientMap.getInstance().remove(client.getUserInfo().getUserid());
	}

	public ServerActivity getClientActivityById(int id) {
		return ClientMap.getInstance().getById(id);
	}
}