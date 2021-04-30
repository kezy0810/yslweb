package com.qkl.ztysl.controller.thirdparty;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.net.URLDecoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringEscapeUtils;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.alibaba.dubbo.common.logger.Logger;
import com.alibaba.dubbo.common.logger.LoggerFactory;
import com.qkl.util.help.AjaxResponse;
import com.qkl.util.help.DateUtil;
import com.qkl.util.help.FileUtil;
import com.qkl.util.help.ImgUtil;
import com.qkl.util.help.StringUtil;
import com.qkl.util.help.UUId;
import com.qkl.util.help.pager.PageData;
import com.qkl.util.help.sql.SqlValid;
import com.qkl.ztysl.api.common.Constant;
import com.qkl.ztysl.api.entity.Page;
import com.qkl.ztysl.api.po.sys.SysGencode;
import com.qkl.ztysl.api.po.sys.SysOauth;
import com.qkl.ztysl.api.po.user.User;
import com.qkl.ztysl.api.po.user.UserDetail;
import com.qkl.ztysl.api.service.sys.api.SysGenCodeService;
import com.qkl.ztysl.api.service.sys.api.SysMaxnumService;
import com.qkl.ztysl.utilEx.JedisManager;
import com.qkl.ztysl.vdcode.LianZhongVdcode;
import com.qkl.ztysl.web.BaseAction;

/**
 * 任务controller
 * <p>Description：任务 </p>
 * @project_Name qkl_ymd_web
 * @class_Name ThirdPartyController.java
 * @author kezhiyi
 * @date 2020年10月11日
 * @version v
 */
@Controller
@RequestMapping("/service/thirdparty")
public class ThirdPartyController extends BaseAction{
    
    private final Logger logger = LoggerFactory.getLogger(ThirdPartyController.class);
    
    
    private  List<SysGencode> tSysGencodeAll = new  ArrayList<SysGencode>();
 

//	@Resource(name = "redisDaoImpl")
//	private RedisDao redisDaoImpl;

	@Autowired
	private JedisManager jedisManager;
	
//	@Autowired
//	private TaskService taskService;
    
