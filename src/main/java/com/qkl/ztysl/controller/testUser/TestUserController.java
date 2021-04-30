package com.qkl.ztysl.controller.testUser;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.qkl.util.help.AjaxResponse;
import com.qkl.util.help.pager.PageData;
import com.qkl.ztysl.api.entity.Page;
import com.qkl.ztysl.api.po.TestUser;
import com.qkl.ztysl.api.service.testUser.api.TestUserService;
import com.qkl.ztysl.web.BaseAction;

/**
 * 测试用户的控制类
 * <p>Description： 测试用户的控制类 </p>
 * @project_Name qkl_ymd_web
 * @class_Name TestUserController.java
 * @author kezhiyi
 * @date 2016年8月13日
 * @version v
 */
@Controller
@RequestMapping("/service/test")
public class TestUserController  extends BaseAction{

	@Autowired
	private TestUserService testUserService;
	
	
	/**
	 * 显示测试用户的信息
	 * <p> (根据测试用户的Id获取测试用户的信息)  </p>
	 * @Title: show 
	 * @return Map json格式的
	 * @create author kezhiyi
	 * @create date 2016年8月13日
	 */
//	@RequestMapping(value="/show", method=RequestMethod.POST)
//	@ResponseBody
//	public Map<String,Object> show(HttpServletRequest request,HttpServletResponse response){
//		
//		Map<String,Object> map = new HashMap<String, Object>();
//		long testUserId =  Long.parseLong(request.getParameter("email")) ;
//		System.out.println( "*****post testUserId is  "+testUserId);
//		TestUser user = testUserService.queryTestUserByUserId(1);
//		System.out.println( "aaaaa  map value is  "+user.toString());
//		map.put("userId", user.getTest_user_id());
//		map.put("name", user.getName());  
//		System.out.println( "aaaaaaa  map value is  "+map.toString());
//		return map;
//	}
	
	@RequestMapping(value="/show", method=RequestMethod.POST)
	@ResponseBody
	public AjaxResponse show(HttpServletRequest request,HttpServletResponse response){
		
		AjaxResponse ar = new AjaxResponse();
		long testUserId =  Long.parseLong(request.getParameter("email")) ;
		System.out.println( "*******post testUserId is  "+testUserId);
		TestUser user = testUserService.queryTestUserByUserId(1);
		System.out.println( "aaaaa1112222  map value is  "+user.toString());

		Map<String,Object> map = new HashMap<String, Object>();
		map.put("userId", user.getTest_user_id());
		map.put("name", user.getName());		
		ar.setSuccess(true);
		ar.setMessage("查询成功！");
		ar.setData(map);
		return ar;
	}
	
	/**
	 * @param request
	 * @param response
	 * @param page
	 * @return
	 */
	@RequestMapping(value="/queryuser", method=RequestMethod.POST)
	@ResponseBody
	public AjaxResponse querylist(HttpServletRequest request,HttpServletResponse response, Page page){
		
		AjaxResponse ar = new AjaxResponse();
//		long testUserId =  Long.parseLong(request.getParameter("email")) ;
//		System.out.println( "******queryuser  "+testUserId);
//		long testUserId =  Long.parseLong(request.getParameter("email")) ;
//		System.out.println( "******queryuser  "+testUserId);
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
		return ar;
	}
	
	/**
	 * 显示测试用户的信息restful
	 * <p> (根据测试用户的Id获取测试用户的信息)  </p>
	 * @Title: showe
	 * @return Map json格式的
	 * @create author kezhiyi
	 * @create date 2016年8月13日
	 */
//	@RequestMapping(value="/showe/{userid}", method=RequestMethod.GET)
//	@ResponseBody
//	public Map<String,Object> showE(@PathVariable String userid){
//		Map<String,Object> map = new HashMap<String, Object>();
//		long testUserId =  Long.parseLong(userid) ;
//		System.out.println( "testUserId is  "+testUserId);
//		TestUser user = testUserService.queryTestUserByUserId(1);
//		System.out.println( "aaaaa  map value is  "+user.toString());
//		map.put("userId", user.getTest_user_id());
//		map.put("name", user.getName());
//		System.out.println( "aaaaa  map value is  "+map.toString());		
//		return map;
//	}
	
	
	@RequestMapping(value="/showe/{userid}", method=RequestMethod.GET)
	@ResponseBody
	public AjaxResponse showE(@PathVariable String userid){
		AjaxResponse ar = new AjaxResponse();
		Map<String,Object> map = new HashMap<String, Object>();
		long testUserId =  Long.parseLong(userid) ;
		System.out.println( "testUserId is  "+testUserId);
		TestUser user = testUserService.queryTestUserByUserId(1);
		System.out.println( "aaaaa  map value is  "+user.toString());
		map.put("userId", user.getTest_user_id());
		map.put("name", user.getName());
		System.out.println( "aaaaa  map value is  "+map.toString());		
		ar.setSuccess(true);
		ar.setMessage("查询成功！");
		ar.setData(map);
		return ar;
	}
	
}
