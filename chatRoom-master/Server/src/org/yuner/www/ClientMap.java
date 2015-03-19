package org.yuner.www;

import java.util.Map;
import java.util.HashMap;
import java.util.ArrayList;

import org.yuner.www.client.ClientActivity;

public class ClientMap {

	private static ClientMap mInstance;

	private HashMap<Integer, ClientActivity> mMap;

	public static ClientMap getInstance() {
		if(mInstance == null) {
			mInstance = new ClientMap();
		}
		return mInstance;
	}

	private ClientMap() {
		mMap = new HashMap<Integer, ClientActivity>();
	}

	public synchronized void insert(int id, ClientActivity ca0) {
		System.out.println("new id = " + id);

		mMap.put(id, ca0);
	}

	public synchronized void remove(int id) {
		mMap.remove(id);
	}

	public synchronized boolean containsOneId(int id) {
		return mMap.containsKey(id);
	}

	public synchronized ClientActivity getById(int id) {
		return mMap.get(id);
	}

	public synchronized ArrayList<ClientActivity> getAll() {
		ArrayList<ClientActivity> list0 = new ArrayList<ClientActivity>();
		for (Map.Entry<Integer, ClientActivity> entry : mMap.entrySet()) {
			list0.add(entry.getValue());
		}
		return list0;
	}

}
