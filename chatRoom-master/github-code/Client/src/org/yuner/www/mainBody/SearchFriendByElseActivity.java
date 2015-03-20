package org.yuner.www.mainBody;

import org.yuner.www.R;
import org.yuner.www.bean.SearchEntity;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Spinner;

public class SearchFriendByElseActivity extends Activity {
	
	private Spinner mSearchSpLowage;
	private Spinner mSearchSpHighage;
	private RadioGroup mRgpSex;
	
	private Button mBtnSearchByElse;
	
	@Override
	protected void onCreate(Bundle savedInstanceState){ 
		
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
//		this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.search_friend_by_else);
		
		mSearchSpLowage = (Spinner)findViewById(R.id.search_friend_by_else_spinner_lowage);
		mSearchSpHighage = (Spinner)findViewById(R.id.search_friend_by_else_spinner_highage);
		mRgpSex = (RadioGroup)findViewById(R.id.search_friend_by_else_rgp_choose_sex);
		mBtnSearchByElse = (Button)findViewById(R.id.search_friend_by_else_btn_search);
		
		mBtnSearchByElse.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				int lage = mSearchSpLowage.getSelectedItemPosition() + 5;
				int uage = mSearchSpHighage.getSelectedItemPosition() + 5;
				
				int sex00;
				int choseId = mRgpSex.getCheckedRadioButtonId();
				switch(choseId) {
				case R.id.cc0_friend_list_search_rdbtn_female:
					sex00 = SearchEntity.FEMALE_GENDER;
					break;
				case R.id.cc0_friend_list_search_rdbtn_male:
					sex00 = SearchEntity.MALE_GENDER;
					break;
				default:
					sex00 = SearchEntity.BOTH_GENDER;
					break;
				}
				
				SearchEntity s_ent0 = new SearchEntity(SearchEntity.SEARCH_BY_ELSE, lage, uage, sex00, "xx");
				MainBodyActivity.getInstance().startSearch(s_ent0);
			}
		});
	}

}
