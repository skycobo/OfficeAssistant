package com.lzz.officeassistant.notices;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import com.lzz.officeassistant.R;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class NoticesFragment extends Fragment {
	private View view;
	private ListView lv_notices;
	private Button title_add;
	private TextView title_text;
	private List<Notice> noticesArray = new ArrayList<Notice>();
	private SharedPreferences sp = null;
	private Handler handler = new Handler(){
		@Override
		public void handleMessage(Message msg) {
			Log.i("tag", (msg.obj==null)+"");
			if(msg.obj!=null){
				noticesArray = (List<Notice>) msg.obj;
				NoticeAdapter adapter = new NoticeAdapter(view.getContext(), R.layout.item_notice, noticesArray);
				lv_notices = (ListView) view.findViewById(R.id.lv_notices);
				lv_notices.setAdapter(adapter);
				lv_notices.setOnItemClickListener(new OnItemClickListener() {
					@Override
					public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
						Notice notice = noticesArray.get(position);
						Intent i = new Intent(view.getContext(),NoticeContentActivity.class);
						i.putExtra("title",notice.getTitle());
						i.putExtra("content", notice.getContent());
						i.putExtra("time", notice.getTime());
						i.putExtra("id", notice.getId());
						startActivity(i);
					}
				});
			}
		}
		
	};

	@Override
	public void onStart() {
		sp=getActivity().getSharedPreferences("currentUser", Context.MODE_PRIVATE);
		String currentUser = sp.getString("account", null);
		sp=getActivity().getSharedPreferences(currentUser, Context.MODE_PRIVATE);
		Editor e = sp.edit();
		e.putInt("noticeCounter", 0);
		e.commit();
		title_text = (TextView) view.findViewById(R.id.title_text);
		title_text.setText("公告");
		title_add=(Button) view.findViewById(R.id.title_add);
		title_add.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if(sp.getString("account", null).equals(sp.getString("teamCreater", null))){
					Intent i = new Intent(view.getContext(),EditNoticeActivity.class);
					startActivity(i);
				}else{
					Toast.makeText(getActivity(), "权限不足!", 0).show();
				}
			}
		});
		if(sp.getString("teamID", null)!=null){
			requestNotices();
		}
		super.onStart();
	}
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}
	@Override
	public void onSaveInstanceState(Bundle outState) {
		// TODO Auto-generated method stub
//		super.onSaveInstanceState(outState);
	}
	public void requestNotices() {
		new Thread(new Runnable(){
			@Override
			public void run() {
				HttpClient hc = null;
				try{
					hc = new DefaultHttpClient();
					HttpGet hg = new HttpGet("http://www.skycobo.com:8080/OfficeAssistantServer/"+sp.getString("teamID", null)+".json");
					HttpResponse hr = hc.execute(hg);
					if(hr.getStatusLine().getStatusCode() == 200){
						HttpEntity he = hr.getEntity();
						String response = EntityUtils.toString(he,"utf-8");
						parseNotice(response);
					}
				}catch(Exception e){
					e.printStackTrace();
				}finally{
					if(hc!=null){
						hc.getConnectionManager().shutdown();
					}
				}		
			}}).start();
		
		
	}
	private void parseNotice(String jsonData){
		JSONArray jsonArray = null;
		JSONObject jo = null;
		Notice notice = null;
		List<Notice> noticesArrayIm = new ArrayList<Notice>();
		try {
			jsonArray = new JSONArray(jsonData);
			Log.i("tag", jsonArray.length()+"");
			for(int i=0;i<jsonData.length();i++){
				jo = jsonArray.getJSONObject(i);
				String id = jo.getString("id");
				String title =jo.getString("title");
				String content = jo.getString("content");
				String time = jo.getString("time");
				notice = new Notice(Integer.valueOf(id),title,content,time);
				noticesArrayIm.add(notice);
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		Message message = new Message();
		if(noticesArrayIm!=null){
			message.obj=noticesArrayIm;
			handler.sendMessage(message);
		}
		
	}


	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		view =inflater.inflate(R.layout.activity_notices, null);
		return view;
	}
	

}
