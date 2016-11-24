package com.lzz;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.cobo.tools.Mysql;

/**
 * Servlet implementation class ReviewRequesterCommitServlet
 */
@WebServlet("/ReviewRequesterCommit")
public class ReviewRequesterCommitServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public ReviewRequesterCommitServlet() {
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
		String reviewTitle = request.getParameter("reviewTitle");
		String reviewType = request.getParameter("reviewType");
		String reviewContent = request.getParameter("reviewContent");
		String commitTime = request.getParameter("commitTime");
		String requester = request.getParameter("requester");
		String handler = request.getParameter("handler");
		String status = request.getParameter("status");
		String reviewIdea = request.getParameter("reviewIdea");
		String responseTime = request.getParameter("responseTime");
		String sqlInsert = "insert into review values('"+reviewID+"','"+reviewTitle+"','"+reviewType+"','"+reviewContent+"','"+commitTime+"','"+
		requester+"','"+handler+"','"+status+"','"+reviewIdea+"','"+responseTime+"');";
		Mysql.connect("localhost", "oa", "oa", "123456");
		Mysql.insert(sqlInsert);
		Mysql.close();
	}

}
