package tim.spider;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.util.List;
import java.util.Map;

/**
 * @author tim
 *
 */
public class ConnectionManager {
	private URL baseURL; 
	private HttpURLConnection urlConnection;
	private InputStream inputStream;
	private ByteArrayOutputStream outputStream;

	private int responseCode;
	private Map<String, List<String>> headerFields;
	
	private String userName;
	private String password;
	
	/**
	 * Connection type used when logging in
	 */
	private static final String CONNECTION_LOGIN = "login";
	
	/**
	 * Connection type used after logging (with a cookie)
	 */
	private static final String CONNECTION_COOKIE = "cookie";

	/**
	 * Creates a new connection without a cookie.
	 * AKA: Logging in to the site.
	 * 
	 * @param baseURL
	 * @throws Exception 
	 */
	public ConnectionManager(String baseURL, String userName, String password) throws Exception {
		try {
			this.userName = userName;
			this.password = password;
			
			validateURL(baseURL);
			setConnection(CONNECTION_LOGIN, "");
			setInputStream();
			setOutputStream();
		} catch (Exception e) {
			String msg = "Can't set up the connection!";
            throw new Exception(msg);
		}
	}

	/**
	 * Creates a new connection with an existing cookie(s) string.
	 * 
	 * @param baseURL
	 * @param cookieString
	 * @throws Exception 
	 */
	public ConnectionManager(String baseURL, String cookies) throws Exception {
		try {
			validateURL(baseURL);
			setConnection(CONNECTION_COOKIE, cookies);
			setInputStream();
			setOutputStream();
		} catch (Exception e) {
			String msg = "Can't set up the connection!";
            throw new Exception(msg);
		}
	}

	/**
	 * Validate the selected URL.
	 * 
	 * @param urlString
	 * @throws Exception
	 */
	private void validateURL(String urlString) throws Exception {
		URL validURL;

	    try {
	        validURL = new URL(urlString);
	    } catch (MalformedURLException e) {
            String msg = "Invalid URL Detected: " + urlString;
            throw new Exception(msg);
        }
	    
	    baseURL = validURL;
	}

	/**
	 * @return the baseURL
	 */
	public URL getBaseURL() {
		return baseURL;
	}

	/**
	 * Set's up a connection for the URL.
	 * 
	 * @throws Exception
	 */
	private void setConnection(String connectionType, String cookies) throws Exception {
		HttpURLConnection conn;
		
		try {
			conn = (HttpURLConnection) baseURL.openConnection();
			conn.setRequestProperty("User-Agent", "Mozilla/5.0 (X11; Ubuntu; Linux x86_64; rv:28.0) Gecko/20100101 Firefox/28.0");
			
			if (connectionType == CONNECTION_LOGIN) {
				setConnectionLoginParams(conn);
			} else if (connectionType == CONNECTION_COOKIE) {
				setConnectionCookieParams(conn, cookies);
			}
			
			conn.connect();
			responseCode = conn.getResponseCode();
			headerFields = conn.getHeaderFields();
		} catch (IOException e) {
			String msg = "Can't set a connection for: " + baseURL;
			throw new Exception(msg);
		}
		
		urlConnection = conn;
	}
	
	/**
	 * Sets up the connection parameters needed for a login
	 * @throws IOException 
	 */
	private void setConnectionLoginParams(HttpURLConnection conn) throws IOException {
		// set the form parameters
		StringBuilder postParameters = new StringBuilder();
		postParameters.append("actionId=c0a8021ebdcce96eb5b6dae8874aaa46");
		postParameters.append("&requestType=reqBuild");
		postParameters.append("&pmid=c0a80241f25605d1f95007fe3b1d785e");
		postParameters.append("&destUrl=").append(URLEncoder.encode("/login?logOff=true", "UTF-8"));
		postParameters.append("&reqType=");
		postParameters.append("&emailAddress=").append(URLEncoder.encode(userName, "UTF-8"));
		postParameters.append("&password=").append(URLEncoder.encode(password, "UTF-8"));
		postParameters.append("&remember=1");
		postParameters.append("&smt_formValidated=21902");
		
		byte[] data = postParameters.toString().getBytes(Charset.forName("UTF-8"));
		
		// additional setup necessary
		conn.setRequestMethod("POST");
		conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded;charset=utf-8");
		conn.setRequestProperty("Content-Length", Integer.toString(data.length));
		conn.setRequestProperty("Accept-Language", "en-US,en;q=0.5");
		conn.setDoOutput(true);
		conn.setInstanceFollowRedirects(false);
		
		// write the post parameters to the connection's OutputStream
		conn.getOutputStream().write(data);
	}
	
	/**
	 * Sets up the connection parameters needed when using a cookie
	 */
	private void setConnectionCookieParams(HttpURLConnection conn, String cookies) {
		conn.setRequestProperty("Cookie", cookies);
	}
	
	/**
	 * Sets the connection's input stream.
	 * 
	 * @throws Exception
	 */
	private void setInputStream() throws Exception {
		InputStream connInStream;
		
		try {
			connInStream = urlConnection.getInputStream();
		} catch (IOException e) {
			String msg = "Can't set the input stream for: " + baseURL;
			throw new Exception(msg);
		}
		
		inputStream = connInStream;
	}
	
	/**
	 * Sets the connection's output stream.
	 * 
	 * @throws Exception
	 */
	private void setOutputStream() throws Exception {
		ByteArrayOutputStream output = new ByteArrayOutputStream();
		
		int read;
		byte[] bytes = new byte[1024];
		
		try {
			while ((read = inputStream.read(bytes)) != -1) {
				output.write(bytes, 0, read);
			}
		} catch (IOException e) {
			String msg = "Can't write to the output stream for: " + baseURL;
			throw new Exception(msg);
		}
		
		outputStream = output;
	}
	
	/**
	 * @return the outputStream
	 */
	public ByteArrayOutputStream getOutputStream() {
		return outputStream;
	}
	
	/**
	 * Gets the specified header field, including any with multiple values
	 * 
	 * @return
	 */
	private List<String> getHeaderField(String fieldName) {
		for (Map.Entry<String, List<String>> entry : headerFields.entrySet()) {
			if (entry.getKey() != null && entry.getKey().equals(fieldName)) {
				return entry.getValue();
			}
		}
		return null;
	}
	
	/**
	 * Gets the value of the specified cookie
	 * 
	 * @return
	 */
	public String getCookieValue(String cookieName) {
		if (getHeaderField("Set-Cookie") == null) return null;
		
		for (String value : getHeaderField("Set-Cookie")) {
			if (value.substring(0, value.indexOf("=")).equals(cookieName)) {
				return value.substring(value.indexOf("=") + 1, value.indexOf(";"));
			}
		}
		
		return null;
	}
	
	/**
	 * Returns a string with all of the cookies
	 * 
	 * @return
	 */
	public String getCookies() {
		StringBuilder cookies = new StringBuilder();
		
		for (String value : getHeaderField("Set-Cookie")) {
			cookies.append(value.substring(0, value.indexOf(";") + 1));
			cookies.append(" ");
		}
		
		// trim off the final space to make things perfect
		cookies.deleteCharAt(cookies.length() - 1);
		
		return cookies.toString();
	}
	
	/**
	 * Checks if a user logged in successfully.
	 * Looks for the existence of the SITEBUILDER_COOKIE cookie.
	 */
	public boolean wasLogInSuccessful() {
		if (getCookieValue("SITEBUILDER_COOKIE") != null) {
			return true;
		}
		return false;
	}
}
