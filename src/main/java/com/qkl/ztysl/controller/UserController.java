package com.qkl.ztysl.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import com.alibaba.dubbo.common.logger.Logger;
import com.alibaba.dubbo.common.logger.LoggerFactory;
import com.qkl.util.help.AjaxResponse;
import com.qkl.util.help.DateUtil;
import com.qkl.util.help.FileUtil;
import com.qkl.util.help.IdcardUtils;
import com.qkl.util.help.ImgUtil;
import com.qkl.util.help.MD5Util;
import com.qkl.util.help.StringUtil;
import com.qkl.util.help.UUId;
import com.qkl.util.help.Validator;
import com.qkl.util.help.pager.PageData;
import com.qkl.util.help.sql.SqlValid;
import com.qkl.ztysl.api.common.CodeConstant;
import com.qkl.ztysl.api.common.Constant;
import com.qkl.ztysl.api.entity.Page;
import com.qkl.ztysl.api.po.acc.AccDetail;
import com.qkl.ztysl.api.po.sys.SysGencode;
import com.qkl.ztysl.api.po.sys.SysOauth;
import com.qkl.ztysl.api.po.user.Sendsms;
import com.qkl.ztysl.api.po.user.SendsmsDetail;
import com.qkl.ztysl.api.po.user.User;
import com.qkl.ztysl.api.po.user.UserDetail;
import com.qkl.ztysl.api.po.user.UserFriendship;
import com.qkl.ztysl.api.service.acc.api.AccService;
import com.qkl.ztysl.api.service.sms.api.SmsService;
import com.qkl.ztysl.api.service.sys.api.SysGenCodeService;
import com.qkl.ztysl.api.service.sys.api.SysMaxnumService;
import com.qkl.ztysl.api.service.testUser.api.TestUserService;
import com.qkl.ztysl.api.service.user.api.UserService;
import com.qkl.ztysl.sms.SmsSend;
import com.qkl.ztysl.utilEx.JedisManager;
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
@RequestMapping("/service/user")
public class UserController extends BaseAction{
    
    private final Logger logger = LoggerFactory.getLogger(UserController.class);
    
    private final long smsInterval = 3600;
    
    private final int smsLimit = 10;
    
    private final String notype ="REFCODE";
    
    private  List<SysGencode> tSysGencodeAll = new  ArrayList<SysGencode>();
 
    @Autowired
    private UserService userService;
    @Autowired
    private SmsService smsService;
    @Autowired
    private SysMaxnumService sysMaxnumService;
    @Autowired
    private SysGenCodeService sysGenCodeService;
    @Autowired
    private AccService accService;
    @Autowired
    private TestUserService testUserService;  
    @Autowired
	private JedisManager jedisManager;
    
    /**
     * 用户登陆
     * <p> 用户登陆  </p>
     * @Title: login 
     * @return  json格式的
     * @create author kezhiyi
     * @create date 2016年8月18日
     */
    @RequestMapping(value="/login", method=RequestMethod.POST)
    @ResponseBody
    public AjaxResponse login(HttpServletRequest request,HttpServletResponse response){
        AjaxResponse ar = new AjaxResponse();
        Map<String,Object> data = new HashMap<String, Object>();
        try {
            String userName  =request.getParameter("username");
            String passWord  =URLDecoder.decode(request.getParameter("password"), "utf-8");
            String imeiid = request.getParameter("imeiid");
            
            userName=userName==null?"":userName.trim();
            passWord=passWord==null?"":passWord.trim();
            imeiid=imeiid==null?"":imeiid.trim();

            if(userName.equals("")) {
            	ar.setSuccess(false);
                ar.setMessage("用户名不能为空！");
                return ar;
            }
            if(passWord.equals("")) {
            	ar.setSuccess(false);
                ar.setMessage("密码不能为空！");
                return ar;
            }
            if(imeiid.equals("")) {
            	ar.setSuccess(false);
                ar.setMessage("设备id不能为空！");
                return ar;
            }
            //sql注入检查
            if(!SqlValid.isSqlValid(userName)) {
            	 ar.setSuccess(false);
                 ar.setMessage(userName+"参数格式错误！");
                 return ar;
            }
            if(!SqlValid.isSqlValid(passWord)) {
           	 ar.setSuccess(false);
                ar.setMessage(passWord+"参数格式错误！");
                return ar;
            }
            if(!SqlValid.isSqlValid(imeiid)) {
              	 ar.setSuccess(false);
                   ar.setMessage(imeiid+"参数格式错误！");
                   return ar;
            }
            
            Map<String, Object> map = userService.login(userName, passWord, Constant.CUR_SYS_CODE,Constant.VERSION_NO);
//            Map<String, Object> map = new HashMap<String , Object>();
//            map.put("status", 1);
            if ((Integer) map.get("status") == Constant.SUCCESS) {
                UserDetail userdetail = (UserDetail) map.get(Constant.LOGIN_USER);
 /*           	User user = new User() ;
            	user.setUserCode("1111");
            	user.setUserType("1");
            	user.setPhone("13333333333");*/
//                request.getSession().setAttribute(Constant.LOGIN_USER, userdetail);
              
             // 生成令牌
        		String accessToken =  UUId.getUUId();
        		String uuid=UUId.getUUId();
        		SysOauth oauthInfo = new SysOauth.OauthBuilder().withId(uuid).withAccessToken(accessToken)
        				.withUserId(userdetail.getId()).build();
        		//将用户信息存入redis
        		jedisManager.set(accessToken,oauthInfo,0);
        		
                logger.info(userName + "登录成功");
                ar.setSuccess(true);
                data.put("userType", userdetail.getUserType());
//                data.put("userId", userdetail.getId());
                data.put("userName", userdetail.getUserName());  
                data.put("realStat", userdetail.getRealStat()==null?"":userdetail.getRealStat());   
                data.put("realName", userdetail.getRealName()==null?"":userdetail.getRealName());   
                data.put("alipayAccno", userdetail.getAlipayAccno()==null?"":userdetail.getAlipayAccno());   
                data.put("refCode", userdetail.getRefCode()==null?"":userdetail.getRefCode());   
                data.put("accessToken", accessToken); 
                ar.setData(data);
            } else {
                ar.setSuccess(false);
                ar.setMessage((String)map.get("msg"));
            }
            
            
        } catch (Exception e) {
            e.printStackTrace();
            ar.setSuccess(false);
            ar.setMessage("网络繁忙，请稍候重试！");
        }   
        ar.setData(data);
        return ar;
        
        /*AjaxResponse ar = new AjaxResponse();
        pd = this.getPageData();
        page.setPd(pd);
        List<PageData> userList = testUserService.queryTestUserList(page);
        Map<String,Object> map = new HashMap<String, Object>();
        map.put("userList", userList);  
        map.put("pd", pd);  
        map.put("page", page);
        ar.setSuccess(true);
        ar.setMessage("查询成功！");
        ar.setData(map);
        return ar;*/
    }
    
