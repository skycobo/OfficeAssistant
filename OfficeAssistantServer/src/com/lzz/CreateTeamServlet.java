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
 * Servlet implementation class CreateTeamServlet
 */
@WebServlet("/createTeam")
public class CreateTeamServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public CreateTeamServlet() {
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
		request.setCharacterEncoding("utf-8");
		response.setContentType("text/html;charaset=utf-8");
		response.setCharacterEncoding("utf-8");
		
		PrintWriter out  = response.getWriter();
		String teamID = request.getParameter("teamID");
		String teamName = request.getParameter("teamName");
		String teamCreater = request.getParameter("teamCreater");
		String teamCreaterName = request.getParameter("teamCreaterName");
		if(teamID!=null&&teamName!=null&&teamCreater!=null){
			String sqlCreateTable = "create table team_"+teamID+"(teamID CHAR(5) NOT NULL,teamName CHAR(9) NOT NULL,"
					+ "teamCreater CHAR(20) NOT NULL,teamCreaterName CHAR(9) NOT NULL,member CHAR(20),memberName CHAR(9))default character set=utf8;";
			Mysql.connect("www.skycobo.com", "oa", "root", "sky132343");
			ResultSet rs =Mysql.showTables();
			try{
				while(rs.next()){
					if(("team_"+teamID).equals(rs.getString(1))){
						out.println("对不起，该团队ID已被使用!");
						break;
					}
				}
				if(rs.isAfterLast()){
					Mysql.createTable(sqlCreateTable);
					Mysql.insert("insert into team_"+teamID+" values('"+teamID+"','"+teamName+"','"+teamCreater+"','"+teamCreaterName+"','"+teamCreater+"','"+teamCreaterName+"');");
					Mysql.update("update users set teamID='"+teamID+"',teamName='"+teamName+"',teamCreater='"+teamCreater+"' where account='"+teamCreater+"'");
					out.println("创建成功!");
				}
			}catch(SQLException se){
				se.printStackTrace();
			}
			Mysql.close();
			out.close();
		}
	}

}
