package com.smartool.service.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.google.common.base.Strings;
import com.smartool.common.dto.Kid;
import com.smartool.common.dto.LoginUser;
import com.smartool.common.dto.Team;
import com.smartool.common.dto.User;
import com.smartool.service.CommonUtils;
import com.smartool.service.ErrorMessages;
import com.smartool.service.SmartoolException;
import com.smartool.service.UserRole;
import com.smartool.service.UserSessionManager;
import com.smartool.service.config.SmartoolServiceConfig;
import com.smartool.service.controller.annotation.ApiScope;
import com.smartool.service.dao.KidDao;
import com.smartool.service.dao.TeamDao;
import com.smartool.service.dao.UserDao;

@RestController
@RequestMapping(value = "/smartool/api/v1")
public class TeamController extends BaseController {

	@Autowired
	private TeamDao teamDao;
	
	@Autowired
	private UserDao userDao;
	
	@Autowired
	SmartoolServiceConfig config;
	
	@Autowired
	private KidDao kidDao;

	@ApiScope(userScope = UserRole.INTERNAL_USER)
	@RequestMapping(value = "/teams", method = RequestMethod.GET, produces = { MediaType.APPLICATION_JSON_VALUE })
	public List<Team> getTeams() {
		return teamDao.list();
	}

	@ApiScope(userScope = UserRole.NORMAL_USER)
	@RequestMapping(value = "/teams/{teamId}/members", method = RequestMethod.GET, produces = {
			MediaType.APPLICATION_JSON_VALUE })
	public List<Kid> getTeamMembers(@PathVariable String teamId) {
		List<String> teamIds = teamDao.getMembers(teamId);
		List<Kid> kids = new ArrayList<>();
		for(String id : teamIds) {
			kids.add(kidDao.get(id));
		}
		return kids;
	}

	@ApiScope(userScope = UserRole.NORMAL_USER)
	@RequestMapping(value = "/teams/{teamId}/members", method = RequestMethod.POST, produces = {
			MediaType.APPLICATION_JSON_VALUE })
	public Team joinTeam(@PathVariable String teamId, @RequestBody Kid kid) {
		User me = UserSessionManager.getSessionUser();
		Team team = teamDao.get(teamId);
		int size = team.getSize();
		List<String> members = teamDao.getMembers(teamId);
		boolean duplicateMobile = false;
		for(String kidId : members) {
			if(kidId.equals(kid.getId())) {
				duplicateMobile = true;
				break;
			}
		}
		kid = kidDao.get(kid.getId());
		User kidUser = userDao.getUserById(kid.getUserId());
		if(kidUser.getId().equals(me.getId())) {
			throw new SmartoolException(HttpStatus.BAD_REQUEST.value(), ErrorMessages.CAN_JOIN_OWNER_SELF);
		}
		
		if(duplicateMobile) {
			throw new SmartoolException(HttpStatus.BAD_REQUEST.value(), ErrorMessages.DUPLICATE_TEAM_MEMBER);
		}
		
		Kid selectKid = kidDao.get(kid.getId());
		if(null == selectKid) {
			throw new SmartoolException(HttpStatus.BAD_REQUEST.value(), ErrorMessages.NOT_ALOW_MANIPULATE_TEAM);
		}
		
		if(kid.getUserId()!=null && !selectKid.getUserId().equals(kid.getUserId())) {
			throw new SmartoolException(HttpStatus.BAD_REQUEST.value(), ErrorMessages.NOT_ALOW_MANIPULATE_TEAM);
		}
		
		int currentSize = members.size();
		
		if(currentSize + 1 > size) {
			throw new SmartoolException(HttpStatus.BAD_REQUEST.value(), ErrorMessages.EXCEED_MAX_TEAM_SIZE);
		}
		teamDao.addMember(kid, teamId);
		kidDao.setTeams(kid.getId(), teamId);
		team = teamDao.get(teamId);
		return team;
	}
	
