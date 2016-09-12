package com.qkl.tfcc.controller.User;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.qkl.tfcc.api.common.Constant;
import com.qkl.tfcc.api.entity.Page;
import com.qkl.tfcc.api.po.user.ComTeamVip;
import com.qkl.tfcc.api.po.user.User;
import com.qkl.tfcc.api.service.user.api.ComTeamVipService;
import com.qkl.tfcc.web.BaseAction;
import com.qkl.util.help.AjaxResponse;
import com.qkl.util.help.pager.PageData;



@Controller
@RequestMapping("/service/team")
public class ComTeamVipController extends BaseAction{

	@Autowired
	@Qualifier("vipService")
	private ComTeamVipService vipservice;

     //查询普通会员—我的团队—各类会员的数量
	@RequestMapping(value="/findVipNum",method=RequestMethod.POST)
	@ResponseBody
	public AjaxResponse findVipNum(HttpServletRequest request){
		AjaxResponse ar = new AjaxResponse();
		ComTeamVip count=null;
		try {
			User user = (User)request.getSession().getAttribute(Constant.LOGIN_USER);
			pd=this.getPageData();
			String userCode="10000000001";
			pd.put("userCode", userCode);
			count = vipservice.findcount(userCode);
			ar.setSuccess(true);
			ar.setMessage("查询成功");
		} catch (Exception e) {
			e.printStackTrace();
			ar.setSuccess(false);
			ar.setMessage("查询失败");
		}
		ar.setData(count);
		return ar;
	}
	
	//查询普通会员—我的团队—各类会员列表信息
    @RequestMapping(value="/findVipPage",method=RequestMethod.POST)
    @ResponseBody
	public AjaxResponse findVipPage(HttpServletRequest request,Page page){
    	AjaxResponse ar = new AjaxResponse();    	
    	Map<String,Object> map = new HashMap<String, Object>();
		try {
			pd = this.getPageData();
			User user = (User)request.getSession().getAttribute(Constant.LOGIN_USER);
	//		String querycontdiction1 = request.getParameter("querycontdiction1");//获取下拉框对应的输入的值
	//		String querycontdiction2 = request.getParameter("querycontdiction2");//获取下拉框对应的输入的值
			String currentPage = request.getParameter("currentPage");
			String relaleveltmp = request.getParameter("str"); //ALL,|A,B,|A,C,|B,C,
			String relalevel="";
			if(relaleveltmp.equals("ALL")||"".equals(relaleveltmp)||relaleveltmp==null){
				relalevel="'A','B','C'";
			}else{
				 relalevel = relaleveltmp.substring(0,(relaleveltmp.length()-1));
			}
			String userCode="10000000001";
			pd.put("userCode", userCode);
			pd.put("relalevel", relalevel);
			page.setPd(pd);
			/*pd.put("relaLevel", relalevel);*/			
			/*pd.put("querycontdiction1", querycontdiction1);
			pd.put("querycontdiction2", querycontdiction2);
			page.setPd(pd);*/
			System.out.println(relalevel+"----------------------------");
			List<PageData> tviplist = vipservice.findVipList(page);
			map.put("tviplist", tviplist);
			map.put("page", page);
			ar.setSuccess(true);
			ar.setMessage("查询成功");
			ar.setData(map);
		} catch (Exception e) {
			e.printStackTrace();
			ar.setSuccess(false);
			ar.setMessage("查询失败");
		}
		return ar;
		
	}
    
    
	
}