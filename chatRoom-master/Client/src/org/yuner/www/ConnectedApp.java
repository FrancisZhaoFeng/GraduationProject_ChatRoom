/**
 * in order to transfer non-primitive information, like socket, class, etc.
 * we need a global class to store these information, that is a child of
 * Application as we are implementing here, you can always access global information
 * stored in ConnectedApp as below:
 *    ConnectedApp connected_app0  =  (ConnectedApp)getApplication();
 * one thing to remember, register ConnectedApp in the manifest file
 */

package org.yuner.www;

import java.util.ArrayList;
import java.util.List;

import org.yuner.www.bean.UserInfo;
import org.yuner.www.chatServices.ChatService;
import org.yuner.www.chatter.ChatActivity;
import org.yuner.www.mainBody.FrdRequestNotifActivity;
import org.yuner.www.myNetwork.NetConnect;

import android.app.Activity;
import android.app.Application;

public  class  ConnectedApp {
	
	private static ConnectedApp mInstance;
	
     private UserInfo mUserInfo;
     private Activity mCurActivity;
     private List<Activity> allActivities; 
     
     private ChatActivity mChatActivity;
     private FrdRequestNotifActivity mFrdRequestNotifActivity;

     public static ConnectedApp getInstance() {
    	 if(mInstance == null) {
    		 mInstance = new ConnectedApp();
    	 }
    	 return mInstance;
     }
     
     private ConnectedApp() {
    	 
     }
      
     public UserInfo getUserInfo() {
    	 return mUserInfo;
     }
     
     public void setUserInfo(UserInfo userInfo) {
    	 mUserInfo = userInfo;
     }

     public Activity getCurActivity() {
    	 return mCurActivity;
     }
     
     public void setCurActivity(Activity act0) {
    	 mCurActivity = act0;
     }
    
     public void instantiateListActivity()
     {
    	 allActivities =new ArrayList<Activity>();
     }
     
     public void clearListActivity()
     {
    	 if (allActivities != null) {
	    	 for (Activity act : allActivities) {
	    		 act.finish();
	    	 }
	    	 
	    	 allActivities.clear();
    	 }
     }
     
     public void addItemIntoListActivity(Activity act0)
     {
    	 allActivities.add(act0);
     }
     
     public ChatActivity getChatActivity() {
    	 return mChatActivity;
     }
     
     public void setChatActivity(ChatActivity ca0) {
    	 mChatActivity = ca0;
     }
     
     public FrdRequestNotifActivity getFrdRequestNotifActivity() {
    	 return mFrdRequestNotifActivity;
     }
     
     public void setFrdRequestNotifActivity(FrdRequestNotifActivity ll) {
    	 mFrdRequestNotifActivity = ll;
     }
     
     public void clearAll() {
    	 clearListActivity();
         mUserInfo = null;
         mCurActivity = null;
         allActivities = null; 
         mInstance = null;
     }
}
