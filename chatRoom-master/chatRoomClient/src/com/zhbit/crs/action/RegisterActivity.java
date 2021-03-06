package com.zhbit.crs.action;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import com.zhbit.crs.R;
import com.zhbit.crs.commons.GlobalErrors;
import com.zhbit.crs.commons.GlobalMsgTypes;
import com.zhbit.crs.domain.User;
import com.zhbit.crs.myNetwork.NetworkService;
import com.zhbit.crs.util.MD5;
import com.zhbit.crs.util.tools;

public class RegisterActivity extends Activity {

	private static RegisterActivity mInstance;

	private EditText mEtNickname;
	private EditText mEtPassword;
	private EditText mEtConfirmPassword;
	private EditText mEtTelephone;
	private Spinner mSpBirthYear;
	private Spinner mSpBirthMonth;
	private Spinner mSpBirthDay;
	private Spinner mSpAge;
	private RadioGroup mRgpSex;

	private Button mBtnRegister;

//	private UserInfo mUserAfterRegister;
	private User mUserAfterRegister;
	private boolean mReceivedRegister;

	public static RegisterActivity getInstance() {
		return mInstance;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		// this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.main_register);

		mInstance = this;

		mEtNickname = (EditText) findViewById(R.id.main_register_edittext_nickname);
		mEtPassword = (EditText) findViewById(R.id.main_register_edittext_password);
		mEtConfirmPassword = (EditText) findViewById(R.id.main_register_edittext_confirm_password);
		mEtTelephone = (EditText) findViewById(R.id.main_register_edittext_telephone);
//		mSpBirthYear = (Spinner) findViewById(R.id.main_register_spinner_year);
//		mSpBirthMonth = (Spinner) findViewById(R.id.main_register_spinner_month);
//		mSpBirthDay = (Spinner) findViewById(R.id.main_register_spinner_day);
		mSpAge = (Spinner) findViewById(R.id.main_register_spinner_age);
		mRgpSex = (RadioGroup) findViewById(R.id.mainRegisterRgpChooseSex);

		/** this is to render the password edittext font to be default */
		mEtPassword.setTypeface(Typeface.DEFAULT);
		mEtPassword.setTransformationMethod(new PasswordTransformationMethod());
		mEtConfirmPassword.setTypeface(Typeface.DEFAULT);
		mEtConfirmPassword.setTransformationMethod(new PasswordTransformationMethod());

		mBtnRegister = (Button) findViewById(R.id.main_register_btn_register);

		mBtnRegister.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				String name = mEtNickname.getText().toString().trim();
				String password = mEtPassword.getText().toString().trim();
				String confirmPassword = mEtConfirmPassword.getText().toString().trim();
				String telephone = mEtTelephone.getText().toString().trim();

//				int birthYear = mSpBirthYear.getSelectedItemPosition() + 1941;
//				int birthMonth = mSpBirthMonth.getSelectedItemPosition() + 1;
//				int birthDay = mSpBirthDay.getSelectedItemPosition() + 1;
				int age = mSpAge.getSelectedItemPosition()+5;

				int sex;
				int sexChoseId = mRgpSex.getCheckedRadioButtonId();
				switch (sexChoseId) {
				case R.id.mainRegisterRdBtnFemale:
					sex = 0;
					break;
				case R.id.mainRegisterRdBtnMale:
					sex = 1;
					break;
				default:
					sex = -1;
					break;
				}
				if (name.equals("")) {
					Toast.makeText(RegisterActivity.this, "please input Nickname", Toast.LENGTH_SHORT).show();
					return;
				} else if (name.length() < 5) {
					Toast.makeText(RegisterActivity.this, "Nickname should be greater than 5 characters", Toast.LENGTH_SHORT).show();
					return;
				} else if (name.length() >= 16) {
					Toast.makeText(RegisterActivity.this, "Nickname should be less than 16 characters", Toast.LENGTH_SHORT).show();
					return;
				}else if(new tools().checkInput(password)){
					Toast.makeText(RegisterActivity.this, "password should 3 kind of (number,upper,lower,underline)", Toast.LENGTH_SHORT).show();
					return;
				}else if (password.length() < 5) {
					Toast.makeText(RegisterActivity.this, "password should be longer than 5 characters", Toast.LENGTH_SHORT).show();
					return;
				} else if (!password.equals(confirmPassword)) {
					Toast.makeText(RegisterActivity.this, "your confirmed password is not same as your password", Toast.LENGTH_SHORT).show();
					return;
				} else if (telephone.length() != 11) {
					Toast.makeText(RegisterActivity.this, "phone numbe should be 11 number", Toast.LENGTH_SHORT).show();
					return;
				}else if (sex < 0) {
					Toast.makeText(RegisterActivity.this, "please choose your gender", Toast.LENGTH_SHORT).show();
					return;
				}

				/**
				 * Toast.makeText(RegisterActivity.this,"name : " + name + "\n"
				 * + "password : " + password + "\n" + "sex " +
				 * sex,Toast.LENGTH_SHORT).show();
				 */
				password = new MD5().toMD5(password);  //MD5加密密码
				tryRegister(name, password, telephone,age , sex==0?false:true);//0代表女
			}
		});
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		mInstance = null;
	}

	@Override
	public void onBackPressed() {
		super.onBackPressed();
		finish(); // destroy RegisterActivity when leave
	}

	public void tryRegister(String name, String password, String telephone,int age, Boolean sex) {
//		UserInfo uu0 = new UserInfo(name, 0, sex, birthYear, birthMonth, birthDay, 0);
		User user = new User(name,password,telephone,age,sex);
		user.setOnline(false);//注册时默认设置不在线
		user.setBlacklist(false);//注册时默认设置没有被限制
		Log.w("user", "-password:"+user.getPassword());

//		String toRegister = uu0.toString() + GlobalStrings.signupDivider + password + GlobalStrings.signupDivider;

		NetworkService.getInstance().closeConnection();
		NetworkService.getInstance().onInit(this);
		NetworkService.getInstance().setupConnection();
		if (NetworkService.getInstance().getIsConnected()) {
			NetworkService.getInstance().sendObject(GlobalMsgTypes.msgSignUp, user);
		} else {
			NetworkService.getInstance().closeConnection();
			Toast.makeText(this, "failed to connect to Server", Toast.LENGTH_LONG).show();
			return;
		}

		mReceivedRegister = false;
		while (!mReceivedRegister) {
		}
		if (mUserAfterRegister.getUserid() == GlobalErrors.nameAlreadyExists) {
			Toast.makeText(this, "sorry name already exist", Toast.LENGTH_SHORT).show();
		} else if (mUserAfterRegister.getUserid() >= 0) {
			Toast.makeText(this, "congratulations register successfully", Toast.LENGTH_SHORT).show();

			Intent intent0 = new Intent(RegisterActivity.this, MainActivity.class);
			startActivity(intent0);
		} else {
			Toast.makeText(this, "sorry cannot register", Toast.LENGTH_SHORT).show();
		}
		// con.closeNetConnect();
	}

	public void uponRegister(User user) {//String msg0
//		mUserAfterRegister = new UserInfo(msg0);
		mUserAfterRegister = user;
		mReceivedRegister = true;
	}

}