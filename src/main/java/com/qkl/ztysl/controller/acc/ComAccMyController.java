package com.qkl.ztysl.controller.acc;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;
import com.qkl.util.help.APIHttpClient;
import com.qkl.util.help.AjaxResponse;
import com.qkl.util.help.DateUtil;
import com.qkl.util.help.OrderGenerater;
import com.qkl.util.help.StringUtil;
import com.qkl.util.help.pager.PageData;
import com.qkl.ztysl.api.common.CodeConstant;
import com.qkl.ztysl.api.common.Constant;
import com.qkl.ztysl.api.entity.Page;
import com.qkl.ztysl.api.po.user.User;
import com.qkl.ztysl.api.po.user.UserDetail;
import com.qkl.ztysl.api.service.acc.api.AccOutdetailService;
import com.qkl.ztysl.api.service.acc.api.AccService;
import com.qkl.ztysl.api.service.acc.api.ComAccMyService;
import com.qkl.ztysl.api.service.sys.api.SysGenCodeService;
import com.qkl.ztysl.api.service.user.api.UserService;
import com.qkl.ztysl.provider.dao.AccInfoDao;
import com.qkl.ztysl.provider.dao.InterfaceLogDao;
import com.qkl.ztysl.web.BaseAction;


@Controller
@RequestMapping("/service/comacc")
public class ComAccMyController extends BaseAction {

	@Autowired
	private ComAccMyService cams;
	@Autowired
	private AccOutdetailService accOutdetailService;
	@Autowired
	private SysGenCodeService sysGenCodeService;
	@Autowired
	private AccService accService;
	@Autowired
	private AccInfoDao accDao;
	@Autowired
	private InterfaceLogDao interfaceLogDao;
	@Autowired
	private UserService userService;
	
	
	@RequestMapping(value="/findMyAcc",method=RequestMethod.POST)
	@ResponseBody
	public  AjaxResponse findTB(HttpServletRequest request){
		
		AjaxResponse ar = new AjaxResponse();
		Map<String, Object> nums=null;
		try {
			User user = (User)request.getSession().getAttribute(Constant.LOGIN_USER);
			String userCode="";
			if(user==null){
				userCode =request.getParameter("userCode");
			}else{
				userCode =user.getUserCode();
			}			
			nums = cams.findMyAcc(userCode);//查询余额，冻结，总量，推荐总奖励，推荐奖励
			ar.setSuccess(true);
			ar.setMessage("查询成功");
		} catch (Exception e) {
			e.printStackTrace();
			ar.setSuccess(false);
			ar.setMessage("网络繁忙，请稍候重试！");
		}
		ar.setData(nums);
		return ar;	
	}
	@RequestMapping(value="/getAmnt",method=RequestMethod.POST)
	@ResponseBody
	public  AjaxResponse getAmnt(HttpServletRequest request){
	    AjaxResponse ar = new AjaxResponse();
	    try {
	        User user = (User)request.getSession().getAttribute(Constant.LOGIN_USER);
	        String userCode="";	       
	        if(user==null){
	        	userCode=request.getParameter("userCode");
	        }else{
	        	userCode=user.getUserCode();
	        }
	        
	        PageData accPd = new PageData();
	        accPd.put("user_code", userCode);
	        accPd = cams.getAmnt(accPd);
	        if(accPd!=null){
	            PageData tpd = new PageData();
	            tpd.put("avb_amnt", accPd.get("avb_amnt")==null?"0.0000":String.format("%.4f",new BigDecimal(accPd.get("avb_amnt").toString())));
	            tpd.put("froze_amnt", accPd.get("froze_amnt")==null?"0.0000":String.format("%.4f",new BigDecimal(accPd.get("froze_amnt").toString())));
	            tpd.put("total_amnt", accPd.get("total_amnt")==null?"0.0000":String.format("%.4f",new BigDecimal(accPd.get("total_amnt").toString())));
	            ar.setSuccess(true);
	            ar.setData(tpd);
	            ar.setMessage("查询成功");
	        }else{
	            ar.setSuccess(false);
                ar.setMessage("账户不存在，请联系客服！");
	        }
	    } catch (Exception e) {
	        e.printStackTrace();
	        ar.setSuccess(false);
	        ar.setMessage("网络繁忙，请稍候重试！");
	    }
	    return ar;	
	}

