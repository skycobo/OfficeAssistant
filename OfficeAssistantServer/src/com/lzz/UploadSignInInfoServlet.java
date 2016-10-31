package com.lzz;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class UploadSignInInfo
 */
@WebServlet("/uploadSignInInfo")
public class UploadSignInInfoServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public UploadSignInInfoServlet() {
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
		String teamID = request.getParameter("teamID");
		String nickname = request.getParameter("nickname");
		String position = request.getParameter("position");
		String time = request.getParameter("time");
		System.out.println(time);
		File file = new File("/usr/local/tomcat/webapps/OfficeAssistantServer/signin");
		file.mkdir();
		BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream
				(new File("/usr/local/tomcat/webapps/OfficeAssistantServer/signin/"+teamID+"_si.txt"),true),"utf-8"));
		System.out.println(time+" "+position+" "+nickname+"\n");
		bw.write(time+" "+position+" "+nickname+"\n");
		bw.flush();
		bw.close();
	}

}
