package com.smartool.service.controller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.google.common.base.Charsets;
import com.google.common.hash.Hashing;
import com.smartool.service.util.WcMessageUtil;
import com.smartool.service.wc.Article;
import com.smartool.service.wc.ImageMessage;
import com.smartool.service.wc.NewsMessage;

@RestController
@RequestMapping(value = "/smartool/api/v1")
public class WchatController {

	@RequestMapping(value = "/wcevents", method = RequestMethod.POST, consumes = {
			MediaType.TEXT_XML_VALUE }, produces = { MediaType.TEXT_XML_VALUE })
	public String createWcEvents(@RequestBody String xml, @RequestParam String signature, @RequestParam long timestamp,
			@RequestParam String nonce) throws Exception {
		System.out.println(xml);
		Map<String, String> requestMap = WcMessageUtil.parseXml(xml);
		// 发送方帐号（open_id）
		String fromUserName = requestMap.get("FromUserName");
		// 公众帐号
		String toUserName = requestMap.get("ToUserName");
		// 消息类型
		String msgType = requestMap.get("MsgType");
		/*LinkMessage message = new  LinkMessage();
		message.setToUserName(fromUserName);
		message.setFromUserName(toUserName);
		message.setCreateTime(new Date().getTime());
		message.setMsgType(WcMessageUtil.REQ_MESSAGE_TYPE_LINK);
		message.setUrl("http://my.csdn.net/luo_yifan");
		message.setDescription("Your Credit: 10000, Your Rank: 10");
		message.setTitle("Your credit");*/
		/**/
		NewsMessage message = new NewsMessage();
		message.setToUserName(fromUserName);
		message.setFromUserName(toUserName);
		message.setCreateTime(new Date().getTime());
		message.setMsgType(WcMessageUtil.RESP_MESSAGE_TYPE_NEWS);
		Article ar = new Article();
		ar.setDescription("Your Credit: 10000, Your Rank: 10");
		ar.setTitle("Your credit");
		//ar.setPicUrl("http://mp.weixin.qq.com/wiki/static/assets/ac9be2eafdeb95d50b28fa7cd75bb499.png");
		ar.setUrl("http://my.csdn.net/luo_yifan");
		List<Article> articles = new ArrayList<Article>();
		articles.add(ar);
		message.setArticleCount(1);
		message.setArticles(articles);
		/*ImageMessage message = new ImageMessage();
		message.setToUserName(fromUserName);
		message.setFromUserName(toUserName);
		message.setCreateTime(new Date().getTime());
		message.setMsgType(WcMessageUtil.RESP_MESSAGE_TYPE_IMAGE);
		message.setImage(image);*/
		System.out.println(signature);
		System.out.println(timestamp);
		System.out.println(nonce);
		// System.out.println(echostr);
		String[] array = new String[] { timestamp + "", nonce, "ryzgb5uel25b3tu0zwihdpmdkdyld250" };
		Arrays.sort(array);
		StringBuilder builder = new StringBuilder();
		for (String s : array) {
			builder.append(s);
		}
		System.out.println(builder.toString());
		String temp = builder.toString();
		String afterHashing = Hashing.sha1().hashString(temp, Charsets.UTF_8).toString();
		System.out.println(afterHashing);
		System.out.println(signature);
		return WcMessageUtil.newsMessageToXml(message);
		//return "<xml><ToUserName><![CDATA[gh_21de324bf80e]]></ToUserName><FromUserName><![CDATA[o4gK0tz4gOlsDxRw_gTo_R5SlFQg]]></FromUserName><CreateTime>1437400439</CreateTime><MsgType><![CDATA[text]]></MsgType><Content><![CDATA[CLICK]]></Content></xml>";
	}

	@RequestMapping(value = "/wcevents", method = RequestMethod.GET, consumes = { MediaType.ALL_VALUE })
	public String getWcEvents(@RequestParam String signature, @RequestParam long timestamp, @RequestParam String nonce,
			@RequestParam String echostr) {
		System.out.println(signature);
		System.out.println(timestamp);
		System.out.println(nonce);
		System.out.println(echostr);
		String[] array = new String[] { timestamp + "", nonce, "ryzgb5uel25b3tu0zwihdpmdkdyld250" };
		Arrays.sort(array);
		StringBuilder builder = new StringBuilder();
		for (String s : array) {
			builder.append(s);
		}
		System.out.println(builder.toString());
		String temp = builder.toString();
		String afterHashing = Hashing.sha1().hashString(temp, Charsets.UTF_8).toString();
		System.out.println(afterHashing);
		System.out.println(signature);

		return echostr;
	}

}