	@RequestMapping(value="/turnOut",method=RequestMethod.POST)
	@ResponseBody
	public AjaxResponse turnOut(HttpServletRequest request){//比较转账数额的大小
	    AjaxResponse ar = new AjaxResponse();
	    String is_turn = "0";
        List<Map<String, Object>> codeList =  sysGenCodeService.findByGroupCode("TURN_FLAG", Constant.VERSION_NO);
        for(Map<String, Object> codeMap:codeList){
            if("IS_TURN".equals(codeMap.get("codeName"))){
                is_turn = codeMap.get("codeValue").toString();
            }
        }
        if(is_turn.equals("0")){
            ar.setSuccess(false);
            ar.setMessage("暂未开放，敬请期待！");
            return ar;
        }
        PageData pd= new PageData();
        pd= this.getPageData();
		User user = (User)request.getSession().getAttribute(Constant.LOGIN_USER);
		String userCode="";
        if(user==null){
            userCode =request.getParameter("userCode");
        }else{
            userCode =user.getUserCode();
        }
        UserDetail userDetail =  userService.findUserDetailByUserCode(userCode, Constant.VERSION_NO);
		Map<String, Object> findNum = cams.findMyAcc(userCode);
		String string = findNum.get("avb_amnt").toString();//获取可用余额
		String money = pd.getString("money");//获取输入的SAN数量
		String recipient = pd.getString("zhanghao");//获取钱包地址
					try {
						BigDecimal	bigDecimal2 = new BigDecimal(string);//转换成BigDecimal类型
							if (money!=null&&!"".equals(money)) {
								BigDecimal bigDecimal = new BigDecimal(money);
								int compareTo = bigDecimal.compareTo(bigDecimal2);//要转账数量和可用数量比较大小
								if (compareTo==1) {//大于
									ar.setSuccess(true);
									ar.setMessage("您的可用余额不足");
									return ar;
								}
								if (compareTo==0||compareTo==-1) {//等于或者小于
									List<Map<String, Object>> list = sysGenCodeService.findByGroupCode("DIGITAL_SIGN", Constant.VERSION_NO);
									String url="";
									String sender="";
									//String recipient=WalletAddress;
									String pri="";
									String salt="";
									String admin_user="";
									for (Map<String, Object> map : list) {
										if ("PRI".equals(map.get("codeName"))) {
											  pri = map.get("codeValue").toString();
										}
										if ("SALT".equals(map.get("codeName"))) {
											salt = map.get("codeValue").toString();
										}
										if ("ADMIN_USER".equals(map.get("codeName"))) {
											admin_user = map.get("codeValue").toString();
										}
										if ("URL".equals(map.get("codeName"))) {
											url = map.get("codeValue").toString();
										}
										/*if ("RECIPIENT".equals(map.get("codeName"))) {
											recipient = map.get("codeValue").toString();
										}*/
										if ("SENDER".equals(map.get("codeName"))) {
											sender = map.get("codeValue").toString();
										}
									}
									if (StringUtil.isEmpty(url)||StringUtil.isEmpty(sender)||StringUtil.isEmpty(recipient)
											||StringUtil.isEmpty(pri)||StringUtil.isEmpty(salt)||StringUtil.isEmpty(admin_user)) {
										ar.setSuccess(false);
										ar.setMessage("转账失败");
										logger.info("申请转账失败----原因：调用接口参数含有null或空字符串------url="+url+"--sender="+sender+"--recipient="+recipient+"--pri="+pri+"--salt="+salt+"--admin_user="+admin_user);
										return ar;
									}
									/*************************************查询是否有转账中的数据**************************************/
									boolean isExistTransfering = accOutdetailService.findIsExistTransfering(userCode,Constant.VERSION_NO);
									if(isExistTransfering){
									    ar.setSuccess(false);
									    ar.setMessage("系统存在处理中订单，请稍候重试！");
									    return ar;
									}
									/*************************************查询是否有转账中的数据**************************************/
									
									/*************************************转账中**************************************/
									String  flowNo = OrderGenerater.generateOrderNo();//获取流水号
									pd.put("userCode", userCode);
                                    pd.put("subAccno", "010401");//普通会员转出至R8账户
                                    pd.put("outamnt", bigDecimal);
                                    pd.put("outdate",DateUtil.getCurrentDate());
                                    pd.put("cntflag", 0);
                                    pd.put("targetSystem","R8");
                                    pd.put("status", 2);//1成功0失败2转出中
                                    pd.put("createTime", DateUtil.getCurrentDate());
                                    pd.put("modifyTime", DateUtil.getCurrentDate());
                                    pd.put("operator", userDetail.getUserName());
                                    pd.put("order_ids", "");//默认置空
                                    pd.put("sender", sender);
                                    pd.put("recipient", recipient);
                                    pd.put("remark1", flowNo);//流水号
                                    boolean result = accOutdetailService.addAccOutdetail(pd, Constant.VERSION_NO);
                                    if(!result){
                                        ar.setMessage("您的余额不足");
                                        ar.setSuccess(false);
                                        return ar;
                                    }
                                    /*************************************转账中**************************************/
									//调用转账接口
									String turnOut =APIHttpClient.turnOut(url, null, sender, recipient, money, pri, salt, admin_user);
									if(turnOut == null){
									    logger.info("------------网络异常，调用转账接口失败----------------------turnOut="+turnOut);
									    ar.setMessage("网络异常，请稍后再试！");
									    ar.setSuccess(false);
									    //更新账户
                                        PageData accData = new PageData();
                                        accData.put("userCode", userCode);
                                        accData.put("value", money);
                                        accData.put("status", "0");
                                        accData.put("remark1", flowNo);//流水号
                                        accOutdetailService.turnOutBack(accData, Constant.VERSION_NO);
									    return ar;
									}
									JSONObject objJson = JSONObject.parseObject(turnOut);
									String status = objJson.getString("status");
									if ("failed".equals(status)) {
									    ar.setSuccess(false);
									    if(objJson.getJSONObject("data").getString("error_code").contains("PERMISSION_DENIED")){
									        ar.setMessage(CodeConstant.PERMISSION_DENIED);
									    }else if(objJson.getJSONObject("data").getString("error_code").contains("INVALID_PARAMS")){
									        if(objJson.getJSONObject("data").getString("message").contains("invalid params")){
									            ar.setMessage(CodeConstant.INVALID_PARAMS.get("invalid params").toString());
									        }else if(objJson.getJSONObject("data").getString("message").contains("sender and receiver must be different")){
									            ar.setMessage(CodeConstant.INVALID_PARAMS.get("sender and receiver must be different").toString());
									        }else if(objJson.getJSONObject("data").getString("message").contains("receiver not found")){
                                                ar.setMessage(CodeConstant.INVALID_PARAMS.get("receiver not found").toString());
                                            }else{
                                                ar.setMessage(objJson.getJSONObject("data").getString("message"));
                                            }
									        
									    }else if(objJson.getJSONObject("data").getString("error_code").contains("NOT_ENOUGH_BALANCE")){
                                            ar.setMessage(CodeConstant.NOT_ENOUGH_BALANCE);
                                        }else if(objJson.getJSONObject("data").getString("error_code").contains("USER_NOT_FOUND")){
                                            ar.setMessage(CodeConstant.USER_NOT_FOUND);
                                        }else{
                                            ar.setMessage(objJson.getJSONObject("data").getString("error_code"));
                                        }
                                        
                                        logger.info("调用转账接口失败！--------fail-----返回串："+objJson.toString());
                                        //添加日志
									    PageData log = new PageData();
									    log.put("log_titile", "调用转账接口失败");
									    log.put("log_content", objJson.toJSONString());
									    log.put("syscode", Constant.CUR_SYS_CODE);
									    log.put("create_time", DateUtil.getCurrDateTime());
									    log.put("modify_time", DateUtil.getCurrDateTime());
									    log.put("log_type", "1");//接口日志类型：1-转账申请2-转账回调
									    log.put("log_status", "0");//转账日志状态：1-成功 0-失败 2-转账中
									    interfaceLogDao.insertSelective(log);
									    //更新账户
									    PageData accData = new PageData();
									    accData.put("userCode", userCode);
									    accData.put("value", money);
									    accData.put("status", "0");
									    accData.put("remark1", flowNo);//流水号
									    accOutdetailService.turnOutBack(accData, Constant.VERSION_NO);
										return ar;
									}if ("success".equals(status)) {
									    logger.info("调用转账接口成功---------success-----返回串："+objJson.toString());
									  //添加日志
                                        PageData log = new PageData();
                                        log.put("log_titile", "调用转账接口成功");
                                        log.put("log_content", objJson.toString());
                                        log.put("syscode", Constant.CUR_SYS_CODE);
                                        log.put("create_time", DateUtil.getCurrDateTime());
                                        log.put("modify_time", DateUtil.getCurrDateTime());
                                        log.put("log_type", "1");//接口日志类型：1-转账申请2-转账回调
                                        log.put("log_status", "2");//转账日志状态：1-成功 0-失败 2-转账中
                                        interfaceLogDao.insertSelective(log);
                                        
										/*pd.put("userCode", userCode);
										pd.put("subAccno", "010401");//普通会员转出至R8账户
										pd.put("outamnt", bigDecimal);
										pd.put("outdate",DateUtil.getCurrentDate());
										pd.put("cntflag", 0);
										pd.put("targetSystem","R8");
										pd.put("status", 2);//1成功0失败2转出中
										pd.put("createTime", DateUtil.getCurrentDate());
										pd.put("modifyTime", DateUtil.getCurrentDate());
										pd.put("operator", userDetail.getPhone());
										pd.put("order_ids", objJson.getString("orderIds"));
										pd.put("sender", sender);
										pd.put("recipient", recipient);
										accOutdetailService.addAccOutdetail(pd, Constant.VERSION_NO);*/
                                        //更新账户
                                        PageData accData = new PageData();
                                        accData.put("order_ids", objJson.getString("orderIds"));
                                        accData.put("remark1", flowNo);//流水号
                                        accOutdetailService.turnOutUpdate(accData, Constant.VERSION_NO);
										ar.setSuccess(true);
										ar.setMessage("转账申请提交成功");
										return ar;
									}
//									ar.setSuccess(true);
//									ar.setMessage("转账功能还未正式上线");
								}
							}
						
					} catch (Exception e) {
						e.printStackTrace();
						ar.setSuccess(false);
                        ar.setMessage("网络繁忙，请稍候重试！");
					}
		return ar;
	}
	
