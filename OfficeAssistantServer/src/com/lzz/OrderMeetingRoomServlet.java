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
 * Servlet implementation class OrderMeetingRoomServlet
 */
@WebServlet("/OrderMeetingRoom")
public class OrderMeetingRoomServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public OrderMeetingRoomServlet() {
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
		
		String name = request.getParameter("name");
		String mRGID = request.getParameter("mRGID");
		String topic = request.getParameter("topic");
		String orderTeam = request.getParameter("orderTeam");
		String startTime = request.getParameter("startTime");
		String endTime = request.getParameter("endTime");
		
		Mysql.connect("localhost", "oa", "oa", "123456");
		Mysql.insert("insert into meetings(name,mRGID,topic,orderTeam,startTime,endTime)values"
				+ "('"+name+"','"+mRGID+"','"+topic+"','"+orderTeam+"','"+startTime+"','"+endTime+"')");
		out.print("预约成功!");
		Mysql.close();
		out.close();
	}

}
