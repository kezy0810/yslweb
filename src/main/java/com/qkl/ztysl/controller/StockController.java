package com.qkl.ztysl.controller;

import java.math.BigDecimal;
import java.util.HashMap;
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
import com.qkl.util.help.AjaxResponse;
import com.qkl.util.help.DateUtil;
import com.qkl.util.help.pager.PageData;
import com.qkl.ztysl.api.common.Constant;
import com.qkl.ztysl.api.entity.Page;
import com.qkl.ztysl.api.po.acc.BankAccInfo;
import com.qkl.ztysl.api.po.user.User;
import com.qkl.ztysl.api.service.acc.api.BankAccService;
import com.qkl.ztysl.api.service.sys.api.SysGenCodeService;
import com.qkl.ztysl.api.service.trade.api.TradeService;
import com.qkl.ztysl.web.BaseAction;


@Controller
@RequestMapping("/service/stock")
public class StockController extends BaseAction {

	private final Logger logger = LoggerFactory.getLogger(StockController.class);
	
	private BankAccInfo bankAccInfo = new BankAccInfo();
	
	private BigDecimal stockprice=new BigDecimal("1");
	
	@Autowired
	private SysGenCodeService sysGenCodeService;
	@Autowired
	private TradeService tradeService;
	@Autowired
	private BankAccService bankAccService;
	
	
	
	@RequestMapping(value="/stockhome", method=RequestMethod.POST)
	@ResponseBody
	public AjaxResponse stockhome(HttpServletRequest request,HttpServletResponse response ,Page page){
		AjaxResponse ar = new AjaxResponse();
		
		try {
			PageData pdfile = this.getPageData();
			bankAccInfo = bankAccService.findBankAccInfo("", Constant.VERSION_NO);

			Map<String,Object> map = new HashMap<String, Object>();
			map.put("bankAccInfo", bankAccInfo);	
			String tmpprice ="1";
			List<Map<String,Object>> tSysGencodeList =sysGenCodeService.findByGroupCode("PRICE", Constant.VERSION_NO);
			 for(Map<String,Object> mapObj:tSysGencodeList){
	                if("PRICE".equals(mapObj.get("codeName"))){
	                	tmpprice = mapObj.get("codeValue").toString();
	                	break;
	                }	               
	            }
			 if(stockprice==null||stockprice.equals("null")||stockprice.equals("")){
				 map.put("price", 1);
			 }else{
				 try{					 
					 BigDecimal stockprice=new BigDecimal(tmpprice); 
					 stockprice.setScale(2, BigDecimal.ROUND_HALF_UP);//四舍五入
					 map.put("price", stockprice);
				 }catch(Exception e){
					 logger.info("translation stringtoBigDecimal fail,"+e.getMessage());
					 map.put("price", 1);
				 }
			 }
			    ar.setData(map);
				ar.setSuccess(true);
				ar.setMessage("查询收款账号信息成功！");
				return ar;
			
		} catch (Exception e) {
			e.printStackTrace();
			ar.setSuccess(false);
			ar.setMessage("网络繁忙，请稍候重试！");
		}	
		
		return ar;
	}
	
	@RequestMapping(value="/addstock", method=RequestMethod.POST)
	@ResponseBody
	public AjaxResponse addstock(HttpServletRequest request,HttpServletResponse response ,Page page){
		AjaxResponse ar = new AjaxResponse();
		
		try {
		    PageData pd = new PageData();
			 pd = this.getPageData();
			if(bankAccInfo==null){
				bankAccInfo = bankAccService.findBankAccInfo("", Constant.VERSION_NO);
			}
			User user = (User)request.getSession().getAttribute(Constant.LOGIN_USER);
			String userCode="";
            if(user==null){
                userCode =request.getParameter("userCode");
            }else{
                userCode =user.getUserCode();
            }
			Map<String,Object> map = new HashMap<String, Object>();
			String txamntstr = pd.getString("txamnt");
			String txnumstr = pd.getString("txnum");
			BigDecimal txamnt = new BigDecimal("1");
			BigDecimal txnum = new BigDecimal("1");
			try{
				 txamnt = new BigDecimal(txamntstr);
				 txnum = new BigDecimal(txnumstr);				
			}catch(Exception e){
				logger.error("购买数量或金额格式不正确！"+e.getMessage());
				ar.setSuccess(false);
				ar.setMessage("购买数量和金额不匹配！");
				return ar;
			}
			if(txamnt!=txnum.multiply(stockprice)){				
				ar.setSuccess(false);
				ar.setMessage("购买数量和金额不匹配！");
				return ar;
			}
			
			pd.put("bankAccInfo", bankAccInfo);		
			pd.put("txtype", "1");//买入
			pd.put("cuyType", "1");//购买默认ymd		
			pd.put("txamnt", txamnt);
			pd.put("txnum", txnum);
			pd.put("txdate", DateUtil.getCurrentDate());
			pd.put("sourceSystem", Constant.CUR_SYS_CODE);
			pd.put("cntflag", "");
			pd.put("caldate", null);
			pd.put("status", "1");//需要审核
			pd.put("userCode", userCode);
			pd.put("createTime", DateUtil.getCurrentDate());
			pd.put("modifyTime", DateUtil.getCurrentDate());
			pd.put("operator","sys");
			
			if(tradeService.addTradeDetail(pd, Constant.VERSION_NO)){
				ar.setSuccess(false);
				ar.setMessage("购买失败，请稍后重试！");
				return ar;
			}
			
			
			
			
			
			ar.setData(map);
			ar.setSuccess(true);
			ar.setMessage("提交成功！");
			return ar;
			
		} catch (Exception e) {
			e.printStackTrace();
			ar.setSuccess(false);
			ar.setMessage("网络繁忙，请稍候重试！");
		}	
		
		return ar;
	}
	
	
	
	
	
	
	
	
}
