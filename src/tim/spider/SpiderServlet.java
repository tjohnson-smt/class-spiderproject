package tim.spider;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

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
	private String startingURL = "http://www.siliconmtn.com/";
       
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
		Page firstPage = new Page(startingURL);
		
		Set<Page> pageList = new HashSet<Page>();
		pageList.add(firstPage);
		
		for (int i = 0; i < pageList.size(); i++) {
			Page page = pageList.iterator().next();
			
			System.out.println("Getting Links for Page: " + page.getUrlString());
			for (String uri : page.getLinksList()) {
				pageList.add(new Page(startingURL + uri));
			}
		}
		
		response.setContentType("text/html");
		request.setAttribute("output", firstPage);
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