    @RequestMapping(value="/upload" ,method=RequestMethod.POST)
    @ResponseBody
    public AjaxResponse upload(HttpServletRequest request,HttpServletResponse response,
            @RequestParam(value="tp",required=false) MultipartFile tp){
         // 判断文件是否为空  
//        logBefore(logger, "上传截图");
        response.setCharacterEncoding("UTF-8");
        response.setContentType("text/html; charset=UTF-8");
        response.setHeader("Content-Type", "text/html; charset=UTF-8");

        String imeiid  =request.getParameter("imeiid");          
        String userName  =request.getParameter("userName");   
        String filename  =request.getParameter("filename"); 
        String passWord  =request.getParameter("passWord");          
    	logger.info("recieve imeiid:"+imeiid+"dama requeire,userName:"+userName+",time:"+DateUtil.getCurrDateTime());

        
     
        
        Map<String, String> tMap = new HashMap<String, String>();
        tMap.put("user_name", userName);
        tMap.put("user_pw", passWord);
        tMap.put("yzm_minlen", "0");
        tMap.put("yzm_maxlen", "0");
        tMap.put("yzmtype_mark", "1318");
        tMap.put("token", "d0a21e2d0b46cce5fcc57e4308dc6590");
        
        PageData pdfile = this.getPageData();
        pdfile.put("userName", userName);
        pdfile.put("passWord", passWord);
        
        

        Map<String,String> map = new HashMap<String, String>();
        map.put("jpg","jpg");
        map.put("png","png");
        map.put("jpeg","jpeg");
        map.put("png","png");
        map.put("gif","gif");
        map.put("bmp","bmp");
        try {
            String resources_base = "";
            String resources_local = "";
            String resources_backup = "";
            
            resources_base="http://47.104.92.159:8066/";
            resources_local="/data/ymd/pic/";
            resources_backup="111";
            if("".equals(resources_base)||"".equals(resources_local)||"".equals(resources_backup)){
                logger.info("RESOURCES_BASE or RESOURCES_LOCAL or RESOURCES_BACKUP Error:value may be null");
                ar.setSuccess(false);
                ar.setMessage("服务器路径有误，请联系客服！");
                return ar;
            }
            int lastindex = filename.lastIndexOf(".");
            String imgtype = filename.substring(lastindex+1,filename.length());
            System.out.println("----------imgtype:"+imgtype);
            if(!map.containsKey(imgtype)){
                ar.setSuccess(false);
                ar.setMessage("请上传jpg、png、jpeg格式的图片!");
                return ar;
            }
            
//            long currentTimeMillis = System.currentTimeMillis();
            String todayStr =DateUtil.getDays();
            String new_img_name= userName+"_"+imeiid+"_"+get32UUID()+"."+imgtype;
            String http_img_url = resources_base+Constant.LIANZHONG_HEAD_PATH+todayStr+"/"+new_img_name;
            String temp_img_url = resources_local+Constant.LIANZHONG_TEMP_PATH;//临时图片路径
            String server_img_url = resources_local+Constant.LIANZHONG_HEAD_PATH+todayStr+"/";//图片保存服务器路径
            String backup_img_url = resources_backup+Constant.LIANZHONG_HEAD_PATH+todayStr+"/";//图片磁盘备份路径
            
            
            System.out.println("-----------temp_img_url----------------->>>>>>>>>>>>>>>>>"+temp_img_url);
            System.out.println("-----------server_img_url----------------->>>>>>>>>>>>>>>>>"+server_img_url);
            System.out.println("-----------http_img_url------------------->>>>>>>>>>>>>>>>>"+http_img_url);
            System.out.println("-----------backup_img_url----------------->>>>>>>>>>>>>>>>>"+backup_img_url);
            
            FileUtil.copyFile(tp.getInputStream(), temp_img_url,new_img_name);
            Map<String, Long> imgInfo = ImgUtil.getImgInfo(temp_img_url+new_img_name);
//            Long w = imgInfo.get("w");
//            Long h = imgInfo.get("h");
////            Long s = imgInfo.get("s");
//            if(w != Constant.PIC_HEAD_WIDTH && h != Constant.PIC_HEAD_HEIGHT){
//                FileUtil.deleteFile(temp_img_url+new_img_name);
//                out.print("<script>top.alert(\"请上传"+Constant.PIC_HEAD_WIDTH+"*"+Constant.PIC_HEAD_WIDTH+"规格的图片！\");</script>");
//                return;
//            }
            FileUtil.copyFile(new FileInputStream(temp_img_url+new_img_name), server_img_url,new_img_name);
//            FileUtil.copyFile(new FileInputStream(temp_img_url+new_img_name), backup_img_url,new_img_name);
            FileUtil.deleteFile(temp_img_url+new_img_name);
            pdfile.put("imgAddress", http_img_url);

            String lzvdcodeRs =LianZhongVdcode.validateCode(tMap, server_img_url+new_img_name);
            if(lzvdcodeRs.equals("")) {
            	  ar.setSuccess(false);
                  ar.setMessage("系统异常，联众打码失败！");
                  return ar;
            }
            JSONObject jsonObj = new JSONObject(lzvdcodeRs);
            boolean vdResult =(boolean) jsonObj.get("result");
            String vdData =jsonObj.get("data").toString();
            
            if(!vdResult) {
            	  ar.setSuccess(false);
                  ar.setMessage(StringEscapeUtils.unescapeJava(vdData));
                  ar.setData("");
                  return ar;
            }      
            
            ar.setSuccess(true);
            ar.setData(vdData);
            ar.setMessage("图片验证完成!");
            return ar;
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(e.toString(), e);
//            out.print("<script>parent.alert('系统异常，上传失败！');</script>");
            ar.setSuccess(false);
            ar.setMessage("系统异常，上传失败！");
            return ar;
        }finally{
//            out.print("<script>parent.$(\"input[type='file']\").val('');</script>");
//            out.close();
//            logAfter(logger);
        }
        
    }
    
 
    

   
}
