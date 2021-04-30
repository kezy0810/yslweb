package com.qkl.ztysl.provider.dao.impl;


import org.springframework.stereotype.Repository;

import com.qkl.ztysl.api.po.user.SendsmsDetail;
import com.qkl.ztysl.provider.dao.SmsSendDetailDao;
import com.qkl.ztysl.provider.dbhelper.DaoSupport;

/**SmsSendDetailDaoImpl的实现
 * <p>Description：SmsSendDetailDaoImpl的实现  </p>
 * @project_Name qkl_ymd_web
 * @class_Name SmsSendDetailDaoImpl.java
 * @author kezhiyi
 * @date 2016年8月17日
 * @version v1.0
 */
@Repository
public class SmsSendDetailDaoImpl extends DaoSupport<SendsmsDetail> implements
		SmsSendDetailDao {

	private String namespace = "SendsmsDetail";
	
	
	@Override
	public int findPhoneIsExist(String phone) {
		int eCnt=  getSqlSession().selectOne(namespace+"."+"findIsExist", phone);
		return eCnt;
	}
	@Override
	public String findByPhone(String phone,String sysCode) {
		String vcode ="";	
		vcode=  getSqlSession().selectOne(namespace+"."+"findByPhone", phone);
		return vcode;
	}

	@Override
	public void addSmsSendDetail(SendsmsDetail sendsmsDetail) {
	   getSqlSession().insert(namespace+"."+"add", sendsmsDetail);
		
	}

	

	

}
