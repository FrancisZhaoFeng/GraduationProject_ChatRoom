package com.csu.service;

import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;

import com.csu.bean.User;
import com.csu.constant.ContentFlag;
import android.content.Context;
import android.graphics.Bitmap;

public class RegisterService {
	
	/**
	 * 注册用户
	 * @param cropBitmap
	 * @param ip
	 * @param port
	 * @param name
	 * @throws IOException 
	 * @throws Exception 
	 */
	public void registerUser(Context context, Bitmap cropBitmap, String ip, String port,
			String name) throws IOException {
		UserService userService = new UserService(context);
		ByteArrayOutputStream byteoutput = new ByteArrayOutputStream();
		cropBitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteoutput);
		byte datas[] = byteoutput.toByteArray();
		DataOutputStream output = null;
		DataInputStream input = null;
		Socket socket = null;
		long rowId = 0;
		try {
			User user = new User(ip, port, name, cropBitmap, 1);
			SocketAddress socAddress = new InetSocketAddress(InetAddress.getByName(ip), Integer.parseInt(port)); 
			socket = new Socket();
			socket.connect(socAddress, 3000);
			output = new DataOutputStream(socket.getOutputStream());
			input = new DataInputStream(socket.getInputStream());
			String flagLine = ContentFlag.REGOSTER_FLAG + name;
			output.writeUTF(flagLine);
			output.write(datas);
			//读取服务器分配给用户的唯一标识
			rowId = Long.valueOf(input.readUTF());
			user.setId(rowId);
			userService.insertUser(user);
		} catch (IOException e) {
			throw new IOException("fail connect to the server");
		} finally {
			try {
				if(byteoutput!= null)
					byteoutput.close();
				if (output != null)
					output.close();
				if (input != null)
					input.close();
				if (socket != null)
					socket.close();
				if (cropBitmap != null) 
					cropBitmap.recycle();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

}
