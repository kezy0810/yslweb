package com.qkl.ztysl.provider.user.service.impl;



import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.qkl.util.help.pager.PageData;
import com.qkl.ztysl.api.entity.Page;
import com.qkl.ztysl.api.po.user.ComTeamVip;
import com.qkl.ztysl.api.service.user.api.ComTeamVipService;
import com.qkl.ztysl.provider.dao.ComTeamVipDao;


@Service("vipService")
public class ComTeamVipServiceImpl implements ComTeamVipService {

	@Autowired
	@Qualifier("vipDao")
	private ComTeamVipDao dao;
	public void setDao(ComTeamVipDao dao) {
		this.dao = dao;
	}


	@Override
	
	public ComTeamVip findcount(String userCode) {
		// TODO Auto-generated method stub
		return dao.findcount(userCode);
	}


	@Override
	public List<PageData> findVipList(Page page) {
		// TODO Auto-generated method stub
		return  (List<PageData>) dao.findVipList(page);
	}


	


	

	


	

}
