package com.smartool.service.controller;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.google.common.base.Joiner;
import com.google.common.base.Splitter;
import com.smartool.common.dto.Heat;
import com.smartool.service.UserRole;
import com.smartool.service.controller.annotation.ApiScope;

import redis.clients.jedis.Jedis;

@RestController
@RequestMapping(value = "/smartool/api/v1")
public class DataController extends BaseController {

	@Autowired
	private Jedis redis;
	private Joiner key = Joiner.on("$");
	private Splitter keySplitter = Splitter.on("$");
	private Splitter heatSplitter = Splitter.on("%");

	@ApiScope(userScope = UserRole.ADMIN)
	@RequestMapping(value = "/data/heats", method = RequestMethod.GET)
	public List<Heat> getHeats(@RequestParam int cityId, @RequestParam long timestamp) {
		String timeSeg = caltTimeSeg(timestamp);
		List<Heat> retHeats = new ArrayList<>();
		String scanKey = key.join(cityId, timeSeg, "*").toString();
		Set<String> keys = redis.keys(scanKey);
		for (String memberKey : keys) {
			List<String> keyList = keySplitter.splitToList(memberKey);
			Set<String> dats = redis.smembers(memberKey);
			for (String data : dats) {
				List<String> heats = heatSplitter.omitEmptyStrings().splitToList(data);
				Heat heat = new Heat();
				heat.setLongitude(heats.get(0));
				heat.setLatitude(heats.get(1));
				heat.setIndex(Integer.valueOf(heats.get(2)));
				heat.setContourId(Integer.valueOf(keyList.get(2)));
				heat.setPathId(Integer.valueOf(keyList.get(3)));
				retHeats.add(heat);
			}
		}
		return retHeats;
	}

	public String caltTimeSeg(long timestsamp) {
		SimpleDateFormat d = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Long cur = Long.valueOf(timestsamp);
		String temporary = d.format(new Timestamp(cur.longValue()));
		System.out.println(temporary);
		String[] clock = temporary.split(" ")[1].split("\\:");
		Integer timeSeg = Integer.valueOf((int) (((Integer.parseInt(clock[0]) * 60 + Integer.parseInt(clock[1])) * 60
				+ Integer.parseInt(clock[2])) * 1.0D / 1800.0D));
		String formattedSeg = timeSeg.toString();
		return formattedSeg;
	}
}
