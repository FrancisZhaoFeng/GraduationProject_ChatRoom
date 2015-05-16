package com.zhbit.crs.dao;

import java.util.List;

import javax.annotation.Resource;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.zhbit.crs.domain.ChatPerLog;
import com.zhbit.crs.domain.ChatRoom;
import com.zhbit.crs.domain.ChatRoomLog;
import com.zhbit.crs.domain.Manager;
import com.zhbit.crs.domain.Mkey;
import com.zhbit.crs.domain.User;

@Transactional
public class ManagerDao {
	Session session = null;
	@Resource
	private SessionFactory sessionFactory;

	public ManagerDao() {
		// TODO Auto-generated constructor stub
	}

	// 没有被调用，功能已合并到insertManager
	@SuppressWarnings("unchecked")
	public List<Mkey> checkMkey(Mkey mkey) {
		List<Mkey> mkeys = null;
		String hql = "from Mkey u where u.mid='" + mkey.getMid() + "'and u.used=0";
		System.out.println(hql);
		mkeys = sessionFactory.getCurrentSession().createQuery(hql).list();
		return mkeys;
	}

	// 没有被调用，功能已合并到insertManager
	public boolean flagMkey(Mkey mkey) {
		try {
			sessionFactory.getCurrentSession().merge(mkey);
		} catch (Exception e) {
			return false;
		}
		return true;
	}

