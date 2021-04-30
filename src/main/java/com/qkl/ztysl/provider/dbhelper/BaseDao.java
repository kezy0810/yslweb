package com.qkl.ztysl.provider.dbhelper;

import org.mybatis.spring.SqlSessionTemplate;
import org.mybatis.spring.support.SqlSessionDaoSupport;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * 注入持久化的基类
 * 
 * @author liuyang
 * @date 2014-10-14 14:07:52
 *
 */
public class BaseDao extends SqlSessionDaoSupport {

	@Autowired
	public void setSqlSessionTemplate(SqlSessionTemplate sqlSessionTemplate) {
		super.setSqlSessionTemplate(sqlSessionTemplate);
	}
}
