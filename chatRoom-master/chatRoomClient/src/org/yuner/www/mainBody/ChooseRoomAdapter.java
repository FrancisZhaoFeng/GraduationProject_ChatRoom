package org.yuner.www.mainBody;

import java.util.ArrayList;
import java.util.List;

import org.yuner.www.R;
import org.yuner.www.chatter.ChatActivity;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class ChooseRoomAdapter extends BaseAdapter{
	private List<String> mVector;
	private LayoutInflater mInflater;
	private Context mContext0;
	
	public ChooseRoomAdapter(Context context) {
		mContext0=context;
		
		this.mVector = new ArrayList();
		mVector.add("Groups");
		mVector.add("Friends");
		mInflater = LayoutInflater.from(context);
	}

	public View getView(int position, View convertView, ViewGroup root) {
		String name=mVector.get(position);
		
		convertView = mInflater.inflate(R.layout.cc0_choose_room_item, null);
		ImageView contactView = (ImageView)convertView.findViewById(R.id.cc0_choose_room_item_icon);
		if(position == 0) {
			contactView.setBackgroundResource(R.drawable.contacts_for_group);
		} else {
			contactView.setBackgroundResource(R.drawable.contacts_for_people);
		}
		TextView contact = (TextView) convertView.findViewById(R.id.cc0ChooseRoomItemLabel);
		contact.setText(name);

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