	@SuppressWarnings("unchecked")
	public boolean insertManager(Manager manager, Mkey mkey) {
		List<Mkey> mkeys = null;
		String hql = "from Mkey u where u.mid='" + mkey.getMid() + "' and u.used=0";
		System.out.println(hql);
		try {
			session = sessionFactory.getCurrentSession();
			session.beginTransaction();
			mkeys = session.createQuery(hql).list();
			if (mkeys.isEmpty()) {
				System.out.println("mkey invaliable");
				mkey.setUsed(true);
				return false; // used 如果是1代表已经被使用
			}
			sessionFactory.getCurrentSession().save(manager);
			mkey.setUsed(true);
			sessionFactory.getCurrentSession().merge(mkey);
			session.getTransaction().commit();
			session.close();
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}

	@SuppressWarnings("unchecked")
	@Transactional(propagation = Propagation.NOT_SUPPORTED, readOnly = true)
	public List<Manager> Login(Manager manager) {
		List<Manager> managers = null;
		String hql = "from Manager u where u.adminname='" + manager.getAdminname() + "' and u.password ='" + manager.getPassword() + "'";
		System.out.println(hql);
		session = sessionFactory.getCurrentSession();
		session.beginTransaction();
		managers = session.createQuery(hql).list();
		session.getTransaction().commit();
		session.close();
		return managers;
	}

	// 查看用户
	@SuppressWarnings("unchecked")
	@Transactional(propagation = Propagation.NOT_SUPPORTED, readOnly = true)
	public List<User> selectUser(String searchStr) {
		List<User> users = null;
		String hql = null;
		if (searchStr == null || searchStr.equals("")) {
			System.out.println("==selectUser searchStr is null");
			hql = "from User";
		} else {
			System.out.println("==selectUser searchStr is not null");
			hql = "from User where username like '%" + searchStr + "%' or telephone like '%" + searchStr + "%' or age like '%" + searchStr + "%'";
		}
		System.out.println(hql);
		session = sessionFactory.getCurrentSession(); // 获取session的操作
		session.beginTransaction();// 开启Transaction的操作
		users = session.createQuery(hql).list(); // 从缓存中读取数据，缓存中不存在数据则从数据库中读取
		session.getTransaction().commit(); // 此处才是真正与数据库交互的语句
		session.close();
		return users;
	}

	// 查看聊天室
	@SuppressWarnings("unchecked")
	@Transactional(propagation = Propagation.NOT_SUPPORTED, readOnly = true)
	public List<ChatRoom> selectChatRoom(String searchStr) {
		List<ChatRoom> chatRooms = null;
		String hql = null;
		if (searchStr == null || searchStr.equals("")) {
			System.out.println("==selectChatRoom searchStr is null");
			hql = "from ChatRoom where isdelete!=1";
		} else {
			hql = "from ChatRoom where isdelete!=1 and (chatroomname like '%" + searchStr + "%' or note like '%" + searchStr + "%' or createby like '%" + searchStr + "%')";
		}
		System.out.println(hql);
		session = sessionFactory.getCurrentSession();
		session.beginTransaction();
		chatRooms = session.createQuery(hql).list();
		session.getTransaction().commit();
		session.close();
		return chatRooms;
	}

	// 查看聊天室聊天记录
	@SuppressWarnings("unchecked")
	@Transactional(propagation = Propagation.NOT_SUPPORTED, readOnly = true)
	public List<ChatRoomLog> selectChatRoomLog(String searchStr) {
		List<ChatRoomLog> chatRoomLogs = null;
		String hql = null;
		if (searchStr == null || searchStr.equals("")) {
			System.out.println("==selectChatRoomLog searchStr is null");
			hql = "from ChatRoomLog where type!=4";
		} else {
			hql = "from ChatRoomLog where type!=4 and (senderid like '%" + searchStr + "%' or chatroomid like '%" + searchStr + "%' or sendtime like '%" + searchStr
					+ "%' or sendtext like '%" + searchStr + "%' )";
		}
		System.out.println(hql);
		session = sessionFactory.getCurrentSession();
		session.beginTransaction();
		chatRoomLogs = session.createQuery(hql).list();
		session.getTransaction().commit();
		session.close();
		return chatRoomLogs;
	}

	// 查看好友聊天记录
	@SuppressWarnings("unchecked")
	@Transactional(propagation = Propagation.NOT_SUPPORTED, readOnly = true)
	public List<ChatPerLog> selectChatPerLog(String searchStr) {
		List<ChatPerLog> chatPerLogs = null;
		String hql = null;
		if (searchStr == null || searchStr.equals("")) {
			System.out.println("==selectChatPerLog searchStr is null");
			hql = "from ChatPerLog where type!=4";
		} else {
			hql = "from ChatPerLog where type!=4 and (senderid like '%" + searchStr + "%' or receiverid like '%" + searchStr + "%' or sendtime like '%" + searchStr
					+ "%' or sendtext like '%" + searchStr + "%')";
		}
		System.out.println(hql);
		session = sessionFactory.getCurrentSession();
		session.beginTransaction();
		chatPerLogs = session.createQuery(hql).list();
		session.getTransaction().commit();
		session.close();
		return chatPerLogs;
	}

	// //搜索用户
	// @SuppressWarnings("unchecked")
	// @Transactional(propagation = Propagation.NOT_SUPPORTED, readOnly = true)
	// public List<User> selectSUser(String subStr){
	// List<User> users = null;
	// String hql="from User where username like %"+subStr+"%";
	// System.out.println(hql);
	// users = sessionFactory.getCurrentSession().createQuery(hql).list();
	// return users;
	// }
	//
	// //搜索聊天室
	// @SuppressWarnings("unchecked")
	// @Transactional(propagation = Propagation.NOT_SUPPORTED, readOnly = true)
	// public List<ChatRoom> selectSChatRoom(){
	// List<ChatRoom> chatRooms = null;
	// String hql="from ChatPerLog where type!=4";
	// System.out.println(hql);
	// chatPerLogs = sessionFactory.getCurrentSession().createQuery(hql).list();
	// return chatPerLogs;
	// }

	// 更新用户，主要用于限制用户权限
	public boolean updateUser(User user) {
		try {
			session = sessionFactory.getCurrentSession();
			session.beginTransaction();
			session.merge(user);
			session.getTransaction().commit();
			session.close();
		} catch (Exception e) {
			e.printStackTrace();
			return false;
			// TODO: handle exception
		}
		return true;
	}

	// 更新管理员，主要用于更新管理员密码
	public boolean updateManager(Manager manager) {
		try {
			session = sessionFactory.getCurrentSession();
			session.beginTransaction();
			session.merge(manager);
			session.getTransaction().commit();
			session.close();
		} catch (Exception e) {
			e.printStackTrace();
			return false;
			// TODO: handle exception
		}
		return true;
	}

	// 更新聊天室，主要用于删除聊天室
	public boolean updateChatRoom(ChatRoom chatRoom) {
		try {
			session = sessionFactory.getCurrentSession();
			session.beginTransaction();
			session.merge(chatRoom);
			session.getTransaction().commit();
			session.close();
		} catch (Exception e) {
			e.printStackTrace();
			return false;
			// TODO: handle exception
		}
		return true;
	}

	// 更新聊天室聊天记录，主要用于删除聊天记录，逻辑：将type修改为4，表示删除
	public boolean updateChatRoomLog(ChatRoomLog chatRoomLog) {
		try {
			session = sessionFactory.getCurrentSession();
			session.beginTransaction();
			session.merge(chatRoomLog);
			session.getTransaction().commit();
			session.close();
		} catch (Exception e) {
			e.printStackTrace();
			return false;
			// TODO: handle exception
		}
		return true;
	}

	// 更新聊天室聊天记录，主要用于删除聊天记录，逻辑：将type修改为4，表示删除
	public boolean updateChatPerLog(ChatPerLog chatPerLog) {
		try {
			session = sessionFactory.getCurrentSession();
			session.beginTransaction();
			session.merge(chatPerLog);
			session.getTransaction().commit();
			session.close();
		} catch (Exception e) {
			e.printStackTrace();
			return false;
			// TODO: handle exception
		}
		return true;
	}
}
