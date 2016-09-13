package com.qkl.tfcc.provider.dao.impl;



import java.util.List;

import org.springframework.stereotype.Repository;

import com.qkl.tfcc.api.entity.Page;
import com.qkl.tfcc.api.po.user.UserDetail;
import com.qkl.tfcc.provider.dao.LPmemberDao;
import com.qkl.tfcc.provider.dbhelper.DaoSupport;
import com.qkl.util.help.pager.PageData;

@Repository
public class LPmemberDaoImpl extends DaoSupport<UserDetail> implements LPmemberDao {

	
	private String namespace = "UserDetail";
	
	@Override
	public long findLPmemberNum(String userCode) {
		long num=0;
		try {
			num = getSqlSession().selectOne(namespace+"."+"findLPmemberNum", userCode);
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("数据为空");
		}
		return num;
	}

	@Override
	public List<PageData> findLPmemberInfo(Page page) {
		List<PageData> list=null;
		try {
			list = getSqlSession().selectList(namespace+"."+"findLPmemberInfolistPage", page);
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("集合为空");
		}
		return list;
	}

}
