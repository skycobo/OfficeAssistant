package com.lzz.officeassistant.review;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;

import org.json.JSONArray;
import org.json.JSONObject;

import com.lzz.officeassistant.R;
import com.lzz.officeassistant.notices.Notice;
import com.lzz.officeassistant.notices.NoticeContentActivity;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

public class ReviewFragment extends Fragment {
	private View view;
	private SharedPreferences sp;
	
	@Override
	public void onStart() {
		if(sp.getString("account", null).equals(sp.getString("teamCreater", null))){
		
		}else{
			TextView title_text = (TextView) view.findViewById(R.id.title_text);
			title_text.setText("审批");
			Button title_add=(Button) view.findViewById(R.id.title_add);
			title_add.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View arg0) {
					Intent i = new Intent(view.getContext(),EditReviewActivity.class);
					view.getContext().startActivity(i);
				}
			});
			File file = new File(view.getContext().getFilesDir(),"review.json");
			System.out.println("review-"+file.exists());
			if(file.exists()){
				showWaitReview();
			}
		}
		super.onStart();
	}
	public void showWaitReview(){
		BufferedReader br=null;
		JSONArray ja = null;
		String data [] =null;
		try {
			br = new BufferedReader(new InputStreamReader(view.getContext().openFileInput("review.json")));
			ja = new JSONArray(br.readLine());
			br.close();
			System.out.println(ja.length());
			data = new String[ja.length()];
			for(int i = 0;i<ja.length();i++){
				JSONObject jo = ja.getJSONObject(i);
				data[i]="标题:"+jo.getString("reviewTitle")+"\n时间:"+jo.getString("requestTime");
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(view.getContext(),android.R.layout.simple_list_item_1, data);
		ListView lv_review_requester_wait = (ListView) view.findViewById(R.id.lv_review_requester_wait);
		lv_review_requester_wait.setAdapter(adapter);
		lv_review_requester_wait.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				Intent i = new Intent(view.getContext(),ReviewItemActivity.class);
				i.putExtra("id", position);
				startActivity(i);
			}
		});
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		view =inflater.inflate(R.layout.fragment_review_requester, null);
		sp = view.getContext().getSharedPreferences("userInfo", Context.MODE_PRIVATE);
		if(sp.getString("account", null).equals(sp.getString("teamCreater", null))){
			view =inflater.inflate(R.layout.fragment_review_handler, null);
		}
		return view;
	}
	

}
