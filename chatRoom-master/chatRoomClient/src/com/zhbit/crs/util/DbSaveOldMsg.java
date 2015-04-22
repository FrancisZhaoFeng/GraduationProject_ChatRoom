package com.zhbit.crs.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.util.SparseIntArray;

import com.zhbit.crs.commons.GlobalInts;
import com.zhbit.crs.domain.ChatPerLog;
import com.zhbit.crs.domain.ZdbFrdReqNotifItemEntity;
import com.zhbit.crs.domain.ZdbTabMsgItemEntity;

/**
 * @author zhaoguofeng
 * 
 */
public class DbSaveOldMsg {

	private static final String DbName = "oldmsgs.db";
	private static DbSaveOldMsg mInstance;
	private static Context mContext;

	private SQLiteDatabase mSqlDb;

	private int mStartPoint;
	private final int mReadLength = 10;
	private SparseIntArray mStartPointArr;
	/*--保存数据库语句--*/
	private String createSql;
	private String insertSql;
	private String deleteSql;
	private String selectSql;
	private List<Object> mObjs = null; 

	public static DbSaveOldMsg getInstance() {
		if (mInstance == null) {
			mInstance = new DbSaveOldMsg();
		}
		return mInstance;
	}

	private DbSaveOldMsg() {
		mSqlDb = mContext.openOrCreateDatabase(mContext.getFilesDir().getParent() + "/databases/" + DbName, Context.MODE_PRIVATE, null);// getParent:/data/data/com.zhbit.crs
		Log.e("mContext.getFilesDir().getParent():", mContext.getFilesDir().getParent());
		mStartPointArr = new SparseIntArray();
	}

	public static void onInit(Context context) {
		mContext = context;
	}

	/******************************** above are for initialization ************************************/

	// masterId is the id of client user, guestId is the id of user this guy is talking to
	/**
	 * @param masterId
	 * @param guestId
	 * @param isSelfBool
	 * @param entity
	 *            保存聊天记录到app客户端中
	 */
	public void saveMsg(int masterId, int guestId, boolean isSelfBool, ChatPerLog chatPerLog) {
		// createSql = "create table if not exists oldMsg_" + masterId + "_" + guestId +  " (_index integer primary key,isSelf int,strEntity text)";
		// insertSql = "insert into oldMsg_" + masterId + "_" + guestId + " (isSelf,strEntity)" + " values(?,?)";
		// mSqlDb.execSQL(createSql);
		// mSqlDb.execSQL(insertSql, new Object[] { isSelf, strOfEntity });
		// Log.e("saveMsg", createSql);
		// Log.e("saveMsg", insertSql);
		createSql = "create table if not exists oldMsg_" + masterId + "_" + guestId + " (_index integer primary key,classtabledata text)";
		insertSql = "insert into oldMsg_" + masterId + "_" + guestId + "(classtabledata)" + " values(?)";
		mSqlDb.execSQL(createSql); // db
		saveObject(insertSql, chatPerLog);// db
		Log.e("saveMsg", createSql);
		Log.e("saveMsg", insertSql);
	}

	/**
	 * @param chatList
	 * @param boolList
	 * @param masterId
	 * @param guestId
	 * @return 在本地app客户端中获取聊天记录
	 */
	public int getMsg(ArrayList<ChatPerLog> chatList, ArrayList<Boolean> boolList, int masterId, int guestId) {
		int key = getKeyFromMasterGuestId(masterId, guestId);
		mStartPoint = mStartPointArr.get(key);

		createSql = "create table if not exists oldMsg_" + masterId + "_" + guestId + " (_index integer primary key,classtabledata text)";
		selectSql = "SELECT * from OldMsg_" + masterId + "_" + guestId + " order by _index desc LIMIT " + mStartPoint + "," + mReadLength;
		mSqlDb.execSQL(createSql);
		mObjs = getObject(selectSql);
		ChatPerLog chatPerLog = null;
		for(int i = 0 ; mObjs != null && i < mObjs.size()  ; i++){
			chatPerLog = (ChatPerLog)mObjs.get(i);
			chatList.add(0,chatPerLog);
//			chatList.add((ChatPerLog)mObjs.get(mObjs.size()-(i+1)));
			boolList.add(0,chatPerLog.getUserBySenderid().getUserid() == masterId);
		}
		if(mObjs != null){
			mStartPoint += mObjs.size();
			mStartPointArr.put(key, mStartPoint);
		}
		Log.e("getMsg", createSql);
		Log.e("getMsg", selectSql);
		return mObjs != null?mObjs.size():0;
//
//		if (!curs.moveToFirst()) {
//			return 0;
//		} else {
//			mStartPoint += curs.getCount();
//			mStartPointArr.put(key, mStartPoint);
//		}
//
//		do {
//			String strEnt = curs.getString(curs.getColumnIndex("strEntity"));
//			ChatPerLog ent = new ChatEntity(strEnt0);
//			chatList.add(0, ent);
//			int isSelf = curs.getInt(curs.getColumnIndex("isSelf"));
//			if (isSelf == 1) {
//				boolList.add(0, true);
//			} else {
//				boolList.add(0, false);
//			}
//		} while (curs.moveToNext());
//
//		int cursGetcount = curs.getCount();
//		curs.close();
//		return cursGetcount;
	}

