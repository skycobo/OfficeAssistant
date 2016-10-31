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
 * Servlet implementation class ApplyForJoinTeamServlet
 */
@WebServlet("/applyForJoinTeam")
public class ApplyForJoinTeamServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public ApplyForJoinTeamServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doPost(request,response);
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
		String account = request.getParameter("account");
		String nickname = request.getParameter("nickname");
		
		if(teamID!=null&&account!=null&&nickname!=null){
			Mysql.connect("www.skycobo.com", "oa", "root", "sky132343");
			ResultSet rs =Mysql.showTables();
			try{
				while(rs.next()){
					if(("team_"+teamID).equals(rs.getString(1))){
						Mysql.insert("insert into AFJTeam values('"+teamID+"','"+account+"','"+nickname+"')");
						out.println("申请已提交,请等待审核!");
						break;
					}
				}
				if(rs.isAfterLast()){
					out.println("该团队不存在!");
				}
			}catch(SQLException se){
				se.printStackTrace();
			}
			Mysql.close();
			out.close();
		
		}
	}

}
