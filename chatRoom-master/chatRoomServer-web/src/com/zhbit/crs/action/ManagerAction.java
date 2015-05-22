package com.zhbit.crs.action;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.struts2.ServletActionContext;

import com.opensymphony.xwork2.ActionSupport;
import com.zhbit.crs.domain.ChatPerLog;
import com.zhbit.crs.domain.ChatRoom;
import com.zhbit.crs.domain.ChatRoomLog;
import com.zhbit.crs.domain.Manager;
import com.zhbit.crs.domain.Mkey;
import com.zhbit.crs.domain.User;
import com.zhbit.crs.service.ManagerService;

public class ManagerAction extends ActionSupport {
	@Resource
	private ManagerService managerService;
	private Manager manager;
	private Mkey mkey;
	private String message;
	private String repwd;
	private User user;
	private String oldpwd;
	private ChatRoomLog chatRoomLog;
	private ChatPerLog chatPerLog;
	private ChatRoom chatRoom;
	private String searchStr;

	public String getSearchStr() {
		return searchStr;
	}

	public void setSearchStr(String searchStr) {
		this.searchStr = searchStr;
	}

	public ChatPerLog getChatPerLog() {
		return chatPerLog;
	}

	public void setChatPerLog(ChatPerLog chatPerLog) {
		this.chatPerLog = chatPerLog;
	}

	public ChatRoom getChatRoom() {
		return chatRoom;
	}

	public void setChatRoom(ChatRoom chatRoom) {
		this.chatRoom = chatRoom;
	}

	public ManagerService getManagerService() {
		return managerService;
	}

	public void setManagerService(ManagerService managerService) {
		this.managerService = managerService;
	}

	public ChatRoomLog getChatRoomLog() {
		return chatRoomLog;
	}

	public void setChatRoomLog(ChatRoomLog chatRoomLog) {
		this.chatRoomLog = chatRoomLog;
	}

	public String getOldpwd() {
		return oldpwd;
	}

