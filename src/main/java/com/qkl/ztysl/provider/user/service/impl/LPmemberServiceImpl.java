package com.qkl.ztysl.provider.user.service.impl;


import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.qkl.util.help.pager.PageData;
import com.qkl.ztysl.api.entity.Page;
import com.qkl.ztysl.api.service.user.api.LPmemberService;
import com.qkl.ztysl.provider.dao.LPmemberDao;

@Service
public class LPmemberServiceImpl implements LPmemberService {

	@Autowired
	private LPmemberDao lpdao;
	
	@Override
	public long findLPmemberNum(String userCode) {
		// TODO Auto-generated method stub
		return lpdao.findLPmemberNum(userCode);
	}

	@Override
	public List<PageData> findLPmemberInfo(Page page) {
		// TODO Auto-generated method stub
		return lpdao.findLPmemberInfo(page);
	}


	 

}
