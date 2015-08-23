package com.smartool.service.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.smartool.common.dto.Kid;
import com.smartool.common.dto.Team;
import com.smartool.common.dto.User;
import com.smartool.service.CommonUtils;
import com.smartool.service.SmartoolException;
import com.smartool.service.UserRole;
import com.smartool.service.UserSessionManager;
import com.smartool.service.controller.annotation.ApiScope;
import com.smartool.service.dao.KidDao;
import com.smartool.service.dao.TeamDao;

@RestController
@RequestMapping(value = "/smartool/api/v1")
public class TeamController  extends BaseController {

	@Autowired
	private TeamDao teamDao;
	
	@Autowired
	private KidDao kidDao;
	
	@ApiScope(userScope = UserRole.INTERNAL_USER)
	@RequestMapping(value = "/teams", method = RequestMethod.GET, produces = { MediaType.APPLICATION_JSON_VALUE })
	public List<Team> getTeams() {
		return teamDao.list();
	}
	
	@ApiScope(userScope = UserRole.INTERNAL_USER)
	@RequestMapping(value = "/teams/{teamId}/members", method = RequestMethod.GET, produces = { MediaType.APPLICATION_JSON_VALUE })
	public List<Kid> getTeamMembers(@PathVariable String teamId) {
		return teamDao.getMembers(teamId);
	}
	
	@ApiScope(userScope = UserRole.INTERNAL_USER)
	@RequestMapping(value = "/teams/{teamId}/members", method = RequestMethod.POST, produces = { MediaType.APPLICATION_JSON_VALUE })
	public Kid joinTeam(@PathVariable String teamId, @RequestBody Kid kid) {
		kid.setTeamId(teamId);
		return kidDao.update(kid);
	}
	
	
	@ApiScope(userScope = UserRole.INTERNAL_USER)
	@RequestMapping(value = "/teams", method = RequestMethod.POST, consumes = {
			MediaType.APPLICATION_JSON_VALUE }, produces = { MediaType.APPLICATION_JSON_VALUE })
	public Team createTeam(@RequestBody Team team) {
		team.setId(CommonUtils.getRandomUUID());
		return teamDao.create(team);
	}
	
	@ApiScope(userScope = UserRole.NORMAL_USER)
	@RequestMapping(value = "/teams/{teamId}", method = RequestMethod.GET,  produces = { MediaType.APPLICATION_JSON_VALUE })
	public Team getTeam(@PathVariable String teamId) {
		User sessionUser = UserSessionManager.getSessionUser();
		List<Team> teams = teamDao.memberOf(sessionUser.getId());
		boolean isMemberOf = false;
		for(Team team : teams) {
			if(teamId.equals(team.getId())) {
				isMemberOf = true;
				break;
			}
		}
		if(!isMemberOf) {
			throw new SmartoolException(HttpStatus.UNAUTHORIZED.value(), "you are not the member of this team");
		}
		return teamDao.get(teamId);
	}
	
	@ApiScope(userScope = UserRole.INTERNAL_USER)
	@RequestMapping(value = "/teams/{teamId}", method = RequestMethod.PUT, consumes = {
			MediaType.APPLICATION_JSON_VALUE }, produces = { MediaType.APPLICATION_JSON_VALUE })
	public Team updateTeam(@RequestBody Team team, @PathVariable String teamId) {
		if(!teamId.equals(team.getId())) {
			throw new SmartoolException(HttpStatus.BAD_REQUEST.value(), "teamId should be same as the id in request body");
		}
		team.setId(teamId);
		return teamDao.update(team);
	}
	
	@ApiScope(userScope = UserRole.INTERNAL_USER)
	@RequestMapping(value = "/teams/{teamId}", method = RequestMethod.DELETE)
	public void deleteUser(@PathVariable String teamId) {
		teamDao.delete(teamId);
	}

}
