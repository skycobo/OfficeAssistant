package com.lzz;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.cobo.tools.Mysql;

/**
 * Servlet implementation class RegisterServlet
 */
@WebServlet("/register")
public class RegisterServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
    /**
     * Default constructor. 
     */
    public RegisterServlet() {
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
		// TODO Auto-generated method stub
		request.setCharacterEncoding("utf-8");
		response.setContentType("text/html;charaset=utf-8");
		response.setCharacterEncoding("utf-8");
		
		PrintWriter out  = response.getWriter();
		String account = request.getParameter("account");
		String pw = request.getParameter("pw");
		String nickname = request.getParameter("nickname");
		if(account!=null&&pw!=null&&nickname!=null){
			Mysql.connect("www.skycobo.com", "oa", "root", "sky132343");
			ResultSet rs = Mysql.query("select account from users;");
			try {
				while(rs.next()){
					if(account.equals(rs.getString("account"))){
						out.println("对不起，该邮箱已被使用!");
						break;
					}
				}
				if(rs.isAfterLast()){
					Mysql.insert("insert into users(account,pw,nickname) values('"+account+"','"+pw+"','"+nickname+"')");
					out.println("注册成功!");
				}
	
			} catch (SQLException e) {
				e.printStackTrace();
			}
			Mysql.close();
		}
		out.close();
	}

}