    /**
     * 用户修改
     * <p> 用户修改  </p>
     * @Title: modifyuser 
     * @return  json格式的
     * @create author kezhiyi
     * @create date 2016年8月18日
     */
    @RequestMapping(value="/modifyuser", method = RequestMethod.POST)
    @ResponseBody
    public AjaxResponse modifyuser(UserDetail mUserDetail,HttpServletRequest request){
        logBefore(logger, "绑定支付宝信息");     
        AjaxResponse ar = new AjaxResponse();
        Map<String,Object> data = new HashMap<String, Object>();

        try {
         
            String realName = request.getParameter("realName");
            String imeiid = request.getParameter("imeiid");
//            String idno = request.getParameter("idno");
//            String wxnum = request.getParameter("wxnum");
            String alipayAccno = request.getParameter("alipayAccno");
            String accessToken  =request.getParameter("accessToken");          
         
            realName=realName==null?"":realName.trim();
            alipayAccno=alipayAccno==null?"":alipayAccno.trim();
            imeiid=imeiid==null?"":imeiid.trim();
            if(realName.equals("")) {
            	ar.setSuccess(false);
                ar.setMessage("姓名不能为空！");
                return ar;
            }
            if(alipayAccno.equals("")) {
            	ar.setSuccess(false);
                ar.setMessage("支付宝账号不能为空！");
                return ar;
            }
            if(imeiid.equals("")) {
            	ar.setSuccess(false);
                ar.setMessage("设备id不能为空！");
                return ar;
            }
            
            if(!SqlValid.isSqlValid(realName)) {
              	 ar.setSuccess(false);
                   ar.setMessage(realName+"参数格式错误！");
                   return ar;
            }
            if(!SqlValid.isSqlValid(alipayAccno)) {
             	 ar.setSuccess(false);
                  ar.setMessage(alipayAccno+"参数格式错误！");
                  return ar;
           }
            if(!SqlValid.isSqlValid(imeiid)) {
             	 ar.setSuccess(false);
                  ar.setMessage(imeiid+"参数格式错误！");
                  return ar;
           }
            
            SysOauth mSysOauth =(SysOauth) jedisManager.get(accessToken);      
            Integer userId=0;
            if(mSysOauth==null){
            	ar.setSuccess(false);
                ar.setMessage("找不到登陆信息！");
                return ar;
            }else{
            	userId =mSysOauth.getUserId();
            }
            
            UserDetail tUserDetail = new UserDetail();
                tUserDetail.setId(userId);;
            if(realName!=null&&!realName.equals("")){
                tUserDetail.setRealName(realName);
            }
            if(alipayAccno!=null&&!alipayAccno.equals("")){
                tUserDetail.setAlipayAccno(alipayAccno);
            }
            tUserDetail.setOperator("sys");
            tUserDetail.setRealStat("1");     //1：实名      
//            tUserDetail.setUserCode(userCode);
//            tUserDetail.setWxnum(wxnum);
//            tUserDetail.setBankaccno(bankaccno);
//            tUserDetail.setMailAddrss(mailAddrss);
//            tUserDetail.setZipCode(zipCode);
//            tUserDetail.setImgAddrss(imgAddrss);
//            tUserDetail.setModifyTime(DateUtil.getCurrentDate());
//            tUserDetail.setOperator(userDetail.getPhone());
     
            data.put("realName", realName);   
            data.put("alipayAccno", alipayAccno);   
            ar.setData(data);
            if(userService.modifyUserDetail(tUserDetail, Constant.VERSION_NO)){         
                ar.setSuccess(true);
                ar.setMessage("绑定支付宝成功！");
                return ar;
            }   
             ar.setSuccess(false);
             ar.setMessage("绑定支付宝失败！");
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
    
    
    
    
    /**
     * 用户注册
     * <p> 用户注册  </p>
     * @Title: register 
     * @return  json格式的
     * @create author kezhiyi
     * @create date 2016年8月18日
     */
    @RequestMapping(value="/register", method=RequestMethod.POST)
    @ResponseBody
    public AjaxResponse register(HttpServletRequest request,HttpServletResponse response){
        AjaxResponse ar = new AjaxResponse();
        try {
            String userName  =request.getParameter("username");
            String passWord  =request.getParameter("password");
            String cfPassWord  =request.getParameter("resPassword");
//            String vcode  =request.getParameter("yzm");                        //先不需要验证码
            String refCode  =request.getParameter("refcode");
            String imeiid = request.getParameter("imeiid");
            String accessToken = request.getParameter("accessToken");
            
            String userType ="1";                                                //默认普通用户
//          String branchName =request.getParameter("branchName")==null?"":URLDecoder.decode(request.getParameter("branchName"), "UTF-8"); 
//            String realName = request.getParameter("realName")==null?"":URLDecoder.decode(request.getParameter("realName"), "UTF-8");  
//            String idno =request.getParameter("idno");
//            String cropName  =request.getParameter("cropName")==null?"":URLDecoder.decode(request.getParameter("cropName"), "UTF-8");
//            String cropPerson  =request.getParameter("cropPerson")==null?"":URLDecoder.decode(request.getParameter("cropPerson"), "UTF-8");
//            
            userName=userName==null?"":userName.trim();
            passWord=passWord==null?"":passWord.trim();
            cfPassWord=cfPassWord==null?"":cfPassWord.trim();
            refCode=refCode==null?"":refCode.trim();
//            vcode=vcode==null?"":vcode.trim();
//            realName=realName==null?"":realName.trim();
//            idno=idno==null?"":idno.trim();
//            cropName=cropName==null?"":cropName.trim();
//            cropPerson=cropPerson==null?"":cropPerson.trim();
            
            //sql注入检查
            if(!SqlValid.isSqlValid(userName)) {
            	 ar.setSuccess(false);
                 ar.setMessage(userName+"参数格式错误！");
                 return ar;
            }
            if(!SqlValid.isSqlValid(passWord)) {
           	 	ar.setSuccess(false);
                ar.setMessage(passWord+"参数格式错误！");
                return ar;
           }
            if(!SqlValid.isSqlValid(cfPassWord)) {
           	 	ar.setSuccess(false);
                ar.setMessage(cfPassWord+"参数格式错误！");
                return ar;
           }
            if(!SqlValid.isSqlValid(refCode)) {
            	ar.setSuccess(false);
                ar.setMessage(refCode+"参数格式错误！");
                return ar;
           }
            
//            if(!Validator.isMobile(userName)){
//                ar.setSuccess(false);
//                ar.setMessage("手机号格式不正确！");
//                return ar;
//            }
            
            //判断用户是否已存在
            if(userService.findIsExist(userName, Constant.VERSION_NO)){
                ar.setSuccess(false);
                ar.setMessage("用户已存在！");
                return ar;
            }          
            
            if(StringUtil.isEmpty(passWord)||StringUtil.isEmpty(cfPassWord)){
                ar.setSuccess(false);
                ar.setMessage("密码不能为空!");
                return ar;
            }                   
            
            if(!passWord.equals(cfPassWord)){
                ar.setSuccess(false);
                ar.setMessage("两次密码输入不一致！");
                return ar;
            }
        
//            if(idno!=null&&!idno.trim().equals("")){
//                if(!IdcardUtils.validateCard(idno)){
//                    ar.setSuccess(false);
//                    ar.setMessage("身份证格式不正确！");
//                    return ar;
//                };
//            }
            try{
                tSysGencodeAll =sysGenCodeService.findAll();
            }catch(Exception e){
                logger.info("sysGenCodeService.findAll fail ! reason is "+e.getMessage());
            }
            
            //1小时之内的短信验证码有效
//            String tVcode =smsService.findSendsmsDetail(userName,Constant.CUR_SYS_CODE); 
//            if(tVcode==null||!vcode.equals(tVcode.trim())){
//                ar.setSuccess(false);
//                ar.setMessage("验证码输入不正确！");
//                return ar;
//            }
            
            Long tMaxno =sysMaxnumService.findMaxNo(notype, Constant.VERSION_NO);
            if(tMaxno==null){
                logger.info("tSysMaxnum findMaxNo  is null!");
                ar.setSuccess(false);
                ar.setMessage("系统繁忙,请稍后重试！");
                return ar;
            }
           // 生成用户编号
            String selfRefCode = userType +StringUtil.LCh(tMaxno.toString(), "0", 7);
            
            
            UserDetail refUserUserDetail= userService.findbyRefcode(refCode, Constant.VERSION_NO);
            if(refUserUserDetail == null){
                ar.setSuccess(false);
                ar.setMessage("推介人不存在！");
                return ar;
            }
//            UserFriendship fMaxFriendship =  userService.findMaxFriendship(refUser.getUserCode(), Constant.VERSION_NO);//推荐人上级关系最大级数    
            
//            if("1".equals(refUser.getUserType())&&fMaxFriendship == null){//如果用户为普通用户且没有上级用户
//                ar.setSuccess(false);
//                ar.setMessage("推介人数据异常，请联系客服！");
//                return ar;
//            }
        
            UserDetail tUserDetail = new UserDetail();
            tUserDetail.setUserName(userName);
            tUserDetail.setUserType(userType);
            tUserDetail.setTransPassword(MD5Util.getMd5Code(passWord));
            tUserDetail.setRegTime(DateUtil.getCurrentDate());
            tUserDetail.setRefCode(selfRefCode);
            tUserDetail.setParentRefcode(refCode);
            tUserDetail.setParentUsername(refUserUserDetail.getUserName());
            tUserDetail.setStatus("1");           
            tUserDetail.setCreateTime(DateUtil.getCurrentDate());
            tUserDetail.setModifyTime(DateUtil.getCurrentDate());
            tUserDetail.setOperator(userName);
            
            
            if(!userService.addUser(tUserDetail,Constant.VERSION_NO)){            
                ar.setSuccess(false);
                ar.setMessage("注册失败！");
                return ar;
            }
            
            //添加用户关系
//          createUserFriendship(tUserDetail);
            
            //计算注册奖励
//          calRegitAccDetail(tUserDetail);

            ar.setSuccess(true);
            ar.setMessage("注册成功！");
            return ar;
            
        } catch (Exception e) {
            e.printStackTrace();
            ar.setSuccess(false);
            ar.setMessage("网络繁忙，请稍候重试！");
        }
        return ar;
    }

    
    /**
     * 计算奖励关系
     * 1-普通会员
       2-网点会员
       3-LP会员
       4-投资公司
       5-众筹会员
     * */
    /*private void   calRegitAccDetail(UserDetail mUserDetail){
        String refPhone =mUserDetail.getRefPhone(); 
        String UserCode = mUserDetail.getUserCode();
        String userType = mUserDetail.getUserType();
        //普通奖金定义参数
        SysGencode tSysGencodeBNTJDM1 = new   SysGencode();
        SysGencode tSysGencodeBNTJDM2 = new   SysGencode();
        SysGencode tSysGencodeBNTJDM3 = new   SysGencode();
        SysGencode tSysGencodeBNTJDMWD4 = new   SysGencode();
        SysGencode tSysGencodeBNDMZC5 = new   SysGencode();
        SysGencode tSysGencodeBNDMWDKD6 = new   SysGencode();
        
        //LP奖励参数
        SysGencode tSysGencodeLPRDSL1 = new   SysGencode();
        SysGencode tSysGencodeLPRDSL2 = new   SysGencode();
        SysGencode tSysGencodeLPRDSL3 = new   SysGencode();
        SysGencode tSysGencodeLPRDML1 = new   SysGencode();
        SysGencode tSysGencodeLPRDML2 = new   SysGencode();
        SysGencode tSysGencodeLPRDML3 = new   SysGencode();
        SysGencode tSysGencodeLPRDML4 = new   SysGencode();
        
        try{
            if(tSysGencodeAll!=null&&tSysGencodeAll.size()>0){
                for(SysGencode tSysGencode:tSysGencodeAll){
                    if(tSysGencode.getGroupCode().equals("BOUND_DEF")){//获取奖金定义
                        if(tSysGencode.getCodeValue().equals("TJDM1")){
                            tSysGencodeBNTJDM1 = tSysGencode;
                        }else if(tSysGencode.getCodeValue().equals("TJDM2")){
                            tSysGencodeBNTJDM2= tSysGencode;
                        }else if(tSysGencode.getCodeValue().equals("TJDM3")){
                            tSysGencodeBNTJDM3= tSysGencode;
                        }else if(tSysGencode.getCodeValue().equals("TJDMWD")){
                            tSysGencodeBNTJDMWD4= tSysGencode;
                        }else if(tSysGencode.getCodeValue().equals("DMZC")){
                            tSysGencodeBNDMZC5= tSysGencode;
                        }else if(tSysGencode.getCodeValue().equals("DMWDKD")){
                            tSysGencodeBNDMWDKD6= tSysGencode;
                        }
                        
                    }else if(tSysGencode.getGroupCode().equals("LPREWARD_DEF")){
                        if(tSysGencode.getCodeValue().equals("SL1")){
                            tSysGencodeLPRDSL1 = tSysGencode;
                        }else if(tSysGencode.getCodeValue().equals("SL2")){
                            tSysGencodeLPRDSL2= tSysGencode;
                        }else if(tSysGencode.getCodeValue().equals("SL2")){
                            tSysGencodeLPRDSL2= tSysGencode;
                        }else if(tSysGencode.getCodeValue().equals("SL3")){
                            tSysGencodeLPRDSL3= tSysGencode;
                        }else if(tSysGencode.getCodeValue().equals("ML1")){
                            tSysGencodeLPRDML1= tSysGencode;
                        }else if(tSysGencode.getCodeValue().equals("ML2")){
                            tSysGencodeLPRDML2= tSysGencode;
                        }else if(tSysGencode.getCodeValue().equals("ML3")){
                            tSysGencodeLPRDML3= tSysGencode;
                        }else if(tSysGencode.getCodeValue().equals("ML4")){
                            tSysGencodeLPRDML4= tSysGencode;
                        }
                    }
                }
            }
        }catch(Exception e){
            logger.info("sysGenCodeService.findAll fail ! reason is "+e.getMessage());
        }
                
        if(userType.equals("1")){//普通用户
            //普通会员注册奖励计算
            if(mUserDetail.getRealStat().equals("1")){
                if(!calPtMemberRegitBouns(mUserDetail)){
                    logger.error("calPtMemberRegitBouns fail ! ");
                };              
            }
           //普通会员推荐奖励计算
            calPtMemberRecmBouns(mUserDetail);
            
        }else if(userType.equals("2")){//网点用户，审核通过后再计算奖励
            
            
            
        }else if(userType.equals("3")){
            
        }else if(userType.equals("4")){
            
        }
        
    }*/
    
    
    /**
     * 计算网点用户注册奖励关系
     * 
     * */
    /*private boolean   calWDMemberRegitBouns(UserDetail mUserDetail){              
        SysGencode tSysGencodeBNDMZC5 = new SysGencode();
        if(tSysGencodeAll!=null&&tSysGencodeAll.size()>0){
            for(SysGencode tSysGencode:tSysGencodeAll){
                if(tSysGencode.getGroupCode().equals("BOUND_DEF")){//获取奖金定义
                    if(tSysGencode.getCodeValue().equals("DMZC")){
                        tSysGencodeBNDMZC5= tSysGencode;
                        break;
                    }                   
                }
            }
        }       
        //根据tb_acc_def的定义，设置子账号
        AccDetail tAccDetail = new AccDetail();
        tAccDetail.setUserCode(mUserDetail.getUserCode());
        tAccDetail.setSubAccno("010101");
        tAccDetail.setBounsSource1("10");
        tAccDetail.setBounsSource2("1001");
        tAccDetail.setRelaUsercode(mUserDetail.getRefPhone());
        tAccDetail.setRelaUserlevel("");
        try{
            String rsAmnt="0";
            if(tSysGencodeBNDMZC5.getCodeValue()==null||
                    tSysGencodeBNDMZC5.getCodeValue().equals("")||
                    tSysGencodeBNDMZC5.getCodeValue().equals("null"))
            {
                logger.info("calPtMemberRegitBouns fail ,tSysGencodeBNDMZC5 is null !");
            }else{
                rsAmnt = tSysGencodeBNDMZC5.getCodeValue();
                
            }
            BigDecimal bdAmnt=new BigDecimal(rsAmnt);  
            bdAmnt.setScale(2, BigDecimal.ROUND_HALF_UP);//四舍五入
            tAccDetail.setAmnt(bdAmnt); 
        }catch(Exception e){
            logger.error("calPtMemberRegitBouns fail ,translate string to decimal fail!"+e.getMessage());
        }
        tAccDetail.setCaldate(DateUtil.getCurrentDate());
        tAccDetail.setStatus("1");
        tAccDetail.setCreateTime(DateUtil.getCurrentDate());
        tAccDetail.setModifyTime(DateUtil.getCurrentDate());
        
        return accService.addAccDetail(tAccDetail,Constant.VERSION_NO);
        
    }*/
    
    
    
    /**
     * 计算普通会员注册奖励关系
     * 
     * */
    /*private void  calPtMemberRecmBouns(UserDetail mUserDetail){   
        SysGencode tSysGencodeBNTJDM1 = new   SysGencode();
        SysGencode tSysGencodeBNTJDM2 = new   SysGencode();
        SysGencode tSysGencodeBNTJDM3 = new   SysGencode();
        if(tSysGencodeAll!=null&&tSysGencodeAll.size()>0){
            for(SysGencode tSysGencode:tSysGencodeAll){
                if(tSysGencode.getGroupCode().equals("BOUND_DEF")){//获取奖金定义
                    if(tSysGencode.getCodeValue().equals("TJDM1")){
                        tSysGencodeBNTJDM1= tSysGencode;
                    }else if(tSysGencode.getCodeValue().equals("TJDM2")){
                        tSysGencodeBNTJDM2= tSysGencode;
                    }else if(tSysGencode.getCodeValue().equals("TJDM3")){
                        tSysGencodeBNTJDM3= tSysGencode;
                    }                   
                }
            }
        }
        
        String refPhone =mUserDetail.getRefPhone(); 
        String UserCode = mUserDetail.getUserCode();
        String userType = mUserDetail.getUserType();
        
        User tRefUser =  userService.findbyPhone(refPhone, Constant.VERSION_NO);
                
         boolean tUpflag = userService.findIsExistUpFriendship(tRefUser.getUserCode(), Constant.VERSION_NO);
         UserFriendship fUserFriendship= new UserFriendship();//推荐人上级关系
         boolean tSvSelShipFlag =true;           
         String tReflvl ="";
         String sefLev="";
         if(!tUpflag){//如果推荐人没有上级会员，则自己为A级会员
             tReflvl="0";
             sefLev="1";
         }
         if(tUpflag){
             fUserFriendship =  userService.findUpFriendship(tRefUser.getUserCode(), Constant.VERSION_NO);
             if(fUserFriendship.getRelaLevel().equals("A")){
                 sefLev="2";
             }else if(fUserFriendship.getRelaLevel().equals("B")){
                 sefLev="3";
             }else if(fUserFriendship.getRelaLevel().equals("C")){
                 sefLev="4";
             }else{
                 sefLev="4";                    
             }
         }
         
         //推荐奖励 A级别会员
         AccDetail tAccDetail = new AccDetail();
         tAccDetail.setUserCode(tRefUser.getUserCode());        
         tAccDetail.setSubAccno("010101");
         tAccDetail.setBounsSource1("11");//普通用户推荐会员PDM      
         tAccDetail.setBounsSource2("1101");//普通用户推荐A级会员
         tAccDetail.setRelaUsercode(UserCode);
         tAccDetail.setRelaUserlevel("A");
          try{
                String rsAmnt="0";
                if(tSysGencodeBNTJDM1.getCodeValue()==null||
                        tSysGencodeBNTJDM1.getCodeValue().equals("")||
                        tSysGencodeBNTJDM1.getCodeValue().equals("null"))
                {
                    logger.info("calPtMemberRecmBouns fail ,tSysGencodeBNTJDM1 is null !");
                }else{
                    rsAmnt = tSysGencodeBNTJDM1.getCodeValue();
                    
                }
                BigDecimal bdAmnt=new BigDecimal(rsAmnt);  
                bdAmnt.setScale(2, BigDecimal.ROUND_HALF_UP);//四舍五入
                tAccDetail.setAmnt(bdAmnt); 
            }catch(Exception e){
                logger.error("calPtMemberRecmBouns fail ,translate string to decimal fail!"+e.getMessage());
            }
         tAccDetail.setCaldate(DateUtil.getCurrentDate());
         tAccDetail.setStatus("1");
         tAccDetail.setCreateTime(DateUtil.getCurrentDate());
         tAccDetail.setModifyTime(DateUtil.getCurrentDate());
         tAccDetail.setOperator("sys");
         if(! accService.addAccDetail(tAccDetail,Constant.VERSION_NO)){
             logger.info("addAccDetail fail,tAccDetail is "+tAccDetail.toString());
         }; 
         
         
                                         
         if(sefLev.equals("2")){//总共有2层             
             AccDetail tAccDetail2 = new AccDetail();
             tAccDetail2.setUserCode(fUserFriendship.getUserCode());        
             tAccDetail2.setSubAccno("010101");
             tAccDetail2.setBounsSource1("11");//普通用户推荐会员参考PDM       
             tAccDetail2.setBounsSource2("1102");//普通用户推荐B级会员
             tAccDetail2.setRelaUsercode(UserCode);
             tAccDetail2.setRelaUserlevel("B");
              try{
                    String rsAmnt="0";
                    if(tSysGencodeBNTJDM2.getCodeValue()==null||
                            tSysGencodeBNTJDM2.getCodeValue().equals("")||
                            tSysGencodeBNTJDM2.getCodeValue().equals("null"))
                    {
                        logger.info("calPtMemberRecmBouns fail ,tSysGencodeBNTJDM2 is null !");
                    }else{
                        rsAmnt = tSysGencodeBNTJDM2.getCodeValue();
                        
                    }
                    BigDecimal bdAmnt=new BigDecimal(rsAmnt);  
                    bdAmnt.setScale(2, BigDecimal.ROUND_HALF_UP);//四舍五入
                    tAccDetail2.setAmnt(bdAmnt);    
                }catch(Exception e){
                    logger.error("B calPtMemberRecmBouns fail ,translate string to decimal fail!"+e.getMessage());
                }
             tAccDetail2.setCaldate(DateUtil.getCurrentDate());
             tAccDetail2.setStatus("1");
             tAccDetail2.setCreateTime(DateUtil.getCurrentDate());
             tAccDetail2.setModifyTime(DateUtil.getCurrentDate());
             tAccDetail2.setOperator("sys");
             if(! accService.addAccDetail(tAccDetail2,Constant.VERSION_NO)){
                 logger.info("addAccDetail fail,tAccDetail2 is "+tAccDetail2.toString());
             };                  
         }
         if(sefLev.equals("3")){//总共有3层
             UserFriendship  fUserFriendship3 =  userService.findUpFriendship(fUserFriendship.getUserCode(), Constant.VERSION_NO); 
             AccDetail tAccDetail3 = new AccDetail();
             tAccDetail3.setUserCode(fUserFriendship3.getUserCode());       
             tAccDetail3.setSubAccno("010101");
             tAccDetail3.setBounsSource1("11");//普通用户推荐会员参考PDM       
             tAccDetail3.setBounsSource2("1103");//普通用户推荐B级会员
             tAccDetail3.setRelaUsercode(UserCode);
             tAccDetail3.setRelaUserlevel("C");
              try{
                    String rsAmnt="0";
                    if(tSysGencodeBNTJDM3.getCodeValue()==null||
                            tSysGencodeBNTJDM3.getCodeValue().equals("")||
                            tSysGencodeBNTJDM3.getCodeValue().equals("null"))
                    {
                        logger.info("calPtMemberRecmBouns fail ,tSysGencodeBNTJDM3 is null !");
                    }else{
                        rsAmnt = tSysGencodeBNTJDM3.getCodeValue();
                        
                    }
                    BigDecimal bdAmnt=new BigDecimal(rsAmnt);  
                    bdAmnt.setScale(2, BigDecimal.ROUND_HALF_UP);//四舍五入
                    tAccDetail3.setAmnt(bdAmnt);    
                }catch(Exception e){
                    logger.error("C calPtMemberRecmBouns fail ,translate string to decimal fail!"+e.getMessage());
                }
             tAccDetail3.setCaldate(DateUtil.getCurrentDate());
             tAccDetail3.setStatus("1");
             tAccDetail3.setCreateTime(DateUtil.getCurrentDate());
             tAccDetail3.setModifyTime(DateUtil.getCurrentDate());
             tAccDetail3.setOperator("sys");
             if(! accService.addAccDetail(tAccDetail3,Constant.VERSION_NO)){
                 logger.info("addAccDetail fail,tAccDetail3 is "+tAccDetail3.toString());
             }; 
        
         }
    
    }*/
    
    
    
    /**
     * 计算普通会员注册奖励关系
     * 
     * */
    /*private boolean   calPtMemberRegitBouns(UserDetail mUserDetail){              
        SysGencode tSysGencodeBNDMZC5 = new SysGencode();
        if(tSysGencodeAll!=null&&tSysGencodeAll.size()>0){
            for(SysGencode tSysGencode:tSysGencodeAll){
                if(tSysGencode.getGroupCode().equals("BOUND_DEF")){//获取奖金定义
                    if(tSysGencode.getCodeValue().equals("DMZC")){
                        tSysGencodeBNDMZC5= tSysGencode;
                        break;
                    }                   
                }
            }
        }       
        //根据tb_acc_def的定义，设置子账号
        AccDetail tAccDetail = new AccDetail();
        tAccDetail.setUserCode(mUserDetail.getUserCode());
        tAccDetail.setSubAccno("010101");
        tAccDetail.setBounsSource1("10");
        tAccDetail.setBounsSource2("1001");
        tAccDetail.setRelaUsercode(mUserDetail.getRefPhone());
        tAccDetail.setRelaUserlevel("");
        try{
            String rsAmnt="0";
            if(tSysGencodeBNDMZC5.getCodeValue()==null||
                    tSysGencodeBNDMZC5.getCodeValue().equals("")||
                    tSysGencodeBNDMZC5.getCodeValue().equals("null"))
            {
                logger.info("calPtMemberRegitBouns fail ,tSysGencodeBNDMZC5 is null !");
            }else{
                rsAmnt = tSysGencodeBNDMZC5.getCodeValue();
                
            }
            BigDecimal bdAmnt=new BigDecimal(rsAmnt);  
            bdAmnt.setScale(2, BigDecimal.ROUND_HALF_UP);//四舍五入
            tAccDetail.setAmnt(bdAmnt); 
        }catch(Exception e){
            logger.error("calPtMemberRegitBouns fail ,translate string to decimal fail!"+e.getMessage());
        }
        tAccDetail.setCaldate(DateUtil.getCurrentDate());
        tAccDetail.setStatus("1");
        tAccDetail.setCreateTime(DateUtil.getCurrentDate());
        tAccDetail.setModifyTime(DateUtil.getCurrentDate());
        
        return accService.addAccDetail(tAccDetail,Constant.VERSION_NO);
        
    }*/
    
    
    
    
    /**
     * 创建用户关系
     * */
    /*private void   createUserFriendship(UserDetail mUserDetail){
        String refPhone = mUserDetail.getRefPhone(); 
        String UserCode = mUserDetail.getUserCode();
        String userType = mUserDetail.getUserType();
        
        User tRefUser =  userService.findbyPhone(refPhone, Constant.VERSION_NO);
                
         boolean tUpflag = userService.findIsExistUpFriendship(tRefUser.getUserCode(), Constant.VERSION_NO);
         UserFriendship fUserFriendship= new UserFriendship();//推荐人上级关系
         boolean tSvSelShipFlag =true;           
         String tReflvl ="";
         String sefLev="";
         if(!tUpflag){//如果推荐人没有上级会员，则自己为A级会员
             tReflvl="0";
             sefLev="1";
         }
         if(tUpflag){
             fUserFriendship =  userService.findUpFriendship(tRefUser.getUserCode(), Constant.VERSION_NO);
             if(fUserFriendship.getRelaLevel().equals("A")){
                 sefLev="2";
             }else if(fUserFriendship.getRelaLevel().equals("B")){
                 sefLev="3";
             }else if(fUserFriendship.getRelaLevel().equals("C")){
                 sefLev="4";
             }else{
                 sefLev="4";                    
             }
         }
         
         UserFriendship tUserFriendship = new UserFriendship();
         tUserFriendship.setUserCode(tRefUser.getUserCode());
         tUserFriendship.setRecomuserCode(UserCode);    
         if(userType.equals("1")){
         tUserFriendship.setRelaLevel("A");//
         }
         tUserFriendship.setUserType(userType);
         tUserFriendship.setCalflag("0");
         tUserFriendship.setCreateTime(DateUtil.getCurrentDate());
         tUserFriendship.setModifyTime(DateUtil.getCurrentDate());
         if(!userService.addUserFriendShip(tUserFriendship, Constant.VERSION_NO)){
                 logger.info("addUserFriendShip fail,tUserFriendship is "+tUserFriendship.toString());
         };     
         if(userType.equals("1")){//普通会员         
             if(sefLev.equals("2")){//总共有2层             
                 UserFriendship tUserFriendship2 = new UserFriendship();
                 tUserFriendship2.setUserCode(fUserFriendship.getUserCode());
                 tUserFriendship2.setRecomuserCode(UserCode);
                 tUserFriendship2.setRelaLevel("B");//只要有
                 tUserFriendship2.setUserType(userType);
                 tUserFriendship2.setCalflag("0");
                 tUserFriendship2.setCreateTime(DateUtil.getCurrentDate());
                 tUserFriendship2.setModifyTime(DateUtil.getCurrentDate());
                 if(!userService.addUserFriendShip(tUserFriendship2, Constant.VERSION_NO)){
                     logger.info("addUserFriendShip fail,tUserFriendship2 is "+tUserFriendship2.toString());
                 };                  
             }
             if(sefLev.equals("3")){//总共有3层
                 UserFriendship  fUserFriendship3 =  userService.findUpFriendship(fUserFriendship.getUserCode(), Constant.VERSION_NO); 
                 UserFriendship tUserFriendship3 = new UserFriendship();
                 tUserFriendship3.setUserCode(fUserFriendship3.getUserCode());
                 tUserFriendship3.setRecomuserCode(UserCode);
                 tUserFriendship3.setRelaLevel("C");//只要有
                 tUserFriendship3.setUserType(userType);
                 tUserFriendship3.setCalflag("0");
                 tUserFriendship3.setCreateTime(DateUtil.getCurrentDate());
                 tUserFriendship3.setModifyTime(DateUtil.getCurrentDate());
                 if(!userService.addUserFriendShip(tUserFriendship3, Constant.VERSION_NO)){
                     logger.info("addUserFriendShip fail,tUserFriendship3 is "+tUserFriendship3.toString());
                 };
             }
         }
        
    }*/
    
    
    
    
    
    /**
     * 修改密码
     * <p> 修改密码  </p>
     * @Title: modifypwd 
     * @return  json格式的
     * @create author kezhiyi
     * @create date 2016年8月23日
     */
    @RequestMapping(value="/modifypwd", method=RequestMethod.POST)
    @ResponseBody
    public AjaxResponse modifypwd(HttpServletRequest request,HttpServletResponse response){
        AjaxResponse ar = new AjaxResponse();
        try {
        	      	         
            String oldPassWord  =request.getParameter("oldpassword");
            String passWord  =request.getParameter("newpassword");
            String cfPassWord  =request.getParameter("resnewpassword");
            String accessToken  =request.getParameter("accessToken");          
            SysOauth mSysOauth =(SysOauth) jedisManager.get(accessToken);      
            Integer userId=0;
            if(mSysOauth==null){           	
            	ar.setSuccess(false);
                ar.setMessage("找不到登陆信息！");
                return ar;
            }else{
            	userId =mSysOauth.getUserId();
            }  
            
            //sql注入检查
            if(!SqlValid.isSqlValid(userId.toString())) {
           	    ar.setSuccess(false);
                ar.setMessage(userId+"参数格式错误！");
                return ar;
           }
            if(!SqlValid.isSqlValid(oldPassWord)) {
            	 ar.setSuccess(false);
                 ar.setMessage(oldPassWord+"参数格式错误！");
                 return ar;
            }
            if(!SqlValid.isSqlValid(passWord)) {
           	 ar.setSuccess(false);
                ar.setMessage(passWord+"参数格式错误！");
                return ar;
            }
            if(!SqlValid.isSqlValid(cfPassWord)) {
              	 ar.setSuccess(false);
                   ar.setMessage(cfPassWord+"参数格式错误！");
                   return ar;
              }
            
          
            
            if(StringUtil.isEmpty(passWord)||StringUtil.isEmpty(cfPassWord)){
                ar.setSuccess(false);
                ar.setMessage("密码不能为空!");
                return ar;
            }                   
            
            if(!passWord.equals(cfPassWord)){
                ar.setSuccess(false);
                ar.setMessage("两次密码输入不一致！");
                return ar;
            }
            
//          User fUser =userService.findbyPhone(userName, Constant.VERSION_NO);
            UserDetail fUserDetail = userService.findbyId(userId,Constant.VERSION_NO);
            if(fUserDetail!=null&&!MD5Util.getMd5Code(oldPassWord).equals(fUserDetail.getTransPassword())){
                ar.setSuccess(false);
                ar.setMessage("输入原密码错误,请重新输入！");
                return ar;
            }

            if(!userService.modifypwdById(userId, MD5Util.getMd5Code(passWord), Constant.VERSION_NO)){          
                ar.setSuccess(false);
                ar.setMessage("修改密码失败！");
                return ar;
            }
                ar.setSuccess(true);
                ar.setMessage("修改密码成功！");
                return ar;
            
        } catch (Exception e) {
            e.printStackTrace();
            ar.setSuccess(false);
            ar.setMessage("网络繁忙，请稍候重试！");
        }
        return ar;
    }
    
    /**
     * 忘记密码
     * <p> 忘记密码  </p>
     * @Title: forgetpwd 
     * @return  json格式的
     * @create author kezhiyi
     * @create date 2016年8月23日
     */
    @RequestMapping(value="/forgetpwd", method=RequestMethod.POST)
    @ResponseBody
    public AjaxResponse forgetpwd(HttpServletRequest request,HttpServletResponse response){
        AjaxResponse ar = new AjaxResponse();
        try {
            String userName  =request.getParameter("phone");
            String passWord  =request.getParameter("password");
            String cfPassWord  =request.getParameter("resPassword");
            String vcode  =request.getParameter("yzm");
            
            if(!Validator.isMobile(userName)){
                ar.setSuccess(false);
                ar.setMessage("手机号格式不正确！");
                return ar;
            }
            
            if(StringUtil.isEmpty(passWord)||StringUtil.isEmpty(cfPassWord)){
                ar.setSuccess(false);
                ar.setMessage("密码不能为空!");
                return ar;
            }                   
            
            if(!passWord.equals(cfPassWord)){
                ar.setSuccess(false);
                ar.setMessage("两次密码输入不一致！");
                return ar;
            }
                    
            //1小时之内的短信验证码有效
            String tVcode =smsService.findSendsmsDetail(userName,Constant.CUR_SYS_CODE); 
            if(tVcode ==null||!vcode.equals(tVcode.trim())){
                ar.setSuccess(false);
                ar.setMessage("验证码输入不正确！");
                return ar;
            }

            if(!userService.modifyPwd(userName, MD5Util.getMd5Code(passWord),Constant.VERSION_NO)){         
                ar.setSuccess(false);
                ar.setMessage("修改密码失败！");
                return ar;
            }
                ar.setSuccess(true);
                ar.setMessage("修改密码成功！");
                return ar;
            
        } catch (Exception e) {
            e.printStackTrace();
            ar.setSuccess(false);
            ar.setMessage("网络繁忙，请稍候重试！");
        }
        return ar;
    }
    
    /**
     * 用户实名
     * <p>用户实名  </p>
     * @Title: realname 
     * @return  json格式的
     * @create author kezhiyi
     * @create date 2016年8月23日
     */
    @RequestMapping(value="/realname", method=RequestMethod.POST)
    @ResponseBody
    public AjaxResponse realname(HttpServletRequest request,HttpServletResponse response){
        AjaxResponse ar = new AjaxResponse();
        try {
            User user = (User)request.getSession().getAttribute(Constant.LOGIN_USER);
            String userCode="";
			if(user==null){
				userCode =request.getParameter("userCode");
			}else{
				userCode =user.getUserCode();
			}
            String realName  = request.getParameter("realName");
            if(!StringUtil.isEmpty(realName)){
                realName = URLDecoder.decode(realName, "UTF-8");
            }
            String idno  =request.getParameter("idno");
            UserDetail userDetail = userService.findUserDetailByUserCode(userCode, Constant.VERSION_NO);
            
            /*if(!isMobile(userName)){
                ar.setSuccess(false);
                ar.setMessage("手机号格式不正确！");
                return ar;
            }*/
            
            if(StringUtil.isEmpty(realName)||StringUtil.isEmpty(idno)){
                ar.setSuccess(false);
                ar.setMessage("真实姓名、身份证号不能为空！");
                return ar;
            }
                    
            if(!IdcardUtils.validateCard(idno)){
                ar.setSuccess(false);
                ar.setMessage("身份证格式不正确！");
                return ar;
            };
            if(!userService.realUser(userDetail.getUserName(), realName, idno, Constant.VERSION_NO)){            
                ar.setSuccess(false);
                ar.setMessage("用户实名失败！");
                return ar;
            }
                ar.setSuccess(true);
                ar.setMessage("用户实名成功！");
                return ar;
            
        } catch (Exception e) {
            e.printStackTrace();
            ar.setSuccess(false);
            ar.setMessage("网络繁忙，请稍候重试！");
        }
        return ar;
    }
    
    /**
     * 用户实名
     * <p>用户实名  </p>
     * @Title: realname 
     * @return  json格式的
     * @create author kezhiyi
     * @create date 2016年8月23日
     */
    @RequestMapping(value="/modifyphone", method=RequestMethod.POST)
    @ResponseBody
    public AjaxResponse modifyphone(HttpServletRequest request,HttpServletResponse response){
        AjaxResponse ar = new AjaxResponse();
        try {
            String oldPhone  =request.getParameter("oldphone");
            String phone  =request.getParameter("phone");
            String vcode  =request.getParameter("yzm");
            if(StringUtil.isEmpty(phone)||StringUtil.isEmpty(vcode)){
                ar.setSuccess(false);
                ar.setMessage("手机号、验证码不能为空！");
                return ar;
            }
            
            if(!Validator.isMobile(phone)){
                ar.setSuccess(false);
                ar.setErrorCode(CodeConstant.MOBILE_ERROR);
                ar.setMessage("手机号格式不正确！");
                return ar;
            }
            

            //判断用户是否已存在
            if(userService.findIsExist(phone, Constant.VERSION_NO)){
                ar.setSuccess(false);
                ar.setMessage("手机号已存在！");
                ar.setErrorCode(CodeConstant.MOBILE_EXISTS);
                return ar;
            }
            
            
            //1小时之内的短信验证码有效
            String tVcode =smsService.findSendsmsDetail(phone,Constant.CUR_SYS_CODE); 
            if(tVcode==null||!vcode.equals(tVcode.trim())){
                ar.setSuccess(false);
                ar.setErrorCode(CodeConstant.SMS_ERROR);
                ar.setMessage("验证码输入不匹配！");
                return ar;
            }
            
            User fUser = userService.findbyPhone(oldPhone, Constant.VERSION_NO);
            if(fUser==null){
                ar.setSuccess(false);
                logger.info("修改手机号功能,根据原手机号"+oldPhone+"未查询到用户信息！");
                ar.setMessage("系统异常！");
                return ar;
            }
            String tUserCode =fUser.getUserCode();
            
        
            User tUser = new User();
            tUser.setUserCode(tUserCode);
            tUser.setPhone(phone);
            tUser.setModifyTime(DateUtil.getCurrentDate());
        
            UserDetail tUserDetail = new UserDetail();
//            tUserDetail.setuser(tUserCode);
//            tUserDetail.setPhone(phone);
            tUserDetail.setModifyTime(DateUtil.getCurrentDate());
            
            
            if(!userService.modifyPhone(tUserCode, phone,oldPhone,Constant.VERSION_NO)){            
                ar.setSuccess(false);
                ar.setMessage("修改手机号失败！");
                return ar;
            }
            
                ar.setSuccess(true);
                ar.setMessage("修改手机号成功！");
                return ar;
            
        } catch (Exception e) {
            e.printStackTrace();
            ar.setSuccess(false);
            ar.setMessage("网络繁忙，请稍候重试！");
        }
        return ar;
    }
    
    /**
     * 发送短信
     * <p> 发送短信  </p>
     * @Title: sendsms 
     * @return  json格式的
     * @create author kezhiyi
     * @create date 2016年8月18日
     */
    @RequestMapping(value="/sendsms", method=RequestMethod.POST)
    @ResponseBody
    public AjaxResponse sendsms(HttpServletRequest request,HttpServletResponse response){
        AjaxResponse ar = new AjaxResponse();
        try {
            String phone  =request.getParameter("phone");
            if(!Validator.isMobile(phone)){
                ar.setSuccess(false);
                ar.setMessage("手机号格式不正确！");
                return ar;
            }
            Map<String, Object> smsmap = userService.findLockSmsStatus(phone,smsInterval, Constant.VERSION_NO);
            String tSmsLockStatus = 
                    smsmap==null?Constant.UN_LOCKED:smsmap.get("is_smslocked")==null?Constant.UN_LOCKED:(String) smsmap.get("is_smslocked");
            Date tSmsLockTime =
                    smsmap==null?null:smsmap.get("smslock_time")==null? null: (Date) smsmap.get("smslock_time");
            if(tSmsLockTime!=null&&
                    tSmsLockTime.compareTo(Constant.SYS_BUILDDATE)>0
                    &&(DateUtil.getCurrentDate().getTime() -tSmsLockTime.getTime())/1000<=smsInterval){
                ar.setSuccess(false);
                ar.setMessage("短信发送太频繁，请稍后重试!");
                return ar;
            }
            
             List<Map<String,Object>> tSysGencodeList =sysGenCodeService.findByGroupCode("SENDSMS_FLAG", Constant.VERSION_NO);
             String smsflag ="";
                for(Map<String,Object> mapObj:tSysGencodeList){
                    if("SENDSMS_FLAG".equals(mapObj.get("codeName"))){
                        smsflag = mapObj.get("codeValue").toString();
                    }
                  
                }
            String vCode = "";
            if("0".equals(smsflag)){
                vCode = "888888";
            }else{
                vCode =SmsSend.sendSms(phone);
            }
//          String vCode =SmsSend.sendSms(phone);           
//          String vCode =String.valueOf((int)((Math.random()*9+1)*100000));
            if(vCode.equals("0")){              
                ar.setSuccess(false);
                ar.setMessage("短信发送失败！");
                return ar;
            }           
            int sendcnt= smsService.findSendCntByPhone(phone,smsInterval);
        
            Sendsms sendsmsInfo = new Sendsms();
            sendsmsInfo.setPhone(phone);
            sendsmsInfo.setSendsmsCount(sendcnt+1);
            sendsmsInfo.setSendTime(DateUtil.getCurrentDate());                     
            if(sendcnt==0){             
                smsService.addSendsmsInfo(sendsmsInfo);             
            }else if(sendcnt<smsLimit-1&&tSmsLockStatus.equals(Constant.UN_LOCKED)){
                if(!smsService.modifySendsmsInfo(sendsmsInfo)){
                    logger.info("modifySendsmsInfo fail,sendcnt is "+sendcnt+1);
                };
                if(tSmsLockStatus.equals(Constant.LOCKED)&&
                !userService.modifyLockSmsStatus(phone, Constant.UN_LOCKED,DateUtil.getCurrentDate(), Constant.VERSION_NO)){
                    logger.info("modifyLockSmsStatus fail,phone is "+phone +"unlock fail!");
                }               
            }else if(tSmsLockStatus.equals(Constant.LOCKED)){//超过系统设定的时间，可以再次申请发送短信，次数记为1
                sendsmsInfo.setSendsmsCount(1);
                if(!smsService.modifySendsmsInfo(sendsmsInfo)){
                    logger.info("modifySendsmsInfo fail,reset sendcnt fail, sendcnt is  "+1);
                };
                if(!userService.modifyLockSmsStatus(phone,  Constant.UN_LOCKED,null, Constant.VERSION_NO)){
                    logger.info("modifyLockSmsStatus fail ,phone is "+phone +"lock fail!");
                }
                
            }else{
                sendsmsInfo.setLockedTime(DateUtil.getCurrentDate());
                if(!smsService.modifySendsmsInfo(sendsmsInfo)){
                    logger.info("modifySendsmsInfo fail,sendcnt is "+sendcnt+1);
                };
                if(!userService.modifyLockSmsStatus(phone,  Constant.LOCKED,DateUtil.getCurrentDate(), Constant.VERSION_NO)){
                    logger.info("modifyLockSmsStatus fail ,phone is "+phone +"lock fail!");
                }
                
            }
            SendsmsDetail tSendsmsDetail = new SendsmsDetail();
            tSendsmsDetail.setPhone(phone);
            tSendsmsDetail.setSendTime(DateUtil.getCurrentDate());
            tSendsmsDetail.setVcode(vCode);
            tSendsmsDetail.setSysCode(Constant.CUR_SYS_CODE);
            if(!smsService.addSendsmsDetail(tSendsmsDetail)){
                logger.info("addSendsmsDetail fail ,phone is "+phone +"!");             
            }
            ar.setSuccess(true);
            ar.setMessage("发送短信成功！");
            return ar;
            
        } catch (Exception e) {
            e.printStackTrace();
            ar.setSuccess(false);
            ar.setMessage("网络繁忙，请稍候重试！");
        }
        return ar;
    }
    
    /**
     * 退出
     * @param request
     * @return
     */
    /*@RequestMapping(value = "/logout", method = RequestMethod.GET)
    @ResponseBody
    public AjaxResponse logout(HttpServletRequest request) {
        request.getSession().setAttribute(Constant.LOGIN_USER, null);
        ar.setSuccess(true);
        ar.setMessage("成功退出！");
        return ar;
    }*/
    /**
     * 退出
     * @param request
     * @return
     * @throws Exception 
     */
    @RequestMapping(value = "/logout", method = RequestMethod.POST)
    @ResponseBody
    public AjaxResponse logout(HttpServletRequest request,HttpServletResponse response) throws Exception {
    	String accessToken  =request.getParameter("accessToken");          
        SysOauth mSysOauth =(SysOauth) jedisManager.get(accessToken);      
        if(mSysOauth==null){
        	ar.setSuccess(false);
            ar.setMessage("找不到登陆信息！");
            return ar;
        }else{
        	jedisManager.del(accessToken);
        }   
        ar.setSuccess(true);
        ar.setMessage("成功退出！");
        return ar;
    }
    
    
    /**
     * 检查token信息
     * @param request
     * @return
     * @throws Exception 
     */
    @RequestMapping(value = "/checkToken", method = RequestMethod.POST)
    @ResponseBody
    public AjaxResponse checkToken(HttpServletRequest request,HttpServletResponse response) throws Exception {
    	String accessToken  =request.getParameter("accessToken");       
    	String imeiid  =request.getParameter("imeiid");          

        SysOauth mSysOauth =(SysOauth) jedisManager.get(accessToken);      
        if(mSysOauth==null){
        	ar.setSuccess(false);
            ar.setMessage("找不到登陆信息！");
            return ar;
        }  
        ar.setSuccess(true);
        ar.setMessage("token存在！");
        return ar;
    }
    
    
//    /**
//     * 退出
//     * @param request
//     * @return
//     * @throws Exception 
//     */
//    @RequestMapping(value = "/logout", method = RequestMethod.GET)
//    @ResponseBody
//    public void logout(HttpServletRequest request,HttpServletResponse response) throws Exception {
//    	String accessToken  =request.getParameter("accessToken");          
//        SysOauth mSysOauth =(SysOauth) jedisManager.get(accessToken);      
//        if(mSysOauth==null){
//        
//        }else{
//        	jedisManager.del(accessToken);
//        }    	
//    }

    
    @RequestMapping(value="/upload")
    public void upload(HttpServletRequest request,HttpServletResponse response,
            @RequestParam(value="tp",required=false) MultipartFile tp){
         // 判断文件是否为空  
        logBefore(logger, "上传头像");
        response.setCharacterEncoding("UTF-8");
        response.setContentType("text/html; charset=UTF-8");
        response.setHeader("Content-Type", "text/html; charset=UTF-8");
        PrintWriter out  = null;
        try {
            out = response.getWriter();
        } catch (IOException e1) {
            e1.printStackTrace();
        }
        User user = (User)request.getSession().getAttribute(Constant.LOGIN_USER);
        String userCode="";
        if(user==null){
            userCode =request.getParameter("userCode");
        }else{
            userCode =user.getUserCode();
        }
        UserDetail userDetail = userService.findUserDetailByUserCode(userCode, Constant.VERSION_NO);
        PageData pdfile = this.getPageData();
        Map<String,String> map = new HashMap<String, String>();
        map.put("jpg","jpg");
        map.put("png","png");
        map.put("jpeg","jpeg");
        map.put("png","png");
        map.put("gif","gif");
        map.put("bmp","bmp");
        try {
            String filepath = pdfile.getString("filepath");
            String resources_base = "";
            String resources_local = "";
            String resources_backup = "";
            List<Map<String,Object>> tSysGencodeList =sysGenCodeService.findByGroupCode("RESOURCES_PATH", Constant.VERSION_NO);
            for(Map<String,Object> mapObj:tSysGencodeList){
                if("RESOURCES_BASE".equals(mapObj.get("codeName"))){
                    resources_base = mapObj.get("codeValue").toString();
                }
                if("RESOURCES_LOCAL".equals(mapObj.get("codeName"))){
                    resources_local = mapObj.get("codeValue").toString();
                }
                if("RESOURCES_BACKUP".equals(mapObj.get("codeName"))){
                    resources_backup = mapObj.get("codeValue").toString();
                }
            }
            if("".equals(resources_base)||"".equals(resources_local)||"".equals(resources_backup)){
                logger.info("RESOURCES_BASE or RESOURCES_LOCAL or RESOURCES_BACKUP Error:value may be null");
                out.print("<script>parent.window.alert(\"服务器路径有误，请联系客服！\");</script>");
                return;
            }
            int lastindex = filepath.lastIndexOf(".");
            String imgtype = filepath.substring(lastindex+1,filepath.length());
            System.out.println("----------imgtype:"+imgtype);
            if(!map.containsKey(imgtype)){
                out.print("<script>parent.window.alert(\"请上传jpg、png、jpeg、gif、bmp格式的图片！\");</script>");
                return;
            }
            
//            long currentTimeMillis = System.currentTimeMillis();
            String new_img_name= get32UUID()+"_"+Constant.PIC_HEAD_WIDTH+"_"+Constant.PIC_HEAD_HEIGHT+"."+imgtype;
            String http_img_url = resources_base+Constant.PIC_HEAD_PATH+new_img_name;
            String temp_img_url = resources_local+Constant.PIC_TEMP_PATH;//临时图片路径
            String server_img_url = resources_local+Constant.PIC_HEAD_PATH;//图片保存服务器路径
            String backup_img_url = resources_backup+Constant.PIC_HEAD_PATH;//图片磁盘备份路径
            
            
            System.out.println("-----------temp_img_url----------------->>>>>>>>>>>>>>>>>"+temp_img_url);
            System.out.println("-----------server_img_url----------------->>>>>>>>>>>>>>>>>"+server_img_url);
            System.out.println("-----------http_img_url------------------->>>>>>>>>>>>>>>>>"+http_img_url);
            System.out.println("-----------backup_img_url----------------->>>>>>>>>>>>>>>>>"+backup_img_url);
            
            FileUtil.copyFile(tp.getInputStream(), temp_img_url,new_img_name);
            Map<String, Long> imgInfo = ImgUtil.getImgInfo(temp_img_url+new_img_name);
            Long w = imgInfo.get("w");
            Long h = imgInfo.get("h");
//            Long s = imgInfo.get("s");
            if(w != Constant.PIC_HEAD_WIDTH && h != Constant.PIC_HEAD_HEIGHT){
                FileUtil.deleteFile(temp_img_url+new_img_name);
                out.print("<script>top.alert(\"请上传"+Constant.PIC_HEAD_WIDTH+"*"+Constant.PIC_HEAD_WIDTH+"规格的图片！\");</script>");
                return;
            }
            FileUtil.copyFile(new FileInputStream(temp_img_url+new_img_name), server_img_url,new_img_name);
            FileUtil.copyFile(new FileInputStream(temp_img_url+new_img_name), backup_img_url,new_img_name);
            FileUtil.deleteFile(temp_img_url+new_img_name);
//            //上传新图片时删除原图片
//            if(!StringUtil.isEmpty(userDetail.getImgAddrss())){
//                int index = userDetail.getImgAddrss().indexOf("/uploadfile");
//                if(index>0){
//                    String tomcat_path = resources_local+userDetail.getImgAddrss().substring(index+1, userDetail.getImgAddrss().length());
//                    String backup_path = resources_backup+userDetail.getImgAddrss().substring(index+1, userDetail.getImgAddrss().length());
//                    FileUtil.deleteFile(tomcat_path);
//                    FileUtil.deleteFile(backup_path);
//                }
//            }
            userService.modifyUserHeadPic(pdfile.getString("userCode"), http_img_url, Constant.VERSION_NO);//修改数据库图片地址
            out.print("<script>parent.document.getElementById('headPicId').src=\""+http_img_url+"\";parent.document.getElementById('left_headPicId').src=\""+http_img_url+"\";parent.$('#imgAddrss').val('"+http_img_url+"')</script>"); 
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(e.toString(), e);
            out.print("<script>parent.alert('系统异常，上传失败！');</script>");
        }finally{
            out.print("<script>parent.$(\"input[type='file']\").val('');</script>");
            out.close();
            logAfter(logger);
        }
        return;
    }
    
    
    public static void main(String[] args) {
        String str = "http://192.168.0.116:8080/uploadfile/img/head/40cf2ab223f14422affd2dcc006cc4a9_122_122.bmp";
        int index = str.indexOf("/uploadfile");
        String path = str.substring(index+1, str.length());
        System.out.println("-------------------imgpath="+path);
    }
    @RequestMapping(value="delPic",method = RequestMethod.POST)
    @ResponseBody
    public AjaxResponse delPic(){
        logBefore(logger, "删除图片");
        AjaxResponse ar = new AjaxResponse();
        try{
            PageData pd = new PageData();
            pd = this.getPageData();
            String imgAddrss = pd.getString("imgAddrss");
            if(!StringUtil.isEmpty(imgAddrss)){
                //删除硬盘上的文件 start
                String img_name = imgAddrss.substring(imgAddrss.lastIndexOf("/")+1); 
                String resources_server = "";
                String resources_backup = "";
                List<Map<String,Object>> tSysGencodeList =sysGenCodeService.findByGroupCode("RESOURCES_PATH", Constant.VERSION_NO);
                for(Map<String,Object> mapObj:tSysGencodeList){
                    if("RESOURCES_LOCAL".equals(mapObj.get("codeName"))){
                        resources_server = mapObj.get("codeValue").toString();
                    }
                    if("RESOURCES_BACKUP".equals(mapObj.get("codeName"))){
                        resources_backup = mapObj.get("codeValue").toString();
                    }
                }
                String server_img_url = resources_server+Constant.PIC_HEAD_PATH+img_name;//图片存放在服务器路径
                String backup_img_url = resources_backup+Constant.PIC_HEAD_PATH+img_name;//图片存放在磁盘路径
                File serverFile = new File(server_img_url.trim()); 
                File backupFile = new File(backup_img_url.trim()); 
                if(serverFile.exists()){
                    serverFile.delete();
                }else{
                    logger.info("服务器文件不存在");
                }
                if(backupFile.exists()){
                    serverFile.delete();
                }else{
                    logger.info("磁盘文件不存在");
                }
                //删除硬盘上的文件 end
                userService.modifyUserHeadPic(pd.getString("userCode"), null, Constant.VERSION_NO);//删除数据库图片地址
                ar.setSuccess(true);
                ar.setMessage("删除成功！");
            }   
                
        }catch(Exception e){
            logger.error(e.toString(), e);
            ar.setSuccess(false);
            ar.setMessage("删除失败！");
        }
        logAfter(logger);
        return ar;
    }
    
    private UserDetail findUserDetail(HttpServletRequest request){
        
        UserDetail userDetail = (UserDetail)request.getSession().getAttribute(Constant.LOGIN_USER);
        Integer userId =0;
        if(userDetail==null){
        	userId =Integer.parseInt(request.getParameter("userId"));
        }else{
        	userId = userDetail.getId();
        }
        if(userId == null||userId==0)
            return null;
    
        return userDetail;
    }
    
    /**
     * @describe:跳往普通会员个人中心
     * @author: zhangchunming
     * @date: 2016年9月2日上午10:08:58
     * @params: @return
     * @return: AjaxResponse
     */
    @RequestMapping(value="/toMyself", method = RequestMethod.GET)
    @ResponseBody
    public AjaxResponse toMyself(HttpServletRequest request){
        logBefore(logger,"去往用户中兴");
        AjaxResponse ar = new AjaxResponse();
        /*UserDetail userDetail = JSON.parseObject(params,UserDetail.class);
        if(userDetail==null||StringUtil.isEmpty(userDetail.getUserCode())){
            ar.setSuccess(false);
            ar.setErrorCode(CodeConstant.PARAM_ERROR);
            ar.setMessage("请求参数有误！");
        }*/
        UserDetail userDetail = findUserDetail(request);
        if(userDetail != null){
            ar.setSuccess(true);
            ar.setData(userDetail);
            ar.setMessage("查询数据成功！");
        }else{
            ar.setSuccess(false);
            ar.setMessage("数据不存在！");
        }
        logAfter(logger);
        return ar;
    }
    
    @RequestMapping(value="/getUserInfo", method = RequestMethod.GET)
    @ResponseBody
    public AjaxResponse getHeadPic(HttpServletRequest request){
        logBefore(logger,"获取用户信息");
        AjaxResponse ar = new AjaxResponse();
        Map<String,String> map = new HashMap<String,String>();
        UserDetail userDetail = findUserDetail(request);
        if(userDetail != null){
            ar.setSuccess(true);
            map.put("alipayAccno", userDetail.getAlipayAccno());
            map.put("userType", userDetail.getUserType());
            map.put("userName", userDetail.getUserName());
            map.put("realName", userDetail.getRealName());
            ar.setData(map);
            ar.setMessage("查询数据成功！");
        }else{
            ar.setSuccess(false);
            ar.setMessage("数据不存在！");
        }
        logAfter(logger);
        return ar;
    }
    /**
     * @describe:判断用户手机号是否存在
     * @author: zhangchunming
     * @date: 2016年9月20日下午6:36:46
     * @return: AjaxResponse
     */
    @RequestMapping(value="/isExistPhone", method=RequestMethod.POST)
    @ResponseBody
    public AjaxResponse isExistPhone(){
        AjaxResponse ar = new AjaxResponse();
        PageData pd = new PageData();
        pd = this.getPageData();
        String phone = pd.getString("phone").trim();
        if(StringUtil.isEmpty(phone)){
            ar.setSuccess(false);
            ar.setMessage("请求参数有误！");
            return ar;
        }
        User user = userService.findbyPhone(phone, Constant.VERSION_NO);
        if(user==null){
            ar.setSuccess(false);
            ar.setMessage("该手机号未注册！");
            return ar;
        }else{
            ar.setMessage("该手机号已注册！");
            ar.setSuccess(true);
        }
        return ar;
    }
}
