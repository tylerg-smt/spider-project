package gaffaney;

import java.util.HashMap;

/****************************************************************************
 * <b>Title</b>: TG_Request.java
 * <b>Project</b>: Spyder
 * <b>Description: </b> Object to store HTTP Requests
 * <b>Copyright:</b> Copyright (c) 2021
 * <b>Company:</b> Silicon Mountain Technologies
 ****************************************************************************/
public class TG_Request {
	private String file;
	private String httpType;
	private String httpMethod;
	private String query;
	private String data;
	private HashMap<String,String> headers;
	
	/**
	 * Constructs a TG_Request object
	 * @param file Server file location being requested
	 * @param httpMethod HTTP method we are using (GET|POST)
	 * @param httpType HTTP version we are using (1.1)
	 */
	public TG_Request(String file, String httpMethod, String httpType) {
		this.file = file;
		this.httpMethod = httpMethod;
		this.httpType = httpType;
		this.data = "";
		this.headers = new HashMap<String,String>();
	}

	/**
	 * Returns server file location
	 * @return String
	 */
	public String getFile() {
		return this.file;
	}
	
	/**
	 * Returns HTTP Version
	 * @return String
	 */
	public String getHTTPType() {
		return this.httpType;
	}
	
	/**
	 * Returns HTTP Method
	 * @return String
	 */
	public String getHTTPMethod() {
		return this.httpMethod;
	}
	
	/**
	 * Sets the query string for the request
	 * @param query String to be added to URL
	 */
	public void setQueryString(String query) {
		this.query = query;
	}
	
	/**
	 * Returns query string
	 * @return String
	 */
	public String getQueryString() {
		return this.query;
	}
	
	/**
	 * Adds a header to the request
	 * @param key Key for header
	 * @param value Value for header
	 */
	public void addHeader(String key, String value){
		this.headers.put(key, value);
	}

	/**
	 * Returns a header for a specific key
	 * @param key Key of possible header
	 * @return String
	 */
	public String getHeaderValueFor(String key){
		return this.headers.get(key);
	}
	
	/**
	 * Returns a HashMap of all headers
	 * @return HashMap<String,String>
	 */
	public HashMap<String,String> getAllHeaders(){
		return this.headers;
	}
	
	/**
	 * Sets data for the request
	 * @param data URL encoded data
	 */
	public void setData(String data){
		this.data = data;
	}
	
	/**
	 * Returns URL Encoded data
	 * @return String
	 */
	public String getData(){
		return this.data;
	}
	
	/**
	 * toString() override
	 */
	public String toString(){
		String output = "";
		
		String[] keys = this.headers.keySet().toArray(new String[this.headers.size()]);
		for(int x = 0; x < keys.length; x++) {
			output += keys[x] + ": " + this.headers.get(keys[x]) + "\n";
		}
		
		output += "\n";
		
		output += this.data;
		
		output += "\n";
		
		return output;
	}
}
