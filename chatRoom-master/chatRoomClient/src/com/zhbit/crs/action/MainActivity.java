/**
 * main portal/entry for this app, 
 * it collects user information, 
 * 		instantiate the ConnectedApp, 
 * 		establish socket connection with our server
 * 		and start activity for the next stage
 */

package com.zhbit.crs.action;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.zhbit.crs.R;
import com.zhbit.crs.chatServices.InitData;
import com.zhbit.crs.commons.GlobalMsgTypes;
import com.zhbit.crs.domain.User;
import com.zhbit.crs.mainBody.MainBodyActivity;
import com.zhbit.crs.myNetwork.NetConnect;
import com.zhbit.crs.myNetwork.NetworkService;
import com.zhbit.crs.util.MD5;

/**
 * MainActivity is the entry activity, which provides a login page set to collect user information and verification of
 * username/password
 */
public class MainActivity extends Activity {

	private String mName;
	// private UserInfo mUserInfo;
	private User user;
	private String mPassword;
	private NetConnect mNetCon;

	private EditText mEtAccount;
	private EditText mEtPassword;
	private Button mBtnLogin;
	private Button mBtnRegister;

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		// this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.main_login);

		mEtAccount = (EditText) findViewById(R.id.mainLoginEditAccount);
		mEtPassword = (EditText) findViewById(R.id.mainLoginEditPassword);
		mBtnLogin = (Button) findViewById(R.id.mainLoginBtn);
		mBtnRegister = (Button) findViewById(R.id.main_btn_register);

		/* this is to render the password edittext font to be default */
		mEtPassword.setTypeface(Typeface.DEFAULT);
		mEtPassword.setTransformationMethod(new PasswordTransformationMethod());

		mBtnLogin.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				MainActivity.this.tryLogin(v);
			}
		});

		mBtnRegister.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				Intent intent0 = new Intent(MainActivity.this, RegisterActivity.class);
				startActivity(intent0);
			}
		});
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
	}

	public void tryLogin(View v) {
		hideSystemKeyBoard(this, v);
		mName = mEtAccount.getText().toString();
		mPassword = mEtPassword.getText().toString();
		if (mName.equals("") || mName.length() < 5 || mName.length() >= 16 || mPassword.length() < 5 || mPassword.length() >= 16) {// Please
			// Name and Sex"
			Toast.makeText(MainActivity.this, "Please Specify Your Nickname and Password correctly", Toast.LENGTH_LONG).show();
		} else {
			mPassword = new MD5().toMD5(mPassword);
			user = new User(mName, mPassword);
			/**
			 * if mNetcon is connected already, close it first here we use try because mNetCon might not have been instantiated
			 * yet try { NetConnect.getnetConnect().closeNetConnect(); } catch (Exception e) {} try { InitData.closeInitData();
			 * FriendListInfo.closeFriendListInfo(); ChatServiceData.closeChatServiceData(); } catch (Exception e) {}
			 */
			CloseAll.closeAll();
			/* to establish a new connect */
			NetworkService.getInstance().onInit(this);
			Log.w("mark", "NetworkService.getInstance().onInit(this)");
			NetworkService.getInstance().setupConnection();
			Log.w("mark", "NetworkService.getInstance().setupConnection()");
			if (NetworkService.getInstance().getIsConnected()) {
				NetworkService.getInstance().sendUpload(GlobalMsgTypes.msgHandShake, user);
				Log.w("mark", "NetworkService.getInstance().sendUpload(GlobalMsgTypes.msgHandShake, user)");
			} else {
				NetworkService.getInstance().closeConnection();
				Toast.makeText(this, "failed to connect to Server", Toast.LENGTH_LONG).show();
				return;
			}
			InitData initData = InitData.getInitData();
			initData.start();
			Log.w("mark", "initData.start()");
			try {
				initData.join(); // 表示：当前线程等待这个线程对象对应的线程结束。原则：join是测试其它工作状态的唯一正确方法
			} catch (Exception e) {
				e.printStackTrace();
			}
			user = initData.getUser();
			if (user == null) {
				Toast.makeText(this, "invalid username or password", Toast.LENGTH_LONG).show();
				return;
			}
			Log.d("connectedApp isonline : ", "" + user.getOnline() + "+++" + "++++++++++");
			ConnectedApp connected_app0 = ConnectedApp.getInstance();
			connected_app0.setUserInfo(user);
			connected_app0.clearListActivity();
			connected_app0.instantiateListActivity();

			Intent intent0 = new Intent(MainActivity.this, MainBodyActivity.class);
			startActivity(intent0);

			finish();
		}
	}

	public static void hideSystemKeyBoard(Context mcontext, View v) {
		InputMethodManager imm = (InputMethodManager) ((MainActivity) mcontext).getSystemService(Context.INPUT_METHOD_SERVICE);
		imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
	}
}