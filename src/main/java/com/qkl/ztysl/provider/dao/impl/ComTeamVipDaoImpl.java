package com.qkl.ztysl.provider.dao.impl;



import org.springframework.stereotype.Repository;

import com.alibaba.druid.support.logging.Log;
import com.alibaba.druid.support.logging.LogFactory;
import com.qkl.ztysl.api.entity.Page;
import com.qkl.ztysl.api.po.user.ComTeamVip;
import com.qkl.ztysl.provider.dao.ComTeamVipDao;
import com.qkl.ztysl.provider.dbhelper.DaoSupport;


@Repository("vipDao")
public class ComTeamVipDaoImpl extends DaoSupport<ComTeamVip> implements ComTeamVipDao {

	protected static final Log logger = LogFactory.getLog(ComTeamVipDaoImpl.class);
	private String namespace = "ComTeamVip";
	@Override
	public ComTeamVip findcount(String userCode) {
		ComTeamVip count=null;
		try {
			count = getSqlSession().selectOne(namespace+"."+"findcount", userCode);
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("没有数据");
		}
		return  count;	
	}
	@Override
	public Object findVipList(Page page) {
		Object list=null;
		try {
			list = getSqlSession().selectList(namespace+"."+"findViplistPage",page);
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("对象为空");
		}
		return list;
	}
	
	
}
