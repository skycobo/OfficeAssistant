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
 * Servlet implementation class ShowRequesterReviewServlet
 */
@WebServlet("/ShowRequesterReview")
public class ShowRequesterReviewServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public ShowRequesterReviewServlet() {
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
		String requester = request.getParameter("requester");
		Mysql.connect("localhost", "oa", "oa", "123456");
		ResultSet rs = Mysql.query("select * from review where requester='"+requester+"';");
		try {
			if(rs.next()){
				JSONArray ja = new JSONArray();
				JSONObject jo = null;
				rs.previous();
				while(rs.next()){
					jo = new JSONObject();
					jo.put("reviewID", rs.getString("reviewID"));
					jo.put("reviewTitle",rs.getString("reviewTitle"));
					jo.put("reviewType", rs.getString("reviewType"));
					jo.put("reviewContent",rs.getString("reviewContent"));
					jo.put("commitTime",rs.getString("commitTime"));
					jo.put("requester",rs.getString("requester"));
					jo.put("handler",rs.getString("handler"));
					jo.put("status",rs.getString("status"));
					jo.put("reviewIdea",rs.getString("reviewIdea"));
					jo.put("responseTime",rs.getString("responseTime"));
					ja.put(jo);
				}
				System.out.println(ja.toString());
				out.print(ja.toString());
				
			}else{
				out.println("暂无审批!");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			out.close();
			Mysql.close();
		}
		
	}

}