	public int getKeyFromMasterGuestId(int masterId, int guestId) {
		int key = masterId * GlobalInts.MaxFriendNumber * 2 + guestId;
		return key;
	}

	/************************************ for TabMsgItems ***************************************/
	// masterId is the id of client user, guestId is the id of user this guy is talking to
	/**
	 * @param id
	 * @param listOfEnt0
	 *            保存消息界面的，list的item实体
	 */
	public void saveTabMsgItem(int id, List<ZdbTabMsgItemEntity> listOfEnt) {
		createSql = "create table if not exists oldTabMsg_" + id + " (_index integer primary key,classtabledata text)";
		deleteSql = "delete from oldTabMsg_" + id;  //删除以前list的item实体，因为消息界面已更新
		mSqlDb.execSQL(createSql);  //db
		mSqlDb.execSQL(deleteSql);  //db
		Log.e("saveTabMsgItem", createSql);
		Log.e("saveTabMsgItem", deleteSql);

		insertSql = "insert into oldTabMsg_" + id + " (classtabledata)" + " values(?)";
		for (ZdbTabMsgItemEntity ent : listOfEnt) {
			saveObject(insertSql, ent);  //db
		}
	}

	/**
	 * @param id
	 * @param msgItemList
	 *            获取消息界面的，list 实体
	 */
	public void getTabMsgItem(int id, ArrayList<ZdbTabMsgItemEntity> msgItemList) {
		createSql = "create table if not exists oldTabMsg_" + id + " (_index integer primary key,classtabledata text)";
		selectSql = "SELECT * from OldTabMsg_" + id + " ORDER BY _index DESC";
		mSqlDb.execSQL(createSql); //db
//		Cursor curs = mSqlDb.rawQuery(selectSql, null);
		mObjs = getObject(selectSql);
		for(int i = 0 ; mObjs != null && i < mObjs.size() ; i++){
			msgItemList.add(0,(ZdbTabMsgItemEntity)mObjs.get(i));
		}
		Log.e("getTabMsgItem", createSql);
		Log.e("getTabMsgItem", selectSql);

//		if (!curs.moveToFirst()) {
//			return;
//		}
//
//		do {
//			String strEnt0 = curs.getString(curs.getColumnIndex("strEntity"));
//			ZdbTabMsgItemEntity ent0 = new ZdbTabMsgItemEntity(strEnt0);
//
//			msgItemList.add(0, ent0);
//		} while (curs.moveToNext());
//		curs.close();
	}

	/************************************ for Frd Req Notifications **************************************/
	/**
	 * @param id
	 * @param listOfEnt0
	 */
	public void saveFrdReqNotif(int id, List<ZdbFrdReqNotifItemEntity> listOfEnt) {
		createSql = "create table if not exists oldFrdReqNotif_" + id + " (_index integer primary key,classtabledata text)";
		deleteSql = "delete from oldFrdReqNotif_" + id;
		mSqlDb.execSQL(createSql);
		mSqlDb.execSQL(deleteSql);
		Log.e("saveFrdReqNotif", createSql);
		Log.e("saveFrdReqNotif", deleteSql);

		insertSql = "insert into oldFrdReqNotif_" + id + " (classtabledata)" + " values(?)";
		for (ZdbFrdReqNotifItemEntity ent : listOfEnt) {
			saveObject(insertSql, ent);
		}
	}

