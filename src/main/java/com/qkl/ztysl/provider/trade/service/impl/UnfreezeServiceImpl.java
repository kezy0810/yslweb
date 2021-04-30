package com.qkl.ztysl.provider.trade.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.dubbo.common.logger.Logger;
import com.alibaba.dubbo.common.logger.LoggerFactory;
import com.qkl.util.help.DateUtil;
import com.qkl.util.help.pager.PageData;
import com.qkl.ztysl.api.common.Constant;
import com.qkl.ztysl.api.service.trade.api.UnfreezeService;
import com.qkl.ztysl.provider.dao.AccInfoDao;
import com.qkl.ztysl.provider.dao.SysGencodeDao;
import com.qkl.ztysl.provider.dao.UnfreezeDetailDao;


@Service
public class UnfreezeServiceImpl implements UnfreezeService {

	private Logger loger = LoggerFactory.getLogger(UnfreezeServiceImpl.class);
	
	@Autowired
	private UnfreezeDetailDao unfreezeDetailDao;
	@Autowired
	private SysGencodeDao sysGencodeDao;
	@Autowired
	private AccInfoDao accDao;
	
	
	
	@Override
	@Transactional(propagation =Propagation.REQUIRED)
	public boolean addUnfreezeDetail(PageData pd, String versionNo) throws Exception{
		 List<Map<String,Object>> tSysGencodeList =sysGencodeDao.findByGroupCode("UNFREE_DATE");
		 String ptufdate ="";
		 String lpufdate="";
		 String jgufdate="";
		 
            for(Map<String,Object> mapObj:tSysGencodeList){
                if("1".equals(mapObj.get("codeName"))){
                	ptufdate = mapObj.get("codeValue").toString();
                }
                if("3".equals(mapObj.get("codeName"))){
                	lpufdate = mapObj.get("codeValue").toString();
                }
                if("4".equals(mapObj.get("codeName"))){
                	jgufdate = mapObj.get("codeValue").toString();
                }
            }
		
        //普通会员解冻
		if(DateUtil.getNowDay().equals(ptufdate)){
			//addUnfreezeDetail 添加解冻明细需要参数为：
			//1、unfreezeRatio 解冻比例
			//2、syscode 系统编码
			//3、userType 用户类型，普通会员为1
			if(unfreezeDetailDao.addUnfreezeDetail(pd)){
				//updatefroze 更新账户表可用、冻结数量等信息
				//1、syscode 系统编码
				//2、userType 用户类型，普通会员为1
				accDao.updatefroze(pd);
				//modifyUnfreezeDetailStatus 更新解冻明细表状态
				//1、syscode 系统编码
				//2、userType 用户类型，普通会员为1
				unfreezeDetailDao.modifyUnfreezeDetailStatus(pd);					
			}
			
			
		}
		 //LP会员解冻
		if(DateUtil.getNowDay().equals(lpufdate)){
			//addUnfreezeDetail 添加解冻明细需要参数为：
			//1、unfreezeRatio 解冻比例
			//2、syscode 系统编码
			//3、userType 用户类型，LP会员为3
			if(unfreezeDetailDao.addUnfreezeDetail(pd)){
				//updatefroze 更新账户表可用、冻结数量等信息
				//1、syscode 系统编码
				//2、userType 用户类型，LP会员为3
				accDao.updatefroze(pd);
				//modifyUnfreezeDetailStatus 更新解冻明细表状态
				//1、syscode 系统编码
				//2、userType 用户类型，LP会员为3
				unfreezeDetailDao.modifyUnfreezeDetailStatus(pd);	
			}
		}
		 //机构会员解冻
		if(DateUtil.getNowDay().equals(jgufdate)){
			//addUnfreezeDetail 添加解冻明细需要参数为：
			//1、unfreezeRatio 解冻比例
			//2、syscode 系统编码
			//3、userType 用户类型，机构会员为4
			if(unfreezeDetailDao.addUnfreezeDetail(pd)){
				//updatefroze 更新账户表可用、冻结数量等信息
				//1、syscode 系统编码
				//2、userType 用户类型，机构会员为4
				accDao.updatefroze(pd);
				//modifyUnfreezeDetailStatus 更新解冻明细表状态
				//1、syscode 系统编码
				//2、userType 用户类型，机构会员为4
				unfreezeDetailDao.modifyUnfreezeDetailStatus(pd);
			}
		}
		return true;
	}

}
