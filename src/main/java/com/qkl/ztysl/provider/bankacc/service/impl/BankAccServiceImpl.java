package com.qkl.ztysl.provider.bankacc.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.dubbo.common.logger.Logger;
import com.alibaba.dubbo.common.logger.LoggerFactory;
import com.qkl.ztysl.api.po.acc.BankAccInfo;
import com.qkl.ztysl.api.service.acc.api.BankAccService;
import com.qkl.ztysl.provider.dao.BankAccDao;


@Service
public class BankAccServiceImpl implements BankAccService {

	
	private Logger loger = LoggerFactory.getLogger(BankAccServiceImpl.class);
	
	@Autowired
	private BankAccDao bankAccDao;
	
	
	@Override
	public BankAccInfo findBankAccInfo(String orgName, String versionNo) {
		try{
			return bankAccDao.findBankAccInfo(orgName);
		}catch(Exception e){
			loger.error("findBankAccInfo fail ,reason is "+e.getMessage());
			return null;
		}
		
		
	}


	@Override
	public BankAccInfo findBankInfo(String versionNo) {
		 
		try {
			return  bankAccDao.findBankInfo();
		} catch (Exception e) {
			loger.error("findBankInfo fail ,reason is "+e.getMessage());			
			return null;
		}
	}

}
