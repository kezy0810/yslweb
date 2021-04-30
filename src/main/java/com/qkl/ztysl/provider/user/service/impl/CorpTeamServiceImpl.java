package com.qkl.ztysl.provider.user.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.qkl.util.help.pager.PageData;
import com.qkl.ztysl.api.entity.Page;
import com.qkl.ztysl.api.service.user.api.CorpTeamService;
import com.qkl.ztysl.provider.dao.CorpTeamDao;

@Service
public class CorpTeamServiceImpl implements CorpTeamService {

	@Autowired
	private CorpTeamDao corpTeamDao;
	@Override
	public long findNum(String userCode) {
		// TODO Auto-generated method stub
		return corpTeamDao.findLPNum(userCode);
	}

	@Override
	public List<PageData> findlplist(Page page) {
		// TODO Auto-generated method stub
		return corpTeamDao.findlpInfo(page);
	}

}
