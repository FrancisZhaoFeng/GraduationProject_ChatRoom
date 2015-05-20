package com.zhbit.crs.service;

import java.util.Iterator;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.struts2.ServletActionContext;

import com.zhbit.crs.dao.ManagerDao;
import com.zhbit.crs.domain.ChatPerLog;
import com.zhbit.crs.domain.ChatRoom;
import com.zhbit.crs.domain.ChatRoomLog;
import com.zhbit.crs.domain.Manager;
import com.zhbit.crs.domain.Mkey;
import com.zhbit.crs.domain.User;
import com.zhbit.crs.md5.MD5;


public class ManagerService {
	@Resource
	private ManagerDao managerDao;
	@Resource
	private MD5 md;

	public ManagerService() {
		// TODO Auto-generated constructor stub
	}
	
//	public Object getObject(Object obj,List<Object> objs){
//		Iterator<Object> it = objs.iterator();
//		while(it.hasNext()){
//			if(it.)
//		}
//	}
	
	public boolean checkMkey(Mkey mkey){
		List<Mkey> mkeys = managerDao.checkMkey(mkey);
		if(mkeys.isEmpty()){
			return false;
		}else{
			mkey.setUsed(true);
			managerDao.flagMkey(mkey);
			return true;
		}
	}
	
	public boolean insertManager(Manager manager,Mkey mkey){
		manager.setPassword(md.toMD5(manager.getPassword()));
		System.out.println(manager.getAdminname()+"--"+manager.getPassword()+"--"+mkey.getMid()+"--"+mkey.getUsed());
		return managerDao.insertManager(manager,mkey);
	}
	
	public boolean login(Manager manager){
		HttpServletRequest request = ServletActionContext.getRequest();
		HttpSession session = request.getSession(false);
		manager.setPassword(md.toMD5(manager.getPassword()));
		System.out.println(manager.getPassword());
		List<Manager> list = managerDao.Login(manager);
		if(list.isEmpty()){
			return false;
		}else{
			manager = new Manager();
			manager = list.get(0);
			session.setAttribute("ssiMng", manager);
			session.setAttribute("ssiMngId", manager.getAdminid());
			session.setAttribute("ssiMngName", manager.getAdminname());
			return true;
		}
	}
	
	//查看用户
	public boolean selectUser(String searchStr){
		HttpServletRequest request = ServletActionContext.getRequest();
		HttpSession session = request.getSession();
		List<User> users = managerDao.selectUser(searchStr);
		if(users.isEmpty()){
			return false;
		}else{
//			this.removeSession();
			session.setAttribute("pageCheck", "checkUser");
			session.setAttribute("pageCheckUser", "checkUser");
			session.removeAttribute("Users");
			session.setAttribute("Users", users);
			return true;
		}
	}
	
	//查看聊天室
	public boolean selectChatRoom(String searchStr){
		HttpServletRequest request = ServletActionContext.getRequest();
		HttpSession session = request.getSession();
		List<ChatRoom> chatRooms = managerDao.selectChatRoom(searchStr);
		if(chatRooms.isEmpty()){
			return false;
		}else{
//			this.removeSession();
			session.setAttribute("pageCheck", "checkChatRoom");
			session.setAttribute("pageCheckChatRoom", "checkChatRoom");
			session.removeAttribute("ChatRooms");
			session.setAttribute("ChatRooms", chatRooms);
			return true;
		}
	}
	
	//查看聊天室聊天记录
	public boolean selectChatRoomLog(String searchStr){
		HttpServletRequest request = ServletActionContext.getRequest();
		HttpSession session = request.getSession();
		List<ChatRoomLog> chatRoomLogs = managerDao.selectChatRoomLog(searchStr);
		if(chatRoomLogs.isEmpty()){
			return false;
		}else{
//			this.removeSession();
			session.setAttribute("pageCheck", "checkChatRoomLog");
			session.setAttribute("pageCheckChatRoomLog", "checkChatRoomLog");
			session.removeAttribute("ChatRoomLogs");
			session.setAttribute("ChatRoomLogs", chatRoomLogs);
			return true;
		}
	}
	
	//查看好友聊天记录
	public boolean selectChatPerLog(String searchStr){
		HttpServletRequest request = ServletActionContext.getRequest();
		HttpSession session = request.getSession();
		List<ChatPerLog> chatPerLogs = managerDao.selectChatPerLog(searchStr);
		if(chatPerLogs.isEmpty()){
			return false;
		}else{
//			this.removeSession();
			session.setAttribute("pageCheck", "checkChatPerLog");
			session.setAttribute("pageCheckChatPerLog", "checkChatPerLog");
			session.removeAttribute("ChatPerLogs");
			session.setAttribute("ChatPerLogs", chatPerLogs);
			return true;
		}
	}
	
