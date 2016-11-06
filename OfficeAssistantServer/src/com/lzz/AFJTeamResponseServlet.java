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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.cobo.tools.Mysql;

/**
 * Servlet implementation class ResponseAFJTeamServlet
 */
@WebServlet("/AFJTeamResponse")
public class AFJTeamResponseServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public AFJTeamResponseServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		request.setCharacterEncoding("utf-8");
		response.setContentType("text/html;charaset=utf-8");
		response.setCharacterEncoding("utf-8");
		
		
		
		String teamID = request.getParameter("teamID");
		String member = request.getParameter("member");
		String memberName = request.getParameter("memberName");
		String teamName = request.getParameter("teamName");
		String teamCreater = request.getParameter("teamCreater");
		String teamCreaterName =request.getParameter("teamCreaterName");
		String operation = request.getParameter("operation");

		if(operation.equals("query")){
			PrintWriter out  = response.getWriter();
			Mysql.connect("localhost", "oa", "oa", "123456");
			ResultSet rs = Mysql.query("select * from AFJTeam where teamID='"+teamID+"'");
			JSONArray ja = new JSONArray();
			JSONObject jo = null;
			try {
				while(rs.next()){
					jo = new JSONObject();
					jo.put("account",rs.getString("account"));
					jo.put("nickname",rs.getString("nickname"));
					ja.put(jo);
				}
			} catch (SQLException e) {
				e.printStackTrace();
			} catch (JSONException e) {
				e.printStackTrace();
			}
			if(ja.length()!=0){
				out.println(ja.toString());
			}
			out.close();
			Mysql.close();
			
		}else if(operation.equals("agree")){
			Mysql.connect("localhost", "oa", "oa", "123456");
			Mysql.insert("insert into team_"+teamID+" values('"+teamID+"','"+teamName+"','"+teamCreater+"','"+teamCreaterName+"','"+member+"','"+memberName+"');");
			Mysql.update("update users set teamID='"+teamID+"',teamName='"+teamName+"',teamCreater='"+teamCreater+"',teamCreaterName='"+teamCreaterName+"' where account='"+member+"'");
			Mysql.delect("delete from AFJTeam where account='"+member+"'");
			Mysql.close();
			
			
			
		}else if(operation.equals("deny")){
			Mysql.connect("localhost", "oa", "oa", "123456");
			Mysql.delect("delete from AFJTeam where account='"+member+"'");
			Mysql.close();
		}
		
		
	}

}
