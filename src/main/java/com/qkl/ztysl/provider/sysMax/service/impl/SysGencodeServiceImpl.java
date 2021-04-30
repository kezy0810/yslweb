package com.qkl.ztysl.provider.sysMax.service.impl;

import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import com.alibaba.dubbo.common.logger.Logger;
import com.alibaba.dubbo.common.logger.LoggerFactory;
import com.qkl.ztysl.api.po.sys.SysGencode;
import com.qkl.ztysl.api.service.sys.api.SysGenCodeService;
import com.qkl.ztysl.provider.dao.SysGencodeDao;

@Service
public class SysGencodeServiceImpl implements SysGenCodeService {
	
	private Logger loger = LoggerFactory.getLogger(SysGencodeServiceImpl.class);
		
	@Autowired
	private SysGencodeDao sysGencodeDao;

	
	@Override
	@Transactional(propagation =Propagation.SUPPORTS)
	public List<SysGencode> findAll() {
		List<SysGencode> tSysGencodeList =sysGencodeDao.findAll();
		return tSysGencodeList;
	}

	@Override
	@Transactional(propagation =Propagation.SUPPORTS)
	public List<Map<String,Object>> findByGroupCode(String groupCode, String versionNo) {
		List<Map<String,Object>> tSysGencodeList =sysGencodeDao.findByGroupCode(groupCode);
		return tSysGencodeList;
	}

}
