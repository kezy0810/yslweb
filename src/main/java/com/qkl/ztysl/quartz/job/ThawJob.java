package com.qkl.ztysl.quartz.job;


import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.alibaba.dubbo.common.logger.Logger;
import com.alibaba.dubbo.common.logger.LoggerFactory;
import com.qkl.util.help.DateUtil;
import com.qkl.ztysl.api.common.Constant;
import com.qkl.ztysl.api.service.sys.api.SysGenCodeService;
import com.qkl.ztysl.api.service.user.api.UserService;
/**
 * @author zhangchunming
 * @描述：解冻定时任务
 */
@Component
public class ThawJob {
    private Logger logger = LoggerFactory.getLogger(ThawJob.class);
    @Resource
	private UserService userService;
    @Autowired
    private SysGenCodeService sysGenCodeService;
    private String ratio = "";
	/**
	 * 日期：2016-9-19<br>
	 * 版本：v1.0<br>
	 * 描述：thaw 每月一日解冻
	 * 创建日期：2016-9-19 下午18:27:20 <br>
	 * 创建人 zhangchunming<br>
	 */
//    @Scheduled(cron="0 0 14  0/2 * ?")//配置时间表达式，每天凌晨1分钟执行一次任务
	public void execute() throws Exception{
        /*logger.debug("---------------------------------------------每月1日定时器解冻余额----thaw--start------"+DateUtil.getCurrDateTime()+"---------------------------------------");
	    List<Map<String, Object>> codeList =  sysGenCodeService.findByGroupCode("RATIO", Constant.VERSION_NO);
	    for(Map<String, Object> codeMap:codeList){
	        if("THAW_RATIO".equals(codeMap.get("codeName"))){
	            ratio = codeMap.get("codeValue").toString();
            }
	    }
	    if(!"".equals(ratio)){
	        boolean restult = userService.thaw(ratio, Constant.VERSION_NO);
	        if(restult){
	            logger.debug("---------------------------------------------每月1日定时器解冻余额---thaw---success------"+DateUtil.getCurrDateTime()+"---------------------------------------");
	        }else{
	            logger.debug("---------------------------------------------每月1日定时器解冻余额---thaw---fail------"+DateUtil.getCurrDateTime()+"---------------------------------------");
	        }
	    }
	    logger.debug("---------------------------------------------每月1日定时器解冻余额---thaw---end------"+DateUtil.getCurrDateTime()+"---------------------------------------");*/
	}
}
