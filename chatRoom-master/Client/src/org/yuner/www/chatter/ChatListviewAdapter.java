/**
 * ChatListviewAdapter is responsible for updating the content and presentation of 
 * the chatting history Listview in ChatActivity
 * 
 * the major function here is getView to control the display of each child view in listview
 */

package org.yuner.www.chatter;

import java.util.List;
import java.util.Vector;

import android.content.Context;
import android.graphics.Color;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import org.yuner.www.R;
import org.yuner.www.bean.ChatEntity;

public class ChatListviewAdapter extends BaseAdapter{
	private List<ChatEntity> mVector;
	private List<Boolean> mVectorIsSelf;
	private LayoutInflater mInflater;
	private Context mContext0;
	
	public ChatListviewAdapter(ChatActivity par,Context context,List<ChatEntity> vector, 
			List<Boolean> vectorIsSelf){
		this.mVector = vector;
		mInflater = LayoutInflater.from(context);
		mContext0=context;
		mVectorIsSelf = vectorIsSelf;
	}

	public View getView(int position, View convertView, ViewGroup root) {
		ImageView avatar;
		TextView content;
		TextView NameOfSpeaker;
		
		ChatEntity ent0=mVector.get(position);
		String name=ent0.getName();
		String time=ent0.getTime();
		int sex=ent0.getSex();
		String real_msg=ent0.getContent();
		
		if(mVectorIsSelf.get(position).booleanValue())
		{
			convertView = mInflater.inflate(R.layout.cb0_chat_listview_item_right, null);
			content=(TextView) convertView.findViewById(R.id.cb0ChatListviewMsgRight);
			NameOfSpeaker=(TextView) convertView.findViewById(R.id.cb0ChatListviewNameRight);
			avatar=(ImageView) convertView.findViewById(R.id.cb0ChatListviewAvatarRight);
			
			String zhengze = "f0[0-9]{2}|f10[0-7]";											//正则表达式，用来判断消息内是否有表情
			try {
				SpannableString spannableString = ChatEmoticonUtil.getExpressionString(mContext0, real_msg, zhengze);
				content.setText(spannableString);
			} catch (NumberFormatException e) {
				e.printStackTrace();
			} catch (SecurityException e) {
				e.printStackTrace();
			} catch (IllegalArgumentException e) {
				e.printStackTrace();
			}
			
			NameOfSpeaker.setText(time);
			
			int avatarId = ent0.getSenderAvatarid();
			if(avatarId==0)
				avatar.setImageResource(R.drawable.cb0_h001);
			else
				avatar.setImageResource(R.drawable.cb0_h003);
		}
		else
		{
			convertView = mInflater.inflate(R.layout.cb0_chat_listview_item_left, null);
			
			content=(TextView) convertView.findViewById(R.id.cb0ChatListviewMsgLeft);
			
			String zhengze = "f0[0-9]{2}|f10[0-7]";											//正则表达式，用来判断消息内是否有表情
			try {
				SpannableString spannableString = ChatEmoticonUtil.getExpressionString(mContext0, real_msg, zhengze);
				content.setText(spannableString);
			} catch (NumberFormatException e) {
				e.printStackTrace();
			} catch (SecurityException e) {
				e.printStackTrace();
			} catch (IllegalArgumentException e) {
				e.printStackTrace();
			}
			
			NameOfSpeaker=(TextView) convertView.findViewById(R.id.cb0ChatListviewNameLeft);
			NameOfSpeaker.setText(name+" "+time);
			
			avatar=(ImageView) convertView.findViewById(R.id.cb0ChatListviewAvatarLeft);
			int avatarId = ent0.getSenderAvatarid();
			if(avatarId==0)
				avatar.setImageResource(R.drawable.cb0_h001);
			else
				avatar.setImageResource(R.drawable.cb0_h003);
		}

		return convertView;
	}
	
	public int getCount() {
		return mVector.size();
	}

	public Object getItem(int position) {
		return mVector.get(position);
	}

	public long getItemId(int position) {
		return position;
	}
}