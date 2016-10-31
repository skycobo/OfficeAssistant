package com.lzz;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class ShowSignInInfo
 */
@WebServlet("/showSignInInfo")
public class ShowSignInInfo extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public ShowSignInInfo() {
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
		
		String teamID = request.getParameter("teamID");
		File file = new File("/usr/local/tomcat/webapps/OfficeAssistantServer/signin/"+teamID+"_si.txt");
		BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(file),"utf-8"));
		String line = null;
		String info = "";
		while((line =br.readLine())!=null){
			info = info + line +"\n";
		}
		br.close();
		System.out.println(info);
		Date date  = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("MM/dd");
		if(info.substring(0,5).equals(sdf.format(date))){
			out.println(info);
		}else{
			file.delete();
		}
		out.close();
	
	}

}
