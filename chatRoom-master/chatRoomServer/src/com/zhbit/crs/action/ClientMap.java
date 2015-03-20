package com.zhbit.crs.action;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.zhbit.crs.service.ServerActivity;



public class ClientMap {

	private static ClientMap mInstance;

	private HashMap<Integer, ServerActivity> mMap;

	public static ClientMap getInstance() {
		if(mInstance == null) {
			mInstance = new ClientMap();
		}
		return mInstance;
	}

	private ClientMap() {
		mMap = new HashMap<Integer, ServerActivity>();
	}

	public synchronized void insert(int id, ServerActivity ca0) {
		System.out.println("new id = " + id);

		mMap.put(id, ca0);
	}

	public synchronized void remove(int id) {
		mMap.remove(id);
	}

	public synchronized boolean containsOneId(int id) {
		return mMap.containsKey(id);
	}

	public synchronized ServerActivity getById(int id) {
		return mMap.get(id);
	}

	public synchronized ArrayList<ServerActivity> getAll() {
		ArrayList<ServerActivity> list0 = new ArrayList<ServerActivity>();
		for (Map.Entry<Integer, ServerActivity> entry : mMap.entrySet()) {
			list0.add(entry.getValue());
		}
		return list0;
	}

}