	@RequestMapping(value="/findout",method=RequestMethod.POST)
	@ResponseBody
	public AjaxResponse findOutList(HttpServletRequest request,Page page){
	    AjaxResponse ar = new AjaxResponse();
	    Map<String,Object> map = new HashMap<String, Object>();
		User user = (User)request.getSession().getAttribute(Constant.LOGIN_USER);
		String userCode="";
        if(user==null){
            userCode =request.getParameter("userCode");
        }else{
            userCode =user.getUserCode();
        }
		List<PageData> outList=null;
		try {
		    PageData pd = new PageData();
			pd=this.getPageData();
			pd.put("userCode", userCode);
			page.setPd(pd);
//			page.setShowCount(1);
			outList = cams.listPageAccOut(page,Constant.VERSION_NO);
			ar.setSuccess(true);
			ar.setMessage("查询成功");
		} catch (Exception e) {
			e.printStackTrace();
			ar.setSuccess(false);
			ar.setMessage("网络繁忙，请稍候重试！");
		}
		map.put("outList", outList);
		map.put("page", page);
		ar.setData(map);
//		ar.setPage(page);
		return ar;
	}
	/*public static void main(String[] args) {
		String turnOut = APIHttpClient.turnOut(null, null,  "sender", "recipient", "10", null,null,null);
		try {
			System.out.println("\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\");
			JSONObject objJson = (JSONObject)JSON.parse(turnOut);
			System.out.println(objJson.getString("status")+"++++++++++++++++++++++++");
			Object object = objJson.getString("data");
			
		System.out.println(objJson.toString()+"+++++++++++==============================");
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}*/
}
