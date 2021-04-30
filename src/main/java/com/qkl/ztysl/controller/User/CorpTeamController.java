package com.qkl.ztysl.controller.User;

import java.util.List;

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
import com.qkl.ztysl.api.service.user.api.CorpTeamService;
import com.qkl.ztysl.web.BaseAction;


@Controller
@RequestMapping("/service/corp")
public class CorpTeamController extends BaseAction {
	
	@Autowired
	private CorpTeamService corpTeamService;
	
	
	@RequestMapping(value="/lpnum",method=RequestMethod.POST)
	@ResponseBody
	public AjaxResponse findLpNum(HttpServletRequest request){
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
			lpNum = corpTeamService.findNum(userCode);
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
	
	@RequestMapping(value="/lpInfo",method=RequestMethod.POST)
//	@RequestMapping(value="/lpInfo",method=RequestMethod.GET)
	@ResponseBody
	public AjaxResponse findLplist(HttpServletRequest request,Page page){
		AjaxResponse ar = new AjaxResponse();
		User user = (User)request.getSession().getAttribute(Constant.LOGIN_USER);
		String userCode="";
        if(user==null){
            userCode =request.getParameter("userCode");
        }else{
            userCode =user.getUserCode();
        }
		//String userCode="10000000001";
        PageData pd = new PageData();
		pd = this.getPageData();
		pd.put("userCode", userCode);
		//pd.put("userCode", userCode);
		page.setPd(pd);
		List<PageData> list = corpTeamService.findlplist(page);
		ar.setData(list);
		return ar;
	}
	

}
