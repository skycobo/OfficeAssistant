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

import com.cobo.tools.Mysql;

/**
 * Servlet implementation class CreateMeetingRoomGroupIDServlet
 */
@WebServlet("/CreateMeetingRoomGroupID")
public class CreateMeetingRoomGroupIDServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public CreateMeetingRoomGroupIDServlet() {
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
		request.setCharacterEncoding("utf-8");
		PrintWriter out  = response.getWriter();
		String mRGID = request.getParameter("mRGID");
		String pw = request.getParameter("pw");
		
		Mysql.connect("localhost", "oa", "oa", "123456");
		ResultSet rs = Mysql.query("select mRGID from mrg;");
		try {
			if(!rs.next()){
				Mysql.insert("insert into mrg(mRGID,pw) values('"+mRGID+"','"+pw+"')");
				out.print("创建成功!");
			}else{
				rs.previous();
				while(rs.next()){
					if(mRGID.equals(rs.getString("mRGID"))){
					out.print("该ID已被使用!");
					break;
					}
				}
				if(rs.isAfterLast()){
					Mysql.insert("insert into mrg(mRGID,pw) values('"+mRGID+"','"+pw+"')");
					out.print("创建成功!");
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		Mysql.close();
		out.close();
	}

}
