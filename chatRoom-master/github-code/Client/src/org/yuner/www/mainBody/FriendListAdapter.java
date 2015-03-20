package org.yuner.www.mainBody;

import java.util.List;

import org.yuner.www.R;
import org.yuner.www.bean.UserInfo;
import org.yuner.www.chatServices.ChatServiceData;

import android.content.Context;
import android.database.DataSetObserver;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;

public class FriendListAdapter extends BaseExpandableListAdapter {

    private Context mContext;
    private LayoutInflater mInflater = null;
    private List<FriendListGroupItem> mGrpInfo=null;
    private List<List<UserInfo>>   mUsrInfo = null;

    public FriendListAdapter(Context ctx, List<FriendListGroupItem> listGrp, List<List<UserInfo>> listUsr) {
        mContext = ctx;
        mInflater = (LayoutInflater) mContext
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mGrpInfo = listGrp;
        mUsrInfo = listUsr;
    }

    public void setData(List<List<UserInfo>> list) {
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
    public List<UserInfo> getGroup(int groupPosition) {
        // TODO Auto-generated method stub
        return mUsrInfo.get(groupPosition);
    }

    @Override
    public UserInfo getChild(int groupPosition, int childPosition) {
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
    public View getGroupView(int groupPosition, boolean isExpanded,
            View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.cc0_friend_list_group, null);
        }
        GroupViewHolder holder = new GroupViewHolder();
        holder.mGroupName = (TextView) convertView
                .findViewById(R.id.cc0_friend_list_group_name);
        holder.mGroupName.setText(mGrpInfo.get(groupPosition).getName());
        holder.mGroupCount = (TextView) convertView
                .findViewById(R.id.cc0_friend_list_user_count);
        holder.mGroupCount.setText("[" + mUsrInfo.get(groupPosition).size() + "]");
        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition,
            boolean isLastChild, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.cc0_friend_list_item, null);
        }
        ChildViewHolder holder = new ChildViewHolder();
        
        int isOnline = getChild(groupPosition,
                childPosition).getIsOnline();
        Log.d("isonline ? " + isOnline,"++++++++++++++++++++++++++++++++++++++++" +
        		"++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++");
        if(isOnline == 1) {
	        holder.mIcon = (ImageView) convertView.findViewById(R.id.cc0_friend_list_item_avatar);
	        holder.mIcon.setBackgroundResource(getChild(groupPosition,
	                childPosition).getAvatarId()==0 ? R.drawable.cb0_h001 : R.drawable.cb0_h003);
        } else {
        	holder.mIcon = (ImageView) convertView.findViewById(R.id.cc0_friend_list_item_avatar);
	        holder.mIcon.setBackgroundResource(getChild(groupPosition,
	                childPosition).getAvatarId()==0 ? R.drawable.cb0_h001_dark : R.drawable.cb0_h003_dark);
        }
        
        holder.mChildName = (TextView) convertView.findViewById(R.id.cc0_friend_list_item_name);
        holder.mChildName.setText(getChild(groupPosition, childPosition)
                .getName());
        
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
        /*很重要：实现ChildView点击事件，必须返回true*/
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