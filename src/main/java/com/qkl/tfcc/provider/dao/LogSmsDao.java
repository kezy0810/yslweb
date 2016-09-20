package com.qkl.tfcc.provider.dao;

import java.util.List;

import com.qkl.tfcc.api.po.sms.LogSms;
import com.qkl.tfcc.provider.dbhelper.DAO;
import com.qkl.util.help.pager.PageData;

public interface LogSmsDao extends DAO<LogSms> {

	/**
	 * @describe:查询未发送的短信
	 * @author: zhangchunming
	 * @date: 2016年9月19日下午7:08:53
	 * @param pd
	 * @return: List<PageData>
	 */
	public List<PageData> getSmsNoSend(PageData pd);
	/**
	 * @describe:更新短信内容
	 * @author: zhangchunming
	 * @date: 2016年9月19日下午7:08:17
	 * @param pd
	 * @return: void
	 */
	public void updateSms(PageData pd);
}
