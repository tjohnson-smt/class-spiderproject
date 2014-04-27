package tim.spider;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.StringReader;
import java.util.SortedSet;
import java.util.TreeSet;

import javax.swing.text.MutableAttributeSet;
import javax.swing.text.html.HTML.Attribute;
import javax.swing.text.html.HTML.Tag;
import javax.swing.text.html.HTMLEditorKit.ParserCallback;
import javax.swing.text.html.parser.ParserDelegator;


/**
 * @author tim
 *
 */
public class Page {
	private SortedSet<String> linksList = new TreeSet<String>();
	private ConnectionManager connection;
	
	/**
	 * Creates a new page.
	 * @param baseURL
	 * @throws Exception 
	 */
	public Page(String baseURL, String savePath, String cookies) throws Exception {
		try {
			this.connection = new ConnectionManager(baseURL, cookies);
			savePage(savePath);
			findLinks();
		} catch (Exception e) {
			String msg = "Can't create the page!";
            throw new Exception(msg);
		}
	}

	/**
	 * Return's information about the page.
	 * 
	 * @return
	 */
	@Override
	public String toString() {
		StringBuilder output = new StringBuilder();
		String newLine = "<br />";
		
		output.append("<strong>Links found for page: ");
		output.append(getUrlString());
		output.append("</strong>");
		output.append(newLine);
		
		for (String link : linksList) {
			output.append(link);
			output.append(newLine);
		}
		
		return output.toString();
	}
	
	/**
	 * Determines if the page is equal to another Page.
	 * 
	 * @return
	 */
	@Override
	public boolean equals(Object otherObj) {
		if (otherObj == null) return false;
		if (this.getClass() != otherObj.getClass()) return false;
		
		if (!this.getUrlString().equals(((Page) otherObj).getUrlString())) return false;
		
		return true;
	}
	
	/**
	 * @return the linksList
	 */
	public SortedSet<String> getLinksList() {
		return linksList;
	}
	
	/**
	 * Returns the page's HTML.
	 * 
	 * @return
	 */
	public String getHTML() {
		return connection.getOutputStream().toString();
	}

	/**
	 * Returns a string representing the page's URL.
	 * 
	 * @return
	 */
	public String getUrlString() {
		return connection.getBaseURL().toString();
	}
	
	/**
	 * Returns a valid file system path/name, based on the URL string
	 * @return
	 */
	private String getFilePathAndName() {
		StringBuilder filePath = new StringBuilder();
		filePath.append(getUrlString());
		
		if (filePath.toString().endsWith("/")) filePath.append("index");
		filePath.append(".htm");
		
		if (filePath.substring(0, 5) == "http:") {
			filePath.delete(0,7);
		} else {
			filePath.delete(0,8);
		}

		return filePath.toString();
	}
	
	/**
	 * Returns a valid file name for the page
	 * @return
	 */
	private String getFileName() {
		return new File(getFilePathAndName()).getName();
	}
	
	/**
	 * Returns a valid file system path to save the page to
	 * @return
	 */
	private String getFilePath() {
		int fileIndex = getFilePathAndName().lastIndexOf(getFileName());
		return getFilePathAndName().substring(0, fileIndex);
	}
	
	/**
	 * Parses the links in the page.
	 * For now, we are only concerned about the links starting with a '/'.
	 * 
	 * @throws IOException
	 */
	private void findLinks() throws IOException {
		ParserDelegator parserDelegator = new ParserDelegator();
		ParserCallback parserCallback = new ParserCallback() {
			public void handleStartTag(Tag tag, MutableAttributeSet attribute, int pos) {
				try {
					if (tag == Tag.A) {
						String address = (String) attribute.getAttribute(Attribute.HREF);
						if (address.charAt(0) == '/' && address.length() > 1 && address != null)
							linksList.add(address.substring(1));
					}
				} catch(NullPointerException e) {}
			}
		};
		
		parserDelegator.parse(new StringReader(getHTML()), parserCallback, true);
	}
	
	/**
	 * Saves a copy of the page to the file system.
	 * @throws IOException 
	 */
	private void savePage(String rootPath) throws IOException {
		File directory = new File(rootPath + getFilePath());
		directory.mkdirs();

		File outputFile = new File(rootPath + getFilePath() + getFileName());
		FileOutputStream fileOutputStream = new FileOutputStream(outputFile, true);
		connection.getOutputStream().writeTo(fileOutputStream);
	}
}
