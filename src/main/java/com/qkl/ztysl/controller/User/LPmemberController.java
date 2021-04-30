package com.qkl.ztysl.controller.User;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.qkl.util.help.AjaxResponse;
import com.qkl.util.help.pager.PageData;
import com.qkl.ztysl.api.common.Constant;
import com.qkl.ztysl.api.entity.Page;
import com.qkl.ztysl.api.po.user.User;
import com.qkl.ztysl.api.service.user.api.LPmemberService;
import com.qkl.ztysl.web.BaseAction;


@Controller
@RequestMapping("/service/lp")
public class LPmemberController extends BaseAction {
	
	@Autowired
	private LPmemberService lpService;
	
	
	
	@RequestMapping(value="/getnum",method=RequestMethod.POST)
	@ResponseBody
	public AjaxResponse findLPNum(HttpServletRequest request){
		AjaxResponse ar = new AjaxResponse();
		long lpNum=0;
		try {
			User user = (User)request.getSession().getAttribute(Constant.LOGIN_USER);
			String userCode="";
            if(user==null){
                userCode =request.getParameter("userCode");
            }else{
                userCode =user.getUserCode();
            }
			lpNum = lpService.findLPmemberNum(userCode);
			ar.setSuccess(true);
			ar.setMessage("查询成功");
		} catch (Exception e) {
			e.printStackTrace();
			ar.setSuccess(false);
			ar.setMessage("网络繁忙，请稍候重试！");
		}
		ar.setData(lpNum);
		return ar;
	}
	
	
	@RequestMapping(value="/lpinfo",method=RequestMethod.POST)
	@ResponseBody
	public AjaxResponse findLPlist(HttpServletRequest request,Page page){
		AjaxResponse ar = new AjaxResponse();
		Map<String,Object> map = new HashMap<String, Object>();
		List<PageData> lpmemberInfo=null;
		try {
			User user = (User)request.getSession().getAttribute(Constant.LOGIN_USER);
			String userCode="";
            if(user==null){
                userCode =request.getParameter("userCode");
            }else{
                userCode =user.getUserCode();
            }
			String userName = request.getParameter("userName");
			PageData pd = new PageData();
			pd=this.getPageData();
			map.put("userCode", userCode);
			//pd.put("userCode", user.getUserCode());
			map.put("userName", userName);
			
			page.setPd(pd);
	        lpmemberInfo = lpService.findLPmemberInfo(page);
	        map.put("page", page);
	        map.put("lplist", lpmemberInfo);
			ar.setSuccess(true);
			ar.setMessage("查询成功");
			
		} catch (Exception e) {
			ar.setSuccess(false);
			ar.setMessage("网络繁忙，请稍候重试！");
			e.printStackTrace();
		}
		return ar;
	}

}
