package com.lzz;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.ResultSet;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.json.JSONObject;
import com.cobo.tools.Mysql;

/**
 * Servlet implementation class InputMeetingRoomVerifyServlet
 */
@WebServlet("/InputMeetingRoomVerify")
public class InputMeetingRoomVerifyServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public InputMeetingRoomVerifyServlet() {
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
		ResultSet rs = Mysql.query("select * from mrg;");
		try {
			if(!rs.next()){
				out.print("ID或密码错误!");
			}else{
				rs.previous();
				while(rs.next()){
					if(mRGID.equals(rs.getString("mRGID"))&&pw.equals(rs.getString("pw"))){
						out.print("验证成功!");
						System.out.println("验证成功!");
						break;
					}
				}
				if(rs.isAfterLast()){
					out.print("ID或密码错误!");
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		out.close();
		Mysql.close();

	}

}
