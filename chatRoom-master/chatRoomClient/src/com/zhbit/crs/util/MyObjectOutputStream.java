package com.zhbit.crs.util;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;

public class MyObjectOutputStream extends ObjectOutputStream {
	public MyObjectOutputStream() throws IOException {
		super();
	}

	public MyObjectOutputStream(OutputStream out) throws IOException {
		super(out);
	}

	@Override
	protected void writeStreamHeader() throws IOException {
		return;
	}
//
//	public void ObjectInputStream(InputStream in) throws IOException {
//		verifySubclass();
//		bin = new BlockDataInputStream(in);
//		handles = new HandleTable(10);
//		vlist = new ValidationList();
//		enableOverride = false;
//		readStreamHeader(); // 去读取头
//		bin.setBlockDataMode(true);
//	}
}
