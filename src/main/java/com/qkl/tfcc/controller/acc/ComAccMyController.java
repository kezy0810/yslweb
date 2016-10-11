package com.qkl.tfcc.controller.acc;
import java.math.BigDecimal;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.dubbo.common.json.JSON;
import com.alibaba.dubbo.common.json.JSONArray;
import com.alibaba.dubbo.common.json.JSONObject;
import com.alibaba.dubbo.common.json.ParseException;
import com.qkl.tfcc.api.common.Constant;
import com.qkl.tfcc.api.entity.Page;
import com.qkl.tfcc.api.po.acc.AccDetail;
import com.qkl.tfcc.api.po.acc.ComAccMy;
import com.qkl.tfcc.api.po.user.User;
import com.qkl.tfcc.api.service.acc.api.AccOutdetailService;
import com.qkl.tfcc.api.service.acc.api.AccService;
import com.qkl.tfcc.api.service.acc.api.ComAccMyService;
import com.qkl.tfcc.api.service.sys.api.SysGenCodeService;
import com.qkl.tfcc.provider.dao.AccDao;
import com.qkl.tfcc.web.BaseAction;
import com.qkl.util.help.APIHttpClient;
import com.qkl.util.help.AjaxResponse;
import com.qkl.util.help.DateUtil;
import com.qkl.util.help.StringUtil;
import com.qkl.util.help.pager.PageData;




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
	private AccDao accDao;
	
	
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
			ar.setMessage("查询失败");
		}
		ar.setData(nums);
		return ar;	
	}
	@RequestMapping(value="/getAmnt",method=RequestMethod.POST)
	@ResponseBody
	public  AjaxResponse getAmnt(HttpServletRequest request){
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
	        ar.setMessage("系统异常，请联系客服！");
	    }
	    return ar;	
	}

	@RequestMapping(value="/acccompare",method=RequestMethod.POST)
	@ResponseBody
	public AjaxResponse findAccOut(HttpServletRequest request){//比较转账数额的大小
		User user = (User)request.getSession().getAttribute(Constant.LOGIN_USER);
		String userCode="";
        if(user==null){
            userCode =request.getParameter("userCode");
        }else{
            userCode =user.getUserCode();
        }
		pd=this.getPageData();
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
										return ar;
									}
									
									//调用转账接口
									String turnOut =APIHttpClient.turnOut(url, null, sender, recipient, money, pri, salt, admin_user);
									JSONObject objJson = (JSONObject)JSON.parse(turnOut);
									String status = objJson.getString("status");
									if ("failed".equals(status)) {
										ar.setSuccess(false);
										ar.setMessage("转账失败");
										return ar;
									}if ("success".equals(status)) {
									    logger.info("调用转账接口成功---------success----------");
									    pd.put("userCode", userCode);
									    //更新账户表，转出冻结
									    boolean transferResult = accDao.transfering(pd);
									    logger.info("调用转账接口---------更新账户表，转出冻结-----------结果--transferResult="+transferResult);
										String order_ids = objJson.getString("orderIds");
										//AccDetail accDetail = new AccDetail();
										//accDetail.setUserCode(userCode);
										//AccDetail detail = accService.findAccDetail(accDetail, Constant.VERSION_NO);
										
										pd.put("subAccno", "010401");//普通会员转出至R8账户
										pd.put("outamnt", bigDecimal);
										pd.put("outdate",DateUtil.getCurrentDate());
										pd.put("cntflag", 0);
										pd.put("targetSystem","R8");
										pd.put("status", 2);//1成功0失败2转出中
										pd.put("createTime", DateUtil.getCurrentDate());
										pd.put("modifyTime", DateUtil.getCurrentDate());
										pd.put("operator", user.getPhone());
										pd.put("order_ids", order_ids);
									//	int num = cams.saveOutAcc(pd);
										boolean outdetail = accOutdetailService.addAccOutdetail(pd, Constant.VERSION_NO);
										logger.info("调用转账接口---------添加转出记录结果-----------outdetail="+outdetail);
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
					}
		return ar;
	}
	/*@RequestMapping(value="/acccompare",method=RequestMethod.POST)
	@ResponseBody
	public AjaxResponse findAccOut(HttpServletRequest request){//比较转账数额的大小
		User user = (User)request.getSession().getAttribute(Constant.LOGIN_USER);
		pd=this.getPageData();
		Map<String, Object> findNum = cams.findNum(user.getUserCode());
		Iterator<Entry<String, Object>> iterator = findNum.entrySet().iterator();
		while (iterator.hasNext()) {
			Entry<String, Object> entry = iterator.next();
			String key = entry.getKey();
			if (key=="findTTReward"||"findTTReward".equals(key)) {
			 //BigDecimal value = (BigDecimal) entry.getValue();
				String string = entry.getValue().toString();
				BigDecimal bigDecimal2 = new BigDecimal(string);
				String money = pd.get("money").toString();
				if (money!=null&&"".equals(money)) {
					BigDecimal bigDecimal = new BigDecimal(money);
					int compareTo = bigDecimal.compareTo(bigDecimal2);
					if (compareTo==1) {
						ar.setMessage("您的可用余额不足");
						ar.setSuccess(true);
					}
				}
				
				
				
			}
		}
		return ar;
	}
	*/
	
	
	/*@RequestMapping(value="/fall",method=RequestMethod.POST)
	@ResponseBody
	public AjaxResponse findAll(HttpServletRequest request,Page page){
		AjaxResponse ar = new AjaxResponse();
		List<PageData> findAll=null;
		try {
			
			
			findAll = cams.findAll(page);
			ar.setSuccess(true);
			ar.setMessage("查询成功");
		} catch (Exception e) {
			e.printStackTrace();
			ar.setSuccess(false);
			ar.setMessage("查询失败");
		}
		ar.setData(findAll);
		return ar;
	}*/
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
