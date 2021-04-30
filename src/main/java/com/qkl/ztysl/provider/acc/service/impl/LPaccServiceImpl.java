package com.qkl.ztysl.provider.acc.service.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.qkl.util.help.pager.PageData;
import com.qkl.ztysl.api.entity.Page;
import com.qkl.ztysl.api.service.acc.api.LPaccService;
import com.qkl.ztysl.provider.dao.LPaccDao;


@Service
public class LPaccServiceImpl implements LPaccService {

	@Autowired
	private LPaccDao lpaccDaoImpl;
	
	@Override
	public long findLPBalance(String userCode) {
		// TODO Auto-generated method stub
		return lpaccDaoImpl.findLPBalance(userCode);
	}

	@Override
	public long findtotalReward(String userCode) {
		// TODO Auto-generated method stub
		return lpaccDaoImpl.findtotalReward(userCode);
	}

	@Override
	public long findrefReward(String userCode) {
		// TODO Auto-generated method stub
		return lpaccDaoImpl.findrefReward(userCode);
	}

	@Override
	public long findmyReward(String userCode) {
		// TODO Auto-generated method stub
		return lpaccDaoImpl.findmyReward(userCode);
	}

	@Override
	public List<PageData> findRewardInfo(Page page) {
		// TODO Auto-generated method stub
		return lpaccDaoImpl.findRewardInfo(page);
	}

	
}
