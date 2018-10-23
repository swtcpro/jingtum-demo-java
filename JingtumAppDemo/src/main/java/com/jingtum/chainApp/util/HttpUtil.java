package com.jingtum.chainApp.util;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.activation.MimetypesFileTypeMap;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;

public class HttpUtil {
	
	/**
	 * 跨域头文件设置
	 * @param req
	 * @param res
	 */
	public static void setHeader(HttpServletRequest req,HttpServletResponse res){
		
		res.setHeader("Access-Control-Allow-Origin", req.getHeader("Origin"));
		res.setHeader("Access-Control-Allow-Credentials", "true");
		res.setHeader("Access-Control-Allow-Methods", "POST, GET, OPTIONS, DELETE");
		res.setHeader("Access-Control-Max-Age", "3600");
		res.addHeader("Access-Control-Allow-Headers","Authorization");
	}

	
	/**
	 * 向指定URL发送GET方法的请求
	 * 
	 * @param url
	 *            发送请求的URL
	 * @param param
	 *            请求参数，请求参数应该是 name1=value1&name2=value2 的形式。
	 * @return URL 所代表远程资源的响应结果
	 */
	public static String sendGet(String url, String param) {
		String result = "";
		BufferedReader in = null;
		try {
			String urlNameString = url + "?" + param;
			URL realUrl = new URL(urlNameString);
			// 打开和URL之间的连接
			URLConnection connection = realUrl.openConnection();
			// 设置通用的请求属性
			connection.setRequestProperty("accept", "*/*");
			connection.setRequestProperty("connection", "Keep-Alive");
			connection.setRequestProperty("user-agent",
					"Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
			// 建立实际的连接
			connection.connect();
			// 定义 BufferedReader输入流来读取URL的响应
			in = new BufferedReader(new InputStreamReader(
					connection.getInputStream()));
			String line;
			while ((line = in.readLine()) != null) {
				result += line;
			}
		} catch (Exception e) {
			return null;
		} finally {
            // 使用finally块来关闭输入流
			try {
				if (in != null) {
					in.close();
				}
			} catch (Exception e2) {
				e2.printStackTrace();
			}
		}
		return result;
	}
	
	/**
	 * 向指定URL发送GET方法的请求
	 * 
	 * @param url
	 *            发送请求的URL
	 * @return URL 所代表远程资源的响应结果
	 */
	public static String sendGet(String url) {
		String result = "";
		BufferedReader in = null;
		try {
			String urlNameString = url;
			URL realUrl = new URL(urlNameString);
			// 打开和URL之间的连接
			URLConnection connection = realUrl.openConnection();
			// 设置通用的请求属性
			connection.setRequestProperty("accept", "*/*");
			connection.setRequestProperty("connection", "Keep-Alive");
			connection.setRequestProperty("user-agent",
					"Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
			// 建立实际的连接
			connection.connect();
			// 定义 BufferedReader输入流来读取URL的响应
			in = new BufferedReader(new InputStreamReader(
					connection.getInputStream(),"UTF-8"));
			String line;
			while ((line = in.readLine()) != null) {
				result += line;
			}
		} catch (Exception e) {
			return null;
		} finally { // 使用finally块来关闭输入流
			try {
				if (in != null) {
					in.close();
				}
			} catch (Exception e2) {
				e2.printStackTrace();
			}
		}
		return result;
	}

	/**
	 * 向指定 URL 发送POST方法的请求
	 * 
	 * @param url
	 *            发送请求的 URL
	 * @param param
	 *            请求参数，请求参数应该是 name1=value1&name2=value2 的形式。
	 * @return 所代表远程资源的响应结果
	 */
	public static String sendPost(String url, String param) {
		PrintWriter out = null;
		BufferedReader in = null;
		String result = "";
		try {
			URL realUrl = new URL(url);
			// 打开和URL之间的连接
			URLConnection conn = realUrl.openConnection();
			// 设置通用的请求属性
			conn.setRequestProperty("accept", "*/*");
			conn.setRequestProperty("connection", "Keep-Alive");
			conn.setRequestProperty("user-agent",
					"Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
			// 发送POST请求必须设置如下两行
			conn.setDoOutput(true);
			conn.setDoInput(true);
			// 获取URLConnection对象对应的输出流
			out = new PrintWriter(conn.getOutputStream());
			// 发送请求参数
			out.print(param);
			// flush输出流的缓冲
			out.flush();
			// 定义BufferedReader输入流来读取URL的响应
			in = new BufferedReader(
					new InputStreamReader(conn.getInputStream()));
			String line;
			while ((line = in.readLine()) != null) {
				result += line;
			}
		} catch (Exception e) {
			return null;
		} finally {// 使用finally块来关闭输出流、输入流
			try {
				if (out != null) {
					out.close();
				}
				if (in != null) {
					in.close();
				}
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}
		return result;
	}
	
	/**
	 * 发送HttpPost请求
	 * @param strURL 服务地址
	 * @param params json字符串,例如: "{ \"id\":\"12345\" }" ;其中属性名必须带双引号<br/> 
	 * @return 成功:返回json字符串<br/>
	 */
	public static String postJson(String strURL, String params) {
        try {
        	// 创建连接  
            URL url = new URL(strURL);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoOutput(true);
            connection.setDoInput(true);
            connection.setUseCaches(false);
            connection.setInstanceFollowRedirects(true);
            // 设置请求方式 
            connection.setRequestMethod("POST");
            // 设置接收数据的格式  
            connection.setRequestProperty("Accept", "application/json");
            // 设置发送数据的格式
            connection.setRequestProperty("Content-Type", "application/json");
            connection.connect();
            // utf-8编码
            OutputStreamWriter out = new OutputStreamWriter(connection.getOutputStream(), "UTF-8");
            out.append(params);
            //关闭流
            out.flush();  
            out.close();  
            // 读取响应 ，获取长度  
            int length = (int) connection.getContentLength();
            InputStream is = connection.getInputStream();
            if (length != -1) {
            	byte[] data = new byte[length];
            	byte[] temp = new byte[512];
            	int readLen = 0;
            	int destPos = 0;
            	while ((readLen = is.read(temp)) > 0) {  
                    System.arraycopy(temp, 0, data, destPos, readLen);
                    destPos += readLen;
                }
            	// utf-8编码  
            	String result = new String(data, "UTF-8");
            	//返回
            	return result;
            }  
        } catch (IOException e) {  
            // TODO Auto-generated catch block  
            e.printStackTrace();  
        }
        // 自定义错误信息  
        return "error";
    }
	
	/**
	 * 使用HttpURLConnection发送get请求 
	 * @param urlParam url
	 * @param params 参数
	 * @param charset 字符
	 * @return String
	 */
	public static String sendGet(String urlParam, Map<String, Object> params, String charset) {  
        StringBuffer resultBuffer = null;  
        // 构建请求参数  
        StringBuffer sbParams = new StringBuffer();  
        if (params != null && params.size() > 0) {  
            for (Entry<String, Object> entry : params.entrySet()) {  
                sbParams.append(entry.getKey());  
                sbParams.append("=");  
                sbParams.append(entry.getValue());  
                sbParams.append("&");  
            }  
        }  
        HttpURLConnection con = null;  
        BufferedReader br = null;  
        try {  
            URL url = null;  
            if (sbParams != null && sbParams.length() > 0) {  
                url = new URL(urlParam + "?" + sbParams.substring(0, sbParams.length() - 1));  
            } else {  
                url = new URL(urlParam);  
            }  
            con = (HttpURLConnection) url.openConnection();  
            con.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");  
            con.connect();  
            resultBuffer = new StringBuffer();  
            br = new BufferedReader(new InputStreamReader(con.getInputStream(), charset));  
            String temp;  
            while ((temp = br.readLine()) != null) {  
                resultBuffer.append(temp);  
            }  
        } catch (Exception e) {  
            throw new RuntimeException(e);  
        } finally {  
            if (br != null) {  
                try {  
                    br.close();  
                } catch (IOException e) {  
                    br = null;  
                    throw new RuntimeException(e);  
                } finally {  
                    if (con != null) {  
                        con.disconnect();  
                        con = null;  
                    }  
                }  
            }  
        }  
        return resultBuffer.toString();  
    }
	
	/**
	 * java模拟表单提交数据，一般用于上传文件
	 * @param urlStr 请求地址
	 * @param textMap 纯文本数据
	 * @param fileMap 上传文件数据
	 * @return String
	 */
	@SuppressWarnings("rawtypes")
	public static String formUpload(String urlStr, Map<String, String> textMap,
			Map<String, String> fileMap) {
		String res = "";
		//网络连接
		HttpURLConnection conn = null;
		// boundary就是request头和上传文件内容的分隔符
		String BOUNDARY = "---------------------------123821742118716";
		try {
			URL url = new URL(urlStr);
			//设置请求信息
			conn = (HttpURLConnection) url.openConnection();
			//连接超时实际aN
			conn.setConnectTimeout(5000);
			conn.setReadTimeout(30000);
			conn.setDoOutput(true);
			conn.setDoInput(true);
			conn.setUseCaches(false);
			//提交方式
			conn.setRequestMethod("POST");
			conn.setRequestProperty("Connection", "Keep-Alive");
			conn.setRequestProperty("User-Agent",
					"Mozilla/5.0 (Windows; U; Windows NT 6.1; zh-CN; rv:1.9.2.6)");
			conn.setRequestProperty("Content-Type",
					"multipart/form-data; boundary=" + BOUNDARY);
			//输出流
			OutputStream out = new DataOutputStream(conn.getOutputStream());
			
			// 处理纯文本参数
			if (textMap != null) {
				StringBuffer strBuf = new StringBuffer();
				Iterator iter = textMap.entrySet().iterator();
				while (iter.hasNext()) {
					Map.Entry entry = (Map.Entry) iter.next();
					String inputName = (String) entry.getKey();
					String inputValue = (String) entry.getValue();
					if (inputValue == null) {
						continue;
					}
					strBuf.append("\r\n").append("--").append(BOUNDARY)
							.append("\r\n");
					strBuf.append("Content-Disposition: form-data; name=\""
							+ inputName + "\"\r\n\r\n");
					strBuf.append(inputValue);
				}
				out.write(strBuf.toString().getBytes());
			}
			
			// 处理文件参数
			if (fileMap != null) {
				Iterator iter = fileMap.entrySet().iterator();
				while (iter.hasNext()) {
					Map.Entry entry = (Map.Entry) iter.next();
					String inputName = (String) entry.getKey();
					String inputValue = (String) entry.getValue();
					if (inputValue == null) {
						continue;
					}
					File file = new File(inputValue);
					String filename = file.getName();
					String contentType = new MimetypesFileTypeMap()
							.getContentType(file);
					if (filename.endsWith(".png")) {
						contentType = "image/png";
					}
					if (contentType == null || contentType.equals("")) {
						contentType = "application/octet-stream";
					}
					StringBuffer strBuf = new StringBuffer();
					strBuf.append("\r\n").append("--").append(BOUNDARY)
							.append("\r\n");
					strBuf.append("Content-Disposition: form-data; name=\""
							+ inputName + "\"; filename=\"" + filename
							+ "\"\r\n");
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
			}
			
			//获取字节流
			byte[] endData = ("\r\n--" + BOUNDARY + "--\r\n").getBytes();
			out.write(endData);
			out.flush();
			out.close();
			
			// 读取返回数据
			StringBuffer strBuf = new StringBuffer();
			BufferedReader reader = new BufferedReader(new InputStreamReader(
					conn.getInputStream()));
			String line = null;
			while ((line = reader.readLine()) != null) {
				strBuf.append(line).append("\n");
			}
			res = strBuf.toString();
			
			//关闭流
			reader.close();
			reader = null;
		} catch (Exception e) {
			System.out.println("发送POST请求出错。" + urlStr);
			e.printStackTrace();
		} finally {
			if (conn != null) {
				conn.disconnect();
				conn = null;
			}
		}
		return res;
	}
	
	/**
	 * GET请求返回请求信息流
	 * @param url 请求地址
	 * @return Map
	 * header：Map<String, List<String>> 头信息
	 * inputStream：InputStream
	 * 错误：null
	 */
	public static Map<String, Object> sendGET(String url) {
		//返回结果
		Map<String, Object> map = new HashMap<String, Object>();
		//处理请求
		try {
			//请求地址
			URL u = new URL(url);
			//打开连接
			HttpURLConnection  conn = (HttpURLConnection) u.openConnection();
			//请求方式
			conn.setRequestMethod("GET");
			//建立连接
			conn.connect();
			
			//请求状态码200成功
			int code = conn.getResponseCode();
			//判断
			if (code == 200) {
				//成功
				BufferedInputStream bis = new BufferedInputStream(conn.getInputStream());
				//请求头信息
				Map<String, List<String>> header = conn.getHeaderFields();
				//添加
				map.put("header", header);
				//返回流
				InputStream inputStream = conn.getInputStream();
				//添加
				map.put("inputStream", inputStream);
				//关闭流
				if (bis != null) {
					bis.close();
				}
			} else {
				//失败
				map = null;
			}
		} catch (MalformedURLException e) {
			map = null;
			e.printStackTrace();
		} catch (IOException e) {
			map = null;
			e.printStackTrace();
		}
		//返回
		return map;
	}
	
	/**
	 * 根据参数名从HttpServletRequest对象中取出对应的参数值，并转化成需要的数据类型，
	 * 包括Java基础数据类型和通用日期类型，如果没有对应的参数值则返回null。
	 * 
	 * 目前支持的通用日期类型包括：
	 * java.util.Date,java.sql.Timestamp,java.util.Calendar,java.util
	 * .GregorianCalendar
	 * 
	 * @param <T>
	 * @param request  request 对象
	 * @param key  键
	 * @param clazz 对象类型
	 * @return
	 */
	public static <T> T getValue(HttpServletRequest request, String key,
			Class<T> clazz) {

		String value = request.getParameter(key);
		if (value == null) { // 如果获取参数值为null,则返回null
			return null;
		} else if (!value.equals("")) { // 如果获取参数值不为"",则通过convertGt方法进行类型转换后返回结果
			return ConvertUtils.convertGt(value, clazz);
		} else if (clazz.getName().equals(String.class.getName())) { // 如果获取参数值为""
			// ,
			// 并且clazz是String类型
			// ,返回""
			// 字符串
			return ConvertUtils.convertGt(value, clazz);
		} else {// 如果获取参数值为"",并且clazz不是是String类型,则返回null
			return null;
		}
	}
}
