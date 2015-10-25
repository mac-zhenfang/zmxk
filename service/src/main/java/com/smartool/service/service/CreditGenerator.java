package com.smartool.service.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.collect.Range;
import com.google.common.collect.RangeMap;
import com.google.common.collect.TreeRangeMap;
import com.smartool.common.dto.Attendee;
import com.smartool.common.dto.CreditRecord;
import com.smartool.common.dto.Event;
import com.smartool.common.dto.EventCreditRule;
import com.smartool.common.dto.Round;
import com.smartool.service.dao.AttendeeDao;
import com.smartool.service.dao.CreditRuleDao;
import com.smartool.service.dao.EventDao;
import com.smartool.service.dao.RoundDao;
import com.smartool.service.dao.UserDao;

public class CreditGenerator {
	private static Logger logger = Logger.getLogger(CreditGenerator.class);

	@Autowired
	private EventDao eventDao;

	@Autowired
	private CreditService creditService;

	@Autowired
	private CreditRuleDao creditRuleDao;

	@Autowired
	private AttendeeDao attendeeDao;
	
	@Autowired
	private UserDao userDao;
	
	@Autowired
	private RoundDao roundDao;

	private long interval;

	public CreditGenerator(long interval) {
		this.interval = interval;
	}

	@Transactional
	public void generateCredit() {
		logger.debug("CreditGenerator generating credit ...");
		List<Event> events = eventDao.listAllEvent(2);
		if (events != null) {
			logger.info("CreditGenerator generating credit for " + events.size() + " events");
		} else {
			return;
		}
		
		List<Round> rounds = roundDao.listGroupByLevel();
		for (Event event : events) {
			if (interval > 0
					&& System.currentTimeMillis() - event.getEventTime().getTime() < interval * 60 * 1000) {
				continue;
			}
			List<EventCreditRule> eventCreditRules = creditRuleDao.listRankingEventCreditRules(event.getEventTypeId(),
					event.getStage(), null);
			Map<Integer, RangeMap<Integer, List<EventCreditRule>>>  creditRuleMapByRound = toCreditRuleMap(eventCreditRules, rounds);
			for (Attendee attendee : event.getAttendees()) {
				if(attendee.getStatus()!=2) {
					continue;
				}
				logger.info("level " + attendee.getRoundLevel());
				RangeMap<Integer, List<EventCreditRule>> creditRuleMap = creditRuleMapByRound.get(attendee.getRoundLevel());
				if(null == creditRuleMap) {
					continue;
				}
				List<EventCreditRule> rulesToApply = creditRuleMap.get(attendee.getRank());
				List<EventCreditRule> rulesToApplyAfterChooseRound = new ArrayList<EventCreditRule>();
				if(rulesToApply!=null) {
					for(EventCreditRule ruleToApply : rulesToApply) {
						if(ruleToApply.getRoundLevel() == attendee.getRoundLevel()) {
							rulesToApplyAfterChooseRound.add(ruleToApply);
						}
					}
				}
				if (rulesToApplyAfterChooseRound != null && !rulesToApplyAfterChooseRound.isEmpty()) {
					for (EventCreditRule rule : rulesToApplyAfterChooseRound) {
						CreditRecord record  = creditService.applyCreditRule(attendee, rule);
						userDao.addCredit(record.getUserId(), record);
					}
				}
			}
			attendeeDao.removeUnused(event.getId());
			event.setStatus(3);
			eventDao.updateEvent(event);
		}
	}

	private Map<Integer, RangeMap<Integer, List<EventCreditRule>>> toCreditRuleMap(List<EventCreditRule> eventCreditRules, List<Round> rounds) {
		 
		Map<Integer, List<EventCreditRule>> toProcessMap = new HashMap<>();
		for (EventCreditRule eventCreditRule : eventCreditRules) {
			if(!toProcessMap.containsKey(eventCreditRule.getRoundLevel())) {
				List<EventCreditRule> list = new ArrayList<>();
				toProcessMap.put(eventCreditRule.getRoundLevel(), list);
			}
			toProcessMap.get(eventCreditRule.getRoundLevel()).add(eventCreditRule);
		}
		
		Set<Entry<Integer, List<EventCreditRule>>> enties = toProcessMap.entrySet();
		Map<Integer, RangeMap<Integer, List<EventCreditRule>>> rangeMapKeyByRound = new HashMap<>();
		for(Entry<Integer, List<EventCreditRule>> entry : enties) {
			int level = entry.getKey();
			RangeMap<Integer, List<EventCreditRule>> creditRuleMap = TreeRangeMap.create();
			rangeMapKeyByRound.put(level, creditRuleMap);
			List<EventCreditRule> eventCreditRulesByRoundLevel = entry.getValue();
			for(EventCreditRule eventCreditRule : eventCreditRulesByRoundLevel) {
				Integer upperRank = eventCreditRule.getUpperRank();
				Integer lowerRank = eventCreditRule.getLowerRank();
				Range<Integer> range = getRange(upperRank, lowerRank);
				Map<Range<Integer>, List<EventCreditRule>> mapOfRanges = creditRuleMap.asMapOfRanges();
				if (!mapOfRanges.containsKey(range)) {
					creditRuleMap.put(range, new LinkedList<EventCreditRule>());
				}
				mapOfRanges.get(range).add(eventCreditRule);
			}
		} 
		
		/*RangeMap<Integer, List<EventCreditRule>> creditRuleMap = TreeRangeMap.create();
		for (EventCreditRule eventCreditRule : eventCreditRules) {
			Integer upperRank = eventCreditRule.getUpperRank();
			Integer lowerRank = eventCreditRule.getLowerRank();
			Range<Integer> range = getRange(upperRank, lowerRank);
			Map<Range<Integer>, List<EventCreditRule>> mapOfRanges = creditRuleMap.asMapOfRanges();
			if (!mapOfRanges.containsKey(range)) {
				creditRuleMap.put(range, new LinkedList<EventCreditRule>());
			}
			mapOfRanges.get(range).add(eventCreditRule);
			int round = eventCreditRule.getRoundLevel();
			if(!rangeMapKeyByRound.containsKey(round)) {
				rangeMapKeyByRound.put(round, creditRuleMap);
				//creditRuleMap = TreeRangeMap.create();
			}
		}*/
		return rangeMapKeyByRound;
	}

	private Range<Integer> getRange(Integer upper, Integer lower) {
		if (upper != null && lower != null) {
			return Range.closed(upper, lower);
		} else if (upper != null) {
			return Range.atLeast(upper);
		} else {
			return Range.atMost(lower);
		}
	}
}
