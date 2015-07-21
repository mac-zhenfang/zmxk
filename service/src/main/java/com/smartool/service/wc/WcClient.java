package com.smartool.service.wc;

import java.net.URI;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;

import com.fasterxml.jackson.databind.JsonNode;
import com.smartool.service.client.Client;
import com.smartool.service.client.ClientFactory;
import com.smartool.service.client.Request;
import com.smartool.service.wc.menu.Button;

public class WcClient extends Client {

	private AccessToken token = new AccessToken();

	protected WcClient(ClientFactory factory) {
		super(factory);
		token.setAccess_token(
				"YhOC9LxFX2QQSPfOaDgLHIzWkP-GRImS1XRr52iyU7X15neB14GlfBOO4qucDTL3UxCq9J4qTQfN5tjuNAuoCO7kGslndNkRbKOtW-GQXGk");
		token.setExpires_in(7200);
		// token = reteriveAccessToken();
		// set
	}

	public AccessToken reteriveAccessToken() {
		Request<HttpGet> get = get(URI.create("https://api.weixin.qq.com/cgi-bin/token"));
		get.addParam("grant_type", "client_credential");
		get.addParam("appid", "wxbf660860aec515bb");
		get.addParam("secret", "94f253804ff1c174bb1e8b26e2854b96");
		AccessToken token = get.execute(AccessToken.class);
		return token;
	}

	public void createButtons(String url, List<Button> buttons) {
		Map<String, List<Button>> requestMap = new HashMap<>();
		requestMap.put("button", buttons);
		url = addAccessToken(url);
		System.out.println(url);
		Request<HttpPost> post = post(URI.create(url));
		post.jsonEntity(requestMap);
		JsonNode node = post.execute(JsonNode.class);
		System.out.println(node.asText());
	}

	private String addAccessToken(String url) {
		StringBuilder sb = new StringBuilder();
		sb.append(url);
		sb.append("?");
		sb.append("access_token=");
		sb.append(token.getAccess_token());
		return sb.toString();
	}

	public static void main(String... args) {
		// https://api.weixin.qq.com/cgi-bin/menu/create
		WcClientFactory clientFactory = WcClientFactory.builder().baseUrl("").disableSSLChecks(true).build();
		WcClient client = new WcClient(clientFactory);
		List<Button> buttons = new ArrayList<Button>();
		Button button1 = new Button();
		button1.setKey("CLICK_TO_SHOW_RANKS");
		button1.setName("My Rank");
		button1.setType("CLICK");
		//button1.setUrl("https://www.google.com");
		buttons.add(button1);
		client.createButtons("https://api.weixin.qq.com/cgi-bin/menu/create", buttons);
	}

}
