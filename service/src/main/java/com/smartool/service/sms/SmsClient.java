package com.smartool.service.sms;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.message.BasicNameValuePair;
import org.apache.log4j.Logger;

import com.fasterxml.jackson.databind.JsonNode;
import com.google.common.base.Charsets;
import com.google.common.io.BaseEncoding;
import com.smartool.service.client.Client;
import com.smartool.service.client.ClientFactory;
import com.smartool.service.client.Request;

public class SmsClient extends Client {
	
	private static Logger logger = Logger.getLogger(SmsClient.class);

	private static String API_KEY = "edbd5fa1ece44cc3a28651d9b8c837ed";

	private static String BASE_URL;

	protected SmsClient(ClientFactory factory, String baseUrl) {
		super(factory);
		BASE_URL = baseUrl;
		setAuthorization("Basic " + BaseEncoding.base64().encode(("api:" + API_KEY).getBytes(Charsets.UTF_8)));
	}

	public boolean send(final String mobileNum, final String message) {
		String sendUrl = BASE_URL + "/send.json";
		List<NameValuePair> nameValuePairs = new ArrayList<>();
		nameValuePairs.add(new BasicNameValuePair("mobile", mobileNum));
		nameValuePairs.add(new BasicNameValuePair("message", message));
		try {
			Request<HttpPost> post = post(URI.create(sendUrl));
			UrlEncodedFormEntity entity;

			entity = new UrlEncodedFormEntity(nameValuePairs);

			post.entity(entity);
			JsonNode response = post.execute(JsonNode.class);
			System.out.println(response);
			return true;
		} catch (Exception e) {
			logger.error("error to send sms", e);
			return false;
		}
	}
	
	public static void main(String[] args) {
		SmsClientFactory clientFactory = SmsClientFactory.builder().disableSSLChecks(true).build();
		SmsClient client = new SmsClient(clientFactory, "http://sms-api.luosimao.com/v1");
		client.send("13706516916", "验证码：286221【彩虹赛道】");
	}
}
