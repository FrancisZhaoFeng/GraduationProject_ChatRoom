package org.yuner.www.beans;

import java.util.*;
import java.text.*;

import org.yuner.www.commons.*;

public class ChatEntity {
	public static final String strSplitter = GlobalStrings.entityDivider;
	
	private int type=0;
	private int senderId=0;
	private int senderAvatarId=0;
	private String nick="";
	private int sex=0;
	private String time="";
	private String content="";
	
	private int receiverId=0;
	
	public ChatEntity(){}
	
	public static ChatEntity Str2Ent(String str0)
	{
		ChatEntity ent0=new ChatEntity();
		
		String[] sbArr0=str0.split(strSplitter);
		
		ent0.setType(Integer.parseInt(sbArr0[0]));
		ent0.setSenderId(Integer.parseInt(sbArr0[1]));
		ent0.setSenderAvatarId(Integer.parseInt(sbArr0[2]));		
		ent0.setNick(sbArr0[3]);
		ent0.setSex(Integer.parseInt(sbArr0[4]));
		ent0.setTime(sbArr0[5]);
		ent0.setContent(sbArr0[6]);
		ent0.setReceiverId(Integer.parseInt(sbArr0[7]));
		
		return ent0;
	}
	
	public String toString()
	{
		String str=this.type+strSplitter;
		str+=this.senderId+strSplitter;
		str+=this.senderAvatarId+strSplitter;
		str+=this.nick+strSplitter;
		str+=this.sex+strSplitter;
		str+=this.time+strSplitter;
		str+=this.content+strSplitter;
		str+=this.receiverId+strSplitter;
		return str;
	}
	
	public static String genDate()
	{
		DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd-HH:mm:ss");
		Date date = new Date();
		String timex=dateFormat.format(date);
		return timex;
	}

	public int getType()
	{
		return type;
	}
	
	public void setType(int type0)
	{
		this.type=type0;
	}
	
	public int getSenderId()
	{
		return senderId;
	}
	
	public void setSenderId(int senderId0)
	{
		this.senderId=senderId0;
	}
	
	public int getSenderAvatarid()
	{
		return senderAvatarId;
	}
	
	public void setSenderAvatarId(int senderAvatarId0)
	{
		this.senderAvatarId=senderAvatarId0;
	}
	
	public String getNick() {
		return nick;
	}

	public void setNick(String nick) {
		this.nick = nick;
	}
	
	public int getSex() {
		return sex;
	}

	public void setSex(int sex) {
		this.sex = sex;
	}
	
	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}
	
	public int getReceiverId()
	{
		return receiverId;
	}
	
	public void setReceiverId(int receiverId0)
	{
		this.receiverId=receiverId0;
	}
}
