package com.smartool.service.dao;

import java.util.List;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;

import com.smartool.common.dto.Kid;
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
		int i = 0;
		Site retSite = null;
		int retryCount = 3;
		Exception throwE = null;
		while (i++ < 3)
			try {
				int siteNum = sqlSession.selectOne("getSitesCount");
				site.setSiteNum(siteNum);
				sqlSession.insert("SITE.create", site);
				retSite = getInternal(site.getId());

				break;
			} catch (Exception e) {
				throwE = e;
			}
		if (i == retryCount && throwE != null) {
			throw new RuntimeException(throwE);
		}

		return retSite;

	}

	private Site getInternal(String id) {
		return sqlSession.selectOne("SITE.get", id);
	}

}
