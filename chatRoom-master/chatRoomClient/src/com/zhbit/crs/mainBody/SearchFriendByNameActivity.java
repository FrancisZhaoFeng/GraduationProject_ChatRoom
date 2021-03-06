package com.zhbit.crs.mainBody;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;

import com.zhbit.crs.R;
import com.zhbit.crs.domain.ZSearchEntity;
import com.zhbit.crs.domain.User;

public class SearchFriendByNameActivity extends Activity {
	
	private EditText mSearchEtName;	
	private Button mBtnSearchByName;
	
	private boolean mMsg9Received;
	private String mSearchedString;
	private User user;
	
	@Override
	protected void onCreate(Bundle savedInstanceState){ 
		
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
//		this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.search_friend_by_name);
		
		mSearchEtName = (EditText)findViewById(R.id.search_friend_by_name_edit_name);
		mBtnSearchByName = (Button)findViewById(R.id.search_friend_by_name_btn_search);
		
		mBtnSearchByName.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Log.d("start search by name", "++++++" + "+++++++++++++");
				String searchName = mSearchEtName.getText().toString();
				ZSearchEntity s_ent0 = new ZSearchEntity(ZSearchEntity.SEARCH_BY_NAME, -1, -1, -1,searchName);
				MainBodyActivity.getInstance().startSearch(s_ent0);				
			}
		});
	}

}
