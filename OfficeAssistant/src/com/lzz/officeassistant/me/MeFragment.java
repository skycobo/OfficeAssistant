package com.lzz.officeassistant.me;

import com.lzz.officeassistant.R;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class MeFragment extends Fragment {
	private View view;;
	private TextView tv_me_teamID;
	@Override
	public void onStart() {
		SharedPreferences sp = view.getContext().getSharedPreferences("userInfo", Context.MODE_PRIVATE);
		TextView tv_me_nickname = (TextView) view.findViewById(R.id.tv_me_nickname);
		tv_me_teamID = (TextView) view.findViewById(R.id.tv_me_teamID);
		TextView tv_me_teamName = (TextView) view.findViewById(R.id.tv_me_teamName);
		Button b_me_createTeam = (Button) view.findViewById(R.id.b_me_createTeam);
		Button b_me_joinTeam = (Button) view.findViewById(R.id.b_me_joinTeam);
		tv_me_nickname.setText(sp.getString("nickname", "名字"));
		tv_me_teamID.setText(sp.getString("teamID", "暂无"));
		tv_me_teamName.setText(sp.getString("teamName", "暂无"));
		/**
		 * 点击打开创建团队页面
		 */
		b_me_createTeam.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v){
				if(tv_me_teamID.getText().toString().equals("暂无")){
					Intent i = new Intent(view.getContext(),CreateTeamActivity.class);
					startActivity(i);
				}else{
					Toast.makeText(getActivity(), "您已属于了一个团队!", 0).show();
				}
				
			}
		});
		/**
		 * 点击打开加入团队页面
		 */
		b_me_joinTeam.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if(tv_me_teamID.getText().toString().equals("暂无")){
					Intent i = new Intent(view.getContext(),JoinTeamActivity.class);
					startActivity(i);
				}else{
					Toast.makeText(getActivity(), "您已属于了一个团队!", 0).show();
				}
				
			}
		});
		super.onStart();
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		view =inflater.inflate(R.layout.fragment_me, null);
		return view;
	}
}
