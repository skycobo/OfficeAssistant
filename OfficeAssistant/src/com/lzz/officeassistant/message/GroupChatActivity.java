package com.lzz.officeassistant.message;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import com.lzz.officeassistant.R;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class GroupChatActivity extends Activity {
	private TextView tv_groupChat_text;
	private EditText et_groupChat_edit;
	private Button b_groupChat_send;
	private String teamID;
	private String account;
	private String nickname;
	private Handler sendHandler;
	public DatagramSocket ds = null;
	private Handler receHandler = new Handler(){

		@Override
		public void handleMessage(Message msg) {
			String text = (String) msg.obj;
			tv_groupChat_text.append(text+"\n\n");
		}
		
	};
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_group_chat);
		SharedPreferences sp = getSharedPreferences("userInfo", Context.MODE_PRIVATE);
		tv_groupChat_text = (TextView) findViewById(R.id.tv_groupChat_text);
		et_groupChat_edit = (EditText) findViewById(R.id.et_groupChat_edit);
		b_groupChat_send = (Button) findViewById(R.id.b_groupChat_send);
		try {
			ds = new DatagramSocket(8888);
		} catch (SocketException e) {
			e.printStackTrace();
		}
		teamID = sp.getString("teamID", null);
		account = sp.getString("account", null);
		nickname = sp.getString("nickname", null);
		new sendMsg().start();
		new receiveMsg().start();
	
		b_groupChat_send.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if(!et_groupChat_edit.getText().toString().equals("")){
					Message msg = new Message();
					msg = new Message();
					msg.obj=nickname+":\n"+et_groupChat_edit.getText().toString();
					sendHandler.sendMessage(msg);
				}
				et_groupChat_edit.setText("");
				
			}
		});
	
	}


	@Override
	protected void onDestroy() {
		Message msg = new Message();
		msg.obj="^&%$#";
		sendHandler.sendMessage(msg);
		super.onDestroy();
	}
	
	class receiveMsg extends Thread{

		@Override
		public void run() {
			try {
				byte [] data = null;
				DatagramPacket dp = null;
				while(true){
					data = new byte[1024];
					dp =new DatagramPacket(data, data.length);
					ds.receive(dp);
					String text = new String(dp.getData(),0,dp.getLength(),"gbk");
					Log.i("tag", "接受数据"+dp.getData()[0]+""+dp.getData()[1]);
					Message msg = new Message();
					msg.obj = text;
					receHandler.sendMessage(msg);
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
			
			
		}
	}
	
	class sendMsg extends Thread{
		@Override
		public void run() {
			byte [] info = ("#$%&^"+teamID+"^"+account+"^"+nickname).getBytes();
			try {
				DatagramPacket dp1 = new DatagramPacket(info, info.length,InetAddress.getByName("123.206.41.115"),12345);
				ds.send(dp1);
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			 Looper.prepare();
			 sendHandler = new Handler(){
					@Override
					public void handleMessage(Message msg) {
						String text = (String) msg.obj;
						try {
							byte [] bys = text.getBytes("gbk");
							DatagramPacket dp = new DatagramPacket(bys, bys.length,InetAddress.getByName("123.206.41.115"),12345);
							Log.i("tag", "发送数据:"+bys[0]+""+bys[1]);
							ds.send(dp);
						} catch (IOException e) {
							e.printStackTrace();
						}	
					}
			 };
			 Looper.loop();
		}
		
	}

	
	
}




