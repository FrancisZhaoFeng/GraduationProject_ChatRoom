package com.csu.test;

import java.io.File;
import com.csu.bean.User;
import com.csu.service.UserService;
import android.os.Environment;
import android.test.AndroidTestCase;

public class chatTest extends AndroidTestCase {
	private static final String TAG = "chatTest";
	public void insertUser(){
		UserService service = new UserService(this.getContext());
		File file = null;
		if(Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)){
			String imgDir = Environment.getExternalStorageDirectory()+"/userImage/";
			file = new File(imgDir);
			if(!file.exists()) file.mkdirs();
		}
		User user = new User();
		user.setId(1);
		user.setIp("172.16.170.231");
		user.setPort("4040");
		user.setName("张三");
		user.setFlag(1);
		user.setImg("");
		service.deleteUser(0);
		//Log.i(TAG, service.queryUser().getId()+"");
	}
}
