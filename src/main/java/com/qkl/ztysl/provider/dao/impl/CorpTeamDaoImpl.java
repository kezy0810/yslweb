package com.qkl.ztysl.provider.dao.impl;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.alibaba.druid.support.logging.Log;
import com.alibaba.druid.support.logging.LogFactory;
import com.qkl.util.help.pager.PageData;
import com.qkl.ztysl.api.entity.Page;
import com.qkl.ztysl.api.po.user.CorpTeam;
import com.qkl.ztysl.provider.dao.CorpTeamDao;
import com.qkl.ztysl.provider.dbhelper.DaoSupport;

@Repository
public class CorpTeamDaoImpl extends DaoSupport<CorpTeam> implements CorpTeamDao {

	
	protected static final Log logger = LogFactory.getLog(CorpTeamDaoImpl.class);
	private String namespace = "CorpTeam";
	
	@Override
	public long findLPNum(String userCode) {
		long LPNum=0;
		try {
			LPNum = getSqlSession().selectOne(namespace+"."+"findLPNum", userCode);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.out.println("数据为空");
		}
		return LPNum;
	}

	@Override
	public List<PageData> findlpInfo(Page page) {
		List<PageData> lpInfo=null;
		try {
			lpInfo = getSqlSession().selectList(namespace+"."+"findlpInfolistPage", page);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return lpInfo;
	}

}
