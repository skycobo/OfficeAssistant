package com.lzz;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONObject;

import com.cobo.tools.Mysql;

/**
 * Servlet implementation class LoginServlet
 */
@WebServlet("/login")
public class LoginServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public LoginServlet() {
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
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("text/html;charaset=utf-8");
		response.setCharacterEncoding("utf-8");
		PrintWriter out  = response.getWriter();
		String account = request.getParameter("account");
		String pw = request.getParameter("pw");
		if(account!=null&&pw!=null){
			Mysql.connect("localhost", "oa", "oa", "123456");
			ResultSet rs = Mysql.query("select * from users;");
			try {
				if(!rs.next()){
					out.println("账号或密码错误!");
				}else{
					rs.previous();
					while(rs.next()){
						if(account.equals(rs.getString("account"))&&pw.equals(rs.getString("pw"))){
							JSONObject jo = new JSONObject();
							jo.put("account", rs.getString("account"));
							jo.put("pw", rs.getString("pw"));
							jo.put("nickname", rs.getString("nickname"));
							jo.put("teamID",rs.getString("teamID"));
							jo.put("teamName",rs.getString("teamName"));
							jo.put("teamCreater",rs.getString("teamCreater"));
							jo.put("teamCreaterName", rs.getString("teamCreaterName"));
							out.println(jo.toString());
							break;
						}
					}
					if(rs.isAfterLast()){
						out.println("账号或密码错误!");
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			out.close();
			Mysql.close();
		}
	}

}
