/*******************************************************************************
 * Copyright 2012-2013 Trento RISE
 * 
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 * 
 *        http://www.apache.org/licenses/LICENSE-2.0
 * 
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 ******************************************************************************/
package eu.trentorise.smartcampus.journeyplanner.util;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Map;

public class HTTPConnector {

	public static String doGet(String address, Map<String, String> req, String accept, String contentType, String token, String encoding) throws Exception {

		StringBuffer response = new StringBuffer();

		String encodedReq = null;
		if (req != null && !req.keySet().isEmpty()) {
			encodedReq = "";
			for (String key : req.keySet()) {
				encodedReq += URLEncoder.encode(key, "UTF-8") + "=" + URLEncoder.encode(req.get(key), "UTF-8") + "&";
			}
			encodedReq = encodedReq.substring(0, encodedReq.length() - 1);
		}

		URL url = new URL(address + ((encodedReq != null) ? ("?" + encodedReq) : ""));

		HttpURLConnection conn = (HttpURLConnection) url.openConnection();
		conn.setRequestMethod("GET");
//		conn.setDoOutput(true);
//		conn.setDoInput(true);

		if (accept != null) {
			conn.setRequestProperty("Accept", accept);
		}
		if (contentType != null) {
			conn.setRequestProperty("Content-Type", contentType);
		}
		conn.setRequestProperty("AUTH_TOKEN", token);

		if (conn.getResponseCode() != 200) {
			throw new RuntimeException("Failed : HTTP error code : " + conn.getResponseCode());
		}

		BufferedReader br;
		if (encoding != null) {
			br = new BufferedReader(new InputStreamReader((conn.getInputStream()), encoding));
		} else {
			br = new BufferedReader(new InputStreamReader((conn.getInputStream())));
		}

		String output = null;
		while ((output = br.readLine()) != null) {
			response.append(output);
		}

		conn.disconnect();

		return response.toString();
	}

	public static void doPost(HttpMethod method, String address, Map<String, String> req, String content, String accept, String contentType, String token) throws Exception {

		StringBuffer response = new StringBuffer();

		String encodedReq = null;
		if (req != null && !req.keySet().isEmpty()) {
			encodedReq = "";
			for (String key : req.keySet()) {
				encodedReq += URLEncoder.encode(key, "UTF-8") + "=" + URLEncoder.encode(req.get(key), "UTF-8") + "&";
			}
			encodedReq = encodedReq.substring(0, encodedReq.length() - 1);
		}

		URL url = new URL(address + ((encodedReq != null) ? ("?" + encodedReq) : ""));

		HttpURLConnection conn = (HttpURLConnection) url.openConnection();
		conn.setRequestMethod(method.toString());
		conn.setDoOutput(true);
		conn.setDoInput(true);

		if (accept != null) {
			conn.setRequestProperty("Accept", accept);
		}
		if (contentType != null) {
			conn.setRequestProperty("Content-Type", contentType);
		}
		conn.setRequestProperty("AUTH_TOKEN", token);

		if (content != null) {
			OutputStream out = conn.getOutputStream();
			Writer writer = new OutputStreamWriter(out, "UTF-8");

			writer.write(content);
			writer.close();
			out.close();
		}

		if (conn.getResponseCode() < 200 || conn.getResponseCode() > 299) {
			throw new RuntimeException("Failed : HTTP error code : " + conn.getResponseCode());
		}

	}
	
	public static String doPostWithReturn(HttpMethod method, String address, Map<String, String> req, String content, String accept, String contentType, String token, String encoding) throws Exception {

		StringBuffer response = new StringBuffer();

		String encodedReq = null;
		if (req != null && !req.keySet().isEmpty()) {
			encodedReq = "";
			for (String key : req.keySet()) {
				encodedReq += URLEncoder.encode(key, "UTF-8") + "=" + URLEncoder.encode(req.get(key), "UTF-8") + "&";
			}
			encodedReq = encodedReq.substring(0, encodedReq.length() - 1);
		}

		URL url = new URL(address + ((encodedReq != null) ? ("?" + encodedReq) : ""));

		HttpURLConnection conn = (HttpURLConnection) url.openConnection();
		conn.setRequestMethod(method.toString());
		conn.setDoOutput(true);
		conn.setDoInput(true);

		if (accept != null) {
			conn.setRequestProperty("Accept", accept);
		}
		if (contentType != null) {
			conn.setRequestProperty("Content-Type", contentType);
		}
		conn.setRequestProperty("AUTH_TOKEN", token);

		if (content != null) {
			OutputStream out = conn.getOutputStream();
			Writer writer = new OutputStreamWriter(out, "UTF-8");

			writer.write(content);
			writer.close();
			out.close();
		}

		if (conn.getResponseCode() < 200 || conn.getResponseCode() > 299) {
			throw new RuntimeException("Failed : HTTP error code : " + conn.getResponseCode());
		}
		
		BufferedReader br;
		if (encoding != null) {
			br = new BufferedReader(new InputStreamReader((conn.getInputStream()), encoding));
		} else {
			br = new BufferedReader(new InputStreamReader((conn.getInputStream())));
		}

		String output = null;
		while ((output = br.readLine()) != null) {
			response.append(output);
		}

		conn.disconnect();

		return response.toString();		

	}	
	

}
