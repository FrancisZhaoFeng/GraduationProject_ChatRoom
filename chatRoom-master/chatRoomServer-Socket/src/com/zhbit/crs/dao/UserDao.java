package com.zhbit.crs.dao;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;

import com.zhbit.crs.domain.Friend;
import com.zhbit.crs.domain.FriendId;
import com.zhbit.crs.domain.User;
import com.zhbit.crs.domain.ZSearchEntity;
import com.zhbit.crs.tools.HibernateUtils;
import com.zhbit.crs.tools.tools;

public class UserDao {
	Session session = null;
	
//	public static void main(String args[]) {
//		UserDao userDao = new UserDao();
		// userDao().signUp(new User("zgfailmr","zgfailmr","15961324654",45));
		// userDao().login(new User("zgfailmr","zgfailmr"));
		// userDao().searchUser(new SearchEntity(false,0,0,0,"z"));
		// userDao().searchUser(new SearchEntity(true, 22, 27, 1, ""));
		/*--------测试insertFriend，isFriend------------*/
//		User user1 = userDao.login(new User("zgfab", "31B69A7494A0EEC4AC544FD648C9D604")).get(0);
//		User user2 = userDao.login(new User("lmrab", "31B69A7494A0EEC4AC544FD648C9D604")).get(0);
//		Friend friend1 = new Friend(new FriendId(user1,user2), tools.getDate());
//		Friend friend2 = new Friend(new FriendId(user2,user1), tools.getDate());
//		userDao.insertFriend(friend1);
//		userDao.insertFriend(friend2);
//		@SuppressWarnings("unused")
//		List<Friend> friends = userDao.isFriend(friend1);
//		return;
//	}
	
	
	/**
	 * @param user
	 * @return
	 * 注册
	 */
	public boolean signUp(User user){
		try {
			session = HibernateUtils.getSession();
			session.beginTransaction();
			session.save(user);
			session.beginTransaction().commit();
			session.close();
		} catch (Exception e) {
			e.printStackTrace();
			return false;
			// TODO: handle exception
		}
		return true;
	}
	
	/**
	 * @param user
	 * @return
	 * 登陆
	 */
	@SuppressWarnings("unchecked")
	public List<User> login(User user){
		List<User> users = null;
		String hql = "from User where username=? and password=?";
		System.out.println(hql);
		session = HibernateUtils.getSession();
		session.beginTransaction();
		Query query;
		query = session.createQuery(hql);
		query.setParameter(0, user.getUsername());
		query.setParameter(1, user.getPassword());
		users = query.list();
		session.close();
		return users;
	}
	
	@SuppressWarnings("unchecked")
	public List<User> selectUser(User user){
		List<User> users = null;
		String hql = "from User where userid=?";
		System.out.println(hql);
		session = HibernateUtils.getSession();
		session.beginTransaction();
		Query query;
		query = session.createQuery(hql);
		query.setParameter(0, user.getUserid());
		users = query.list();
		session.close();
		return users;
	}
	
	@SuppressWarnings("unchecked")
	public List<Friend> selectFriend(User user){
		List<Friend> friends = null;
		String hql = "from Friend where userid=?";
		System.out.println(hql);
		session = HibernateUtils.getSession();
		session.beginTransaction();
		Query query;
		query = session.createQuery(hql);
		query.setParameter(0, user.getUserid());
		friends = query.list();
		session.close();
		return friends;
	}
	
	public boolean updateUser(User user){
		try{
			session = HibernateUtils.getSession();
			session.beginTransaction();
			session.update(user);
			session.beginTransaction().commit();
			session.close();
		}catch(Exception e){
			e.printStackTrace();
			return false;
		}
		return true;
	}
	
	@SuppressWarnings("unchecked")
	public List<Friend> isFriend(Friend friend){
		List<Friend> friends = null;
		String hql = "from Friend where userid=? and friendid=?";
		System.out.println(hql);
		session = HibernateUtils.getSession();
		session.beginTransaction();
		Query query;
		query = session.createQuery(hql);
//		query.setParameter(0, friend.getUserByUserid());
//		query.setParameter(1, friend.getUserByFriendid());
		friends = query.list();
		session.close();
		return friends;
	}
	
	public boolean insertFriend(Friend friend){
		try {
			session = HibernateUtils.getSession();
			session.beginTransaction();
			session.save(friend);
			session.beginTransaction().commit();
			session.close();
		} catch (Exception e) {
			e.printStackTrace();
			return false;
			// TODO: handle exception
		}
		return true;
	}
	
	@SuppressWarnings("unchecked")
	public List<User> searchUser(ZSearchEntity searchEntity){
		List<User> users = null;
		String hql;
		if(!searchEntity.getType()){
			hql = "from User where username like '%"+searchEntity.getName()+"%'";
		}else{
			if(searchEntity.getSex() ==2 ){
				hql = "from User where age between "+searchEntity.getLowerAge()+" and "+searchEntity.getUpperAge()+"";
			}else{
				hql = "from User where sex="+searchEntity.getSex()+" and age between "+searchEntity.getLowerAge()+" and "+searchEntity.getUpperAge()+"";
			}
		}
		System.out.println(hql);
		session = HibernateUtils.getSession();
		session.beginTransaction();
		Query query;
		query = session.createQuery(hql);
		users = query.list();
		session.close();
		return users;
	}
}
