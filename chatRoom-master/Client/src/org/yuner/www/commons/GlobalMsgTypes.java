package org.yuner.www.commons;

/**
 * @author zhaoguofeng
 * 定义全局变量类型，
 */
public class GlobalMsgTypes {

	public static final int msgPublicRoom = 0;
	public static final int msgChattingRoom = 1;
	/**
	 * 好友信息类型，并需告知服务器已获得该信息
	 */
	public static final int msgFromFriend = 2;
	/**
	 * 握手类型,登陆时会用到
	 */
	public static final int msgHandShake = 3;
	/**
	 * 获得好友列表类型，登陆时会用到
	 */
	public static final int msgHandSHakeFriendList = 5;
	/**
	 * 更新好友列表类型
	 */
	public static final int msgUpdateFriendList = 6;
	/**
	 * 注册信息类型
	 */
	public static final int msgSignUp = 7;
	/**
	 * 搜索用户类型
	 */
	public static final int msgSearchPeople = 9;
	/**
	 * 好友请求类型，并需告知服务器已获得该信息
	 */
	public static final int msgFriendshipRequest = 11;
	/**
	 * 好友请求回应类型，并需告知服务器已获得该信息
	 */
	public static final int msgFriendshipRequestResponse = 12;
	/**
	 * 好友在线类型 
	 */
	public static final int msgFriendGoOnline = 13;
	/**
	 * 好友不在线类型
	 */
	public static final int msgFriendGoOffline = 14;
	public static final int msgUpdateUserInfo = 15;
	public static final int msgAskForUnsendMsgs = 16;
	public static final int msgMsgReceived = 17;
	/**
	 * 用户重新上线类型
	 */
	public static final int msgBackOnline = 18;

}
