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
 * Servlet implementation class UploadMRInfoServlet
 */
@WebServlet("/UploadMRInfo")
public class UploadMRInfoServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public UploadMRInfoServlet() {
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
		String location = request.getParameter("location");
		String floor = request.getParameter("floor");
		String accommodate = request.getParameter("accommodate");
		String wifi = request.getParameter("wifi");
		String projector = request.getParameter("projector");
		String othersSupport = request.getParameter("othersSupport");
		
		Mysql.connect("localhost", "oa", "oa", "123456");
		ResultSet rs = Mysql.query("select name,mRGID from meetingrooms;");
		try {
			if(!rs.next()){
				Mysql.insert("insert into meetingrooms(name,mRGID,location,floor,"
						+ "accommodate,wifi,projector,othersSupport)values"
						+ "('"+name+"','"+mRGID+"','"+location+"','"+floor+"','"+accommodate+"','"+wifi+"','"+projector+"','"+othersSupport+"')");
				out.print("录入成功!");
			}else{
				rs.previous();
				while(rs.next()){
					if(name.equals(rs.getString("name"))&&mRGID.equals(rs.getString("mRGID"))){
					out.print("该会议室已存在!");
					break;
					}
				}
				if(rs.isAfterLast()){
					Mysql.insert("insert into meetingrooms(name,mRGID,location,floor,"
							+ "accommodate,wifi,projector,othersSupport)values"
							+ "('"+name+"','"+mRGID+"','"+location+"','"+floor+"','"+accommodate+"','"+wifi+"','"+projector+"','"+othersSupport+"')");
					out.print("录入成功!");
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		Mysql.close();
		out.close();
	}

}
