package tim.spider;

import java.util.HashSet;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

/**
 * @author tim
 *
 */
public class Spider {
	private String savePath = System.getProperty("user.home") + "/Cache/";
	private String cookies;
	
	private Page rootPage;
	private Set<Page> pageSet = new HashSet<Page>();
	private SortedSet<String> pageURIs = new TreeSet<String>();
	
	private boolean loggedIn = false;
	private int pageCount = 0;

	/**
	 * Creates a new spider.
	 * @param rootURL
	 */
	public Spider(String rootURL, String cookies, boolean loggedIn) {
		try {
			this.cookies = cookies;
			this.loggedIn = loggedIn;
			
			rootPage = new Page(rootURL, savePath, cookies);
			pageCount += 1;
			pageSet.add(rootPage);
			System.out.println(rootURL);
			addPages(rootPage.getLinksList());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Adds pages to the page set.
	 * @param urisToAdd
	 */
	private void addPages(Set<String> urisToAdd) {
		// get the Set of everything we will be adding to the pageSet
		urisToAdd.removeAll(pageURIs);
		
		// add the new URIs to the list
		pageURIs.addAll(urisToAdd);
		
		// create new pages for every uri and add to the pageSet
		for (String uri : urisToAdd) {
			try {
				Page page = new Page(rootPage.getUrlString() + uri, savePath, cookies);
				System.out.println(uri);
				pageCount += 1;
				pageSet.add(page);
				addPages(page.getLinksList());
			} catch(Exception e) {
				pageURIs.remove(uri);
			}
		}
	}
	
	/**
	 * Return's spider statistics.
	 * 
	 * @return
	 */
	@Override
	public String toString() {
		StringBuilder output = new StringBuilder();
		String newLine = "<br />";
		
		if (loggedIn == true) {
			output.append("You successfully logged in or you attempted to hijack a session.");
		} else {
			output.append("You were not logged in, so expect fewer pages.");
		}
		output.append(newLine);

		output.append("Page Count: ");
		output.append(pageCount);
		output.append(newLine);
		output.append(newLine);
		
		output.append("<strong>Links found for crawl on domain: ");
		output.append(rootPage.getUrlString());
		output.append("</strong>");
		output.append(newLine);
		
		for (String uri : pageURIs) {
			output.append(uri);
			output.append(newLine);
		}
		
		return output.toString();
	}
}