	//更新用户，主要用于限制用户权限
	@SuppressWarnings("unchecked")
	public boolean updateUser(User user){
		HttpServletRequest request = ServletActionContext.getRequest();
		HttpSession session = request.getSession();
		List<User> users = (List<User>) session.getAttribute("Users");
		//遍历搜索
		Iterator<User> it = users.iterator();
		User uTemp = null;
		while(it.hasNext()){
			uTemp = it.next();
			if(uTemp.getUserid().equals(user.getUserid())){
				user = uTemp;
				break;
			}
		}
		if (user.getBlacklist() == false) {
			user.setBlacklist(true);
		} else {
			user.setBlacklist(false);
		}
		return managerDao.updateUser(user);
	}
	
	//更新管理员，主要用于更新管理员密码
	public String updateManager(String oldpwd,String repwd,Manager manager){
		HttpServletRequest request = ServletActionContext.getRequest();
		HttpSession session = request.getSession();
		Manager ssiMng =(Manager) session.getAttribute("ssiMng");
		manager.setPassword(md.toMD5(manager.getPassword()));
		oldpwd = md.toMD5(oldpwd);
		repwd = md.toMD5(repwd);
		if(!oldpwd.equals(ssiMng.getPassword())){
			return "旧密码输入错误！请从新输入";
		}else if(!manager.getPassword().equals(repwd)){
			return "新密码输入不一致";
		}
		ssiMng.setPassword(repwd);
		if(managerDao.updateManager(ssiMng)){
			session.removeAttribute("ssiMng");
			session.removeAttribute("ssiMngId");
			session.removeAttribute("ssiMngName");
			return "修改密码成功，请重新登陆！";
		}else{
			return "修改密码不成功";
		}
	}
	
	//更新聊天室，主要用于删除聊天室
	@SuppressWarnings("unchecked")
	public boolean updateChatRoom(ChatRoom chatRoom){
		HttpServletRequest request = ServletActionContext.getRequest();
		HttpSession session = request.getSession();
		List<ChatRoom> chatRooms = (List<ChatRoom>) session.getAttribute("ChatRooms");
		//遍历搜索
		Iterator<ChatRoom> it = chatRooms.iterator();
		ChatRoom cmTemp = null;
		while(it.hasNext()){
			cmTemp = it.next();
			if(cmTemp.getChatroomid().equals(chatRoom.getChatroomid())){
				chatRoom = cmTemp;
				break;
			}
		}
		chatRoom.setIsdelete(true);
		return managerDao.updateChatRoom(chatRoom);
	}
	
	//更新聊天室聊天记录，主要用于删除聊天记录，逻辑：将type修改为4，表示删除
	@SuppressWarnings("unchecked")
	public boolean updateChatRoomLog(ChatRoomLog chatRoomLog){
		HttpServletRequest request = ServletActionContext.getRequest();
		HttpSession session = request.getSession();
		List<ChatRoomLog> chatRoomLogs = (List<ChatRoomLog>) session.getAttribute("ChatRoomLogs");
		//遍历搜索
		Iterator<ChatRoomLog> it = chatRoomLogs.iterator();
		ChatRoomLog crlTemp = null;
		while(it.hasNext()){
			crlTemp = it.next();
			if(crlTemp.getLogid().equals(chatRoomLog.getLogid())){
				chatRoomLog = crlTemp;
				break;
			}
		}
		chatRoomLog.setType(4); //将type修改为4，表示删除
		return managerDao.updateChatRoomLog(chatRoomLog);
	}
	
	//更新聊天室聊天记录，主要用于删除聊天记录，逻辑：将type修改为4，表示删除
	@SuppressWarnings("unchecked")
	public boolean updateChatPerLog(ChatPerLog chatPerLog){
		HttpServletRequest request = ServletActionContext.getRequest();
		HttpSession session = request.getSession();
		List<ChatPerLog> chatPerLogs = (List<ChatPerLog>) session.getAttribute("ChatPerLogs");
		//遍历搜索
		Iterator<ChatPerLog> it = chatPerLogs.iterator();
		ChatPerLog cplTemp = null;
		while(it.hasNext()){
			cplTemp =it.next();
			if(cplTemp.getLogid().equals(chatPerLog.getLogid())){
				chatPerLog = cplTemp;
				break;
			}
		}
		chatPerLog.setType(4); //将type修改为4，表示删除
		return managerDao.updateChatPerLog(chatPerLog);
	}
	
	public void removeSession(){
		HttpServletRequest request = ServletActionContext.getRequest();
		HttpSession session = request.getSession();
		//页面
		session.removeAttribute("pageCheck");
		session.removeAttribute("pageCheckUser");
		session.removeAttribute("pageCheckChatRoom");
		session.removeAttribute("pageCheckChatRoomLog");
		session.removeAttribute("pageCheckChatPerLog"); 
		session.removeAttribute("SearchStr"); 
	}

}
