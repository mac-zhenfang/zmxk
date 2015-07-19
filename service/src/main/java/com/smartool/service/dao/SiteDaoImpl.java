package com.smartool.service.dao;

import java.util.List;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;

import com.smartool.common.dto.Site;

public class SiteDaoImpl implements SiteDao {

	@Autowired
	private SqlSession sqlSession;
	
	@Override
	public List<Site> list() {
		return sqlSession.selectList("SITE.list");
	}

	@Override
	public Site get(String id) {
		return getInternal(id);
	}

	@Override
	public Site update(Site site) {
		sqlSession.insert("SITE.update", site);
		return getInternal(site.getId());
	}

	@Override
	public void delete(String id) {
		sqlSession.selectOne("SITE.remove", id);
	}

	@Override
	public Site create(Site site) {
		sqlSession.insert("SITE.create", site);
		return  getInternal(site.getId());
	}
	
	private Site getInternal(String id) {
		return sqlSession.selectOne("SITE.get", id);
	}

}
