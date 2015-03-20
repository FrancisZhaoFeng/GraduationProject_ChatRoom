package org.yuner.www.mainBody;

import org.yuner.www.ConnectedApp;
import org.yuner.www.R;
import org.yuner.www.bean.UserInfo;
import org.yuner.www.commons.GlobalMsgTypes;
import org.yuner.www.commons.GlobalStrings;
import org.yuner.www.myNetwork.NetConnect;
import org.yuner.www.myNetwork.NetworkService;

import android.content.Context;
import android.view.View;
import android.widget.Button;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

public class MainTabSettingsPage {

	private static MainTabSettingsPage mInstance;
	
	private View mViewOfPage;
	private Context mContext0;
	
	private Spinner mSpBirthYear;
	private Spinner mSpBirthMonth;
	private Spinner mSpBirthDay;
	private RadioGroup mRgpSex;
	private Button mBtnUpdate;
	
	private int stYear = 1941;
	
	public static MainTabSettingsPage getInstance() {
		if(mInstance == null) {
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
		mSpBirthYear = (Spinner)mViewOfPage.findViewById(R.id.tabsettings_spinner_year);
		mSpBirthMonth = (Spinner)mViewOfPage.findViewById(R.id.tabsettings_spinner_month);
		mSpBirthDay = (Spinner)mViewOfPage.findViewById(R.id.tabsettings_spinner_day);
		mRgpSex = (RadioGroup)mViewOfPage.findViewById(R.id.tabsettings_rgp_chooseSex);
		mBtnUpdate = (Button)mViewOfPage.findViewById(R.id.tabsettings_btn_update);
		
		UserInfo me = ConnectedApp.getInstance().getUserInfo();
		mSpBirthYear.setSelection(me.getBirthYear()-stYear);
		mSpBirthMonth.setSelection(me.getBirthMonth()-1);
		mSpBirthDay.setSelection(me.getBirthDay()-1);
		
		if(me.getSex() == 0) {
			mRgpSex.check(R.id.tabsettings_rd_btnFemale);
		} else {
			mRgpSex.check(R.id.tabsettings_rd_btnMale);
		}
		
		mBtnUpdate.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				int birthYear = mSpBirthYear.getSelectedItemPosition() + 1941;
				int birthMonth = mSpBirthMonth.getSelectedItemPosition() + 1;
				int birthDay = mSpBirthDay.getSelectedItemPosition() + 1;
				
				int sex;
				int sexChoseId=mRgpSex.getCheckedRadioButtonId();
				switch(sexChoseId){
					case R.id.tabsettings_rd_btnFemale:
						sex=0;
						break;
					case R.id.tabsettings_rd_btnMale:
						sex=1;
						break;
					default:
						sex=-1;
						break;
				}
				if(sex == -1) {
					Toast.makeText(mContext0, "please select your gender", Toast.LENGTH_SHORT).show();
					return;
				}
				
				UserInfo me = ConnectedApp.getInstance().getUserInfo();
				me.setBirthYear(birthYear);
				me.setBirthMonth(birthMonth);
				me.setBirthDay(birthDay);
				me.setSex(sex);
				
				NetworkService.getInstance().sendUpload(GlobalMsgTypes.msgUpdateUserInfo,me.toString());
				ConnectedApp.getInstance().setUserInfo(me);
				Toast.makeText(mContext0, "congratulations, you have successfully updated your information",
						Toast.LENGTH_SHORT).show();
			}
		});
	}
}
