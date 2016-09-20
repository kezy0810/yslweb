package com.qkl.tfcc.provider.dao.impl;

import java.util.List;
import org.springframework.stereotype.Repository;
import com.alibaba.druid.support.logging.Log;
import com.alibaba.druid.support.logging.LogFactory;
import com.qkl.tfcc.api.po.sms.LogSms;
import com.qkl.tfcc.provider.dao.LogSmsDao;
import com.qkl.tfcc.provider.dbhelper.DaoSupport;
import com.qkl.util.help.pager.PageData;


@Repository
public class LogSmsDaoImpl extends DaoSupport<LogSms> implements LogSmsDao {

protected static final Log logger = LogFactory.getLog(LogSmsDaoImpl.class);
	
	private String namespace = "LogSms";

    @Override
    public List<PageData> getSmsNoSend(PageData pd) {
        return getSqlSession().selectList(namespace+"."+"selectByIsSend", pd);
    }

    @Override
    public void updateSms(PageData pd) {
        getSqlSession().update(namespace+"."+"updateByIdSelective", pd);
    }

   

}
