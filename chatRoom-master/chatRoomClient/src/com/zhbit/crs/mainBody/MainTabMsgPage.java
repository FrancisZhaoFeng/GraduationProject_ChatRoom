package com.zhbit.crs.mainBody;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import android.app.ActivityManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.zhbit.crs.R;
import com.zhbit.crs.action.ConnectedApp;
import com.zhbit.crs.chatServices.ChatService;
import com.zhbit.crs.chatServices.ChatServiceData;
import com.zhbit.crs.chatter.ChatActivity;
import com.zhbit.crs.domain.ZdbTabMsgItemEntity;
import com.zhbit.crs.domain.User;
import com.zhbit.crs.util.DbSaveOldMsg;

/**
 * @author zhaoguofeng
 * 消息显示 界面
 */
public class MainTabMsgPage {

	private static MainTabMsgPage mInstance;

	private View mViewOfPage;
	private Context mContext;

	private ListView mListView;
	private List<Integer> mListOfIds;
	private List<ZdbTabMsgItemEntity> mListOfEntity;

	private int mNotifId = 1;

	public static MainTabMsgPage getInstance() {
		if (mInstance == null) {
			mInstance = new MainTabMsgPage();
		}
		return mInstance;
	}

	private MainTabMsgPage() {
	}

	public void onInit(View view, Context context) {
		mViewOfPage = view;
		mContext = context;
	}

	/**
	 * @return
	 * 获取消息界面，的list
	 */
	public List<ZdbTabMsgItemEntity> getListOfEntity() {
		return mListOfEntity;
	}

	public void onCreate() {
		mListView = (ListView) mViewOfPage.findViewById(R.id.tabmsg_main_listview);
		mListOfIds = new ArrayList<Integer>();
		mListOfEntity = new ArrayList<ZdbTabMsgItemEntity>();

		DbSaveOldMsg.onInit(mContext);
		DbSaveOldMsg.getInstance().getTabMsgItem(ConnectedApp.getInstance().getUserInfo().getUserid(), (ArrayList<ZdbTabMsgItemEntity>) mListOfEntity);
		for (ZdbTabMsgItemEntity ent : mListOfEntity) {
			mListOfIds.add(ent.getTalkerId());
		}

		//点击消息界面的触发事件
		mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

				int id_x = mListOfEntity.get(position).getTalkerId();

				if (id_x > 0) {
					ChatService.getInstance().setType(2);
					ChatService.getInstance().setId(id_x);
					ChatService.getInstance().setEnterFromNotification(false);
					ChatServiceData.getInstance().clearUnreadMsgs(id_x);
					MainBodyActivity.getInstance().gotoChatActivity();
				} else if (id_x == ZdbTabMsgItemEntity.FrdReqNotifId) {
					MainBodyActivity.getInstance().gotoTabMsgFrdReqNotifActivity();
				}
			}
		});
	}

	/**
	 * @param user
	 * @param sentence
	 * @param time
	 * @param updateNotif
	 * 更新 通过用户
	 */
	public void onUpdateByUserinfo(User user, String sentence, Date time, boolean updateNotif) {
		if (mListOfIds.contains(user.getUserid())) {
			int idx = mListOfIds.indexOf(user.getUserid());
			mListOfIds.remove(idx);
			mListOfEntity.remove(idx);
		}
		mListOfIds.add(0, user.getUserid());
		mListOfEntity.add(0, new ZdbTabMsgItemEntity(user.getUserid(), user.getSex(), user.getUsername(), sentence, time));

		if (updateNotif) {
			onUpdateNotification(user.getUserid(), sentence);
		}
	}

	/**
	 * @param id
	 * @param avatarId
	 * @param name
	 * @param sentence
	 * @param time
	 * @param updateNotif
	 * 更新 通过id 
	 */
	public void onUpdateById(int id, boolean avatarId, String name, String sentence, Date time, boolean updateNotif) {
		if (mListOfIds.contains(id)) {
			int idx = mListOfIds.indexOf(id);
			mListOfIds.remove(idx);
			mListOfEntity.remove(idx);
		}
		mListOfIds.add(0, id);
		mListOfEntity.add(0, new ZdbTabMsgItemEntity(id, avatarId, name, sentence, time));

		if (updateNotif) {
			onUpdateNotification(id, sentence);
		}
	}

	public void onUpdateView() {
		mListView.setAdapter(new MainTabMsgAdapter(mContext, mListOfEntity));
	}

	/**
	 * @param id
	 * @param sentence
	 * 更新通知栏
	 */
	@SuppressWarnings("deprecation")
	public void onUpdateNotification(int id, String sentence) {
		int icon = R.drawable.new_launcher;
		CharSequence tickerText = sentence;
		long when = System.currentTimeMillis();
		Notification mNotification = new Notification(icon, tickerText, when);

		mNotification.flags = Notification.FLAG_NO_CLEAR;
		mNotification.flags |= Notification.FLAG_AUTO_CANCEL;
		if (!isForeGround()) {
			mNotification.defaults |= Notification.DEFAULT_SOUND;
			mNotification.defaults |= Notification.DEFAULT_VIBRATE;
		}
		mNotification.contentView = null;

		Intent intent;
		if (id < 0) {
			intent = new Intent(mContext, FrdRequestNotifActivity.class);
		} else {
			intent = new Intent(mContext, ChatActivity.class);
			ChatService.getInstance().setEnterFromNotification(true);
			ChatService.getInstance().setEnterFromNotificationId(id);
		}

		PendingIntent contentIntent = PendingIntent.getActivity(mContext, 0, intent, 0);
		mNotification.setLatestEventInfo(mContext, "new message", sentence, contentIntent);
		NotificationManager mNotificationManager = (NotificationManager) mContext.getSystemService(Context.NOTIFICATION_SERVICE);
		mNotificationManager.notify(this.mNotifId, mNotification);
	}

	/**
	 * @return
	 */
	public boolean isForeGround() {
		ActivityManager am = (ActivityManager) mContext.getSystemService(Context.ACTIVITY_SERVICE);
		ActivityManager.RunningTaskInfo foregroundTaskInfo = am.getRunningTasks(1).get(0);
		String curPackageName = foregroundTaskInfo.baseActivity.getPackageName();

		String myPackageName = mContext.getPackageName();

		if (curPackageName.equals(myPackageName)) {
			return true;
		} else {
			return false;
		}
	}

}
