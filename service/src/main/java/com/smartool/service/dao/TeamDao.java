package com.smartool.service.dao;

import java.util.List;

import com.smartool.common.dto.Kid;
import com.smartool.common.dto.Team;

public interface TeamDao {

	public List<Team> list();

	public Team create(Team dto);

	public Team get(String teamId);

	public Team update(Team dto);

	public void delete(String id);

	public List<Team> memberOf(String userId);

	public List<Kid> getMembers(String teamId);
}
