package com.csu.chatroom;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class WelcomeActivity extends Activity{

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.welcome);
	}
	
    public void welcome_login(View v) {  
      	Intent intent = new Intent();
		intent.setClass(WelcomeActivity.this, ChatActivity.class);
		startActivity(intent);
      }  
    
    public void welcome_exit(View view){
		this.finish();
		System.exit(0);
    }
}
