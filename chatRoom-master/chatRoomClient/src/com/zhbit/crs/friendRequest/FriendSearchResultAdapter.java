package com.zhbit.crs.friendRequest;

import java.util.List;

import org.yuner.www.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.zhbit.crs.domain.User;

public class FriendSearchResultAdapter extends BaseAdapter{
	private List<User> mVector;
	private LayoutInflater mInflater;
	private Context mContext0;
	
	public FriendSearchResultAdapter(Context context,List<User> vector) {
		this.mVector = vector;
		mInflater = LayoutInflater.from(context);
		mContext0=context;
	}

	public View getView(int position, View convertView, ViewGroup root) {
		ImageView avatarView;
		TextView nameView;
		TextView ageView;
		TextView sexView;
		
		User uu0 = mVector.get(position);
		int avatarId = uu0.getUserid();
		String name = uu0.getUsername();
		int age = uu0.getAge();
		Boolean sex = uu0.getSex();
		
		convertView = mInflater.inflate(R.layout.cc0_friend_search_result_item, null);
		avatarView = (ImageView)convertView.findViewById(R.id.cc0_friend_search_result_item_avatar);
		nameView = (TextView)convertView.findViewById(R.id.cc0_friend_search_result_item_name);
		ageView = (TextView)convertView.findViewById(R.id.cc0_friend_search_result_item_age);
		sexView = (TextView)convertView.findViewById(R.id.cc0_friend_search_result_item_gender);
		
		nameView.setText(name);
		if(avatarId==0)
			avatarView.setImageResource(R.drawable.cb0_h001);
		else
			avatarView.setImageResource(R.drawable.cb0_h003);
		
		ageView.setText(age + "");
		if(sex == false) {
			sexView.setText("girl");
		} else {
			sexView.setText("guy");
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
