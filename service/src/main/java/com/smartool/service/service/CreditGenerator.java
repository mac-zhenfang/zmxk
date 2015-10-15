package com.smartool.service.service;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.collect.Range;
import com.google.common.collect.RangeMap;
import com.google.common.collect.TreeRangeMap;
import com.smartool.common.dto.Attendee;
import com.smartool.common.dto.Event;
import com.smartool.common.dto.EventCreditRule;
import com.smartool.service.dao.AttendeeDao;
import com.smartool.service.dao.CreditRuleDao;
import com.smartool.service.dao.EventDao;

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
		}
		for (Event event : events) {
			if (interval > 0
					&& System.currentTimeMillis() - event.getEventTime().getTime() < interval * 60 * 60 * 1000) {
				continue;
			}
			List<EventCreditRule> eventCreditRules = creditRuleDao.listRankingEventCreditRules(event.getEventTypeId(),
					event.getStage(), null, event.getEventGroupLevel());
			RangeMap<Integer, List<EventCreditRule>> creditRuleMap = toCreditRuleMap(eventCreditRules);
			for (Attendee attendee : event.getAttendees()) {
				List<EventCreditRule> rulesToApply = creditRuleMap.get(attendee.getRank());
				if (rulesToApply != null && !rulesToApply.isEmpty()) {
					for (EventCreditRule ruleToApply : rulesToApply) {
						creditService.applyCreditRule(attendee, ruleToApply);
					}
				}
			}
			attendeeDao.removeUnused(event.getId());
			event.setStatus(3);
			eventDao.updateEvent(event);
		}
	}

	private RangeMap<Integer, List<EventCreditRule>> toCreditRuleMap(List<EventCreditRule> eventCreditRules) {
		RangeMap<Integer, List<EventCreditRule>> creditRuleMap = TreeRangeMap.create();
		for (EventCreditRule eventCreditRule : eventCreditRules) {
			Integer upperRank = eventCreditRule.getUpperRank();
			Integer lowerRank = eventCreditRule.getLowerRank();
			Range<Integer> range = getRange(upperRank, lowerRank);
			Map<Range<Integer>, List<EventCreditRule>> mapOfRanges = creditRuleMap.asMapOfRanges();
			if (!mapOfRanges.containsKey(range)) {
				creditRuleMap.put(range, new LinkedList<EventCreditRule>());
			}
			mapOfRanges.get(range).add(eventCreditRule);
		}
		return creditRuleMap;
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
