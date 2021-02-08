package gaffaney;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;

/****************************************************************************
 * <b>Title</b>: TG_HTTPSession.java
 * <b>Project</b>: Spyder
 * <b>Description: </b> Controls the HTTP traffic to and from a server
 * <b>Copyright:</b> Copyright (c) 2021
 * <b>Company:</b> Silicon Mountain Technologies
 ****************************************************************************/
public class TG_HTTPSession {
	private String host;
	private int port;
	private HashMap<String,String> cookieList;
	private boolean logTraffic = false;

	/**
	 * Constructs a TG_HTTPSession object
	 * @param host Host of HTTP Server
	 * @param port Port of HTTP Server
	 */
	public TG_HTTPSession(String host, int port) {
		this.host = host;
		this.port = port;
		this.cookieList = new HashMap<String,String>();
	}
	
	/**
	 * Gets a fresh web socket on the host
	 * @return SSLSocket
	 */
	private SSLSocket getSocket() {
		SSLSocketFactory factory = (SSLSocketFactory) SSLSocketFactory.getDefault();
        SSLSocket sslsocket;
		try {
			sslsocket = (SSLSocket) factory.createSocket(this.host, this.port);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
        try {
			sslsocket.startHandshake();
		} catch (IOException e) {
			return null;
		}
        
        return sslsocket;
	}
	
	/**
	 * This performs a request and returns a response
	 * @param request What is being sent to the server
	 * @return TG_Response detailing what the server response was
	 * @throws IOException
	 */
	public TG_Response performRequest(TG_Request request) throws IOException {
		SSLSocket sslSocket = getSocket();
		
		PrintWriter socketWriter = new PrintWriter(sslSocket.getOutputStream());
        BufferedReader socketReader = new BufferedReader(new InputStreamReader(sslSocket.getInputStream()));

		writeRequest(request, socketWriter);
		TG_Response response = readReponse(socketReader);
        
        socketReader.close();
        socketWriter.close();
        return response;
	}
	
	/**
	 * This writes the TG_Request data to the socket
	 * @param request The request being written to the socket
	 * @param socketWriter PrintWriter used to write data to the socket
	 */
	private void writeRequest(TG_Request request, PrintWriter socketWriter) {
		if(logTraffic)
			System.out.println("--- Request ---");
		
		String line = request.getHTTPMethod() + " " + request.getFile() +  " HTTP/" + request.getHTTPType();
		println(socketWriter, line);
        println(socketWriter, "Host: " + this.host);
        
        HashMap<String,String> headers = request.getAllHeaders();
        String[] keys = headers.keySet().toArray(new String[headers.size()]);
        for(int x = 0; x < keys.length; x++) {
        	String curLine = keys[x] + ": " + headers.get(keys[x]);
        	println(socketWriter, curLine);
        }
        
        if(cookieList.size() > 0) {
        	String cookieString = "";
        	HashMap<String,String> cookies = this.cookieList;
            String[] cookieKeys = cookies.keySet().toArray(new String[cookies.size()]);
            for(int x = 0; x < cookieKeys.length; x++) {
            	cookieString += cookieKeys[x] + "=" + cookies.get(cookieKeys[x]) + "; ";
            }
            
            String cookieLine = "Cookie: " + cookieString.trim();
            
            println(socketWriter,cookieLine);
        }

        println(socketWriter,"");
        
        if(!request.getData().equals("")) {
        	println(socketWriter, request.getData());
        	println(socketWriter, "");
        }
        
        socketWriter.flush();
	}
	
	/**
	 * Used for logging purposes and keeping the file tidy.
	 * @param s PrintWriter to write a string too
	 * @param line The String that is written to the PrintWriter
	 */
	private void println(PrintWriter s, String line) {
		if(logTraffic) {
			System.out.println(line);
		}
		s.println(line);
	}
	
	/**
	 * After a request has been made, this reads the response from the Server
	 * @param socketReader The object that reads the server response.
	 * @return TG_Response object with the response details
	 * @throws IOException
	 */
	private TG_Response readReponse(BufferedReader socketReader) throws IOException {
		if(logTraffic)
			System.out.println("--- Response ---");
		
		TG_Response response = new TG_Response();
        boolean isHeaderData = true;
        String body = "";
        do{
        	String responseString = socketReader.readLine();
        	if(logTraffic)
        		System.out.println(responseString);
        	if(responseString == null);
        		String s = "";
        	if(isHeaderData) {
        		String[] headerSplit = responseString.split(": ");
        		if(responseString.startsWith("HTTP/")) {
        			String[] split = responseString.split(" ");
        			int statusCode = Integer.parseInt(split[1]);
        			System.out.println(statusCode);
        			response.setStatusCode(statusCode);
        		}else if(headerSplit.length > 1) {
        			response.addHeader(headerSplit[0], headerSplit[1]);
        			
        			if(headerSplit[0].equals("Set-Cookie")) {
        				String[] cookie = headerSplit[1].split("=");
        				String header = cookie[0];
        				String value = cookie[1].split(";")[0];
        				cookieList.put(header, value);
        			}
        		}
        	}else { body += responseString; }
        	
        	if(responseString.equals("")) { isHeaderData = false; }
        	
        } while(socketReader.ready());
        response.setBody(body);
        return response;
	}
}
