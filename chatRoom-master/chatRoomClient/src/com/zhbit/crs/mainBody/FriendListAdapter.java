package com.zhbit.crs.mainBody;

import java.util.List;

import android.content.Context;
import android.database.DataSetObserver;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.zhbit.crs.R;
import com.zhbit.crs.domain.User;

public class FriendListAdapter extends BaseExpandableListAdapter {

	private Context mContext;
	private LayoutInflater mInflater = null;
	private List<FriendListGroupItem> mGrpInfo = null;
	private List<List<User>> mUsrInfo = null;

	public FriendListAdapter(Context ctx, List<FriendListGroupItem> listGrp, List<List<User>> listUsr) {
		mContext = ctx;
		mInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		mGrpInfo = listGrp;
		mUsrInfo = listUsr;
	}

	public void setData(List<List<User>> list) {
		mUsrInfo = list;
	}

	@Override
	public int getGroupCount() {
		// TODO Auto-generated method stub
		return mUsrInfo.size();
	}

	@Override
	public int getChildrenCount(int groupPosition) {
		// TODO Auto-generated method stub
		return mUsrInfo.get(groupPosition).size();
	}

	@Override
	public List<User> getGroup(int groupPosition) {
		// TODO Auto-generated method stub
		return mUsrInfo.get(groupPosition);
	}

	@Override
	public User getChild(int groupPosition, int childPosition) {
		// TODO Auto-generated method stub
		return mUsrInfo.get(groupPosition).get(childPosition);
	}

	@Override
	public long getGroupId(int groupPosition) {
		// TODO Auto-generated method stub
		return groupPosition;
	}

	@Override
	public long getChildId(int groupPosition, int childPosition) {
		// TODO Auto-generated method stub
		return childPosition;
	}

	@Override
	public boolean hasStableIds() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		if (convertView == null) {
			convertView = mInflater.inflate(R.layout.cc0_friend_list_group, null);
		}
		GroupViewHolder holder = new GroupViewHolder();
		holder.mGroupName = (TextView) convertView.findViewById(R.id.cc0_friend_list_group_name);
		holder.mGroupName.setText(mGrpInfo.get(groupPosition).getName());
		holder.mGroupCount = (TextView) convertView.findViewById(R.id.cc0_friend_list_user_count);
		holder.mGroupCount.setText("[" + mUsrInfo.get(groupPosition).size() + "]");
		return convertView;
	}

	/* (non-Javadoc)
	 * @see android.widget.ExpandableListAdapter#getChildView(int, int, boolean, android.view.View, android.view.ViewGroup)
	 * 显示Friend List
	 */
	@Override
	public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		if (convertView == null) {
			convertView = mInflater.inflate(R.layout.cc0_friend_list_item, null);
		}
		ChildViewHolder holder = new ChildViewHolder();

		boolean isOnline = getChild(groupPosition, childPosition).getOnline();
		Log.d("isonline ? " + isOnline, "++++++++++++++++++++++++++++++++++++++++" + "++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++");
		if (isOnline == true) {
			holder.mIcon = (ImageView) convertView.findViewById(R.id.cc0_friend_list_item_avatar);
			holder.mIcon.setBackgroundResource(!getChild(groupPosition, childPosition).getSex() ? R.drawable.cb0_h001 : R.drawable.cb0_h003);
		} else {
			holder.mIcon = (ImageView) convertView.findViewById(R.id.cc0_friend_list_item_avatar);
			holder.mIcon.setBackgroundResource(!getChild(groupPosition, childPosition).getSex() ? R.drawable.cb0_h001_dark : R.drawable.cb0_h003_dark);
		}

		holder.mChildName = (TextView) convertView.findViewById(R.id.cc0_friend_list_item_name);
		holder.mChildName.setText(getChild(groupPosition, childPosition).getUsername());

		return convertView;
	}

	@Override
	public void unregisterDataSetObserver(DataSetObserver observer) {
		if (observer != null) {
			super.unregisterDataSetObserver(observer);
		}
	}

	@Override
	public boolean isChildSelectable(int groupPosition, int childPosition) {
		// TODO Auto-generated method stub
		/* 很重要：实现ChildView点击事件，必须返回true */
		return true;
	}

	private class GroupViewHolder {
		TextView mGroupName;
		TextView mGroupCount;
	}

	private class ChildViewHolder {
		ImageView mIcon;
		TextView mChildName;
	}

}