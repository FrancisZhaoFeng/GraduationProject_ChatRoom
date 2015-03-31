package com.zhbit.crs.mainBody;

import org.yuner.www.R;

import android.content.Context;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import com.zhbit.crs.action.ConnectedApp;
import com.zhbit.crs.action.RegisterActivity;
import com.zhbit.crs.commons.GlobalMsgTypes;
import com.zhbit.crs.domain.User;
import com.zhbit.crs.myNetwork.NetworkService;
import com.zhbit.crs.util.MD5;
import com.zhbit.crs.util.tools;

public class MainTabSettingsPage {

	private static MainTabSettingsPage mInstance;

	private View mViewOfPage;
	private Context mContext0;

	private EditText mTelephone;
	private Spinner mSpAge;
	private RadioGroup mRgpSex;
	private Button mBtnUpdateInfo;
	private EditText mOldPassword;
	private EditText mNewPassword;
	private EditText mNewConfirmPwd;
	private Button mBtnUpdatePwd;

	public static MainTabSettingsPage getInstance() {
		if (mInstance == null) {
			mInstance = new MainTabSettingsPage();
		}
		return mInstance;
	}

	private MainTabSettingsPage() {
	}

	public void onInit(View view, Context context) {
		mViewOfPage = view;
		mContext0 = context;
	}

	public void onCreate() {
		// mTelephone = (EditText)mViewOfPage.findViewById(R.id.tabsettings_edittext_telephone);
		mSpAge = (Spinner) mViewOfPage.findViewById(R.id.tabsettings_spinner_age);
		mRgpSex = (RadioGroup) mViewOfPage.findViewById(R.id.tabsettings_rgp_chooseSex);
		mBtnUpdateInfo = (Button) mViewOfPage.findViewById(R.id.tabsettings_btn_updateInfo);
		mOldPassword = (EditText)mViewOfPage.findViewById(R.id.tabsettings_edittext_oldpassword);
		mNewPassword = (EditText)mViewOfPage.findViewById(R.id.tabsettings_edittext_newpassword);
		mNewConfirmPwd = (EditText)mViewOfPage.findViewById(R.id.tabsettings_edittext_confirm_newpassword);
		mBtnUpdatePwd = (Button)mViewOfPage.findViewById(R.id.tabsettings_btn_updatepassword);
		

		User me = ConnectedApp.getInstance().getUserInfo();
		mSpAge.setSelection(me.getAge() - 5);

		if (me.getSex() == false) {
			mRgpSex.check(R.id.tabsettings_rd_btnFemale);
		} else {
			mRgpSex.check(R.id.tabsettings_rd_btnMale);
		}

		mBtnUpdateInfo.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				int age = mSpAge.getSelectedItemPosition() + 5;

				boolean sex = false;
				int sexChoseId = mRgpSex.getCheckedRadioButtonId();
				switch (sexChoseId) {
				case R.id.tabsettings_rd_btnFemale:
					sex = false;
					break;
				case R.id.tabsettings_rd_btnMale:
					sex = true;
					break;
				}
				// if(sex == -1) {
				// Toast.makeText(mContext0, "please select your gender",
				// Toast.LENGTH_SHORT).show();
				// return;
				// }

				User me = ConnectedApp.getInstance().getUserInfo();
				me.setAge(age);
				me.setSex(sex);

				NetworkService.getInstance().sendUpload(GlobalMsgTypes.msgUpdateUserInfo, me);
				ConnectedApp.getInstance().setUserInfo(me);
				Toast.makeText(mContext0, "congratulations, you have successfully updated your information", Toast.LENGTH_SHORT).show();
			}
		});
		
		mBtnUpdatePwd.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				MD5 md = new MD5();
				String oldPassword = md.toMD5(mOldPassword.getText().toString().trim());
				String newPwd = mNewPassword.getText().toString().trim(); //未经加密
				String newPassword = md.toMD5(newPwd);
				String confirmPwd = md.toMD5(mNewConfirmPwd.getText().toString().trim());
				User me = ConnectedApp.getInstance().getUserInfo();

				if(!oldPassword.equals(me.getPassword())){
					Toast.makeText(mContext0, "Old password error input again!", Toast.LENGTH_SHORT).show();
					return;
				}else if(new tools().checkInput(newPwd)){
					Toast.makeText(mContext0, "New password should 3 kind of (number,upper,lower,underline)", Toast.LENGTH_SHORT).show();
					return;
				}else if (newPwd.length() < 5) {
					Toast.makeText(mContext0, "password should be longer than 5 characters", Toast.LENGTH_SHORT).show();
					return;
				} else if (!newPassword.equals(confirmPwd)) {
					Toast.makeText(mContext0, "your confirmed password is not same as your password", Toast.LENGTH_SHORT).show();
					return;
				}
				me.setPassword(newPassword);

				NetworkService.getInstance().sendUpload(GlobalMsgTypes.msgUpdateUserInfo, me);
				ConnectedApp.getInstance().setUserInfo(me);
				Toast.makeText(mContext0, "congratulations, you have successfully updated your information", Toast.LENGTH_SHORT).show();
			}
		});
	}
}
