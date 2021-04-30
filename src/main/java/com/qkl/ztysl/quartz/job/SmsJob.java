package com.qkl.ztysl.quartz.job;


import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.alibaba.dubbo.common.logger.LoggerFactory;
import com.alibaba.dubbo.common.logger.Logger;
import com.qkl.util.help.DateUtil;
import com.qkl.util.help.pager.PageData;
import com.qkl.ztysl.api.service.sms.api.SmsService;
import com.qkl.ztysl.sms.SmsSend;
import com.qkl.ztysl.web.BaseAction;
/**
 * @author zhangchunming
 * @描述：发送短信定时任务
 */
@Component
public class SmsJob {
    private Logger logger = LoggerFactory.getLogger(SmsJob.class);
	@Autowired
	private SmsService smsService;
	/**
	 * 日期：2016-9-19<br>
	 * 版本：v1.0<br>
	 * 描述：sendSms 每10分钟检查一下短信，如果没有发送，就发送
	 * 创建日期：2016-9-19 下午18:27:20 <br>
	 * 创建人 zhangchunming<br>
	 */
//	@Scheduled(cron="0 0/5 *  * * ?")//配置时间表达式，每5分钟执行一次任务
	public void sendSms() throws Exception{
	    /*logger.debug("---------------------------------------------定时器发送短信------start------"+DateUtil.getCurrDateTime()+"---------------------------------------");
	    List<PageData> smsList = smsService.getSmsNoSend(null);
	    PageData pd = new PageData();
        for(PageData tpd:smsList){
            boolean result = SmsSend.sendSms(tpd.getString("ls_phone"), tpd.getString("ls_content"));
            if(result){
                logger.debug("---------------------------------------------定时器发送短信------success---------"+DateUtil.getCurrDateTime()+"------------------------------------");
                pd.put("logSmsId", tpd.get("log_sms_id").toString());
                pd.put("lsIsSuccess", 1);
                pd.put("lsIsSend", 1);
                
            }else{
                logger.debug("---------------------------------------------定时器发送短信------fail---------------"+DateUtil.getCurrDateTime()+"------------------------------");
                pd.put("logSmsId", tpd.get("log_sms_id").toString());
                pd.put("lsIsSuccess", 0);
                pd.put("lsIsSend", 1);
            }
            smsService.updateSms(pd);
        }
        logger.debug("---------------------------------------------定时器发送短信------end----------"+DateUtil.getCurrDateTime()+"-----------------------------------");*/
	}
	public static void main(String[] args) {
        PageData pd = new PageData();
        pd.put("str", "123456.00");
        System.out.println(pd.getString("str"));
        System.out.println(DateUtil.getCurrDateTime());
    }
}
