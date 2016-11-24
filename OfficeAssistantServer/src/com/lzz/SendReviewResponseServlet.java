package com.lzz;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.cobo.tools.Mysql;

/**
 * Servlet implementation class sendReviewResponseServlet
 */
@WebServlet("/sendReviewResponse")
public class SendReviewResponseServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public SendReviewResponseServlet() {
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

		String reviewID = request.getParameter("reviewID");
		String reviewIdea = request.getParameter("reviewIdea");
		String responseTime = request.getParameter("responseTime");
		String reviewStatus = request.getParameter("reviewStatus");
		System.out.println(reviewID+"-"+reviewIdea+"-"+ responseTime+"-"+reviewStatus);
		String sqlUpdate = "update review set reviewIdea='"+reviewIdea+"',responseTime='"+responseTime+"',status='"+reviewStatus+"' where reviewID='"+reviewID+"';";
		
		Mysql.connect("localhost", "oa", "oa", "123456");
		Mysql.update(sqlUpdate);
		Mysql.close();
	}

}
