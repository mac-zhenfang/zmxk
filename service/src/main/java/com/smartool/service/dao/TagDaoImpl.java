package com.smartool.service.dao;

import java.util.List;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;

import com.smartool.common.dto.Tag;

public class TagDaoImpl implements TagDao {
	
	@Autowired
	private SqlSession sqlSession;
	@Override
	public Tag getTag(String tagId) {
		return getInternal(tagId);
	}

	@Override
	public List<Tag> getTags(String type) {
		
		return sqlSession.selectList("TAG.listByType", type);
	}

	@Override
	public Tag create(Tag tag) {
		sqlSession.selectOne("TAG.create", tag);
		return getInternal(tag.getId());
	}

	@Override
	public Tag update(Tag tag) {
		sqlSession.selectOne("TAG.update", tag);
		return getInternal(tag.getId());
	}
	
	private Tag getInternal(String tagId) {
		Tag tag = sqlSession.selectOne("TAG.getById", tagId);
		return tag;
	}

	@Override
	public List<Tag> getTags() {
		return sqlSession.selectList("TAG.list");
	}

}
