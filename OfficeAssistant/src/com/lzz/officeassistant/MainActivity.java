package com.lzz.officeassistant;

import com.lzz.officeassistant.me.MeFragment;
import com.lzz.officeassistant.message.MessageFragment;
import com.lzz.officeassistant.notices.NoticesFragment;
import com.lzz.officeassistant.review.ReviewFragment;
import com.lzz.officeassistant.signin.SignInFragment;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;

public class MainActivity extends Activity {
	String account;
	NoticesFragment noticesFg;
	MessageFragment msgFg;
	SignInFragment siFg;
	MeFragment meFg;
	ReviewFragment reFg;
	FragmentManager fm;
	FragmentTransaction ft;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_main);
//		Intent i = getIntent();
//		account = i.getStringExtra("account");
//		Log.i("tag", account);
		noticesFg = new NoticesFragment();
		fm = getFragmentManager();
		ft = fm.beginTransaction();
		ft.replace(R.id.main_content, noticesFg);
		ft.commit();
		RadioGroup rg = (RadioGroup) findViewById(R.id.tab_menu);
		rg.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				switch(checkedId){
				case R.id.rb_main_Notice:
					noticesFg = new NoticesFragment();
					fm = getFragmentManager();
					ft = fm.beginTransaction();
					ft.replace(R.id.main_content, noticesFg);
					ft.commit();
					break;
				case R.id.rb_main_message:
					msgFg= new MessageFragment();
					fm = getFragmentManager();
					ft = fm.beginTransaction();
					ft.replace(R.id.main_content, msgFg);
					ft.commit();
					break;
				case R.id.rb_main_signIn:
					siFg = new SignInFragment();
					fm = getFragmentManager();
					ft = fm.beginTransaction();
					ft.replace(R.id.main_content, siFg);
					ft.commit();
					break;
				case R.id.rb_main_review:
					reFg = new ReviewFragment();
					fm = getFragmentManager();
					ft = fm.beginTransaction();
					ft.replace(R.id.main_content, reFg);
					ft.commit();
					break;
				case R.id.rb_main_me:
					meFg = new MeFragment();
					fm = getFragmentManager();
					ft = fm.beginTransaction();
					ft.replace(R.id.main_content, meFg);
					ft.commit();
					break;
					
				default:
					break;
				}
				
				
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	protected void onRestart() {
		noticesFg = new NoticesFragment();
		fm = getFragmentManager();
		ft = fm.beginTransaction();
		ft.replace(R.id.main_content, noticesFg);
		ft.commit();
		super.onRestart();
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
}
