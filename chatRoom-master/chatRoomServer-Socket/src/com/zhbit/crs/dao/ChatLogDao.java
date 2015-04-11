package com.zhbit.crs.dao;


import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;

import com.zhbit.crs.domain.ChatPerLog;
import com.zhbit.crs.domain.ChatPerLogTemp;
import com.zhbit.crs.domain.ChatRoomLog;
import com.zhbit.crs.domain.ChatRoomLogTemp;
import com.zhbit.crs.tools.HibernateUtils;


public class ChatLogDao {
	Session session = null;
	public ChatLogDao() {
		// TODO Auto-generated constructor stub
	}
	
	public static void main(String[] args) {
		ChatLogDao chatLogDao = new ChatLogDao();
		chatLogDao.selectByReceiveId(2);
	}
	
	public boolean insertChatRoomLog(ChatRoomLog chatRoomLog){
		try {
			System.out.println("insertChatPerLog:"+chatRoomLog.getLogid());
			session = HibernateUtils.getSession();
			session.beginTransaction();
			session.save(chatRoomLog);
			session.beginTransaction().commit();
			session.close();
		} catch (Exception e) {
			e.printStackTrace();
			return false;
			// TODO: handle exception
		}
		return true;
	}
	
	public boolean insertChatRoomLogTemp(ChatRoomLogTemp chatRoomLogTemp){
		try {
			System.out.println("insertChatPerLog:"+chatRoomLogTemp.getLogid());
			session = HibernateUtils.getSession();
			session.beginTransaction();
			session.save(chatRoomLogTemp);
			session.beginTransaction().commit();
			session.close();
		} catch (Exception e) {
			e.printStackTrace();
			return false;
			// TODO: handle exception
		}
		return true;
	}
	
	public boolean insertChatPerLog(ChatPerLog chatPerLog){
		try {
			System.out.println("insertChatPerLog:"+chatPerLog.getLogid());
			session = HibernateUtils.getSession();
			session.beginTransaction();
			session.save(chatPerLog);
			session.beginTransaction().commit();
			session.close();
		} catch (Exception e) {
			e.printStackTrace();
			return false;
			// TODO: handle exception
		}
		return true;
	}
	
	public boolean insertChatPerLogTemp(ChatPerLogTemp chatPerLogTemp){
		try {
			System.out.println("insertChatPerLog:"+chatPerLogTemp.getLogid());
			session = HibernateUtils.getSession();
			session.beginTransaction();
			session.save(chatPerLogTemp);
			session.beginTransaction().commit();
			session.close();
		} catch (Exception e) {
			e.printStackTrace();
			return false;
			// TODO: handle exception
		}
		return true;
	}
	
	public boolean deleteChatRoomLog(ChatRoomLog chatRoomLog){
		try {
			System.out.println("deleteChatRoomLog:"+chatRoomLog.getLogid());
			session = HibernateUtils.getSession();
			session.beginTransaction();
			session.delete(chatRoomLog);
			session.beginTransaction().commit();
			session.close();
		} catch (Exception e) {
			return false;
		}
		// TODO Auto-generated method stub
		return true;
	}
	
	public boolean deleteChatRoomLogTemp(ChatRoomLogTemp chatRoomLogTemp){
		try {
			System.out.println("deleteChatRoomLog:"+chatRoomLogTemp.getLogid());
			session = HibernateUtils.getSession();
			session.beginTransaction();
			session.delete(chatRoomLogTemp);
			session.beginTransaction().commit();
			session.close();
		} catch (Exception e) {
			return false;
		}
		// TODO Auto-generated method stub
		return true;
	}
	
	public boolean deleteChatPerLog(ChatPerLog chatPerLog){
		try {
			System.out.println("deleteChatPerLog:"+chatPerLog.getLogid());
			session = HibernateUtils.getSession();
			session.beginTransaction();
			session.delete(chatPerLog);
			session.beginTransaction().commit();
			session.close();
		} catch (Exception e) {
			return false;
		}
		// TODO Auto-generated method stub
		return true;
	}
	
	public boolean deleteChatPerLogTemp(ChatPerLogTemp chatPerLogTemp){
		try {
			System.out.println("deleteChatPerLog:"+chatPerLogTemp.getLogid());
			session = HibernateUtils.getSession();
			session.beginTransaction();
			session.delete(chatPerLogTemp);
			session.beginTransaction().commit();
			session.close();
		} catch (Exception e) {
			return false;
		}
		// TODO Auto-generated method stub
		return true;
	}
	
	@SuppressWarnings("unchecked")
	public List<ChatPerLogTemp> selectByReceiveId(int receiveId){
		List<ChatPerLogTemp> chatPerLogTemps = null;
		String hql = "from ChatPerLogTemp where receiverid=?";
		System.out.println(hql);
		session = HibernateUtils.getSession();
		session.beginTransaction();
		Query query;
		query = session.createQuery(hql);
		query.setParameter(0, receiveId);
		chatPerLogTemps = query.list();
		session.close();
		return chatPerLogTemps;
	}
}
