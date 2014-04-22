package tim.spider;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;

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
	private URL baseURL; 
	private List<String> linkList = new ArrayList<String>();
	
	private URLConnection urlConnection;
	private InputStream inputStream;
	private ByteArrayOutputStream outputStream;
	
	/**
	 * Creates a new page.
	 * @param baseURL
	 */
	public Page(String baseURL) {
		try {
			this.baseURL = validateURL(baseURL);
			this.urlConnection = getConnection();
			this.inputStream = getInputStream();
			this.outputStream = getOutputStream();
			findLinks();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/**
	 * Validate the page's URL.
	 * 
	 * @param urlString
	 * @return
	 * @throws Exception
	 */
	private URL validateURL(String urlString) throws Exception {
		URL validURL;

	    try {
	        validURL = new URL(urlString);
	    } catch (MalformedURLException e) {
            String msg = "Invalid URL Detected: " + urlString;
            throw new Exception(msg);
        }
	    
	    return validURL;
	}

	/**
	 * Get's a connection to the page's URL.
	 * 
	 * @return
	 * @throws Exception
	 */
	private URLConnection getConnection() throws Exception {
		URLConnection urlConn;
		
		try {
			urlConn = baseURL.openConnection();
		} catch (IOException e) {
			String msg = "Can't get a connection for: " + baseURL;
			throw new Exception(msg);
		}
		
		return urlConn;
	}
	
	/**
	 * Returns the page's input stream.
	 * 
	 * @return
	 * @throws Exception
	 */
	private InputStream getInputStream() throws Exception {
		InputStream pageInStream;
		
		try {
			pageInStream = urlConnection.getInputStream();
		} catch (IOException e) {
			String msg = "Can't get the input stream for: " + baseURL;
			throw new Exception(msg);
		}
		
		return pageInStream;
	}
	
	/**
	 * Return's the output stream for the page.
	 * 
	 * @return
	 * @throws Exception
	 */
	private ByteArrayOutputStream getOutputStream() throws Exception {
		ByteArrayOutputStream output = new ByteArrayOutputStream();
		
		int read;
		byte[] bytes = new byte[1024];
		
		try {
			while ((read = inputStream.read(bytes)) != -1) {
				output.write(bytes, 0, read);
			}
		} catch (IOException e) {
			String msg = "Can't write the output stream for: " + baseURL;
			throw new Exception(msg);
		}
		
		return output;
	}
	
	/**
	 * Return's the page's HTML.
	 * 
	 * @return
	 */
	public String getHTML() {
		return outputStream.toString();
	}

	/**
	 * Return's a string representing the page's URL.
	 * 
	 * @return
	 */
	public String getUrlString() {
		return baseURL.toString();
	}

	/**
	 * Return's information about the page.
	 * 
	 * @return
	 */
	public String toString() {
		StringBuilder output = new StringBuilder();
		String newLine = "<br />";
		
		output.append("<strong>Links found for: ");
		output.append(getUrlString());
		output.append("</strong>");
		output.append(newLine);
		
		for (String link : linkList) {
			output.append(link);
			output.append(newLine);
		}
		
		return output.toString();
	}
	
	private void findLinks() throws IOException {
		ParserDelegator parserDelegator = new ParserDelegator();
		ParserCallback parserCallback = new ParserCallback() {
			public void handleStartTag(Tag tag, MutableAttributeSet attribute, int pos) {
				if (tag == Tag.A) {
					String address = (String) attribute.getAttribute(Attribute.HREF);
					linkList.add(address);
				}
			}
		};
		
		parserDelegator.parse(new StringReader(getHTML()), parserCallback, true);
	}
}
