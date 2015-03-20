package org.yuner.www.mainBody;

import java.util.ArrayList;
import java.util.List;

import org.yuner.www.ConnectedApp;
import org.yuner.www.R;
import org.yuner.www.bean.TabMsgItemEntity;
import org.yuner.www.bean.UserInfo;
import org.yuner.www.chatServices.ChatService;
import org.yuner.www.chatServices.ChatServiceData;
import org.yuner.www.chatServices.FriendListInfo;
import org.yuner.www.chatter.ChatActivity;
import org.yuner.www.util.DbSaveOldMsg;

import android.app.ActivityManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ExpandableListView;
import android.widget.ListView;

public class MainTabMsgPage{
	
	private static MainTabMsgPage mInstance;
	
	private View mViewOfPage;
	private Context mContext0;
	
	private ListView mListView;
	private List<Integer> mListOfIds;
	private List<TabMsgItemEntity> mListOfEntity;
	
	private int mNotifId = 1;
	
	public static MainTabMsgPage getInstance(){
		if(mInstance == null) {
			mInstance = new MainTabMsgPage();
		}
		return mInstance;
	}
	
	private MainTabMsgPage() {
	}
	
	public void onInit(View view, Context context) {
		mViewOfPage = view;
		mContext0 = context;
	}
	
	public List<TabMsgItemEntity> getListOfEntity() {
		return mListOfEntity;
	}
	
	public void onCreate() {
		mListView = (ListView)mViewOfPage.findViewById(R.id.tabmsg_main_listview);
		mListOfIds = new ArrayList<Integer>();
		mListOfEntity = new ArrayList<TabMsgItemEntity>();
		
		DbSaveOldMsg.onInit(mContext0);
		DbSaveOldMsg.getInstance().getTabMsgItem(ConnectedApp.getInstance().getUserInfo().getId(), 
				(ArrayList<TabMsgItemEntity>)mListOfEntity);
		for(TabMsgItemEntity ent0 : mListOfEntity) {
			mListOfIds.add(ent0.getTalkerId());
		}
		
		mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				
		    	int id_x = mListOfEntity.get(position).getTalkerId();
				
		    	if(id_x > 0) {
					ChatService.getInstance().setType(2);
					ChatService.getInstance().setId(id_x);
					ChatService.getInstance().setEnterFromNotification(false);
					ChatServiceData.getInstance().clearUnreadMsgs(id_x);
					MainBodyActivity.getInstance().gotoChatActivity();
		    	} else if (id_x == TabMsgItemEntity.FrdReqNotifId) {
		    		MainBodyActivity.getInstance().gotoTabMsgFrdReqNotifActivity();
		    	}
			}
		});
	}
	
	public void onUpdateByUserinfo(UserInfo user, String sentence, String time, 
			boolean updateNotif) {
		if(mListOfIds.contains(user.getId())) {
			int idx = mListOfIds.indexOf(user.getId());
			mListOfIds.remove(idx);
			mListOfEntity.remove(idx);
		}
		mListOfIds.add(0, user.getId());
		mListOfEntity.add(0, new TabMsgItemEntity(user.getId(), user.getAvatarId(), user.getName(),
							sentence, time));
		
		if(updateNotif) {
			onUpdateNotification(user.getId(), sentence);
		}
	}
	
	public void onUpdateById(int id, int avatarId, String name, String sentence, String time,
			boolean updateNotif) {
		if(mListOfIds.contains(id)) {
			int idx = mListOfIds.indexOf(id);
			mListOfIds.remove(idx);
			mListOfEntity.remove(idx);
		}
		mListOfIds.add(0, id);
		mListOfEntity.add(0, new TabMsgItemEntity(id, avatarId, name, sentence, time));
		
		if(updateNotif) {
			onUpdateNotification(id, sentence);
		}
	}
	
	public void onUpdateView() {
		mListView.setAdapter(new MainTabMsgAdapter(mContext0, mListOfEntity));
	}
	
	@SuppressWarnings("deprecation")
	public void onUpdateNotification(int id, String sentence) {		
		int icon = R.drawable.new_launcher;
		CharSequence tickerText = sentence;
		long when = System.currentTimeMillis();
		Notification mNotification = new Notification(icon, tickerText, when);

		mNotification.flags = Notification.FLAG_NO_CLEAR;
		mNotification.flags |= Notification.FLAG_AUTO_CANCEL;
		if(!isForeGround()) {
			mNotification.defaults |= Notification.DEFAULT_SOUND;
			mNotification.defaults |= Notification.DEFAULT_VIBRATE;
		}
		mNotification.contentView = null;

		Intent intent;
		if(id < 0) {
			intent = new Intent(mContext0,
					FrdRequestNotifActivity.class);
		} else {
			intent = new Intent(mContext0,
					ChatActivity.class);
			ChatService.getInstance().setEnterFromNotification(true);
			ChatService.getInstance().setEnterFromNotificationId(id);
		}
		
		PendingIntent contentIntent = PendingIntent.getActivity(
				mContext0, 0, intent, 0);
		mNotification.setLatestEventInfo(mContext0, "new message", sentence,
				contentIntent);
		NotificationManager mNotificationManager =
			    (NotificationManager) mContext0.getSystemService(Context.NOTIFICATION_SERVICE);
		mNotificationManager.notify(this.mNotifId, mNotification);
	}
	
	public boolean isForeGround() {
		ActivityManager am = (ActivityManager)mContext0.getSystemService(Context.ACTIVITY_SERVICE);
		ActivityManager.RunningTaskInfo foregroundTaskInfo = am.getRunningTasks(1).get(0);
		String curPackageName = foregroundTaskInfo.baseActivity.getPackageName();
		
		String myPackageName = mContext0.getPackageName();
		
		if(curPackageName.equals(myPackageName)) {
			return true;
		} else {
			return false;
		}
	}
	
}
