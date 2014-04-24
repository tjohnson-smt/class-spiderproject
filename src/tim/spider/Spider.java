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
	private Page rootPage;
	private Set<Page> pageSet = new HashSet<Page>();
	private SortedSet<String> pageURIs = new TreeSet<String>();

	/**
	 * Creates a new spider.
	 * @param rootURL
	 */
	public Spider(String rootURL) {
		try {
			rootPage = new Page(rootURL);
			pageSet.add(rootPage);
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
				Page page = new Page(rootPage.getUrlString() + uri);
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
