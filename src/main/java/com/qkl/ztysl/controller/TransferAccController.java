package com.qkl.ztysl.controller;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.dubbo.common.logger.Logger;
import com.alibaba.dubbo.common.logger.LoggerFactory;
import com.alibaba.fastjson.JSONObject;
import com.qkl.util.help.APIHttpClient;
import com.qkl.util.help.AjaxResponse;
import com.qkl.util.help.DateUtil;
import com.qkl.util.help.pager.PageData;
import com.qkl.ztysl.api.common.CodeConstant;
import com.qkl.ztysl.api.common.Constant;
import com.qkl.ztysl.api.service.acc.api.AccOutdetailService;
import com.qkl.ztysl.api.service.sms.api.SmsService;
import com.qkl.ztysl.api.service.sys.api.SysGenCodeService;
import com.qkl.ztysl.provider.dao.InterfaceLogDao;
import com.qkl.ztysl.sms.SmsSend;
import com.qkl.ztysl.web.BaseAction;
/**
 * 用户的控制类
 * <p>Description： 用户的控制类 </p>
 * @project_Name qkl_ymd_web
 * @class_Name FinanceController.java
 * @author kezhiyi
 * @date 2016年8月18日
 * @version v
 */
@Controller
@RequestMapping("/transfer")
public class TransferAccController extends BaseAction{
    
    private final Logger logger = LoggerFactory.getLogger(TransferAccController.class);
    
    @Autowired
    private SysGenCodeService sysGenCodeService;
    @Autowired
    private AccOutdetailService accOutdetailService;
    @Autowired
    private InterfaceLogDao interfaceLogDao;
    @Autowired
    private SmsService smsService;
    
    private static int txcount=0;
    
