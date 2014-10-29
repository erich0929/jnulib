package com.erich0929.http.jnulib;

import org.apache.http.HttpResponse;
import org.apache.http.protocol.*;
import org.apache.http.client.*;
import org.apache.http.*;
import org.apache.http.cookie.*;
import org.apache.http.impl.client.*;
import org.apache.http.params.*;
import org.apache.http.client.methods.*;
import org.apache.http.client.protocol.ClientContext;
import org.apache.http.message.*;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.util.*;
import java.net.CookieHandler;
import java.net.CookieManager;
import java.net.*;
import java.util.*;
import java.io.*;

public class Jnulib {
	public static void main (String [] args) {
		try {
			CookieHandler.setDefault(new CookieManager());
			DefaultHttpClient client = new DefaultHttpClient ();
			HttpPost post = new HttpPost ("http://sso.jnu.ac.kr/AuthServer/Logon.aspx");
			
			List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(1);
		    nameValuePairs.add(new BasicNameValuePair("URL", "http://lib.jnu.ac.kr/tulip/jsp/theme/chonnam/logon.jsp"));
		    nameValuePairs.add(new BasicNameValuePair("userid", "74114"));
		    nameValuePairs.add(new BasicNameValuePair("passwd", "/^\\s*$/djnu0"));
		   
		    post.setEntity(new UrlEncodedFormEntity(nameValuePairs));
		    printCookies (client);
			HttpResponse response = client.execute(post);
			System.out.println ("status : " + response.getStatusLine().getStatusCode());
			printCookies (client);
			
			
			EntityUtils.consume(response.getEntity());
			Header locationHeader = response.getLastHeader("location");
			String location = locationHeader.getValue();
			System.out.println ("Location : " + location);
			
			HttpContext context = new BasicHttpContext ();
			context.setAttribute(ClientContext.COOKIE_STORE, client.getCookieStore());
			HttpGet get = new HttpGet (location);
			//client.getConnectionManager().;
			response = client.execute(get, context);
			System.out.println ("status : " + response.getStatusLine().getStatusCode());
			printCookies (client);
			EntityUtils.consume(response.getEntity());
			
			
			HttpGet loanlist = new HttpGet ("http://lib.jnu.ac.kr/myloan/list");
			response = client.execute (loanlist);
			System.out.println ("status : " + response.getStatusLine().getStatusCode());
			printCookies (client);
			
			HttpEntity entity = response.getEntity();
			BufferedReader in = new BufferedReader (new InputStreamReader (entity.getContent()));
			String inputLine;
			while ((inputLine = in.readLine()) != null) {
				System.out.println (inputLine);
			}
			
		} catch (Exception e) {
			e.printStackTrace ();
		}
	}
	
	public static void printCookies (DefaultHttpClient client) {
		List <Cookie> cookies = client.getCookieStore().getCookies();
			System.out.println ("--- Print cookies ---");
		for (Cookie cookie : cookies ) {
			
			System.out.println (cookie.getName() + " : " + cookie.getValue());
			
		}
		System.out.println ("-------------------");
		
	}
}
