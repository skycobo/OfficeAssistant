package com.lzz;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class AddNoticeServlet
 */
@WebServlet("/addNotice")
public class AddNoticeServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public AddNoticeServlet() {
        super();

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
		String newNotice = request.getParameter("notice");
		String noticeId  = request.getParameter("noticeId");
		BufferedReader br =null;
		BufferedWriter bw =null;
		if(newNotice!=null&&noticeId!=null){
			if(Integer.valueOf(noticeId)>1){
				br = new BufferedReader(new InputStreamReader(new FileInputStream("/usr/local/tomcat/webapps/OfficeAssistantServer/"+teamID+".json"),"utf-8"));
				String line = null;
				String notices="";
				while((line=br.readLine())!=null){
					notices=notices+line;
				}
				br.close();
				bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream("/usr/local/tomcat/webapps/OfficeAssistantServer/"+teamID+".json"),"utf-8"));
				notices = notices.substring(0, notices.length()-1);
				if(newNotice!=null){
					notices = notices+newNotice;
					bw.write(notices);
					bw.flush();
				}
				bw.close();
			}else if(Integer.valueOf(noticeId)==1){
				bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream("/usr/local/tomcat/webapps/OfficeAssistantServer/"+teamID+".json"),"utf-8"));
				bw.write(newNotice);
				bw.flush();
				bw.close();
			}
		}
	}

}
