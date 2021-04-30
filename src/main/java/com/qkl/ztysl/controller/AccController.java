package com.qkl.ztysl.controller;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.qkl.util.help.AjaxResponse;
import com.qkl.util.help.pager.PageData;
import com.qkl.ztysl.api.common.CodeConstant;
import com.qkl.ztysl.api.common.Constant;
import com.qkl.ztysl.api.po.acc.AccInfo;
import com.qkl.ztysl.api.po.user.User;
import com.qkl.ztysl.api.po.user.UserDetail;
import com.qkl.ztysl.api.service.acc.api.AccService;
import com.qkl.ztysl.api.service.acc.api.ComAccMyService;
import com.qkl.ztysl.api.service.sms.api.SmsService;
import com.qkl.ztysl.api.service.user.api.UserService;
import com.qkl.ztysl.sms.SmsSend;
import com.qkl.ztysl.web.BaseAction;
/**
 * 账户的控制类
 * <p>Description： 账户的控制类 </p>
 * @project_Name qkl_ymd_web
 * @class_Name AccController.java
 * @author zhangchunming
 * @date 2016年9月9日
 * @version v
 */
@Controller
@RequestMapping("/service/acc")
public class AccController extends BaseAction{
    
    @Autowired
    private UserService userService;
    @Autowired
    private AccService accService;
    @Autowired
    private SmsService smsService;
    @Autowired
    private ComAccMyService cams;
    
    /**
     * @describe:投资公司发放奖励
     * @author: zhangchunming
     * @date: 2016年9月9日上午11:06:59
     * @param request
     * @return: AjaxResponse
     */
    @RequestMapping(value="/rewardymd", method = RequestMethod.POST)
    @ResponseBody
    public AjaxResponse modifyuser(HttpServletRequest request){
        logBefore(logger, "投资机构发放ymd给普通会员");
        AjaxResponse ar = new AjaxResponse();
        Map<String,String> resMap = new HashMap<String, String>();
        try {
             PageData pd = new PageData();
             pd = this.getPageData();
             if(pd.get("params") == null){
                 ar.setSuccess(false);
                 ar.setErrorCode(CodeConstant.PARAM_ERROR);
                 ar.setMessage("请求参数有误");
                 return ar;
             }
             JSONArray jsonArray = JSONArray.parseArray(pd.get("params").toString());
             UserDetail user = (UserDetail)request.getSession().getAttribute(Constant.LOGIN_USER);
             UserDetail userDetail = userService.findbyId(user.getId(), Constant.VERSION_NO);
             AccInfo acc = new AccInfo();
             acc.setUserId(user.getId());
             acc.setSyscode(Constant.CUR_SYS_CODE);
             AccInfo tAcc = accService.findAcc(acc, Constant.VERSION_NO);
             if(tAcc == null){
                 resMap.put("failStr", "您的账户不存在，请联系客服！");
                 ar.setSuccess(false);
                 ar.setData(resMap);
                 ar.setMessage("您的账户不存在，请联系客服！");
                 return ar;
             }
             if(tAcc.getAvbAmnt()==null||tAcc.getAvbAmnt().compareTo(new BigDecimal("0"))==0){
                 resMap.put("failStr", "您的账户余额不足发放失败！");
                 ar.setSuccess(false);
                 ar.setData(resMap);
                 ar.setMessage("您的账户余额不足发放失败！");
                 return ar;
             }
             resMap = accService.rewardTfcc(jsonArray,userDetail,tAcc.getAvbAmnt(),Constant.VERSION_NO);
             StringBuffer successStr = new StringBuffer("发放成功的手机号：");
             StringBuffer phoneStr = new StringBuffer("");
             BigDecimal totalSan = new BigDecimal("0");//总计转出SAN数量
             if(resMap.get("successStr")!=null){
                 JSONArray array = JSONArray.parseArray(resMap.get("successStr"));
                 for(int i=0;i<array.size();i++){
                     JSONObject obj = (JSONObject)array.get(i);
                     successStr.append(obj.getString("phone")+"，额度："+obj.getString("ymdNum")+"；");
                     phoneStr.append(obj.getString("phone")+"，");
                     totalSan = totalSan.add(new BigDecimal(obj.getString("ymdNum")));
                     int num = smsService.getBlackPhone(obj.getString("phone"));
                     if (num==0) {
                         logger.info("发放数字资产短信--------start---------");
                         String content = "尊敬的【"+obj.getString("phone")+"】会员您好，【"+userDetail.getUserName()+"】会员给您转入【"+obj.getString("ymdNum")+"】三界宝数字资产，请登录网站查收！";
                         SmsSend.sendSms(obj.getString("phone"), content);
                         logger.info("发放数字资产短信--------end:content="+content);
                     }else {
						logger.debug("此人已进入短信黑名单");
						//System.out.println("此人已进入短信黑名单");
					}
                   
                 }
                 resMap.remove("successStr");
                 resMap.put("successStr", successStr.toString());
             }
             PageData  accPd  = new PageData();
             accPd.put("user_code", userDetail.getId());
             accPd = cams.getAmnt(accPd);
             if(accPd!=null){
                 resMap.put("avb_amnt", accPd.get("avb_amnt")==null?null:String.format("%.4f",new BigDecimal(accPd.get("avb_amnt").toString())));
                 resMap.put("froze_amnt", accPd.get("froze_amnt")==null?null:String.format("%.4f",new BigDecimal(accPd.get("froze_amnt").toString())));
                 resMap.put("total_amnt", accPd.get("total_amnt")==null?null:String.format("%.4f",new BigDecimal(accPd.get("total_amnt").toString())));
             }
             //给当前登陆用户发送短信
             /*if(totalSan.compareTo(new BigDecimal("0"))>0){//判断发放总额是否大于0
                 phoneStr = new StringBuffer(phoneStr.substring(0, phoneStr.length()-1));
                 if(phoneStr.toString().contains(",")){
                     SmsSend.sendSms(userDetail.getPhone(), "尊敬的【"+userDetail.getPhone()+"】会员您好，您向【"+phoneStr+"】会员转出【"+totalSan+"】SAN数字货币成功，账户余额【"+resMap.get("avb_amnt").toString()+"】，祝您生活愉快！如有疑问请及时联系网站客服。");
                 }else{
                     SmsSend.sendSms(userDetail.getPhone(), "尊敬的【"+userDetail.getPhone()+"】会员您好，您向【"+phoneStr+"】会员共计转出【"+totalSan+"】SAN数字货币成功，账户余额【"+resMap.get("avb_amnt").toString()+"】，祝您生活愉快！如有疑问请及时联系网站客服。");
                 }
                 
             }*/
             ar.setSuccess(true);
             ar.setData(resMap);
             ar.setMessage("发放成功");
             return ar;
        } catch (Exception e) {
            e.printStackTrace();
            ar.setSuccess(false);
            ar.setMessage("网络繁忙，请稍候重试！");
        }finally{
            logAfter(logger);
        }
        return ar;
    }
}