	public void setOldpwd(String oldpwd) {
		this.oldpwd = oldpwd;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public String getRepwd() {
		return repwd;
	}

	public void setRepwd(String repwd) {
		this.repwd = repwd;
	}

	public Mkey getMkey() {
		return mkey;
	}

	public void setMkey(Mkey mkey) {
		this.mkey = mkey;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public Manager getManager() {
		return manager;
	}

	public void setManager(Manager manager) {
		this.manager = manager;
	}
	
	// 注册
	public String register() {
		System.out.println("密码"+manager.getPassword()+"-"+repwd+"m码："+mkey.getMid());
		if(manager.getPassword().equals("")){
			this.setMessage("密码不能为空，请输入密码");
			return "REGISTER";   //REGISTER
		}
		if (!manager.getPassword().equals(repwd)) {
			this.setMessage("密码不一致,请重新输入");
			return "REGISTER";   //REGISTER
		}
		if( mkey.getMid() == null){
			this.setMessage("M码不能为空，请重新输入");
			return "REGISTER";
		}
		if (managerService.insertManager(manager, mkey)) {
			this.setMessage("注册成功，请登录");
			return "LOGIN";
		} else {
			if (mkey.getUsed() == true) {
				this.setMessage("M码错误或已被使用，请重新输入");
				return "REGISTER";
			}
			this.setMessage("错误");
			return "REGISTER";
		}
		
	}

	// 登陆
	public String login() {
		System.out.println("===:" + manager.getPassword());
		if (managerService.login(manager)) {
			return "HOMEPAGE";
		} else {
			this.setMessage("输入密码错误，请重新输入!");
			return "LOGIN";
		}
	}

	// 管理员管理用户，删除用户
	public String exitLogin() {
		HttpServletRequest request = ServletActionContext.getRequest();
		HttpSession session = request.getSession();
		String exit = request.getParameter("exit");
		if (exit.equals("true")) {
			//数据
			session.removeAttribute("CheckUsers");
			session.removeAttribute("CheckRooms");
			session.removeAttribute("CheckRoomLogs");
			session.removeAttribute("CheckPerLogs");
			//用户
			session.removeAttribute("ssiMng");
			session.removeAttribute("ssiMngId");
			session.removeAttribute("ssiMngName");
			//页面
			managerService.removeSession();
		}
		return "HOMEPAGE";
	}

	// 查看所有用户
	public String selectUser() {
		managerService.removeSession();
		if (managerService.selectUser(searchStr)) {
			this.clearValue();
			return "CHECK";
		} else {
			this.clearValue();
			this.setMessage("查看失败");
			
			return "FALSE";
		}
	}

	// 查看所有聊天室
	public String selectChatRoom() {
		managerService.removeSession();
		if (managerService.selectChatRoom(searchStr)) {
			this.clearValue();
			return "CHECK";
		} else {
			this.clearValue();
			this.setMessage("查看失败或不存在聊天室");
			return "HOMEPAGE";
		}
	}

	// 查看聊天室聊天记录
	public String selectChatRoomLog() {
		managerService.removeSession();
		if (managerService.selectChatRoomLog(searchStr)) {
			this.clearValue();
			return "CHECK";
		} else {
			this.clearValue();
			this.setMessage("查看失败或不存在聊天室聊天记录");
			return "HOMEPAGE";
		}
	}

	// 查看好友天记录
	public String selectChatPerLog() {
		managerService.removeSession();
		if (managerService.selectChatPerLog(searchStr)) {
			this.clearValue();
			return "CHECK";
		} else {
			this.clearValue();
			this.setMessage("查看失败");
			return "FALSE";
		}
	}
	
	// 搜索字符串
	public String searchStr() {
		managerService.removeSession();
		managerService.selectUser(searchStr);
		managerService.selectChatRoom(searchStr);
		managerService.selectChatRoomLog(searchStr);
		managerService.selectChatPerLog(searchStr);
		HttpServletRequest request = ServletActionContext.getRequest();
		HttpSession session = request.getSession();
		session.setAttribute("SearchStr", "searchStr");
		this.clearValue();
		return "CHECK";
	}

	// 更新用户，主要用于限制用户权限
	public String updateUser() {
		if (managerService.updateUser(user)) {
			managerService.selectUser(searchStr);
			return "CHECK";
		} else {
			return "FALSE";
		}
	}

	// 更新管理员，主要用于更新管理员密码
	public String updateManager() {
		System.out.println("修改密码："+manager.getPassword()+"-"+repwd);
		if(manager.getPassword().equals("") || repwd.equals("")){
			this.setMessage("新密码不能为空，请重新输入");
			return "CHANGEPWD";
		}
		String info = managerService.updateManager(oldpwd, repwd, manager);
		this.setMessage(info);
		if (info.equals("修改密码成功，请重新登陆！")) {
			return "LOGIN";
		} else {
			return "CHANGEPWD";
		}
	}

	// 更新聊天室，主要用于删除聊天室
	public String updateChatRoom() {
		if (managerService.updateChatRoom(chatRoom)) {
			managerService.selectChatRoomLog(searchStr);
			return "CHECK";
		} else {
			return "FALSE";
		}
	}

	// 更新用户，主要用于限制用户权限
	public String updateChatRoomLog() {
		if (managerService.updateChatRoomLog(chatRoomLog)) {
			managerService.selectChatRoomLog(searchStr);
			return "CHECK";
		} else {
			return "FALSE";
		}
	}

	// 更新用户，主要用于限制用户权限
	public String updateChatPerLog() {
		if (managerService.updateChatPerLog(chatPerLog)) {
			managerService.selectChatPerLog(searchStr);
			return "CHECK";
		} else {
			return "FALSE";
		}
	}
	
	//清空临时变量
	public void clearValue(){
		// this.setRepwd(null);
		// this.setOldpwd(null);
		this.setMessage(null);
		this.setSearchStr(null);
	}
}
