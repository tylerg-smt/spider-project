package gaffaney;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/****************************************************************************
 * <b>Title</b>: TG_Response.java
 * <b>Project</b>: Spyder
 * <b>Description: </b> Object to store HTTP Responses
 * <b>Copyright:</b> Copyright (c) 2021
 * <b>Company:</b> Silicon Mountain Technologies
 ****************************************************************************/
public class TG_Response {
	private int statusCode;
	private HashMap<String, List<String>> headers;
	private String body;
	
	/**
	 * Constructs TG_Response
	 */
	public TG_Response(){
		this.statusCode = -1;
		this.headers = new HashMap<String, List<String>>();
		this.body = "";
	} 

	/**
	 * Sets status code
	 * @param statusCode Status code of HTTP Response
	 */
	public void setStatusCode(int statusCode){
		this.statusCode = statusCode;
	}

	/**
	 * Returns status code
	 * @return int
	 */
	public int getStatusCode(){
		return this.statusCode;
	}

	/**
	 * Adds a header to the Response object
	 * @param key Header key
	 * @param value Header value
	 */
	public void addHeader(String key, String value){
		if(this.headers.containsKey(key)) {
			List<String> newList =  this.headers.get(key);
			newList.add(value);
			this.headers.put(key, newList);
		}else {
			List<String> s = new ArrayList<String>();
			s.add(value);
			this.headers.put(key, s);
		}
	}

	/**
	 * Returns a Header value string array
	 * @param key Header key
	 * @return String[]
	 */
	public String[] getHeaderValueFor(String key){
		List<String >val = this.headers.get(key);
		return val.toArray(new String[val.size()]);
	}

	/**
	 * Returns all headers for the response
	 */
	public HashMap<String,List<String>> getAllHeaders(){
		return this.headers;
	}

	/**
	 * Sets body content for response
	 * @param content body content for response
	 */
	public void setBody(String content){
		this.body = content;
	}

	/**
	 * Returns body content
	 * @return String
	 */
	public String getBody(){
		return this.body;
	}
	
	/**
	 * toString override
	 */
	public String toString(){
		String output = "";
		
		output += this.statusCode + "\n";
		
		String[] keys = this.headers.keySet().toArray(new String[this.headers.size()]);
		for(int x = 0; x < keys.length; x++) {
			List<String> values = this.headers.get(keys[x]);
			for(int y = 0; y < values.size(); y++) {
				output += keys[x] + ": " + values.get(y) + "\n";
			}
		}
		
		output += "\n";
		
		String[] lines = this.body.split("\n");
		for(int x = 0; x < lines.length; x++) {
			output += lines[x] + "\n";
		}
		
		return output;
	}
}
