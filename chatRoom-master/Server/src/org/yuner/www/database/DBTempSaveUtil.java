package org.yuner.www.database;

import java.sql.*;
import java.util.ArrayList;
import java.util.Calendar;

import org.yuner.www.commons.GlobalErrors;
import org.yuner.www.commons.GlobalMsgTypes;
import org.yuner.www.beans.UserInfo;
import org.yuner.www.beans.ChatEntity;

public class DBTempSaveUtil {

	public static void saveUnsentChatMsg(int senderId, int receiverId, ChatEntity ent0) {
		insertItem(senderId, receiverId, ent0.toString(), GlobalMsgTypes.msgFromFriend);
	}

	public static ArrayList<ChatEntity> getUnsentChatMsg(int receiverId) {

		ArrayList<String> strList0 = readItems(receiverId, GlobalMsgTypes.msgFromFriend);

		ArrayList<ChatEntity> outList = new ArrayList<ChatEntity>();
		for(String str0 : strList0) {
			outList.add(ChatEntity.Str2Ent(str0));
		}
	
		return outList;

	}


	// ok, so here requeter is sender and requestee is receiver
	public static void saveUnsentFrdReqs(int requesterId, int requesteeId, String requestStr) {

		insertItem(requesterId, requesteeId, requestStr, GlobalMsgTypes.msgFriendshipRequest);

	}

	public static ArrayList<String> getUnsentFrdReqs(int requesteeId) {

		return readItems(requesteeId, GlobalMsgTypes.msgFriendshipRequest);

	}


	public static void saveUnsentFrdReqResponse(int requesterId, int requesteeId, String requestStr) {

		insertItem(requesteeId, requesterId, requestStr, GlobalMsgTypes.msgFriendshipRequestResponse);

	}

	public static ArrayList<String> getUnsentFrdReqResponse(int requesterId) {

		return readItems(requesterId, GlobalMsgTypes.msgFriendshipRequestResponse);

	}


	/********************************************************************************************************************************/
	private static void insertItem(int senderId, int receiverId, String str0, int type0) {
		Connection con = DBCon.getConnect();
		try {
			con.setAutoCommit(false);
		} catch(Exception e) {
			e.printStackTrace();
		}

		String sql0 = "use my_IM_GGMM";
		String sql1 = "insert into unSendMsgs (senderId,receiverId,msg,savedTime,msgType)" + 
						" values(?,?,?,now(),?)";
		
		try {
			PreparedStatement ps;

			ps = con.prepareStatement(sql0);
			ps.execute();

			ps = con.prepareStatement(sql1);
			ps.setInt(1, senderId);
			ps.setInt(2, receiverId);
			ps.setString(3, str0);
			ps.setInt(4, type0);
			int res = ps.executeUpdate();

			con.commit();
		} catch(Exception e) {
			try {
				con.rollback();
			} catch(Exception ee) {
				ee.printStackTrace();
			}
			e.printStackTrace();
		}
	}

	private static ArrayList<String> readItems(int receiverId, int type0) {
		Connection con = DBCon.getConnect();
		try {
			con.setAutoCommit(false);
		} catch(Exception e) {
			e.printStackTrace();
		}

		String sql0 = "use my_IM_GGMM";
		String sql1 = "select * from unSendMsgs where receiverId=? and msgType=? order by savedTime asc";
		String sql2 = "delete from unSendMsgs where receiverId=? and msgType=?";

		ArrayList<String> outList = new ArrayList<String>();

		try {
			PreparedStatement ps;
			ResultSet rs;

			ps = con.prepareStatement(sql0);
			ps.execute();

			ps = con.prepareStatement(sql1);
			ps.setInt(1, receiverId);
			ps.setInt(2, type0);
			rs = ps.executeQuery();
			if(rs.first()) {
				do {
					String str0 = rs.getString("msg");
					outList.add(str0);
				} while (rs.next());
			}

			ps = con.prepareStatement(sql2);
			ps.setInt(1, receiverId);
			ps.setInt(2, type0);
			int res = ps.executeUpdate();

			con.commit();
		} catch(Exception e) {
			try {
				con.rollback();
			} catch(Exception ee) {
				ee.printStackTrace();
			}
			e.printStackTrace();
		}
		return outList;
	}

}
