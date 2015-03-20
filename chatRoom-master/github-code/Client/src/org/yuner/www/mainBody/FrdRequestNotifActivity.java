package org.yuner.www.mainBody;

import java.util.ArrayList;

import org.yuner.www.ConnectedApp;
import org.yuner.www.R;
import org.yuner.www.bean.FrdReqNotifItemEntity;
import org.yuner.www.bean.UserInfo;
import org.yuner.www.chatServices.FriendListInfo;
import org.yuner.www.chatter.ChatActivity;
import org.yuner.www.commons.GlobalInts;
import org.yuner.www.commons.GlobalMsgTypes;
import org.yuner.www.commons.GlobalStrings;
import org.yuner.www.myNetwork.NetConnect;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

public class FrdRequestNotifActivity extends Activity {

	private static FrdRequestNotifActivity mInstance;
	private static ArrayList<FrdReqNotifItemEntity> mListOfNotif;
	
	public static FrdRequestNotifActivity getInstance() {
		return mInstance;
	}
	
	public static ArrayList<FrdReqNotifItemEntity> getListOfNotif() {
		if(mListOfNotif == null) {
			mListOfNotif = new ArrayList<FrdReqNotifItemEntity>();
		}
		return mListOfNotif;
	}
	
	public static void clearListOfNotif() {
		mListOfNotif = null;
	}
	
	public static void newNotification(int type, int id, int imgId, String name, String content, String time, String strOfUser) {
		FrdReqNotifItemEntity entity = new FrdReqNotifItemEntity(type, id, imgId, name, content, time, strOfUser);
		
		if(mListOfNotif == null) {
			mListOfNotif = new ArrayList<FrdReqNotifItemEntity>();
		}
		mListOfNotif.add(entity);
	}
	
	private boolean mIsCurPage = false;
	private ListView mListView0;
	private FrdReqNotifItemEntity mEntity;
	private Dialog mDialog0;
	
	@Override
	protected void onCreate(Bundle savedInstanceState){ 
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
//		this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
//				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.tabmsg_frd_req_notif_page);
		
		mInstance = this;
		if(mListOfNotif == null) {
			mListOfNotif = new ArrayList<FrdReqNotifItemEntity>();
		}
		
		mListView0 = (ListView)findViewById(R.id.tabmsg_frd_req_notif_page_listview);
		
		mListView0.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
			}
		});
	}
	
	@Override
	public void onResume() {
		super.onResume();
		mIsCurPage = true;
		onUpdateView();
		
		try {
			FrdRequestNotifActivity preced = ConnectedApp.getInstance().getFrdRequestNotifActivity();
			preced.finish();
		} catch(Exception e) {}
		
		ConnectedApp.getInstance().setFrdRequestNotifActivity(this);
	}
	
	@Override
	public void onPause() {
		super.onPause();
		mIsCurPage = false;
	}
	
	public void onPopupForResponse2(int position) {
		Intent intent0=new Intent(FrdRequestNotifActivity.this,TabMsgFrdReqProcActivity.class);
		mEntity = mListOfNotif.get(position);
		
		if(mEntity.getType() == FrdReqNotifItemEntity.mTypeFrdReq && 
				mEntity.getStatus() == FrdReqNotifItemEntity.mUnanswer) {
			intent0.putExtra("itemStr", mEntity.toString());
			intent0.putExtra("itemPos", position);
			startActivity(intent0.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT));
			overridePendingTransition(R.anim.my_slide_right_in,R.anim.my_slide_left_out);
		}
	}
	
	/*
	public void onPopupForResponse(int position) {
		mEntity = mListOfNotif.get(position);
    	
    	if(mEntity.getType() == FrdReqNotifItemEntity.mTypeFrdReq) {
    		LayoutInflater inflater = LayoutInflater.from(TabMsgFrdReqNotifActivity.this);
    		View conView = inflater.inflate(R.layout.cc0_friendship_request_popup, null);
    		
    		mDialog0 = new Dialog(TabMsgFrdReqNotifActivity.this);
    		mDialog0.setContentView(conView);
    		mDialog0.show();
    		
    		Button btnOk = (Button)conView.findViewById(R.id.cc0_friendship_request_popup_btn_agree);
    		Button btnNo = (Button)conView.findViewById(R.id.cc0_friendship_request_popup_btn_refuse);
    		ImageView avatarView = (ImageView)conView.findViewById(R.id.cc0_friendship_request_popup_requester_avatar);
    		TextView nameView = (TextView)conView.findViewById(R.id.cc0_friendship_request_popup_requester_name);
    		
    		if(mEntity.getImgId() == 0) {
    			avatarView.setImageResource(R.drawable.cb0_h001);
    		} else {
    			avatarView.setImageResource(R.drawable.cb0_h003);
    		}
    		
    		nameView.setText(mEntity.getName());
    		
    		btnOk.setOnClickListener(new View.OnClickListener() {
    			@Override
    			public void onClick(View v) {
    				String toSend = GlobalInts.idAcceptFriendship + GlobalStrings.friendshipRequestDivider +
    						mEntity.getStrOfUser() + GlobalStrings.friendshipRequestDivider +
    						ConnectedApp.getInstance().getUserInfo().toString() + GlobalStrings.friendshipRequestDivider;
    				NetConnect.getnetConnect().sendUpload(GlobalMsgTypes.msgFriendshipRequestResponse + "");
    				NetConnect.getnetConnect().sendUpload(toSend);
    				
    				mDialog0.dismiss();
    				
    				mEntity.setStatus(FrdReqNotifItemEntity.mAccepted);
    				
    				FriendListInfo.getFriendListInfo().uponMakeNewFriend(new UserInfo(mEntity.getStrOfUser()));
    			}
    		});
    		
    		btnNo.setOnClickListener(new View.OnClickListener() {
    			@Override
    			public void onClick(View v) {
    				String toSend = GlobalInts.idRefuseFriendship + GlobalStrings.friendshipRequestDivider +
    						mEntity.getStrOfUser() + GlobalStrings.friendshipRequestDivider +
    						ConnectedApp.getInstance().getUserInfo().toString() + GlobalStrings.friendshipRequestDivider;
    				NetConnect.getnetConnect().sendUpload(GlobalMsgTypes.msgFriendshipRequestResponse + "");
    				NetConnect.getnetConnect().sendUpload(toSend);
    				
    				mDialog0.dismiss();
    				
    				mEntity.setStatus(FrdReqNotifItemEntity.mDeclined);
    			}
    		});
    	}
	}
	*/
	
	public void setItemStatus(int pos, int status) {
		mEntity = mListOfNotif.get(pos);
		mEntity.setStatus(status);
	}
	
	public boolean getIsCurPage() {
		return mIsCurPage;
	}
	
	public void onUpdateView() {		
		mListView0.setAdapter(new FrdRequestNotifAdapter(this, mListOfNotif));
	}
	
}