	/**
	 * @param id
	 * @param msgItemList
	 */
	public void getFrdReqNotif(int id, ArrayList<ZdbFrdReqNotifItemEntity> msgItemList) {
		createSql = "create table if not exists oldFrdReqNotif_" + id + " (_index integer primary key,classtabledata text)";
		selectSql = "SELECT * from oldFrdReqNotif_" + id + " ORDER BY _index DESC";
		mSqlDb.execSQL(createSql);
//		Cursor curs = mSqlDb.rawQuery(selectSql, null);
		mObjs = getObject(selectSql);
		for(int i = 0 ; mObjs != null && i < mObjs.size() ; i++){
			msgItemList.add(0,(ZdbFrdReqNotifItemEntity)mObjs.get(i));
		}
		Log.e("getFrdReqNotif", createSql);
		Log.e("getFrdReqNotif", selectSql);

//		if (!curs.moveToFirst()) {
//			return;
//		}
//
//		do {
//			String strEnt0 = curs.getString(curs.getColumnIndex("strEntity"));
//			ZdbFrdReqNotifItemEntity ent0 = new ZdbFrdReqNotifItemEntity(strEnt0);
//
//			msgItemList.add(0, ent0);
//		} while (curs.moveToNext());
//
//		curs.close();
	}

	/****************************** unread messages ***************************************************/
	/**
	 * @param myId
	 * @param friendId
	 * @param unreadAm
	 */
	public void saveUnreadMsgs(int myId, int friendId, int unreadAm) {
		createSql = "create table if not exists oldUnreadMsgs_" + myId + " (_friendId integer,unreadAmount integer)";
		deleteSql = "delete from oldUnreadMsgs_" + myId + " where _friendId=?";
		insertSql = "insert into oldUnreadMsgs_" + myId + " (_friendId,unreadAmount) values(?,?)";
		mSqlDb.execSQL(createSql);
		mSqlDb.execSQL(deleteSql, new Object[] { friendId });
		mSqlDb.execSQL(insertSql, new Object[] { friendId, unreadAm });
		Log.e("saveUnreadMsgs", createSql);
		Log.e("saveUnreadMsgs", deleteSql);
		Log.e("saveUnreadMsgs", insertSql);
	}

	/**
	 * @param myId
	 * @param friendId
	 * @return
	 */
	public int getUnreadMsgs(int myId, int friendId) {
		createSql = "create table if not exists oldUnreadMsgs_" + myId + " (_friendId integer,unreadAmount integer)";
		selectSql = "SELECT * from oldUnreadMsgs_" + myId + " where _friendId=" + friendId;
		mSqlDb.execSQL(createSql);
		Cursor curs = mSqlDb.rawQuery(selectSql, null);
		Log.e("getUnreadMsgs", createSql);
		Log.e("getUnreadMsgs", selectSql);

		if (!curs.moveToFirst()) {
			return 0;
		}

		int num = curs.getInt(curs.getColumnIndex("unreadAmount"));
		return num;
	}

	/**
	 * @param sql
	 * @param obj
	 *            实体类以对象方式存储在数据库中
	 */
	public void saveObject(String sql, Object obj) {
		ByteArrayOutputStream arrayOutputStream = new ByteArrayOutputStream();
		try {
			ObjectOutputStream objectOutputStream = new ObjectOutputStream(arrayOutputStream);
			objectOutputStream.writeObject(obj);
			objectOutputStream.flush();
			byte data[] = arrayOutputStream.toByteArray();
			objectOutputStream.close();
			arrayOutputStream.close();
			mSqlDb.execSQL(sql, new Object[] { data });  //insert into classtable(classtabledata) values(?)
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * @param sql
	 * @return 从数据库中获取实体类对象
	 */
	public List<Object> getObject(String sql) {
		List<Object> objs = new ArrayList<Object>();
		Cursor cursor = mSqlDb.rawQuery(sql, null);  //select * from classtable
		if (cursor != null) {
			while (cursor.moveToNext()) {
				byte data[] = cursor.getBlob(cursor.getColumnIndex("classtabledata")); // classtabledata 是实体类对象
				ByteArrayInputStream arrayInputStream = new ByteArrayInputStream(data);
				try {
					ObjectInputStream inputStream = new ObjectInputStream(arrayInputStream);
					Object obj = inputStream.readObject();
					objs.add(obj);
					inputStream.close();
					arrayInputStream.close();
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		return objs;
	}

	public void close() {
		if (mSqlDb != null) {
			mSqlDb.close();
			mInstance = null;
		}
	}

}