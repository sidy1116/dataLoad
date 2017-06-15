package com.oracle.qa.dataload.web.rest.util;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.oracle.qa.dataload.web.rest.security.Base64;


@Service
@Scope("prototype")
public class RESTUtils implements RESTUtilsI {
	private final Logger logger = LoggerFactory.getLogger(CookieUtils.class);

	private Map sUtils = new HashMap();
	private Map<String, String> additionalHeaders = null;
	private static volatile CookieUtils ckUtils = new CookieUtils();;
	private static Object syncObject = new Object();

	public static final String PROP_USERNAME = "Username";

	public static final String PROP_PASSWORD = "Password";

	public static final String PROP_ENCODED_UP = "EncodedUP";

	private static final String COOKIE_USER = "cookie_user";

	private static final String HTTP_POST_CONTENT_BYTES = "http_post_content_bytes";

	private static final String HTTP_POST_CONTENT = "http_post_content";

	private static final String HTTP_POST_CONTENT_LENGTH = "http_post_content_length";

	private static final String HTTP_RESPONSE_HEADERS = "response_headers";

	private static final String HTTP_RESPONSE_CODE = "response_code";

	private static final String HTTP_RESPONSE_CONTENT_BYTES = "response_content_bytes";

	private static final String HTTP_RESPONSE_CONTENT = "response_content";

	private static final String HTTP_RESPONSE_LENGTH = "response_length";

	private static final String HTTP_ERROR_CODE = "error_code";

	private static final String OAUTH_NONCE = "oauth_nonce";
	private static final String OAUTH_TIMESTAMP = "oauth_timestamp";
	private static final String OAUTH_SIGNATURE = "oauth_signature";
	public static final String OAUTH_ENCODE_CHARS = "UTF-8";
	private static final String GET_KEY = "GET";
	private static final String POST_KEY = "POST";
	private static final String PUT_KEY = "PUT";
	private static final String DELETE_KEY = "DELETE";
	private String http_proxy = null;

	

	@Override
	public Map<String, List<String>> getHttpResponseHeaders() {
		// TODO Auto-generated method stub
		return (Map<String, List<String>>) sUtils.get("response_headers");
	}

	@Override
	public void addToHeaders(Map<String, String> headersMap) {
		if (additionalHeaders == null) {
			additionalHeaders = new HashMap();
		}
		additionalHeaders.putAll(headersMap);

	}

	@Override
	public String getUrlParameters(Object urlParameters) throws Exception {
		// TODO Auto-generated method stub

		if (urlParameters == null) {
			return "";
		}
		List<Map<String, Object>> parameterList = null;

		if ((urlParameters instanceof Map)) {
			parameterList = new ArrayList();
			parameterList.add((Map) urlParameters);
		} else if ((urlParameters instanceof List)) {
			parameterList = (List) urlParameters;
		}

		StringBuilder sb = new StringBuilder("?");
		int parameterCount = 0;

		for (Iterator localIterator1 = parameterList.iterator(); localIterator1.hasNext();) {
			Map<String, Object> urlParametersMap = (Map<String, Object>) localIterator1.next();
			for (String key : urlParametersMap.keySet()) {
				String encodedVal = "";
				Object val = urlParametersMap.get(key);
				if (val != null) {
					if (!(val instanceof String)) {
						val = val.toString();
					}
					encodedVal = URLEncoder.encode((String) val, "UTF-8");
				}

				if (parameterCount++ > 0) {
					sb.append("&");
				}
				sb.append(URLEncoder.encode(key, "UTF-8"));
				sb.append("=");
				sb.append(encodedVal);
			}
		}

		return sb.toString();

	}

	@Override
	public String getLocationInHeader() {
		Map<String, List<String>> http_headers = (Map) sUtils.get("response_headers");
		if (http_headers == null) {
			return "";
		}
		for (Map.Entry<String, List<String>> entry : http_headers.entrySet()) {
			if ((entry.getKey() != null) && (((String) entry.getKey()).equalsIgnoreCase("location"))) {
				List<String> locationList = (List) entry.getValue();
				for (String location : locationList) {
					if (!location.isEmpty())
						return location;
				}
			}
		}
		return "";
	}

	@Override
	public String getHttpResponseCode() {
		return (String) sUtils.get("response_code");
	}

	@Override
	public String getHttpResponseContent() {
		return (String) sUtils.get("response_content");
	}

	@Override
	public byte[] getHttpResponseContentBytes() {
		return (byte[]) sUtils.get("response_content_bytes");
	}

