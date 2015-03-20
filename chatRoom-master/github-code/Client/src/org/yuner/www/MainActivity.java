/**
 * main portal/entry for this app, 
 * it collects user information, 
 * 		instantiate the ConnectedApp, 
 * 		establish socket connection with our server
 * 		and start activity for the next stage
 */

package org.yuner.www;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.app.*;
import android.app.Notification.Builder;
import android.view.*;
import android.view.View.*;
import android.widget.*;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Typeface;

import java.io.*;
import java.net.*;

import android.text.Spannable;
import android.text.method.PasswordTransformationMethod;
import android.text.style.BackgroundColorSpan;
import android.text.style.ForegroundColorSpan;
import android.util.*;
import android.os.*;
import java.lang.Exception;
import java.util.*;
import org.yuner.www.R;
import org.yuner.www.bean.UserInfo;
import org.yuner.www.chatServices.ChatService;
import org.yuner.www.chatServices.ChatServiceData;
import org.yuner.www.chatServices.FriendListInfo;
import org.yuner.www.chatServices.InitData;
import org.yuner.www.chatter.ChatActivity;
import org.yuner.www.commons.GlobalMsgTypes;
import org.yuner.www.commons.GlobalStrings;
import org.yuner.www.mainBody.MainBodyActivity;
import org.yuner.www.myNetwork.NetConnect;
import org.yuner.www.myNetwork.NetworkService;



/**
 * MainActivity is the entry activity, which provides a login page set to collect user information
 * and verification of username/password
 * 
 * @version 6 June 2013
 * @author wangqingyun
 */
public class MainActivity extends Activity {
	
	private String mName;
	private UserInfo mUserInfo;
	private String mPassword;
	private NetConnect mNetCon;
	
	private EditText mEtAccount;
	private EditText mEtPassword;
	private Button mBtnLogin;
	private Button mBtnRegister;
	
	@Override
	protected void onCreate(Bundle savedInstanceState){ 
		
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
//		this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.main_login);
		
		mEtAccount=(EditText)findViewById(R.id.mainLoginEditAccount);
		mEtPassword = (EditText)findViewById(R.id.mainLoginEditPassword);
		mBtnLogin=(Button)findViewById(R.id.mainLoginBtn);
		mBtnRegister = (Button)findViewById(R.id.main_btn_register);
		
		/* this is to render the password edittext font to be default */
		mEtPassword.setTypeface(Typeface.DEFAULT);
		mEtPassword.setTransformationMethod(new PasswordTransformationMethod());
		
		mBtnLogin.setOnClickListener(new OnClickListener(){
			public void onClick(View v)
			{
				MainActivity.this.tryLogin();
			}	
		});
		
		mBtnRegister.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				Intent intent0=new Intent(MainActivity.this,RegisterActivity.class);
				startActivity(intent0);
			}
		});
	}
	
	@Override
    protected void onDestroy()
    {
    	super.onDestroy();
    }
	
	public void tryLogin()
	{
		mName=mEtAccount.getText().toString();
		mPassword = mEtPassword.getText().toString();
		
		if(mName.equals("") || mPassword.length() < 5)
		{//Please Specify Your Name and Sex"
			Toast.makeText(MainActivity.this,"Please Specify Your Name and Password correctly", Toast.LENGTH_LONG).show();
		}
		else
		{			
			mUserInfo = new UserInfo(mName,0,0,0,0,0,0);
			
			/*  if mNetcon is connected already, close it first  */
			/*  here we use try because mNetCon might not have been instantiated yet  */
/*			try {
				NetConnect.getnetConnect().closeNetConnect();
			} catch (Exception e) {}
			try {
				InitData.closeInitData();
				FriendListInfo.closeFriendListInfo();
				ChatServiceData.closeChatServiceData();
			} catch (Exception e) {}
*/
			CloseAll.closeAll();
			/*  to establish a new connect  */
			
			NetworkService.getInstance().onInit(this);
			NetworkService.getInstance().setupConnection();
			if(NetworkService.getInstance().getIsConnected()) {
				String usrInfo = mUserInfo.toString() + GlobalStrings.signinDivider + mPassword
						+ GlobalStrings.signinDivider;
				NetworkService.getInstance().sendUpload(GlobalMsgTypes.msgHandShake, usrInfo);
			} else {
				NetworkService.getInstance().closeConnection();
				Toast.makeText(this,"failed to connect to Server", Toast.LENGTH_LONG).show();
				return;
			}
			
			InitData initData = InitData.getInitData();
			initData.start();
			try {
				initData.join();
			} catch(Exception e) {}
			mUserInfo = initData.getUserInfo();
			
			Log.d("connectedApp isonline : ", "" + mUserInfo.getIsOnline() + "+++++++++++++" +
					"+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++");
			
			if(mUserInfo.getId() < 0) {
				Toast.makeText(this, "invalid username or password", Toast.LENGTH_SHORT).show();
				return;
			}
			
			Log.d("connectedApp isonline : ", "" + mUserInfo.getIsOnline() + "+++++++++++++" +
					"+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++");
			
			ConnectedApp connected_app0  =  ConnectedApp.getInstance();
//		    connected_app0.setConnect(mNetCon);
		    connected_app0.setUserInfo(mUserInfo);
		    connected_app0.clearListActivity();
		    connected_app0.instantiateListActivity();
	
		    Intent intent0=new Intent(MainActivity.this,MainBodyActivity.class);
//			intent0.putExtra("username", mUserInfo.getName());
//			intent0.putExtra("usersex", mUserInfo.getSex());
			startActivity(intent0);
			
			finish();
		}
	}
	
}