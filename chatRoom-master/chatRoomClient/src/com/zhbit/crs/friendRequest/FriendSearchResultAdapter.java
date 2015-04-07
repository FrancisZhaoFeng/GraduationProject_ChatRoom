package com.zhbit.crs.friendRequest;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.zhbit.crs.R;
import com.zhbit.crs.domain.User;

public class FriendSearchResultAdapter extends BaseAdapter {
	private List<User> mVector;
	private LayoutInflater mInflater;
	private Context mContext0;

	public FriendSearchResultAdapter(Context context, List<User> vector) {
		this.mVector = vector;
		mInflater = LayoutInflater.from(context);
		mContext0 = context;
	}

	public View getView(int position, View convertView, ViewGroup root) {
		ImageView avatarView;
		TextView nameView;
		TextView ageView;
		TextView sexView;

		User user = mVector.get(position);
		String name = user.getUsername();
		int age = user.getAge();
		Boolean sex = user.getSex();
		boolean online = user.getOnline();

		convertView = mInflater.inflate(R.layout.cc0_friend_search_result_item, null);
		avatarView = (ImageView) convertView.findViewById(R.id.cc0_friend_search_result_item_avatar);
		nameView = (TextView) convertView.findViewById(R.id.cc0_friend_search_result_item_name);
		ageView = (TextView) convertView.findViewById(R.id.cc0_friend_search_result_item_age);
		sexView = (TextView) convertView.findViewById(R.id.cc0_friend_search_result_item_gender);

		nameView.setText(name);
		if (sex == false)
			avatarView.setImageResource(online ? R.drawable.cb0_h001 : R.drawable.cb0_h001_dark);
		else
			avatarView.setImageResource(online ? R.drawable.cb0_h003 : R.drawable.cb0_h003_dark);

		ageView.setText(age + "");
		if (sex == false) {
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