	@Override
	public String getHttpResponseContentLength() {
		return (String) sUtils.get("response_length");

	}

	@Override
	public CookieUtils getCookieUtils() {
		return ckUtils;

	}

	@Override
	public String getCookieUser() {
		return "cookie_user";
	}

	@Override
	public String getHttpErrorCode() {
		return (String) sUtils.get("error_code");
	}

	@Override
	public boolean hasHttpErrorCode() {
		return sUtils.get("error_code") != null;
	}

	@Override
	public void setProperty(String key, Object value) {
		sUtils.put(key, value);

	}

	@Override
	public void setHttpExpectedResponseCode(String value) {
		sUtils.put("HttpExpectedResponseCode", value);

	}

	public String getHttpVerbGET() {
		return "GET";
	}

	public String getHttpVerbPOST() {
		return "POST";
	}

	public String getHttpVerbPUT() {
		return "PUT";
	}

	public String getHttpVerbDELETE() {
		return "DELETE";
	}

	@Override
	public void setHttpVerb(String value) {
		sUtils.put("HttpVerb", value);

	}

	public void setHttpVerbGET() {
		sUtils.put("HttpVerb", "GET");
	}

	public void setHttpVerbPOST() {
		sUtils.put("HttpVerb", "POST");
	}

	public void setHttpVerbPUT() {
		sUtils.put("HttpVerb", "PUT");
	}

	public void setHttpVerbDELETE() {
		sUtils.put("HttpVerb", "DELETE");
	}

	@Override
	public void setProxy(String proxyHostPort) {
		http_proxy = proxyHostPort;

	}

	@Override
	public boolean httpGETRequest(String url) throws Exception {
		setHttpVerbGET();
		return httpRequest(url);
	}

	@Override
	public boolean httpDELETERequest(String url) throws Exception {
		setHttpVerbDELETE();
		return httpRequest(url);
	}

	@Override
	public boolean httpRequestWithNewResource(String url) throws Exception {
		return httpRequestPlusLocationRequest(url, "201", null);
	}

	@Override
	public boolean httpRequestWithNewResource(String url, String httpVerb) throws Exception {
		return httpRequestPlusLocationRequest(url, "201", httpVerb);
	}

	public boolean httpRequestWithRedirect(String url) throws Exception {
		return httpRequestPlusLocationRequest(url, "302", null);
	}

	public boolean httpRequestWithRedirect(String url, String httpVerb) throws Exception {
		return httpRequestPlusLocationRequest(url, "302", httpVerb);
	}

	public boolean httpRequest(String url) throws Exception {
		try {
			if (url == null) {
				logger.debug("Error: URL is null in httpRequest()");
				return false;
			}

			url = url.replace(" ", "%20");

			String user = (String) sUtils.get("cookie_user");

			logger.debug(">-- http request -->");
			logger.debug("> " + sUtils.get("HttpVerb") + " " + url);

			HttpURLConnection httpConn = createResource(url);
			addHeaders(ckUtils.getUserCookies(user), httpConn);

			if (("PUT".equals(sUtils.get("HttpVerb"))) || ("POST".equals(sUtils.get("HttpVerb")))) {
				testRequestForPOSTorPUT();
			}
			int response = getResponse(httpConn);

			Map<String, List<String>> http_headers = httpConn.getHeaderFields();
			sUtils.put("response_headers", http_headers);

			String cookie_key = null;

			logger.debug("\n<-- http response <--");

			for (Iterator localIterator = http_headers.entrySet().iterator(); localIterator.hasNext();) {
				Map.Entry<String, List<String>> entry = (Map.Entry) localIterator.next();

				logger.debug("< " + (String) entry.getKey() + " : " + entry.getValue());

				if ((entry.getKey() != null) && (((String) entry.getKey()).equalsIgnoreCase("set-cookie")))
					cookie_key = (String) entry.getKey();
			}

			logger.debug("<");

			if ((user != null) && (cookie_key != null)) {
				Object cookies = (List) http_headers.get(cookie_key);

				if ((cookies != null) && (!((List) cookies).isEmpty())) {
					ckUtils.updateUserCookies(user, (List) cookies);
				}
			}

			boolean status = getStatus(response);

			logger.debug(">-- http request -->");
			logger.debug("> " + sUtils.get("HttpVerb") + " " + url);
			logger.debug("\n<-- http response <--");
			for (Map.Entry<String, List<String>> entry : http_headers.entrySet()) {
				logger.debug("< " + (String) entry.getKey() + " : " + entry.getValue());
			}
			logger.debug("<");

			InputStream is = !hasHttpErrorCode() ? httpConn.getInputStream() : httpConn.getErrorStream();
			String content = null;
			byte[] bytes = getBytes(is);
			if ((bytes != null) && (bytes.length > 0)) {
				content = new String(bytes);
			}

			logger.debug("< http response content: " + content);

			sUtils.put("response_content_bytes", bytes);
			sUtils.put("response_content", content);
			sUtils.put("response_length", content != null ? String.valueOf(content.length()) : "0");

			sUtils.put("HttpResponse", content);

			if (content != null) {
				sUtils.put("HttpResponseBase64", Base64.encode(content));
			} else {
				sUtils.put("HttpResponseBase64", null);
			}

			if (!hasHttpErrorCode()) {
				sUtils.put("HttpResponseCode", getHttpResponseCode());

				if (content != null) {

					sUtils.put("JsonStructure", content);

				} else {
					sUtils.put("HttpResponse", null);
					sUtils.put("JsonStructure", null);
				}
			} else {
				sUtils.put("HttpResponseCode", getHttpErrorCode());
			}

			return status;
		} catch (Exception e) {
			sUtils.remove("response_headers");
			sUtils.remove("response_content_bytes");
			sUtils.remove("response_content");
			sUtils.remove("response_length");

			throw e;
		}
	}

