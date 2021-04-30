package com.qkl.ztysl.sms;

import java.io.IOException;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.methods.PostMethod;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;

import com.qkl.util.help.HttpTool;
import com.qkl.util.help.MD5Util;

public class SmsSend {

	
	private static String Url = "http://106.ihuyi.cn/webservice/sms.php?method=Submit";
	
	private static String account="cf_dingyao";
	
	private static String password="654321";
	
	//发送验证码
	public static  String sendSms(String phone) {
		
		HttpClient client = new HttpClient(); 
		PostMethod method = new PostMethod(Url); 
			
		//client.getParams().setContentCharset("GBK");		
		client.getParams().setContentCharset("UTF-8");
		method.setRequestHeader("ContentType","application/x-www-form-urlencoded;charset=UTF-8");

		
		int mobile_code = (int)((Math.random()*9+1)*100000);

		//System.out.println(mobile);
		
	    String content = new String("您的验证码是：【" + mobile_code + "】,30分钟内有效，如非本人操作，请忽略本短信。");

		NameValuePair[] data = {//提交短信
			    new NameValuePair("account", account), 
//			    new NameValuePair("password", "密码"), //密码可以使用明文密码或使用32位MD5加密
			    new NameValuePair("password",  MD5Util.getMd5Code(password)),
			    new NameValuePair("mobile", phone), 
			    new NameValuePair("content", content),
		};
		
		method.setRequestBody(data);		
		
		
		try {
			client.executeMethod(method);	
			
			String SubmitResult =method.getResponseBodyAsString();
					
			//System.out.println(SubmitResult);

			Document doc = DocumentHelper.parseText(SubmitResult); 
			Element root = doc.getRootElement();


			String code = root.elementText("code");	
			String msg = root.elementText("msg");	
			String smsid = root.elementText("smsid");	
			
			
			System.out.println(code);
			System.out.println(msg);
			System.out.println(smsid);
						
			 if("2".equals(code)){
				System.out.println("短信提交成功");
				return String.valueOf(mobile_code);
			}else{
				return "0";
			}
			
		} catch (HttpException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return "0";
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return "0";
		} catch (DocumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return "0";
		}	
		
	}
	//发送短信
	public static boolean sendSms(String phone,String content) {
	    NameValuePair[] data = PackMobileParas(phone, content);// 封装手机参数
        return HttpTool.sendData(Url,data);
    }
	// 封装手机参数
	public static NameValuePair[] PackMobileParas(String phone, String content){
	    System.out.println("发送短信参数：phone="+phone+",content="+content);
	    NameValuePair[] data = {//提交短信
                new NameValuePair("account", account), 
//              new NameValuePair("password", "密码"), //密码可以使用明文密码或使用32位MD5加密
                new NameValuePair("password",  MD5Util.getMd5Code(password)),
                new NameValuePair("mobile", phone), 
                new NameValuePair("content", content),
        };
	    return data;
	}
	public static void main(String [] args) {
		
//		String vcode =sendSms("13522737092");
	    String content = new String("尊敬的用户您好"); 
        SmsSend.sendSms("18618382548", content);
	}
	
	
}
