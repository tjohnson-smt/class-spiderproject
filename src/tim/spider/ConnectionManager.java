package tim.spider;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * @author tim
 *
 */
public class ConnectionManager {
	private URL baseURL; 
	private HttpURLConnection urlConnection;
	private InputStream inputStream;
	private ByteArrayOutputStream outputStream;

	/**
	 * Creates a new connection.
	 * @param baseURL
	 * @throws Exception 
	 */
	public ConnectionManager(String baseURL) throws Exception {
		try {
			validateURL(baseURL);
			setConnection();
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
	 * Set's a connection for the URL.
	 * 
	 * @throws Exception
	 */
	private void setConnection() throws Exception {
		HttpURLConnection urlConn;
		
		try {
			urlConn = (HttpURLConnection) baseURL.openConnection();
			urlConn.setRequestProperty("User-Agent", "Mozilla/5.0 (X11; Ubuntu; Linux x86_64; rv:28.0) Gecko/20100101 Firefox/28.0");
		} catch (IOException e) {
			String msg = "Can't set a connection for: " + baseURL;
			throw new Exception(msg);
		}
		
		urlConnection = urlConn;
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
			String msg = "Can't write the output stream for: " + baseURL;
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
}
