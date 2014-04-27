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
	private String loginPage = domain + "login";
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public SpiderServlet() {
        super();
    }

	/**
	 * @see Servlet#init(ServletConfig)
	 */
	public void init(ServletConfig config) throws ServletException {
		super.init(config);
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("text/html");
		
		String loadPage;
		if (request.getParameter("logIn") != null) {
			loadPage = doSpider(request, response);
		} else {
			loadPage = "/WEB-INF/include/Login.jsp";
		}
		
		RequestDispatcher rd = request.getRequestDispatcher(loadPage);
    	rd.forward(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}

	/**
	 * Run the spider and return the page to load the results
	 * 
	 * @return
	 */
	private String doSpider(HttpServletRequest request, HttpServletResponse response) {
		String cookies;
		boolean loggedIn;
		
		if (request.getParameter("cookies") != "") {
			cookies = request.getParameter("cookies");
			loggedIn = true; // not guaranteed to be true
		} else {
			ConnectionManager login = doLogin(request.getParameter("userName"), request.getParameter("password"));
			cookies = login.getCookies();
			loggedIn = login.wasLogInSuccessful();
		}
		
		Spider spider = new Spider(domain, cookies, loggedIn);
		request.setAttribute("output", spider);
		
		return "/WEB-INF/include/spiderResults.jsp";
	}

	/**
	 * Login to the site
	 * 
	 * @return
	 */
	private ConnectionManager doLogin(String userName, String password) {
		try {
			ConnectionManager login = new ConnectionManager(loginPage, userName, password);
			return login;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
}
