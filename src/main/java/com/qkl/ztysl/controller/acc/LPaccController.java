package com.qkl.ztysl.controller.acc;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.qkl.util.help.AjaxResponse;
import com.qkl.util.help.pager.PageData;
import com.qkl.ztysl.api.common.Constant;
import com.qkl.ztysl.api.po.user.User;
import com.qkl.ztysl.api.service.acc.api.LPaccService;
import com.qkl.ztysl.web.BaseAction;


@Controller
@RequestMapping("/service/lpacc")
public class LPaccController extends BaseAction {
	
	@Autowired
	private LPaccService lpService;
	
	@RequestMapping(value="/getnum",method=RequestMethod.POST)
	@ResponseBody
	public AjaxResponse findymdNum(HttpServletRequest request){
		AjaxResponse ar = new AjaxResponse();
		try {
			User user = (User)request.getSession().getAttribute(Constant.LOGIN_USER);
			String userCode="";
	        if(user==null){
	            userCode =request.getParameter("userCode");
	        }else{
	            userCode =user.getUserCode();
	        }
	        PageData pd = new PageData();
			pd = this.getPageData();
			long Balance = lpService.findLPBalance(userCode);
			pd.put("Balance", Balance);
			ar.setData(pd);
			ar.setSuccess(true);
			ar.setMessage("查询成功");
		} catch (Exception e) {
			e.printStackTrace();
			ar.setSuccess(false);
			ar.setMessage("网络繁忙，请稍候重试！");
		}
		return ar;
	}
	
	
	/*@RequestMapping(value="/getlist",method=RequestMethod.POST)
	@ResponseBody
	public AjaxResponse findReward(HttpServletRequest request,Page page){
		AjaxResponse ar = new AjaxResponse();
		List<PageData> RewardInfo=null;
		try {
			User user = (User)request.getSession().getAttribute(Constant.LOGIN_USER);
			PageData pd = new PageData();
			pd=this.getPageData();
			pd.put("userCode", user.getUserCode());
			page.setPd(pd);
			RewardInfo = lpService.findRewardInfo(page);
		    ar.setSuccess(true);
		    ar.setMessage("查询成功");
		} catch (Exception e) {
			e.printStackTrace();
			ar.setSuccess(false);
		    ar.setMessage("网络繁忙，请稍候重试！");
		}
		ar.setData(RewardInfo);
		return ar;
	}
*/
}
