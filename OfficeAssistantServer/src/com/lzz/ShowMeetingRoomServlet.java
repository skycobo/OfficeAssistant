package com.lzz;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.ResultSet;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONArray;
import org.json.JSONObject;

import com.cobo.tools.Mysql;

/**
 * Servlet implementation class ShowMeetingRoomServlet
 */
@WebServlet("/ShowMeetingRoom")
public class ShowMeetingRoomServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public ShowMeetingRoomServlet() {
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
		PrintWriter out = response.getWriter();
		String mRGID = request.getParameter("mRGID");
		Mysql.connect("localhost", "oa", "oa", "123456");
		ResultSet rs = Mysql.query("select * from meetingrooms where mRGID='"+mRGID+"';");
		try {
			if(rs.next()){
				JSONArray ja = new JSONArray();
				JSONObject jo = null;
				rs.previous();
				while(rs.next()){
					jo = new JSONObject();
					jo.put("name", rs.getString("name"));
					jo.put("location",rs.getString("location"));
					jo.put("floor", rs.getString("floor"));
					jo.put("accommodate",rs.getString("accommodate"));
					jo.put("wifi",rs.getString("wifi"));
					jo.put("projector",rs.getString("projector"));
					jo.put("othersSupport",rs.getString("othersSupport"));
					ja.put(jo);
				}
				System.out.println(ja.toString());
				out.print(ja.toString());
				
			}else{
				out.println("暂无预约!");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			out.close();
			Mysql.close();
		}
		
	}

}
