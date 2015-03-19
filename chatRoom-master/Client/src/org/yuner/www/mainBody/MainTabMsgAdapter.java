package org.yuner.www.mainBody;

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
import org.yuner.www.bean.TabMsgItemEntity;
import org.yuner.www.chatServices.ChatServiceData;

public class MainTabMsgAdapter extends BaseAdapter{
	
	private List<TabMsgItemEntity> mVector;
	private LayoutInflater mInflater;
	private Context mContext0;
	
	public MainTabMsgAdapter(Context context,List<TabMsgItemEntity> vector){
		this.mVector = vector;
		mInflater = LayoutInflater.from(context);
		mContext0=context;
	}

	public View getView(int position, View convertView, ViewGroup root) {
		ImageView avatarV;
		TextView unreadV;
		TextView nameOfSpeakerV;
		TextView textV;
		TextView timeV;
		
		String name = mVector.get(position).getName();
		int unread = 0;
		if(mVector.get(position).getTalkerId() > 0) {
			unread = ChatServiceData.getInstance().getUnreadMsgs(mVector.get(position).getTalkerId());
		}
		int avatarId = mVector.get(position).getImgId();
		String sentence = mVector.get(position).getSentence();
		String time = mVector.get(position).getTime();
		
		convertView = mInflater.inflate(R.layout.tabmsg_item, null);
		avatarV = (ImageView)convertView.findViewById(R.id.tabmsg_item_left_icon);
		unreadV = (TextView)convertView.findViewById(R.id.tabmsg_item_msg_indicator);
		nameOfSpeakerV = (TextView)convertView.findViewById(R.id.tabmsg_item_text_name);
		textV = (TextView)convertView.findViewById(R.id.tabmsg_item_text_latest_msg);
		timeV = (TextView)convertView.findViewById(R.id.tabmsg_item_text_time);
		
		if(avatarId==0)
			avatarV.setImageResource(R.drawable.cb0_h001);
		else
			avatarV.setImageResource(R.drawable.cb0_h003);
		if(unread == 0) {
			unreadV.setVisibility(View.INVISIBLE);
		} else {
			unreadV.setVisibility(View.VISIBLE);
			if(unread < 10) {
				unreadV.setText("" + unread);
			} else {
				unreadV.setText("9+");
			}
		}
		nameOfSpeakerV.setText(name);
		textV.setText(sentence);
		timeV.setText(time);
		
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