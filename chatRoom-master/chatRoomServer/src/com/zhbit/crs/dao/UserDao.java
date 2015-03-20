package com.zhbit.crs.dao;

import java.util.List;

import javax.annotation.Resource;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.transaction.annotation.Transactional;









import com.zhbit.crs.domain.Friend;
import com.zhbit.crs.domain.User;

@Transactional
public class UserDao {
	Session ssession = null;
	@Resource
	private SessionFactory sessionFactory;
	
	public boolean signUp(User user){
		try {
			sessionFactory.getCurrentSession().save(user);
		} catch (Exception e) {
			e.printStackTrace();
			return false;
			// TODO: handle exception
		}
		return true;
	}
	
	@SuppressWarnings("unchecked")
	public List<User> login(User user){
		List<User> users = null;
		String hql = "from User where username="+user.getUsername()+" and password="+user.getPassword();
		System.out.println(hql);
		users = sessionFactory.getCurrentSession().createQuery(hql).list();
		return users;
				
	}
	
	public List<User> selectUser(User user){
		List<User> users = null;
		String hql = "from User where userid="+user.getUserid();
		System.out.println(hql);
		users = sessionFactory.getCurrentSession().createQuery(hql).list();
		return users;
	}
	
	@SuppressWarnings("unchecked")
	public List<Friend> selectFriend(User user){
		List<Friend> friends = null;
		String hql = "from Friend where userid="+user.getUserid();
		System.out.println(hql);
		friends = sessionFactory.getCurrentSession().createQuery(hql).list();
		return friends;
	}
	
	public boolean updateUser(User user){
		try {
			sessionFactory.getCurrentSession().merge(user);
		} catch (Exception e) {
			e.printStackTrace();
			return false;
			// TODO: handle exception
		}
		return true;
	}
	
	public List<Friend> isFriend(Friend friend){
		List<Friend> friends = null;
		String hql = "from Friend f where (f.userid="+friend.getUserByUserid()+" and f.friendid="+friend.getUserByFriendid()+") or (f.userid="+friend.getUserByFriendid()+" and f.friendid="+friend.getUserByUserid()+")";
		System.out.println(hql);
		friends = sessionFactory.getCurrentSession().createQuery(hql).list();
		return friends;
	}
	
	public boolean makeFriend(Friend friend){
		try {
			sessionFactory.getCurrentSession().save(friend);
		} catch (Exception e) {
			e.printStackTrace();
			return false;
			// TODO: handle exception
		}
		return true;
	}
}
