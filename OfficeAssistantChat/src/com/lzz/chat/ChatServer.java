package com.lzz.chat;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.List;

public class ChatServer {
	public static void main(String[] args) throws IOException {
		List<User> userArray = new ArrayList<User>();
		DatagramSocket ds = new DatagramSocket(12345);
		byte [] bys = null;
		DatagramPacket dp = null;
		while(true){
			bys = new byte[1024];
			dp = new DatagramPacket(bys,bys.length);
			String text = null;
			byte [] text1 = null;
			String ipAddress =null;
			int port = 9999;
			ds.receive(dp);
			text = new String(dp.getData(),0,dp.getLength(),"gbk");
			text1 = dp.getData();
			ipAddress = dp.getAddress().getHostAddress();
			port = dp.getPort();
			if(text.length()>=5){
				System.out.println(text.substring(0, 5).equals("#$%&^"));
				if(text.substring(0, 5).equals("#$%&^")){
					new SaveUserInfo(userArray, text,ipAddress,port).start();
				}else if(text.equals("^&%$#")){
					new DeleteUserInfo(userArray,ipAddress,port).start();
				}else{
					new ReceSend(userArray,ds,text,ipAddress,port,text1).start();
				}
			}else{
				new ReceSend(userArray,ds,text,ipAddress,port,text1).start();
			}
		}
	}
	
}

class ReceSend extends Thread{
	private List<User> userArray;
	private DatagramSocket ds;
	private String ipAddress;
	private int port;
	private String text;
	private byte[] data;
	public ReceSend(List<User> userArray,DatagramSocket ds,String text,String ipAddress,int port,byte[] data){
		this.userArray = userArray;
		this.ds = ds;
		this.text = text;
		this.ipAddress = ipAddress;
		this.port= port;
		this.data = data;
	}
	@Override
	public void run() {
		String teamID = null;
		DatagramPacket packet = null; 
		List<User> members = new ArrayList<User>();
		for(User u : userArray){
			if(ipAddress.equals(u.getIpAddress())&&port==u.getPort()){
				teamID = u.getTeamID();
				System.out.println("teamID:"+teamID);
				break;
			}
		}
		if(teamID!=null){
			for(User u:userArray){
				if(u.getTeamID().equals(teamID)){
					members.add(u);
				}
			}
		}
		for(User u:members){
			try {
				packet = new DatagramPacket(data,data.length,InetAddress.getByName(u.getIpAddress()),u.getPort());
				ds.send(packet);
			} catch (IOException e) {
					e.printStackTrace();
			}
		}
	}
	
}

class DeleteUserInfo extends Thread{
	private List<User> userArray;
	private String ipAddress;
	private int port;
	public DeleteUserInfo(List<User> userArray,String ipAddress, int port){
		this.userArray = userArray;
		this.ipAddress = ipAddress;
		this.port = port;
	}
	@Override
	public void run() {
		for(User u:userArray){
			if(u.getIpAddress().equals(ipAddress)&&u.getPort()==port){
				userArray.remove(u);
			}
		}
		System.out.println("delete info ,now userArray size is "+userArray.size());
	
	}
}

class SaveUserInfo extends Thread{
	private List<User> userArray;
	private String ipAddress;
	private int port;
	private String userInfo;
	public SaveUserInfo(List<User> userArray,String userInfo,String ipAddress,int port){
		this.userArray = userArray;
		this.userInfo = userInfo;
		this.ipAddress = ipAddress;
		this.port=port;
	}
	@Override
	public void run() {
		String info[] =userInfo.split("\\^");
		User user = new User(info[1],info[2],info[3],ipAddress,port);
		userArray.add(user);
		System.out.println("add info,now userArray size is "+userArray.size());
	
	}
}