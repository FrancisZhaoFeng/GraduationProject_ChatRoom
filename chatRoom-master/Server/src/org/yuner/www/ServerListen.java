package org.yuner.www;

import java.io.*;
import java.net.*;
import java.util.*;

import org.yuner.www.client.ClientActivity;
import org.yuner.www.beans.ChatEntity;

public class ServerListen {
	
	/*  the PORT number for this application  */
	public static final int PORT=8888;

	public static void main(String args[]){
		new ServerListen().begin();
	}
	
	public void begin(){
		try{
			ServerSocket ss = new ServerSocket( PORT );
			
			System.out.println("server waiting\n");
			while(true)
			{
				Socket sock = ss.accept();
				new ClientActivity(sock,this);
			}
		} catch( Exception e ){
			e.printStackTrace();
		}
	}

	public void updateFriendList(ClientActivity ca0) {
		ClientMap.getInstance().insert(ca0.getUserInfo().getId(), ca0);
	}
	
	public void removeOneClient(ClientActivity client0)
	{
		ClientMap.getInstance().remove(client0.getUserInfo().getId());
	}

	public ClientActivity getClientActivityById(int id) {
		return ClientMap.getInstance().getById(id);
	}
}
