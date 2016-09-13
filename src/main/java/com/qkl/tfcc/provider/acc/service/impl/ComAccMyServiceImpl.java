package com.qkl.tfcc.provider.acc.service.impl;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.dubbo.common.logger.Logger;
import com.alibaba.dubbo.common.logger.LoggerFactory;
import com.qkl.tfcc.api.entity.Page;
import com.qkl.tfcc.api.po.acc.ComAccMy;
import com.qkl.tfcc.api.service.acc.api.ComAccMyService;
import com.qkl.tfcc.provider.dao.ComAccMyDao;
import com.qkl.util.help.pager.PageData;

@Service
public class ComAccMyServiceImpl implements ComAccMyService {

	private Logger loger = LoggerFactory.getLogger(ComAccMyServiceImpl.class);
	
	@Autowired
	private ComAccMyDao comAccMyDao;

	@Override
	public BigDecimal findTB(String userCode) {
		// TODO Auto-generated method stub
		
		return comAccMyDao.findTB(userCode);
	}

	@Override
	public BigDecimal findJB(String userCode) {
		// TODO Auto-generated method stub
		return comAccMyDao.findJB(userCode);
	}

	

	@Override
	public BigDecimal findReward(String userCode) {
		// TODO Auto-generated method stub
		return comAccMyDao.findReward(userCode);
	}

	@Override
	public BigDecimal findWReward(String userCode) {
		// TODO Auto-generated method stub
		return comAccMyDao.findWReward(userCode);
	}

	@Override
	public BigDecimal findTTReward(String userCode) {
		// TODO Auto-generated method stub
		return comAccMyDao.findTTReward(userCode);
	}

	@Override
	public List<PageData> findAll(Page page) {
		// TODO Auto-generated method stub
		return comAccMyDao.findAllPage(page);
	}

	@Override
	public Map<String, Object> findNum(String userCode) {
		Map<String, Object> map=new HashMap<String, Object>();
		
		BigDecimal findTTReward = comAccMyDao.findTTReward(userCode);
		BigDecimal findTB = comAccMyDao.findTB(userCode);
		BigDecimal findReward = comAccMyDao.findReward(userCode);
		
		map.put("findTTReward", findTTReward);
		map.put("findTB", findTB);
		map.put("findReward", findReward);
		return map;
	}

}
