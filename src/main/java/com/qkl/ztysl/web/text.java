package com.qkl.ztysl.web;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.UnsupportedEncodingException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

import org.apache.commons.lang.StringEscapeUtils;
import org.json.JSONObject;

import com.qkl.util.help.StringUtil;
import com.qkl.util.help.json.JSONUtil;
import com.qkl.ztysl.utilEx.JedisClient;
import com.qkl.ztysl.utilEx.JedisManager;

public class text {

	
	public static void main(String args[]) { 
      
//		 String string = "2015-02-10 22:00:00";
//	        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//	        Date d1;
//	        try {
//	            d1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(string);
//	            System.out.println("DateTime d1>>>>>>: " + d1);
//	            String d2 = format.format(d1);
//	            System.out.println("DateTime d2>>>>>>: " + d2);
//	            Date d3;
//	            d3 = format.parse(d2);
//	            System.out.println("DateTime d3>>>>>>: " + format.format(d3));
//	        } catch (ParseException e1) {
//	            // TODO Auto-generated catch block
//	            e1.printStackTrace();
//	        }
//		String str ="{\"result\":false,\"data\":\"\\u56fe\\u7247\\u9ad8\\u5ea6\\u9700\\u5c0f\\u4e8e1200\\u50cf\\u7d20\"}";
		String str ="{\"result\":true,\"data\":{\"id\":47906543356,\"val\":\"61,541|218,541|218,767|335,908\"}}";
		String str1="{id=47906543356, val=61,541|218,541|218,767|335,908}";
		String str2="{\\\"id\\\":47906543356,\\\"val\\\":\\\"61,541|218,541|218,767|335,908\\\"}";
		
		 JSONObject jsonObj = new JSONObject(str);
//         boolean vdResult =(boolean) jsonObj.get("result");
         
         boolean vdResult =(boolean) jsonObj.get("result");     
         String vdData =jsonObj.get("data").toString();      
         JSONObject jsonObj1 = new JSONObject(vdData);       
         
         if(!vdResult) {
        	 vdData=StringEscapeUtils.unescapeJava(vdData);           
       }
         String val =jsonObj1.get("val").toString();      
         
		System.out.println("vdData:"+vdData);
		System.out.println("val:"+val);
		
    } 
	
	
	
	public static byte[] object2Bytes(Object value) {
		if (value == null) {
			return null;
		}
		
		ByteArrayOutputStream arrayOutputStream = new ByteArrayOutputStream();
		ObjectOutputStream outputStream;
		try {
			outputStream = new ObjectOutputStream(arrayOutputStream);

			outputStream.writeObject(value);
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				arrayOutputStream.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		return arrayOutputStream.toByteArray();
	}
	
	
	
	
}
