package com.qkl.ztysl.provider.sysMax.service.impl;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.dubbo.common.logger.Logger;
import com.alibaba.dubbo.common.logger.LoggerFactory;
import com.qkl.ztysl.api.common.Constant;
import com.qkl.ztysl.api.po.sys.SysMaxnum;
import com.qkl.ztysl.api.service.sys.api.SysMaxnumService;
import com.qkl.ztysl.provider.dao.SysMaxnumDao;



@Service
public class SysMaxnumServiceImpl implements SysMaxnumService {

	
	private Logger loger = LoggerFactory.getLogger(SysMaxnumServiceImpl.class);
	
	
	@Autowired
	private SysMaxnumDao sysMaxnumDao;
	
	
	@Override
	@Transactional(propagation =Propagation.SUPPORTS)
	public Long findMaxNo(String notype, String versionNo) {
		Long tCode = null;
		 SysMaxnum tSysMaxnum =  sysMaxnumDao.findMaxnum(notype);
		 if(tSysMaxnum==null){
			 return null;
		 }
		 tCode =  tSysMaxnum.getCode();
		 tSysMaxnum.setCode(tCode+1);
		 sysMaxnumDao.modifyMaxnum(tSysMaxnum);		 
		 return tCode;
	}

	@Override
	@Transactional(propagation =Propagation.REQUIRED)
	public boolean modifyMaxNo(SysMaxnum sysMaxnum, String versionNo) {
		// TODO Auto-generated method stub
		return false;
	}

}
