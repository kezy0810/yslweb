package com.qkl.tfcc.provider.acc.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.dubbo.common.logger.Logger;
import com.alibaba.dubbo.common.logger.LoggerFactory;
import com.qkl.tfcc.api.service.acc.api.AccOutdetailService;
import com.qkl.tfcc.provider.dao.AccOutdetailDao;
import com.qkl.util.help.pager.PageData;


@Service
public class AccOutdetailServiceImpl implements AccOutdetailService {

	private Logger loger = LoggerFactory.getLogger(AccOutdetailServiceImpl.class);
	
	@Autowired
	private AccOutdetailDao accOutdetailDao;
	
	
	@Override
	@Transactional(propagation =Propagation.REQUIRED)
	public boolean addAccOutdetail(PageData pd, String versionNo) {
		try{			
			accOutdetailDao.addAccOutdetail(pd);
			return true;
		}catch(Exception e){
			loger.debug("addAccOutdetail fail,reason is "+e.getMessage());
			return false;
		}
	}

	@Override
	@Transactional(propagation =Propagation.REQUIRED)
	public boolean modifyAccOutdetailStatus(PageData pd, String versionNo) {
		try{			
			accOutdetailDao.modifyAccOutdetailStatus(pd);
			return true;
		}catch(Exception e){
			loger.debug("modifyAccOutdetailStatus fail,reason is "+e.getMessage());
			return false;
		}
	}

}
