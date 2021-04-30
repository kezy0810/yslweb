package com.qkl.ztysl.provider.memberlimit.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.dubbo.common.logger.Logger;
import com.alibaba.dubbo.common.logger.LoggerFactory;
import com.qkl.ztysl.api.po.acc.MemberLimit;
import com.qkl.ztysl.api.service.acc.api.MemberLimitService;
import com.qkl.ztysl.provider.dao.MemberLimitDao;



@Service
public class MemberLimitServiceImpl implements MemberLimitService {

	private Logger loger = LoggerFactory.getLogger(MemberLimitServiceImpl.class);
	
	@Autowired
	private MemberLimitDao memberLimitDao;
	
	
	@Override
	@Transactional(propagation =Propagation.REQUIRED)
	public boolean addMemberLimit(MemberLimit memberLimit, String versionNo) {		
		try{			
			 memberLimitDao.addMemberLimit(memberLimit);
			return true;
		}catch(Exception e){
			loger.debug("addMemberLimit fail,reason is "+e.getMessage());
			return false;
		}
		
	}

	@Override
	@Transactional(propagation =Propagation.REQUIRED)
	public boolean modifyMemberLimit(MemberLimit memberLimit, String versionNo) {
		try{			
			 memberLimitDao.modifyMemberLimit(memberLimit);
			return true;
		}catch(Exception e){
			loger.debug("modifyMemberLimit fail,reason is "+e.getMessage());
			return false;
		}
	}

	@Override
	@Transactional(propagation =Propagation.SUPPORTS)
	public MemberLimit findMemberLimit(String userCode, String versionNo) {
		return memberLimitDao.findMemberLimit(userCode);
	}

}