    /**
     * @describe:转账回调接口
     * @author: zhangchunming
     * @date: 2016年10月10日上午10:11:41
     * @param request
     * @param response
     * @return: AjaxResponse
     */
    @RequestMapping(value="/callback", method=RequestMethod.POST)
    @ResponseBody
    public AjaxResponse callback(HttpServletRequest request,HttpServletResponse response){
        logBefore(logger,"TransferAccController.callback()");
        AjaxResponse ar = new AjaxResponse();
        PageData pd = new PageData();
    	pd = this.getPageData();
        try {
       //参数解析
            JSONObject params = new JSONObject();
            params.put("orderId", pd.get("orderId"));
            params.put("status", pd.get("status"));
            params.put("ts", pd.get("ts"));
            params.put("sign", pd.get("sign"));
            logger.info("转账回调-----请求参数：params="+params.toJSONString());
            
            if(pd.get("orderId")==null||pd.get("status")==null||pd.get("value")==null||pd.get("ts")==null||pd.get("sign")==null){
                ar.setErrorCode(CodeConstant.PARAM_ERROR);
                ar.setMessage("传递参数有误");
                ar.setSuccess(false);
                logger.info("---------转账回调----------传递参数有误,含有空值：orderId="+pd.get("orderId")+"---status="+pd.get("status")+"--ts="+pd.get("ts")+"--sign="+pd.get("sign"));
                return ar;
            }
            
            String pri = null;
            List<Map<String, Object>> codeList =  sysGenCodeService.findByGroupCode("DIGITAL_SIGN", Constant.VERSION_NO);
            for(Map<String, Object> codeMap:codeList){
                if("PRI".equals(codeMap.get("codeName"))){
                    pri = codeMap.get("codeValue").toString();
                }
            }
            //签名认证
            boolean signResult = APIHttpClient.validSign(pd.getString("orderId"), pd.getString("status"), pd.getString("value"), pd.getString("ts"), pd.getString("sign"), pri);
            if(signResult){//签名认证成功
                boolean transferResult = accOutdetailService.transferCallBack(pd, Constant.VERSION_NO);
                if(transferResult){
                    ar.setMessage("转账成功");
                    ar.setSuccess(true);
                    logger.info("转账回调------转账成功success---------");
                    //添加日志
                    PageData pd_log = new PageData();
                    pd_log.put("log_titile", "转账回调--更新库成功");
                    pd_log.put("log_content",params.toJSONString());
                    pd_log.put("syscode", Constant.CUR_SYS_CODE);
                    pd_log.put("create_time", DateUtil.getCurrDateTime());
                    pd_log.put("modify_time", DateUtil.getCurrDateTime());
                    pd_log.put("log_type", "2");//接口日志类型：1-转账申请2-转账回调
                    pd_log.put("log_status", "1");//转账日志状态：1-成功 0-失败 2-转账中
                    interfaceLogDao.insertSelective(pd_log);
                    logger.info("转账回调------转账成功success---------pd orderId is "+pd.getString("orderId"));
                    String orderId = params.getString("orderId");//获取订单号
                    PageData turnOutInfo = accOutdetailService.getTurnOutInfo(orderId, Constant.VERSION_NO);
                    if(turnOutInfo!=null&&turnOutInfo.get("phone")!=null&&turnOutInfo.get("recipient")!=null&&turnOutInfo.get("outamnt")!=null){
                        int num = smsService.getBlackPhone(turnOutInfo.getString("phone"));
                        logger.info("短信黑名单查询----------num="+num);
                        if(num==0){//非黑名单用户才能发送短信
                            logger.info("转账回调成功发送短信-----------------start------------------");
                            String content = "尊敬的【"+turnOutInfo.getString("phone")+"】会员您好，您已成功向三界链钱包账户【"+turnOutInfo.getString("recipient")+"】转出【"+((BigDecimal)turnOutInfo.get("outamnt")).toString()+"】三界宝数字资产。";
                            SmsSend.sendSms(turnOutInfo.getString("phone"), content); 
                            logger.info("转账回调成功发送短信-----------------end------------------phone="+turnOutInfo.getString("phone")+",content="+content);
                        }
                    }
                    return ar;
                }else{
                    ar.setMessage("转账失败");
                    ar.setSuccess(false);
                    logger.info("转账回调------转账失败fail---------pd id is "+pd.getString("orderId"));
                    //添加日志
                    PageData pd_log = new PageData();
                    pd_log.put("log_titile", "转账回调--更新库失败");
                    pd_log.put("log_content",params.toJSONString());
                    pd_log.put("syscode", Constant.CUR_SYS_CODE);
                    pd_log.put("create_time", DateUtil.getCurrDateTime());
                    pd_log.put("modify_time", DateUtil.getCurrDateTime());
                    pd_log.put("log_type", "2");//接口日志类型：1-转账申请2-转账回调
                    pd_log.put("log_status", "0");//转账日志状态：1-成功 0-失败 2-转账中
                    interfaceLogDao.insertSelective(pd_log);
                    return ar;
                }
            }else{
                ar.setSuccess(false);
                ar.setErrorCode(CodeConstant.SIGN_ERROR);
                ar.setMessage("签名认证失败");
                logger.info("转账回调------签名认证失败fail---------");
              //添加日志
                PageData pd_log = new PageData();
                pd_log.put("log_titile", "转账回调--签名认证失败");
                pd_log.put("log_content",params.toJSONString());
                pd_log.put("syscode", Constant.CUR_SYS_CODE);
                pd_log.put("create_time", DateUtil.getCurrDateTime());
                pd_log.put("modify_time", DateUtil.getCurrDateTime());
                pd_log.put("log_type", "2");//接口日志类型：1-转账申请2-转账回调
                pd_log.put("log_status", "0");//转账日志状态：1-成功 0-失败 2-转账中
                interfaceLogDao.insertSelective(pd_log);
                return ar; 
            }
        } catch (Exception e) {
            e.printStackTrace();
            ar.setSuccess(false);
            ar.setErrorCode(CodeConstant.SYS_ERROR);
            ar.setMessage("网络繁忙，请稍候重试！");
            logger.info("转账回调------系统异常---------");
        }finally{
        	txcount++;
        	logger.info("------------finally txcount is "+txcount+", pd value is -=="+pd.toString());
            logAfter(logger);
        }   
        return ar;
    }
    public static void main(String[] args) {
        BigDecimal bd = new BigDecimal("123");
        String str = bd.toString();
        System.out.println("str==="+str); 
    }
}
