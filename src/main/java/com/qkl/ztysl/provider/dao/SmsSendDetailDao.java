package com.qkl.ztysl.provider.dao;

import com.qkl.ztysl.api.po.user.SendsmsDetail;
import com.qkl.ztysl.provider.dbhelper.DAO;

public interface SmsSendDetailDao extends DAO<SendsmsDetail> {
	
	
	/** 查看是否存在该手机号
	 * @return findPhoneIsExist  查看是否存在该手机号
	 * @create author kezhiyi
	 * @create date 2016年8月19日
	 */ 
	public int findPhoneIsExist(String phone);
	/** 根据phone查最近一次短息验证码信息
	 * @return findByUserCode  根据phone查最近一次短息验证码信息
	 * @create author kezhiyi
	 * @create date 2016年8月17日
	 */ 
	public String findByPhone(String phone,String sysCode);
	/** 添加短信发送信息
	 * @return addSmsSend 用户短信发送次数信息
	 * @create author kezhiyi
	 * @create date 2016年8月17日
	 */ 
	public void addSmsSendDetail(SendsmsDetail sendsmsDetail);


}
