package com.csu.service;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.os.Environment;
import com.csu.bean.User;
import com.csu.db.UserDbHelper;
import com.csu.db.UserDbHelper.UserColumns;

/**
 * 负责用户登录的业务类
 * @author Administrator
 *
 */
public class UserService {
	private UserDbHelper userDbHelpser;
	private SQLiteDatabase db;
	private Context context;
	private String columns[] = { UserColumns._ID, UserColumns.IP,
			  UserColumns.PORT, UserColumns.NAME, UserColumns.IMG };
	public UserService(Context context) {
		this.context = context;
		userDbHelpser = new UserDbHelper(this.context);
	}

	/**
	 * 查询当前标识的用户
	 */
	public User queryUser(){
		int curFlag = 1;
		db = userDbHelpser.getReadableDatabase();
		Cursor cursor = db.query(UserColumns.USER_TABLE_NAME, columns, UserColumns.FALG + "="
				+ curFlag, null, null, null, null);
		if(cursor.moveToFirst()){
			String ip = cursor.getString(cursor.getColumnIndex(UserColumns.IP));
			String port = cursor.getString(cursor.getColumnIndex(UserColumns.PORT));
			String img = cursor.getString(cursor.getColumnIndex(UserColumns.IMG));
			String name = cursor.getString(cursor.getColumnIndex(UserColumns.NAME));
			int id = cursor.getInt(cursor.getColumnIndex(UserColumns._ID));
			User user = new User();
			user.setIp(ip);
			user.setPort(port);
			user.setImg(img);
			user.setName(name);
			user.setId(id);
			cursor.close();
			return user;
		}
		cursor.close();
		return null;
	}
	
	/**
	 * 插入用户 
	 * @throws FileNotFoundException 
	 */
	public long insertUser(User user) throws FileNotFoundException{
		db = userDbHelpser.getWritableDatabase();
		File fileDir;
		if(Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)){
			fileDir = new File(Environment.getExternalStorageDirectory()+"/userImage");
		}else{
			fileDir = new File(context.getFilesDir()+"/userImage");
		}
		if(!fileDir.exists()) fileDir.mkdirs();
		String fileName = System.currentTimeMillis()+".png";
		File imageFile = new File(fileDir.getAbsoluteFile()+"/"+fileName);
		OutputStream output = new FileOutputStream(imageFile);
		Bitmap bitmap = user.getBitmap();
		bitmap.compress(Bitmap.CompressFormat.PNG, 60, output);
		ContentValues values = new ContentValues();
		values.put(UserColumns._ID, user.getId());
		values.put(UserColumns.IP, user.getIp());
		values.put(UserColumns.PORT, user.getPort());
		values.put(UserColumns.NAME, user.getName());
		values.put(UserColumns.IMG, imageFile.getAbsolutePath());
		values.put(UserColumns.FALG, user.getFlag());
		db.beginTransaction();
		long rowId = 0;
		try {
			db.execSQL("update "+UserColumns.USER_TABLE_NAME+" set "+UserColumns.FALG+"='0'");
			rowId = db.insert(UserColumns.USER_TABLE_NAME, null, values);
			db.setTransactionSuccessful();
		} finally {
			db.endTransaction();
		}
		return rowId;
	}
	
	/**
	 * 切换账号
	 * @param user
	 * @return 当前账号的ID
	 */
	public long convertUser(User user){
		db = userDbHelpser.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put(UserColumns.IP, user.getIp());
		values.put(UserColumns.PORT, user.getPort());
		values.put(UserColumns.FALG, user.getFlag());
		db.beginTransaction();
		long rowId = 0;
		try {
			db.execSQL("update "+UserColumns.USER_TABLE_NAME+" set "+UserColumns.FALG+"='0'");
			rowId = db.update(UserColumns.USER_TABLE_NAME, values, UserColumns._ID+"=?", new String[]{String.valueOf(user.getId())});
			db.setTransactionSuccessful();
		} finally {
			db.endTransaction();
		}
		return rowId;
	}
	
	/**
	 * 删除用户
	 */
	public int deleteUser(int rowId){
		db = userDbHelpser.getWritableDatabase();
		String where=null;
		if(rowId!= 0){
			where= UserColumns._ID+"="+rowId;
		}
		int rows = db.delete(UserColumns.USER_TABLE_NAME, where, null);
		return rows;
	}
	
	/**
	 * 更新用户
	 */
	public int updateUser(User user){
		db = userDbHelpser.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put(UserColumns.IP, user.getIp());
		values.put(UserColumns.PORT, user.getPort());
		values.put(UserColumns.NAME, user.getName());
		values.put(UserColumns.IMG, user.getImg());
		values.put(UserColumns.FALG, user.getFlag());
		return db.update(UserColumns.USER_TABLE_NAME, values, UserColumns._ID+"="+user.getId(), null);
	}
	
	/**
	 * 查询已注册的用户
	 */
	public List<User> queryResigterUser() {
		db = userDbHelpser.getWritableDatabase();
		Cursor cursor = db.query(UserColumns.USER_TABLE_NAME, columns, null, null, null, null, UserColumns.FALG+" DESC"); 
		List<User> list = new ArrayList<User>();
		while(cursor.moveToNext()){
			String ip = cursor.getString(cursor.getColumnIndex(UserColumns.IP));
			String port = cursor.getString(cursor.getColumnIndex(UserColumns.PORT));
			String img = cursor.getString(cursor.getColumnIndex(UserColumns.IMG));
			String name = cursor.getString(cursor.getColumnIndex(UserColumns.NAME));
			int id = cursor.getInt(cursor.getColumnIndex(UserColumns._ID));
			User user = new User(id, ip, port, name, img);
			list.add(user);
		}
		cursor.close();
		return list;
	}
}
