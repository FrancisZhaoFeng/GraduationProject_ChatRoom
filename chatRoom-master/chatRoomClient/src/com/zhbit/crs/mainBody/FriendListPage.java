package com.zhbit.crs.mainBody;

import java.util.ArrayList;
import java.util.List;

import org.yuner.www.R;

import android.content.Context;
import android.view.View;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnChildClickListener;

import com.zhbit.crs.chatServices.ChatService;
import com.zhbit.crs.chatServices.ChatServiceData;
import com.zhbit.crs.chatServices.FriendListInfo;
import com.zhbit.crs.domain.User;

public class FriendListPage implements OnChildClickListener {

	private static FriendListPage mInstance;
	
	private View mViewOfPage;
	private Context mContext0;
	
	private Button mBtnBack;
    private PatchedExpandableListView mListView = null;
    private FriendListAdapter mAdapter = null;
    
    private List<FriendListGroupItem> mGrpInfo = null;
    private List<List<User>> mUsrInfo = null;
    
    public static FriendListPage getInstance() {
    	if(mInstance == null) {
    		mInstance = new FriendListPage();
    	}
    	return mInstance;
    }
    
    public void onInit(View view, Context cont) {
    	mViewOfPage = view;
    	mContext0 = cont;
    }
    
    public void onCreate() {
		
		mInstance = this;
		mBtnBack = (Button)mViewOfPage.findViewById(R.id.cc0_friend_list_btn_back);
		mListView = (PatchedExpandableListView)mViewOfPage.findViewById(R.id.cc0_friend_list_listview);        
        mListView.setGroupIndicator(mContext0.getResources().getDrawable(
                R.drawable.expander_floder));

        mBtnBack.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				MainBodyActivity.getInstance().gotoPage(MainBodyActivity.mTabNumContacts, MainBodyActivity.mPageChooseRoom);
			}
		});
        
        onFriendListUpdate();
		
    }

    /*
     * ChildView 设置 布局很可能onChildClick进不来，要在 ChildView layout 里加上
     * android:descendantFocusability="blocksDescendants",
     * 还有isChildSelectable里返回true
     */
    @Override
    public boolean onChildClick(ExpandableListView parent, View v,
            int groupPosition, int childPosition, long id) {
        // TODO Auto-generated method stub
    	
    	int pos = childPosition;
    	int id_x = ChatServiceData.getInstance().getIdForPos(pos);
		ChatService.getInstance().setType(2);
		ChatService.getInstance().setId(id_x);
		ChatService.getInstance().setEnterFromNotification(false);
		ChatServiceData.getInstance().clearUnreadMsgs(id_x);
		
		MainBodyActivity.getInstance().gotoChatActivity();
    	
        return true;
    }

    private void initData() {
    	mGrpInfo = new ArrayList<FriendListGroupItem>();
        mUsrInfo = new ArrayList<List<User>>();
    	
    	List<User> curListFriends = FriendListInfo.getFriendListInfo().getFriendList();
    	
    	mGrpInfo.add(new FriendListGroupItem("My Friend",curListFriends.size()));
    	
    	mUsrInfo.add(curListFriends);
    }
    
    public void onFriendListUpdate() {
    	boolean isExpanded;
    	
    	try {
    		isExpanded = mListView.isGroupExpanded(0);
    	} catch(Exception e) { isExpanded = true; }

    	initData();
    	
    	mAdapter = new FriendListAdapter(mContext0, mGrpInfo, mUsrInfo);
        mListView.setAdapter(mAdapter);
        mListView.setDescendantFocusability(ExpandableListView.FOCUS_AFTER_DESCENDANTS);
        mListView.setOnChildClickListener(this);
        
        if(isExpanded) {
        	mListView.expandGroup(0);
        } else {
        	mListView.collapseGroup(0);
        }
    }

}
