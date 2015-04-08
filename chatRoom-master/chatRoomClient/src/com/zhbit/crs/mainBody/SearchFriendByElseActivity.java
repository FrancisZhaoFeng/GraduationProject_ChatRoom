package com.zhbit.crs.mainBody;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;

import com.zhbit.crs.R;
import com.zhbit.crs.domain.ZSearchEntity;

public class SearchFriendByElseActivity extends Activity {

	private Spinner mSearchSpLowage;
	private Spinner mSearchSpHighage;
	private RadioGroup mRgpSex;

	private Button mBtnSearchByElse;

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		// this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.search_friend_by_else);

		mSearchSpLowage = (Spinner) findViewById(R.id.search_friend_by_else_spinner_lowage);
		mSearchSpHighage = (Spinner) findViewById(R.id.search_friend_by_else_spinner_highage);
		mRgpSex = (RadioGroup) findViewById(R.id.search_friend_by_else_rgp_choose_sex);
		mBtnSearchByElse = (Button) findViewById(R.id.search_friend_by_else_btn_search);

		mBtnSearchByElse.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				int lage = mSearchSpLowage.getSelectedItemPosition() + 5;
				int uage = mSearchSpHighage.getSelectedItemPosition() + 5;

				int sex;
				// int choseId = mRgpSex.getCheckedRadioButtonId();
				RadioButton rdbtn = (RadioButton) findViewById(mRgpSex.getCheckedRadioButtonId());
				String chSex = rdbtn.getText().toString();
				switch (chSex) {
				case "female":
					sex = ZSearchEntity.FEMALE_GENDER;
					break;
				case "male":
					sex = ZSearchEntity.MALE_GENDER;
					break;
				default:
					sex = ZSearchEntity.BOTH_GENDER;
					break;
				}

				ZSearchEntity s_ent0 = new ZSearchEntity(ZSearchEntity.SEARCH_BY_ELSE, lage, uage, sex, "");
				MainBodyActivity.getInstance().startSearch(s_ent0);
			}
		});
	}

}
