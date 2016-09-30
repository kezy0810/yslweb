package com.qkl.tfcc.controller.acc;
import java.math.BigDecimal;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.qkl.tfcc.api.common.Constant;
import com.qkl.tfcc.api.entity.Page;
import com.qkl.tfcc.api.po.acc.ComAccMy;
import com.qkl.tfcc.api.po.user.User;
import com.qkl.tfcc.api.service.acc.api.ComAccMyService;
import com.qkl.tfcc.web.BaseAction;
import com.qkl.util.help.AjaxResponse;
import com.qkl.util.help.pager.PageData;




@Controller
@RequestMapping("/service/comacc")
public class ComAccMyController extends BaseAction {

	@Autowired
	private ComAccMyService cams;
	
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
			nums = cams.findMyAcc(userCode);
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
		Iterator<Entry<String, Object>> iterator = findNum.entrySet().iterator();
		String money = pd.getString("money");//获取输入的SAN数量
		while (iterator.hasNext()) {
			Entry<String, Object> entry = iterator.next();
			String key = entry.getKey();
			if (key!=null&&"avb_amnt".equals(key)) {
			 //BigDecimal value = (BigDecimal) entry.getValue();
				String string = entry.getValue().toString();//获取可用余额
					try {
						BigDecimal	bigDecimal2 = new BigDecimal(string);//转换成BigDecimal类型
							if (money!=null&&!"".equals(money)) {
								BigDecimal bigDecimal = new BigDecimal(money);
								int compareTo = bigDecimal.compareTo(bigDecimal2);//要转账数量和可用数量比较大小
								if (compareTo==1) {//大于
									ar.setSuccess(true);
									ar.setMessage("您的可用余额不足");
								}
								if (compareTo==0||compareTo==-1) {//等于或者小于
									ar.setSuccess(true);
									ar.setMessage("转账功能还未正式上线");
								}
							}
						
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
						
			}
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
}
