package com.jingtum.chainApp.servlet;

import java.io.IOException;
import java.sql.Date;
import java.util.LinkedHashMap;
import java.util.Map;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.aliyun.oss.OSSClient;
import com.aliyun.oss.common.utils.BinaryUtil;
import com.aliyun.oss.model.MatchMode;
import com.aliyun.oss.model.PolicyConditions;

import junit.framework.Assert;
import net.sf.json.JSONObject;

@SuppressWarnings("deprecation")
@WebServlet(name="PostObjectPolicy",urlPatterns="/servlet/PostObjectPolicy")
public class PostObjectPolicy extends HttpServlet{
	/**
	 * 
	 */
	private static final long serialVersionUID = 5522372203700422672L;

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException { 
	
	    String endpoint = "oss-cn-shenzhen.aliyuncs.com";
        String accessId = "LTAIxo8jBTqOTL29";
        String accessKey = "BHbJmVqxbfLS21zZyH9WqJCcpEya5B";
        String bucket = "jingtumt";
        String dir = "test/";
        String host = "http://" + bucket + "." + endpoint;       
        try { 	
        	OSSClient client = new OSSClient(accessId, accessKey);
        	long expireTime = 300;
        	long expireEndTime = System.currentTimeMillis() + expireTime * 1000;
            Date expiration = new Date(expireEndTime);
            PolicyConditions policyConds = new PolicyConditions();
            policyConds.addConditionItem(PolicyConditions.COND_CONTENT_LENGTH_RANGE, 0, 1048576000);
            policyConds.addConditionItem(MatchMode.StartWith, PolicyConditions.COND_KEY, dir);

            String postPolicy = client.generatePostPolicy(expiration, policyConds);
            byte[] binaryData = postPolicy.getBytes("utf-8");
            String encodedPolicy = BinaryUtil.toBase64String(binaryData);
            String postSignature = client.calculatePostSignature(postPolicy);
            
            Map<String, String> respMap = new LinkedHashMap<String, String>();
            respMap.put("accessid", accessId);
            respMap.put("policy", encodedPolicy);
            respMap.put("signature", postSignature);
            //respMap.put("expire", formatISO8601Date(expiration));
            respMap.put("dir", dir);
            respMap.put("host", host);
            respMap.put("expire", String.valueOf(expireEndTime / 1000));
            JSONObject ja1 = JSONObject.fromObject(respMap);
            System.out.println(ja1.toString());
            response.setHeader("Access-Control-Allow-Origin", "*");
            response.setHeader("Access-Control-Allow-Methods", "GET, POST");
            response(request, response, ja1.toString());
            
        } catch (Exception e) {
            Assert.fail(e.getMessage());
            e.printStackTrace();
        }
    }
	
	private void response(HttpServletRequest request, HttpServletResponse response, String results) throws IOException {
		String callbackFunName = request.getParameter("callback");
		if (callbackFunName==null || callbackFunName.equalsIgnoreCase(""))
			response.getWriter().println(results);
		else
			response.getWriter().println(callbackFunName + "( "+results+" )");
		response.setStatus(HttpServletResponse.SC_OK);
        response.flushBuffer();
	}
	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {  
		doGet(request, response);
    }
}
