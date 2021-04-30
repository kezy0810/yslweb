package com.qkl.ztysl.web;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.servlet.ModelAndView;

import com.alibaba.dubbo.common.logger.Logger;
import com.alibaba.dubbo.common.logger.LoggerFactory;
import com.qkl.util.help.AjaxResponse;
import com.qkl.util.help.HtmlUtil;
import com.qkl.util.help.UUId;
import com.qkl.util.help.pager.PageData;
import com.qkl.ztysl.api.entity.Page;

public class BaseAction{
    protected Logger logger = LoggerFactory.getLogger(BaseAction.class);
    
	public final static String SUCCESS ="success";  
	
	public final static String MSG ="msg";  
	
	
	public final static String DATA ="data";  
	
	public final static String LOGOUT_FLAG = "logoutFlag";  
	
	protected AjaxResponse ar = new AjaxResponse();
	protected ModelAndView mv = this.getModelAndView();
    protected PageData pd = new PageData();
    protected HttpServletResponse response;
	
   @InitBinder  
   protected void initBinder(WebDataBinder binder) {  
		 binder.registerCustomEditor(Date.class, new CustomDateEditor(
                new SimpleDateFormat("yyyy-MM-dd"), true));  
		 binder.registerCustomEditor(int.class,new MyEditor()); 
   }  
	 
	 /**
	  * 获取IP地址
	  * @param request
	  * @return
	  */
	 public String getIpAddr(HttpServletRequest request) {
		String ip = request.getHeader("x-forwarded-for");
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("Proxy-Client-IP");
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("WL-Proxy-Client-IP");
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getRemoteAddr();
		}
		return ip;
	}

	 
	 /**
	  * 所有ActionMap 统一从这里获取
	  * @return
	  */
	public Map<String,Object> getRootMap(){
		Map<String,Object> rootMap = new HashMap<String, Object>();
		//添加url到 Map中
		//rootMap.putAll(URLUtils.getUrlMap());
		return rootMap;
	}
	
	public ModelAndView forword(String viewName,Map context){
		return new ModelAndView(viewName,context); 
	}
	
	public ModelAndView error(String errMsg){
		return new ModelAndView("error"); 
	}
	
	/**
	 *
	 * 提示成功信息
	 *
	 * @param message
	 *
	 */
	public void sendSuccessMessage(HttpServletResponse response,  String message) {
		Map<String, Object> result = new HashMap<String, Object>();
		result.put(SUCCESS, true);
		result.put(MSG, message);
		HtmlUtil.writerJson(response, result);
	}

	/**
	 *
	 * 提示失败信息
	 *
	 * @param message
	 *
	 */
	public void sendFailureMessage(HttpServletResponse response,String message) {
		Map<String, Object> result = new HashMap<String, Object>();
		result.put(SUCCESS, false);
		result.put(MSG, message);
		HtmlUtil.writerJson(response, result);
	}
	
	/**
     * 得到PageData
     */
    public PageData getPageData(){
        return new PageData(this.getRequest());
    }
    
    /**
     * 得到ModelAndView
     */
    public ModelAndView getModelAndView(){
        return new ModelAndView();
    }
    
    /**
     * 得到request对象
     */
    public HttpServletRequest getRequest() {
        HttpServletRequest request = ((ServletRequestAttributes)RequestContextHolder.getRequestAttributes()).getRequest();
        
        return request;
    }
    /**
     * 设置response对象
     */
    public void setResponse(HttpServletResponse response) {
        this.response = response;
    }
    /**
     * 得到32位的uuid
     * @return
     */
    public String get32UUID(){
        
        return UUId.getUUId();
    }
    
    /**
     * 得到分页列表的信息 
     */
    public Page getPage(){
        
        return new Page();
    }
    
    public static void logBefore(Logger logger, String interfaceName){
        logger.info("");
        logger.info("start");
        logger.info(interfaceName);
    }
    
    public static void logAfter(Logger logger){
        logger.info("end");
        logger.info("");
    }
}
