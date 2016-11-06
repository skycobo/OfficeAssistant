package com.lzz;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.cobo.tools.Mysql;

/**
 * Servlet implementation class GetMembersIPServlet
 */
@WebServlet("/getMembersIP")
public class GetMembersIPServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	List<Member> membersArray = new ArrayList<Member>();
	public DatagramSocket ds = null;
	
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public GetMembersIPServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doPost(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected  void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		request.setCharacterEncoding("utf-8");
		response.setContentType("text/html;charaset=utf-8");
		response.setCharacterEncoding("utf-8");
		if(ds==null){
			ds=new DatagramSocket(9999);
		}
		String teamID = request.getParameter("teamID");
		String account = request.getParameter("account");
		String nickname = request.getParameter("nickname");
		String ipAddress = request.getRemoteAddr();
		System.out.println(ipAddress);

		Member member = new Member(teamID,account,nickname,ipAddress);
		membersArray.add(member);
		List<Member> teamArray = new ArrayList<Member>();
		
		Mysql.connect("localhost", "oa", "oa", "123456");
		ResultSet rs = Mysql.query("select * from team_"+teamID+";");
		try {
			while(rs.next()){
				String ip=null;
				for(Member me :membersArray){
					if(me.getAccount().equals(rs.getString("member"))){
					 	ip= me.getIpAddress();
					 	break;
					}
				}
				Member m = new Member();
				m.setAccount(rs.getString("member"));
				m.setTeamID(rs.getString("teamID"));
				m.setNickname(rs.getString("memberName"));
				m.setIpAddress(ip);
				
				teamArray.add(m);
			}
		new GroupChat(teamArray).start();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	class GroupChat extends Thread{
		private List<Member> teamArray;
		public GroupChat(List<Member> teamArray){
			this.teamArray=teamArray;
		}
		@Override
		public void run() {
			
			DatagramPacket packet=null;
			DatagramPacket packet2=null;
			try {
				byte buf [] = new byte[1024];
				packet= new DatagramPacket(buf, buf.length);
				while(true){
					ds.receive(packet);
					String text = new String(packet.getData(),0,packet.getLength());
					System.out.println(text);
					for(Member m:teamArray){
						if(packet.getAddress().getHostAddress().equals(m.getIpAddress())){
							m.setPort(packet.getPort());
						}
						System.out.println(m.getAccount());
						System.out.println(m.getIpAddress());
						System.out.println("rport:"+packet.getPort());
						System.out.println("vport:"+m.getPort());
						packet2= new DatagramPacket(packet.getData(),packet.getLength(), 
								InetAddress.getByName(m.getIpAddress()), m.getPort());
						ds.send(packet2);
					}
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
			
			
		}
		
	}


}