	@ApiScope(userScope = UserRole.NORMAL_USER)
	@RequestMapping(value = "/teams/{teamId}/join", method = RequestMethod.POST, produces = {
			MediaType.APPLICATION_JSON_VALUE })
		public void mobileJoin(@PathVariable String teamId, @RequestBody User user) {
			User me = UserSessionManager.getSessionUser();
			Team team = teamDao.get(teamId);
			int size = team.getSize();
			if(Strings.isNullOrEmpty(user.getMobileNum())) {
				throw new SmartoolException(HttpStatus.BAD_REQUEST.value(), ErrorMessages.MISS_MOBILE_NUM);
			}
			
			if(me.getMobileNum().equals(user.getMobileNum())) {
				throw new SmartoolException(HttpStatus.BAD_REQUEST.value(), ErrorMessages.CAN_JOIN_OWNER_SELF);
			}
			
			List<String> members = teamDao.getMembers(teamId);
			boolean duplicateMobile = false;
			for(String kidId : members) {
				Kid kid = kidDao.get(kidId);
				if(null != kid) {
					String mobileNum = userDao.getUserById(kid.getUserId()).getMobileNum();
					if(mobileNum.equals(user.getMobileNum())) {
						duplicateMobile = true;
						break;
					}
				}
				
			}
			
			if(duplicateMobile) {
				throw new SmartoolException(HttpStatus.BAD_REQUEST.value(), ErrorMessages.DUPLICATE_TEAM_MEMBER);
			}
	//		
			int currentSize = members.size();
			
			if(currentSize + 1 > size) {
				throw new SmartoolException(HttpStatus.BAD_REQUEST.value(), ErrorMessages.EXCEED_MAX_TEAM_SIZE);
			}
			
			User existedUser = userDao.getUserByMobileNumber(user.getMobileNum());
			
			if(null == existedUser) {
				user.setName(config.getDefaultUserName());
				user.setId(CommonUtils.getRandomUUID());
				LoginUser loginUser = new LoginUser();
				loginUser.setName(config.getDefaultUserName());
				loginUser.setMobileNum(user.getMobileNum());
				loginUser.setId(CommonUtils.getRandomUUID());
				loginUser.setLocation(me.getLocation());
				loginUser.setIdp(1);
				loginUser.setRoleId("0"); 
				loginUser.setPassword(CommonUtils.encryptBySha2(config.getDefaultPassword()));
				user = userDao.createUser(loginUser);
			}
			
			List<Kid> kids = kidDao.listByUserId(user.getId());
			if(kids.size() == 0 ) {
				Kid kid = new Kid();
				kid.setId(CommonUtils.getRandomUUID());
				kid.setUserId(user.getId());
				kid.setName(config.getDefaultKidName());
				kid = kidDao.create(kid);
				kids.add(kid);
			}
			teamDao.addMember(kids.get(0), teamId);
			kidDao.setTeams(kids.get(0).getId(), teamId);	
		}
	
	
	
	@ApiScope(userScope = UserRole.NORMAL_USER)
	@RequestMapping(value = "/teams/{teamId}/members/{kidId}", method = RequestMethod.DELETE, produces = {
			MediaType.APPLICATION_JSON_VALUE })
	public void leaveTeam(@PathVariable String teamId, @PathVariable String kidId) {
		User user = UserSessionManager.getSessionUser();
		Team team = teamDao.get(teamId);
		if(user.getRoleId().equals("2") || user.getId().equals(team.getOwnerId())) {
			teamDao.delMember(kidId, teamId);
			kidDao.leaveTeams(kidId, teamId);
		} else {
			throw new SmartoolException(HttpStatus.UNAUTHORIZED.value(),
					ErrorMessages.NOT_ALOW_MANIPULATE_TEAM);
		}
	}

	@ApiScope(userScope = UserRole.NORMAL_USER)
	@RequestMapping(value = "/teams", method = RequestMethod.POST, consumes = {
			MediaType.APPLICATION_JSON_VALUE }, produces = { MediaType.APPLICATION_JSON_VALUE })
	public Team createTeam(@RequestBody Team team) {
		User me = UserSessionManager.getSessionUser();
		team.setOwnerId(me.getId());
		int size = team.getSize();
		if(size > me.getMaxTeamMemberSize()) {
			throw new SmartoolException(HttpStatus.BAD_REQUEST.value(), ErrorMessages.EXCEED_MAX_TEAM_SIZE);
		}
		team.setMinSize(3);
		team.setId(CommonUtils.getRandomUUID());
		return teamDao.create(team);
	}

	@ApiScope(userScope = UserRole.NORMAL_USER)
	@RequestMapping(value = "/teams/{teamId}", method = RequestMethod.GET, produces = {
			MediaType.APPLICATION_JSON_VALUE })
	public Team getTeam(@PathVariable String teamId) {
		User sessionUser = UserSessionManager.getSessionUser();
		Team team = teamDao.get(teamId);
		if (UserRole.INTERNAL_USER.getValue().compareTo(sessionUser.getRoleId()) > 0) {
			// Is normal user
			List<String> teamIds = teamDao.getMembers(teamId);
			List<Kid> kids = new ArrayList<>();
			for(String id : teamIds) {
				kids.add(kidDao.get(id));
			}
			for (Kid kid : kids) {
				if (kid.getUserId().equals(sessionUser.getId())) {
					return team;
				}
			}
			throw new SmartoolException(HttpStatus.UNAUTHORIZED.value(), "you are not the member of this team");
		} else {
			return team;
		}
	}

	@ApiScope(userScope = UserRole.NORMAL_USER)
	@RequestMapping(value = "/teams/{teamId}", method = RequestMethod.PUT, consumes = {
			MediaType.APPLICATION_JSON_VALUE }, produces = { MediaType.APPLICATION_JSON_VALUE })
	public Team updateTeam(@RequestBody Team team, @PathVariable String teamId) {
		if (!teamId.equals(team.getId())) {
			throw new SmartoolException(HttpStatus.BAD_REQUEST.value(),
					"teamId should be same as the id in request body");
		}
		team.setId(teamId);
		return teamDao.update(team);
	}

	@ApiScope(userScope = UserRole.NORMAL_USER)
	@RequestMapping(value = "/teams/{teamId}", method = RequestMethod.DELETE)
	public void deleteUser(@PathVariable String teamId) {
		User me = UserSessionManager.getSessionUser();
		Team team = teamDao.get(teamId);
		if(!team.getOwnerId().equals(me.getId())) {
			throw new SmartoolException(HttpStatus.UNAUTHORIZED.value(),
					ErrorMessages.NOT_ALOW_MANIPULATE_TEAM);
		}
		teamDao.delete(teamId);
	}

}
