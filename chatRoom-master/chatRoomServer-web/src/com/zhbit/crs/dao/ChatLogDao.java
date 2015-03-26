package com.zhbit.crs.dao;


import java.util.List;

import javax.annotation.Resource;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.transaction.annotation.Transactional;

import com.zhbit.crs.domain.ChatPerLog;
import com.zhbit.crs.domain.ChatRoomLog;


@Transactional
public class ChatLogDao {
	Session session = null;
	@Resource
	private SessionFactory sessionFactory;

	public ChatLogDao() {
		// TODO Auto-generated constructor stub
	}
	
	public boolean insertChatRoomLog(ChatRoomLog chatRoomLog){
		try{
			System.out.println("insertChatRoomLog:"+chatRoomLog.getLogid());
			sessionFactory.getCurrentSession().save(chatRoomLog);
		}catch(Exception e){
			e.printStackTrace();
			return false;
		}
		return true;
	}
	
	public boolean insertChatPerLog(ChatPerLog chatPerLog){
		try{
			System.out.println("insertChatPerLog:"+chatPerLog.getLogid());
			sessionFactory.getCurrentSession().save(chatPerLog);
		}catch(Exception e){
			e.printStackTrace();
			return false;
		}
		return true;
	}
	
	public boolean deleteChatRoomLog(ChatRoomLog chatRoomLog){
		try{
			sessionFactory.getCurrentSession().delete(chatRoomLog);
		}catch(Exception e){
			e.printStackTrace();
			return false;
		}
		return true;
	}
	
	public boolean deleteChatPerLog(ChatPerLog chatPerLog){
		try{
			sessionFactory.getCurrentSession().delete(chatPerLog);
		}catch(Exception e){
			e.printStackTrace();
			return false;
		}
		return true;
	}
	
	public List<ChatPerLog> selectByReceiveId(int receiveId){
		List<ChatPerLog> chatPerLogs = null;
		String hql = "from ChatPerLog where receiverid="+receiveId;
		System.out.println(hql);
		chatPerLogs = sessionFactory.getCurrentSession().createQuery(hql).list();
		return chatPerLogs;
	}
}
