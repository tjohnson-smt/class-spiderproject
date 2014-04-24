package tim.spider;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class SpiderServlet
 */
@WebServlet("/SpiderServlet")
public class SpiderServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private String domain = "http://www.siliconmtn.com/";
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public SpiderServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see Servlet#init(ServletConfig)
	 */
	public void init(ServletConfig config) throws ServletException {
		// TODO Auto-generated method stub
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		Spider spider = new Spider(domain);
		
		response.setContentType("text/html");
		request.setAttribute("output", spider);
		RequestDispatcher rd = request.getRequestDispatcher("/WEB-INF/include/spiderResults.jsp");
    	rd.forward(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}


}