	public void setContentForPOST(String content) {
		setHttpVerbPOST();

		setContent(content);
	}

	public void setContentForPOST(byte[] content) {
		setHttpVerbPOST();

		setContent(content);
	}

	public void setContentForPUT(String content) {
		setHttpVerbPUT();

		setContent(content);
	}

	public void setContentForPUT(byte[] content) {
		setHttpVerbPUT();

		setContent(content);
	}

	public byte[] getContentToPOST() {
		return (byte[]) sUtils.get("http_post_content_bytes");
	}

	public String getContentToPOSTasString() {
		return (String) sUtils.get("http_post_content");
	}

	public byte[] getContentToPUT() {
		return getContentToPOST();
	}

	public String getContentToPUTasString() {
		return (String) sUtils.get("http_post_content");
	}

	@Override
	public boolean loadDataFileForPOST(String paramString1, String paramString2) throws Exception {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean loadDataFileForPUT(String paramString1, String paramString2) throws Exception {
		// TODO Auto-generated method stub
		return false;
	}

	public String percentEncode(String s, String char_set) throws Exception {
		if (s == null) {
			return "";
		}
		return

		URLEncoder.encode(s, char_set).replace("+", "%20").replace("*", "%2A").replace("%7E", "~");
	}

	@Override
	public void updateOAuthParameters(String paramString1, String paramString2, Map<String, String> paramMap,
			String paramString3) throws Exception {
		// TODO Auto-generated method stub

	}

	private boolean httpRequestPlusLocationRequest(String Url, String initialResponseCode, String httpVerbAfterRedirect)
			throws Exception {
		String expectedResponse = (String) sUtils.get("HttpExpectedResponseCode");
		sUtils.put("HttpExpectedResponseCode", initialResponseCode);

		String originalVerb = (String) sUtils.get("HttpVerb");

		try {
			if (!httpRequest(Url)) {
				sUtils.put("HttpExpectedResponseCode", expectedResponse);
				return false;
			}
		} catch (Exception e) {
			throw e;
		} finally {
			sUtils.put("HttpExpectedResponseCode", expectedResponse);
		}

		String redirectUrl = getLocationInHeader();
		if (redirectUrl.trim().isEmpty()) {
			logger.debug("Error: Http location header is empty");
			return false;
		}

		if (httpVerbAfterRedirect != null) {
			setHttpVerb(httpVerbAfterRedirect);
		}
		boolean result = false;
		try {
			result = httpRequest(redirectUrl);
		} catch (Exception e) {
			throw e;
		} finally {
			if (httpVerbAfterRedirect != null) {
				setHttpVerb(originalVerb);
			}
		}
		return result;
	}

	private HttpURLConnection createResource(String Url) {
		HttpURLConnection httpConn = null;
		try {
			URL urlConn = new URL(Url);

			if (http_proxy == null) {
				httpConn = (HttpURLConnection) urlConn.openConnection();
			} else {
				String[] proxyParts = http_proxy.split(":");
				InetSocketAddress proxyInet = new InetSocketAddress(proxyParts[0],
						Integer.valueOf(proxyParts[1]).intValue());
				Proxy proxy = new Proxy(Proxy.Type.HTTP, proxyInet);
				httpConn = (HttpURLConnection) urlConn.openConnection(proxy);
			}

			httpConn.setInstanceFollowRedirects(false);

			if (isSSL(Url)) {
				((HttpsURLConnection) httpConn).setHostnameVerifier(new HostnameVerifier() {
					public boolean verify(String hostname, SSLSession session) {
						return true;

					}

				});

			}

		} catch (Exception e) {

			logger.debug("**** Exception createResource ****", e);
		}

		return httpConn;
	}

	private boolean isSSL(String Url) {
		return Url.toUpperCase().startsWith("HTTPS");
	}

	private void addHeaders(List<Cookie> cookies, HttpURLConnection httpConn) throws Exception {
		Map<String, String> headers = (Map) sUtils.get("HttpHeaders");
		if (headers != null) {
			for (String key : headers.keySet()) {
				String value = (String) headers.get(key);

				if (key.equals("Authorization")) {
					if (value.contains("Basic")) {
						value = setBasicAuthorization(value);
					} else {
						String[] authType = value.split(" ");
						throw new Exception("Unknown Authorization type: " + authType[0]);
					}

				}

				httpConn.setRequestProperty(key, value);

				logger.debug("> " + key + " : " + value);

			}
		}
		if (additionalHeaders != null) {
			for (String key : additionalHeaders.keySet()) {
				String value = additionalHeaders.get(key);

				httpConn.setRequestProperty(key, value);

				logger.debug("> " + key + " : " + value);

			}
			additionalHeaders = null;
		}

		Object cookieI = cookies.iterator();
		if (!((Iterator) cookieI).hasNext()) {
			return;
		}
		StringBuilder cookiesStr = new StringBuilder();
		while (((Iterator) cookieI).hasNext()) {
			Cookie cookie = (Cookie) ((Iterator) cookieI).next();
			cookiesStr.append(cookie.toString());

			if (((Iterator) cookieI).hasNext()) {
				cookiesStr.append("; ");
			}
		}
		httpConn.setRequestProperty("Cookie", cookiesStr.toString());

		logger.debug("> Cookie : " + cookiesStr.toString());

	}

	private String setBasicAuthorization(String authValue) {
		boolean propUpdated = false;

		String prop_user = (String) sUtils.get("Username");
		String prop_pass = (String) sUtils.get("Password");

		String userName = (String) sUtils.get("Username");
		if (userName != null) {
			propUpdated = true;
			sUtils.put("Username", userName.trim());

			String password = (String) sUtils.get("Password");
			if (password != null) {
				sUtils.put("Password", password.trim());
			}
		}

		String auth = (String) sUtils.get("Authorization");
		String[] authParts = auth.split(" ");

		if (!authValue.contains("EncodedUP")) {
			authParts[1] = Base64.encode(authParts[1]);
		}

		if (propUpdated) {
			sUtils.put("Username", prop_user);
			sUtils.put("Password", prop_pass);
		}

		return authParts[0] + " " + authParts[1];
	}

	private void testRequestForPOSTorPUT() throws Exception {
		if (!testRequestForPOST()) {
			testRequestForPUT();
		}
	}

	private boolean testRequestForPOST() throws Exception {
		boolean setContent = false;

		String content = (String) sUtils.get("POSTContent");
		if (content != null) {
			setContentForPOST(content);
			setContent = true;
		}

		if (!setContent) {
			String js_post = (String) sUtils.get("JsonPOSTBody");
			if (js_post != null) {
				setContentForPOST(js_post.toString());
				setContent = true;
			}
		}

		return setContent;
	}

	private boolean testRequestForPUT() throws Exception {
		boolean setContent = false;

		String content = (String) sUtils.get("PUTContent");
		if (content != null) {
			setContentForPUT(content);
			setContent = true;
		}

		if (!setContent) {
			JsonStructure js_put = (JsonStructure) sUtils.get("JsonPUTBody");
			if (js_put != null) {
				setContentForPUT(js_put.toString());
				setContent = true;
			}
		}

		return setContent;
	}

	private int getResponse(HttpURLConnection httpConn) throws Exception {
		String verb = (String) sUtils.get("HttpVerb");
		if (verb == null) {
			throw new Exception("HttpVerb not specified in Inputs[] or content is null for a POST");
		}

		switch (verb) {
		case "GET":
			httpConn.setRequestMethod("GET");
			httpConn.connect();
			return httpConn.getResponseCode();

		case "POST":
			httpConn.setRequestMethod("POST");

			String contentLength = (String) sUtils.get("http_post_content_length");
			httpConn.setRequestProperty("Content-Length", contentLength);
			httpConn.setFixedLengthStreamingMode(Integer.valueOf(contentLength).intValue());
			logger.debug("> Content-Length : " + contentLength);
			httpConn.setUseCaches(false);

			httpConn.setDoOutput(true);

			byte[] content = (byte[]) sUtils.get("http_post_content_bytes");
			logger.debug("post content\n" + sUtils.get("http_post_content"));
			DataOutputStream wr_post = new DataOutputStream(httpConn.getOutputStream());
			wr_post.write(content);
			wr_post.flush();
			wr_post.close();

			return httpConn.getResponseCode();

		case "PUT":
			httpConn.setRequestMethod("PUT");

			JsonStructure js_put = (JsonStructure) sUtils.get("JsonPUTBody");
			if (js_put != null) {
				setContent(js_put.toString());
			}
			String contentLength1 = (String) sUtils.get("http_post_content_length");
			httpConn.setRequestProperty("Content-Length", contentLength1);
			httpConn.setFixedLengthStreamingMode(Integer.valueOf(contentLength1).intValue());
			logger.debug("> Content-Length : " + contentLength1);
			httpConn.setUseCaches(false);

			httpConn.setDoOutput(true);

			byte[] content1 = (byte[]) sUtils.get("http_post_content_bytes");
			if ((content1 != null)) {
				logger.debug("put content\n" + sUtils.get("http_post_content"));
			}
			DataOutputStream wr_put = new DataOutputStream(httpConn.getOutputStream());

			wr_put.write(content1);
			wr_put.flush();
			wr_put.close();

			return httpConn.getResponseCode();

		case "DELETE":
			httpConn.setRequestMethod("DELETE");
			httpConn.connect();
			return httpConn.getResponseCode();
		}

		return -1;
	}

	public byte[] getBytes(InputStream is) throws IOException {
		if (is == null) {
			return null;
		}
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		byte[] buffer = new byte['Ѐ'];
		try {
			int length;
			while ((length = is.read(buffer)) != -1) {
				out.write(buffer, 0, length);
			}
			return out.toByteArray();
		} catch (IOException e) {
			throw e;
		} finally {
			is.close();
		}
	}

	private void setContent(String content) {
		if (content != null) {
			sUtils.put("http_post_content", content);

			byte[] contentBytes = content.getBytes();
			sUtils.put("http_post_content_bytes", contentBytes);
			sUtils.put("http_post_content_length", String.valueOf(contentBytes.length));
		} else {
			sUtils.remove("http_post_content_bytes");
			sUtils.remove("http_post_content");
			sUtils.remove("http_post_content_length");
		}
	}

	private boolean getStatus(int statusCode) {
		if (statusCode == -1) {
			logger.debug("\n\n***** ERROR: Response is null. Bad HTTP_Verb specified in test scenario file? *****\n\n");
			sUtils.put("error_code", "501");
			return false;
		}

		String resultStatus = (String) sUtils.get("HttpExpectedResponseCode");
		if ((resultStatus == null) || (resultStatus.trim().isEmpty())) {
			resultStatus = "200";
		}

		String statusString = String.valueOf(statusCode);
		sUtils.put("response_code", statusString);

		logger.debug("http code: " + statusCode);
		if (statusCode >= 400) {
			sUtils.put("error_code", statusString);
			logger.debug("\n** WARNING: Response Error Code: " + statusString + " **\n");
		} else {
			sUtils.remove("error_code");
		}

		boolean match = statusString.equals(resultStatus);
		if (!match) {
			logger.debug("\n  Actual Status Code: " + statusString + "\nExpected Status Code: " + resultStatus
					+ " didn't match");
		}

		return match;
	}

	private void setContent(byte[] content) {
		if (content != null) {
			sUtils.put("http_post_content_bytes", content);
			sUtils.put("http_post_content", new String(content));
			sUtils.put("http_post_content_length", String.valueOf(content.length));
		} else {
			sUtils.remove("http_post_content_bytes");
			sUtils.remove("http_post_content");
			sUtils.remove("http_post_content_length");
		}
	}
}
