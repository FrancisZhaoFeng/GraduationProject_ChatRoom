package org.yuner.www.database;

import java.sql.*;
import java.util.ArrayList;
import java.util.Calendar;

import org.yuner.www.commons.GlobalErrors;
import org.yuner.www.beans.UserInfo;
import org.yuner.www.beans.SearchEntity;

public class DBUtil {

/*------------------------------------- to sign up -------------------------------------*/
	public static UserInfo signUp(UserInfo u, String password) {
		Connection con = DBCon.getConnect();
		try {
			con.setAutoCommit(false);
		} catch(Exception e) {
			e.printStackTrace();
		}

		String sql0 = "use my_IM_GGMM";
		String sql1 = "insert into userTable(password,userName,birthYear,birthMonth,birthDay,gender,avatarId,isOnline,signupTime,hometown," + 
						"location) values(?,?,?,?,?,?,?,?,?,?,?)";
		String sql2 = "select max(userId) as userId from userTable";
		
		try {
			PreparedStatement ps;

			ps = con.prepareStatement(sql0);
			ps.execute();

			ps = con.prepareStatement(sql1);
			ps.setString(1, password);
			ps.setString(2, u.getName());
			ps.setInt(3, u.getBirthYear());
			ps.setInt(4, u.getBirthMonth());
			ps.setInt(5, u.getBirthDay());
			ps.setInt(6, u.getSex());
			ps.setInt(7, u.getAvatarId());
			ps.setInt(8, u.getIsOnline());
			ps.setString(9, u.getSignupTime());
			ps.setString(10, u.getHometown());
			ps.setString(11, u.getCurLocation());
			int res = ps.executeUpdate();

			int id;
			if(res>0) {
				ps = con.prepareStatement(sql2);
				ps.execute();
				ResultSet rs = ps.getResultSet();
				if(rs.last()) {
					id = rs.getInt("userId");
					u.setId(id);
				}
			}
			con.commit();
		} catch(Exception e) {
			try {
				con.rollback();
			} catch(Exception ee) {
				ee.printStackTrace();
			}
			e.printStackTrace();
			int id = GlobalErrors.nameAlreadyExists;
			u.setId(id);
		}

		return u;
	}


/*************************************** update user information *******************************************/
	public static UserInfo updateUserInfomaton(UserInfo u) {
		Connection con = DBCon.getConnect();
		try {
			con.setAutoCommit(false);
		} catch(Exception e) {
			e.printStackTrace();
		}

		String sql0 = "use my_IM_GGMM";
		String sql1 = "update userTable set birthYear=?, birthMonth=?, birthDay=?, gender=?, avatarId=?, hometown=?, location=? where 							userId=?";
		String sql2 = "update friendListTable set friendBirthYear=?, friendBirthMonth=?, friendBirthDay=?, friendGender=?, friendAvatarId=?,  							friendHometown=?, friendLocation=? where friendId=?";
		
		try {
			PreparedStatement ps;

			ps = con.prepareStatement(sql0);
			ps.execute();

			ps = con.prepareStatement(sql1);
			ps.setInt(1, u.getBirthYear());
			ps.setInt(2, u.getBirthMonth());
			ps.setInt(3, u.getBirthDay());
			ps.setInt(4, u.getSex());
			ps.setInt(5, u.getAvatarId());
			ps.setString(6, u.getHometown());
			ps.setString(7, u.getCurLocation());
			ps.setInt(8, u.getId());
			ps.executeUpdate();

			ps = con.prepareStatement(sql2);
			ps.setInt(1, u.getBirthYear());
			ps.setInt(2, u.getBirthMonth());
			ps.setInt(3, u.getBirthDay());
			ps.setInt(4, u.getSex());
			ps.setInt(5, u.getAvatarId());
			ps.setString(6, u.getHometown());
			ps.setString(7, u.getCurLocation());
			ps.setInt(8, u.getId());
			ps.executeUpdate();

			con.commit();
		} catch(Exception e) {
			try {
				con.rollback();
			} catch(Exception ee) {
				ee.printStackTrace();
			}
			e.printStackTrace();
		}
		return u;
	}


/*---------------------------- to determine whether two users are friends already -----------------------------------------*/
	public static boolean isFriendAlready(int id1, int id2) {
		Connection con = DBCon.getConnect();

		String sql0 = "use my_IM_GGMM";
		String sql1 = "select masterId from friendListTable where masterId=? and friendId=?";

		try {
			PreparedStatement ps;

			ps = con.prepareStatement(sql0);
			ps.execute();

			ps = con.prepareStatement(sql1);
			ps.setInt(1, id1);
			ps.setInt(2, id2);
			ResultSet rs = ps.executeQuery();

			if(rs.first()) {
				return true;
			} else {
				return false;
			}
		} catch(Exception e) {
			e.printStackTrace();
		}
		return false;
	}

/*----------------------------------------- to establish friendship between two users ------------------------------------------*/
	public static boolean makeFriends(int id1, int id2) {
		if(id1 == id2 || isFriendAlready(id1,id2)) {
			return true;
		}

		Connection con = DBCon.getConnect();
		try {
			con.setAutoCommit(false);
		} catch(Exception e) {
			e.printStackTrace();
		}

		String sql0 = "use my_IM_GGMM";
		String sql1 = "select * from userTable where userId=?";
		String sql2 = "insert into friendListTable(masterId,friendId,friendName,friendBirthYear,friendBirthMonth, friendBirthDay," +
					 "friendGender,friendAvatarId, friendIsOnline,friendSignupTime,friendHometown,friendLocation) " + 
					"values(?,?,?,?,?,?,?,?,?,?,?,?)";

		String name;
		int birthYear;
		int birthMonth;
		int birthDay;
		int gender;
		int avatarId;
		int isOnline;
		String signupTime;
		String hometown;
		String location;
		try {
			PreparedStatement ps;
			ResultSet rs;
			int res;

			ps = con.prepareStatement(sql0);
			ps.execute();

			ps = con.prepareStatement(sql1);
			ps.setInt(1,id2);
			rs = ps.executeQuery();
			if(rs.first()) {
				name = rs.getString("userName");
				birthYear = rs.getInt("birthYear");
				birthMonth = rs.getInt("birthMonth");
				birthDay = rs.getInt("birthDay");
				gender = rs.getInt("gender");
				avatarId = rs.getInt("avatarId");
				isOnline = rs.getInt("isOnline");
				signupTime = rs.getString("signupTime");
				hometown = rs.getString("hometown");
				location = rs.getString("location");
			} else {
				System.out.println("something wrong happens when query id="+id2);
				return false;
			}

			ps = con.prepareStatement(sql2);
			ps.setInt(1, id1);
			ps.setInt(2, id2);
			ps.setString(3, name);
			ps.setInt(4, birthYear);
			ps.setInt(5, birthMonth);
			ps.setInt(6, birthDay);
			ps.setInt(7, gender);
			ps.setInt(8, avatarId);
			ps.setInt(9, isOnline);
			ps.setString(10, signupTime);
			ps.setString(11, hometown);
			ps.setString(12, location);
			res = ps.executeUpdate();
			if(res <= 0) {
				System.out.println("something wrong happened when add id2 as friend of id1");
				return false;
			}

			ps = con.prepareStatement(sql1);
			ps.setInt(1,id1);
			rs = ps.executeQuery();
			if(rs.first()) {
				name = rs.getString("userName");
				birthYear = rs.getInt("birthYear");
				birthMonth = rs.getInt("birthMonth");
				birthDay = rs.getInt("birthDay");
				gender = rs.getInt("gender");
				avatarId = rs.getInt("avatarId");
				isOnline = rs.getInt("isOnline");
				signupTime = rs.getString("signupTime");
				hometown = rs.getString("hometown");
				location = rs.getString("location");
			} else {
				System.out.println("something wrong happens when query id="+id1);
				return false;
			}

			ps = con.prepareStatement(sql2);
			ps.setInt(1,id2);
			ps.setInt(2,id1);
			ps.setString(3,name);
			ps.setInt(4, birthYear);
			ps.setInt(5, birthMonth);
			ps.setInt(6, birthDay);
			ps.setInt(7, gender);
			ps.setInt(8, avatarId);
			ps.setInt(9, isOnline);
			ps.setString(10, signupTime);
			ps.setString(11, hometown);
			ps.setString(12, location);
			res = ps.executeUpdate();
			if(res <= 0) {
				System.out.println("something wrong happened when add id2 as friend of id1");
				return false;
			}

			con.commit();
			return true;
		} catch(Exception e) {
			try {
				con.rollback();
			} catch(Exception ee) {
				ee.printStackTrace();
			}
			e.printStackTrace();
		}
		
		return false;
	}

/*------------------------------------ check if one id exists -------------------------------------------------------------*/
	public static boolean idExistsOrNot(int id) {
		Connection con = DBCon.getConnect();

		String sql0 = "use my_IM_GGMM";
		String sql1 = "select userId from userTable where userId=?";
		
		try {
			PreparedStatement ps;
			ResultSet rs;

			ps = con.prepareStatement(sql0);
			ps.execute();

			ps = con.prepareStatement(sql1);
			ps.setInt(1,id);
			rs = ps.executeQuery();
			if(rs.first()) {
				return true;
			} else {
				return false;
			}
		} catch(Exception e) {
			e.printStackTrace();
		}
		return false;
	}

/*------------------------------- log in to see name exists or not ---------------------------------*/
	public static UserInfo loginWithUsername(UserInfo u, String password) {
		Connection con = DBCon.getConnect();

		String sql0 = "use my_IM_GGMM";
		String sql1 = "select userId from userTable where userName=?";

		int id;
		String name = u.getName();
		try {
			PreparedStatement ps;
			ResultSet rs;

			ps = con.prepareStatement(sql0);
			ps.execute();

			ps = con.prepareStatement(sql1);
			ps.setString(1,name);
			rs = ps.executeQuery();
			if(rs.first()) {
				id = rs.getInt("userId");
			} else {
				id = GlobalErrors.nameDoesnotExists;
			}
			u.setId(id);
			if(id >= 0) {
				u = loginToGetUserInfo(id, password);
			}
			return u;
		} catch(Exception e) {
			e.printStackTrace();
			return null;
		}
	}

/*------------------------------------------ log in for user info --------------------------------------------*/
	public static UserInfo loginToGetUserInfo(int id, String password) {
		// if the password is incorrect, we will then return a userinfo with id set to -1
		Connection con = DBCon.getConnect();

		String sql0 = "use my_IM_GGMM";
		String sql1 = "select * from userTable where userId=? and password=?";

		String name = "xx";
		int birthYear = 0;
		int birthMonth = 0;
		int birthDay = 0;
		int gender = 0;
		int avatarId = 0;
/*		int isOnline = 0;
		String signupTime = "xx";
		String hometown = "xx";
		String location = "xx";
*/		try {
			PreparedStatement ps;
			ResultSet rs;

			ps = con.prepareStatement(sql0);
			ps.execute();

			ps = con.prepareStatement(sql1);
			ps.setInt(1,id);
			ps.setString(2,password);
			rs = ps.executeQuery();
			if(rs.first()) {
				name = rs.getString("userName");
				birthYear = rs.getInt("birthYear");
				birthMonth = rs.getInt("birthMonth");
				birthDay = rs.getInt("birthDay");
				gender = rs.getInt("gender");
				avatarId = rs.getInt("avatarId");
			} else {
				id = -1;
			}
			UserInfo uu = new UserInfo(name, id, gender, birthYear, birthMonth, birthDay, avatarId);
			return uu;
		} catch(Exception e) {
			e.printStackTrace();
			return null;
		}
	}

/*-------------------------------------------- log in for friend list ---------------------------------------*/
	public static ArrayList<UserInfo> loginToGetFriendList(int id) {
		Connection con = DBCon.getConnect();

		String sql0 = "use my_IM_GGMM";
		String sql1 = "select * from friendListTable where masterId=?";

		ArrayList<UserInfo> list0 = new ArrayList<UserInfo>();
		int friendId;
		String name;
		int birthYear;
		int birthMonth;
		int birthDay;
		int gender;
		int avatarId;
		int isOnline;
		try {
			PreparedStatement ps;
			ResultSet rs;

			ps = con.prepareStatement(sql0);
			ps.execute();

			ps = con.prepareStatement(sql1);
			ps.setInt(1,id);
			rs = ps.executeQuery();
			if(rs.first()) {
				do {
					friendId = rs.getInt("friendId");
					name = rs.getString("friendName");
					birthYear = rs.getInt("friendBirthYear");
					birthMonth = rs.getInt("friendBirthMonth");
					birthDay = rs.getInt("friendBirthDay");
					gender = rs.getInt("friendGender");
					avatarId = rs.getInt("friendAvatarId");
					isOnline = rs.getInt("friendIsOnline");
					UserInfo u = new UserInfo(name, friendId, gender, birthYear, birthMonth, birthDay, avatarId);
					u.setIsOnline(isOnline);
					list0.add(u);
				} while(rs.next());
			}
			return list0;
		} catch(Exception e) {
			e.printStackTrace();
			return null;
		}
	}

/*-------------------------------------------- get all online friends ---------------------------------------*/
	public static ArrayList<UserInfo> getOnlineFriendList(int id) {
		Connection con = DBCon.getConnect();

		String sql0 = "use my_IM_GGMM";
		String sql1 = "select * from friendListTable where masterId=? and friendIsOnline=1";

		ArrayList<UserInfo> list0 = new ArrayList<UserInfo>();
		int friendId;
		String name;
		int birthYear;
		int birthMonth;
		int birthDay;
		int gender;
		int avatarId;
		int isOnline;
		try {
			PreparedStatement ps;
			ResultSet rs;

			ps = con.prepareStatement(sql0);
			ps.execute();

			ps = con.prepareStatement(sql1);
			ps.setInt(1,id);
			rs = ps.executeQuery();
			if(rs.first()) {
				do {
					friendId = rs.getInt("friendId");
					name = rs.getString("friendName");
					birthYear = rs.getInt("friendBirthYear");
					birthMonth = rs.getInt("friendBirthMonth");
					birthDay = rs.getInt("friendBirthDay");
					gender = rs.getInt("friendGender");
					avatarId = rs.getInt("friendAvatarId");
					isOnline = rs.getInt("friendIsOnline");
					UserInfo u = new UserInfo(name, friendId, gender, birthYear, birthMonth, birthDay, avatarId);
					u.setIsOnline(isOnline);
					list0.add(u);
				} while(rs.next());
			}
			return list0;
		} catch(Exception e) {
			e.printStackTrace();
			return null;
		}
	}

/*---------------------------------- search for a specified name -------------------------------------*/
	public static ArrayList<UserInfo> searchForPeopleWithName(SearchEntity s_ent0) {
		Connection con = DBCon.getConnect();

		String sql0 = "use my_IM_GGMM";
		String sql1 = "select * from userTable where userName=?";

		String nameOf = s_ent0.getName();

		int id;
		String name;
		int birthYear;
		int birthMonth;
		int birthDay;
		int gender;
		int avatarId;
		int isOnline;
		try {
			PreparedStatement ps;
			ResultSet rs;

			ps = con.prepareStatement(sql0);
			ps.execute();

			ps = con.prepareStatement(sql1);
			ps.setString(1,nameOf);
			rs = ps.executeQuery();

			ArrayList<UserInfo> list0 = new ArrayList<UserInfo>();
			if(rs.first()) {
				id = rs.getInt("userId");
				name = rs.getString("userName");
				birthYear = rs.getInt("birthYear");
				birthMonth = rs.getInt("birthMonth");
				birthDay = rs.getInt("birthDay");
				gender = rs.getInt("gender");
				avatarId = rs.getInt("avatarId");
				isOnline = rs.getInt("isOnline");
				UserInfo uuxi = new UserInfo(name, id, gender, birthYear, birthMonth, birthDay, avatarId);
				uuxi.setIsOnline(isOnline);
				list0.add(uuxi);
			}
			return list0;
		} catch(Exception e) {
			e.printStackTrace();
			return null;
		}
	}

/*-------------------------------------------- search for a list of people ---------------------------------------*/
	public static ArrayList<UserInfo> searchForPeopleList(SearchEntity s_ent0) {
		Connection con = DBCon.getConnect();

		String sql0 = "use my_IM_GGMM";
		String[] sqlSearch = new String[3]; 
		sqlSearch[0] = "select * from userTable where birthYear>=? and birthYear<=? and gender=0";
		sqlSearch[1] = "select * from userTable where birthYear>=? and birthYear<=? and gender=1";
		sqlSearch[2] = "select * from userTable where birthYear>=? and birthYear<=?";

		int sqlType = s_ent0.getSex();
		String sql1 = sqlSearch[sqlType];
		int ageL = s_ent0.getLowerAge();
		int ageU = s_ent0.getUpperAge();

		int curYear = Calendar.getInstance().get(Calendar.YEAR);
		int birthYearL = curYear - ageU;
		int birthYearU = curYear - ageL;

		ArrayList<UserInfo> list0 = new ArrayList<UserInfo>();
		int id;
		String name;
		int birthYear;
		int birthMonth;
		int birthDay;
		int gender;
		int avatarId;
		int isOnline;
		try {
			PreparedStatement ps;
			ResultSet rs;

			ps = con.prepareStatement(sql0);
			ps.execute();

			ps = con.prepareStatement(sql1);
			ps.setInt(1,birthYearL);
			ps.setInt(2,birthYearU);
			rs = ps.executeQuery();
			if(rs.first()) {
				do {
					id = rs.getInt("userId");
					name = rs.getString("userName");
					birthYear = rs.getInt("birthYear");
					birthMonth = rs.getInt("birthMonth");
					birthDay = rs.getInt("birthDay");
					gender = rs.getInt("gender");
					avatarId = rs.getInt("avatarId");
					isOnline = rs.getInt("isOnline");
					UserInfo u = new UserInfo(name, id, gender, birthYear, birthMonth, birthDay, avatarId);
					u.setIsOnline(isOnline);
					list0.add(u);
				} while(rs.next());
			}
			return list0;
		} catch(Exception e) {
			e.printStackTrace();
			return null;
		}
	}

/*---------------------------------- search for a specified name -------------------------------------*/
	public static void setOnAndOffLine(int id, int onOff) {
		Connection con = DBCon.getConnect();
		try {
			con.setAutoCommit(false);
		} catch(Exception e) {
			e.printStackTrace();
		}

		String sql0 = "use my_IM_GGMM";
		String sql1 = "update userTable set isOnline=? where userId=?";
		String sql2 = "update friendListTable set friendIsOnline=? where friendId=?";

		try {
			PreparedStatement ps;
			ResultSet rs;

			ps = con.prepareStatement(sql0);
			ps.execute();

			ps = con.prepareStatement(sql1);
			ps.setInt(1,onOff);
			ps.setInt(2,id);
			ps.executeUpdate();

			ps = con.prepareStatement(sql2);
			ps.setInt(1,onOff);
			ps.setInt(2,id);
			ps.executeUpdate();

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

}
