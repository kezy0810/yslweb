package com.qkl.ztysl.vdcode;

import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

/**
 *
 * 尚未实现 查询点数 以及 报错接口，请参考示例以及文档实现
 *
 */


/**
 *
 * 上传图片
 * @author wxf
 *
 */
public class LianZhongVdcode {
	
	private static String Upload_UrlStr = "http://v1-http-api.jsdama.com/api.php?mod=php&act=upload";
	private static String ReportErr_UrlStr = "http://v1-http-api.jsdama.com/api.php?mod=php&act=error";

	private static String BOUNDARY = "---------------------------naza--kelvin--"; //boundary就是request头和上传文件内容的分隔符


	    public static void main(String[] args) {

	        String filePath="e:\\yzmf0.jpg";//本地图片路径
	        
//	        Map<String, String> paramMap = getParamMap();
//	        String rs= validateCode(paramMap,filePath);
	        Map<String, String> paramMap = getReportParamMap();
	        String rs= rePortErr(paramMap);

	        
	         System.out.println(rs);
	       
	    }

	/**
	 * 参数信息
	 * @return
	 */
	private static Map<String, String> getParamMap() {
		Map<String, String> paramMap = new HashMap<String, String>();
		paramMap.put("user_name", "kezar01");
		paramMap.put("user_pw", "Kezar01998");
		paramMap.put("yzm_minlen", "0");
		paramMap.put("yzm_maxlen", "0");
		paramMap.put("yzmtype_mark", "1318");
		paramMap.put("token", "d0a21e2d0b46cce5fcc57e4308dc6590");
		
		return paramMap;
	}
	
	/**
	 * 参数信息
	 * @return
	 */
	private static Map<String, String> getReportParamMap() {
		Map<String, String> reportParamMap = new HashMap<String, String>();
		reportParamMap.put("user_name", "kezar01");
		reportParamMap.put("user_pw", "Kezar01998");
		reportParamMap.put("yzm_id", "111222");
		return reportParamMap;
	}
	
	
	public static String validateCode(Map<String, String> mMap,String fullfilename) {
		String vdcodeRs="";
		 try {
	            URL url=new URL(Upload_UrlStr);
	            HttpURLConnection connection=(HttpURLConnection)url.openConnection();
	            connection.setDoInput(true);
	            connection.setDoOutput(true);
	            connection.setRequestMethod("POST");
	            connection.setRequestProperty("content-type", "multipart/form-data; boundary="+BOUNDARY);
	            connection.setConnectTimeout(30000);
	            connection.setReadTimeout(30000);
	            
	            OutputStream out = new DataOutputStream(connection.getOutputStream());
				// 普通参数
				if (mMap != null) {
					StringBuffer strBuf = new StringBuffer();
					Iterator<Entry<String, String>> iter = mMap.entrySet().iterator();
					while (iter.hasNext()) {
						Map.Entry<String,String> entry = iter.next();
						String inputName = entry.getKey();
						String inputValue = entry.getValue();
						strBuf.append("\r\n").append("--").append(BOUNDARY).append("\r\n");
						strBuf.append("Content-Disposition: form-data; name=\""
								+ inputName + "\"\r\n\r\n");
						strBuf.append(inputValue);
					}
					out.write(strBuf.toString().getBytes());
				}
				
				// 图片文件
				if (fullfilename != null) {
					File file = new File(fullfilename);
					String filename = file.getName();
					String contentType = "image/jpeg";//这里看情况设置
					StringBuffer strBuf = new StringBuffer();
					strBuf.append("\r\n").append("--").append(BOUNDARY).append("\r\n");
					strBuf.append("Content-Disposition: form-data; name=\""
							+ "upload" + "\"; filename=\"" + filename+ "\"\r\n");
					strBuf.append("Content-Type:" + contentType + "\r\n\r\n");
					out.write(strBuf.toString().getBytes());
					DataInputStream in = new DataInputStream(
							new FileInputStream(file));
					int bytes = 0;
					byte[] bufferOut = new byte[1024];
					while ((bytes = in.read(bufferOut)) != -1) {
						out.write(bufferOut, 0, bytes);
					}
					in.close();
				}
				byte[] endData = ("\r\n--" + BOUNDARY + "--\r\n").getBytes();
				out.write(endData);
				out.flush();
				out.close();
	            
	            //读取URLConnection的响应
	            InputStream in = connection.getInputStream();
				ByteArrayOutputStream bout = new ByteArrayOutputStream();
				byte[] buf = new byte[1024];
				while (true) {
					int rc = in.read(buf);
					if (rc <= 0) {
						break;
					} else {
						bout.write(buf, 0, rc);
					}
				}
				in.close();
				//结果输出
				vdcodeRs=new String(bout.toByteArray());
				
				return vdcodeRs;
	        } catch (Exception e) {
	            e.printStackTrace();	         
	            return vdcodeRs;
	        }
	}

	
	public static String rePortErr(Map<String, String> mMap) {
		String vdcodeRs="";
		 try {
	            URL url=new URL(ReportErr_UrlStr);
	            HttpURLConnection connection=(HttpURLConnection)url.openConnection();
	            connection.setDoInput(true);
	            connection.setDoOutput(true);
	            connection.setRequestMethod("POST");
	            connection.setRequestProperty("content-type", "multipart/form-data; boundary="+BOUNDARY);
	            connection.setConnectTimeout(30000);
	            connection.setReadTimeout(30000);
	            
	            OutputStream out = new DataOutputStream(connection.getOutputStream());
				// 普通参数
				if (mMap != null) {
					StringBuffer strBuf = new StringBuffer();
					Iterator<Entry<String, String>> iter = mMap.entrySet().iterator();
					while (iter.hasNext()) {
						Map.Entry<String,String> entry = iter.next();
						String inputName = entry.getKey();
						String inputValue = entry.getValue();
						strBuf.append("\r\n").append("--").append(BOUNDARY).append("\r\n");
						strBuf.append("Content-Disposition: form-data; name=\""
								+ inputName + "\"\r\n\r\n");
						strBuf.append(inputValue);
					}
					out.write(strBuf.toString().getBytes());
				}
				
				
				byte[] endData = ("\r\n--" + BOUNDARY + "--\r\n").getBytes();
				out.write(endData);
				out.flush();
				out.close();
	            
	            //读取URLConnection的响应
	            InputStream in = connection.getInputStream();
				ByteArrayOutputStream bout = new ByteArrayOutputStream();
				byte[] buf = new byte[1024];
				while (true) {
					int rc = in.read(buf);
					if (rc <= 0) {
						break;
					} else {
						bout.write(buf, 0, rc);
					}
				}
				in.close();
				//结果输出
				vdcodeRs=new String(bout.toByteArray());
				
				return vdcodeRs;
	        } catch (Exception e) {
	            e.printStackTrace();	         
	            return vdcodeRs;
	        }
	}

}
