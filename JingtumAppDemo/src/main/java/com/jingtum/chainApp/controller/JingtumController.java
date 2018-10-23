package com.jingtum.chainApp.controller;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.github.pagehelper.PageInfo;
import com.jingtum.chainApp.entity.AmountEntity;
import com.jingtum.chainApp.entity.BalancesResultEntity;
import com.jingtum.chainApp.entity.ClientTransactionEntity;
import com.jingtum.chainApp.entity.PaymentEntity;
import com.jingtum.chainApp.entity.PaymentResultEntity;
import com.jingtum.chainApp.entity.TransactionEntity;
import com.jingtum.chainApp.entity.WalletEntity;
import com.jingtum.chainApp.service.ClientTransactionService;
import com.jingtum.chainApp.service.WalletService;
import com.jingtum.chainApp.util.DateUtils;
import com.jingtum.chainApp.util.JingtumApiUtil;
import com.jingtum.chainApp.util.RandomUtils;

@Controller
@RequestMapping("/jingtum")
public class JingtumController extends BaseController{
	
	@Autowired
	private WalletService walletService;
	@Autowired
	ClientTransactionService clientTransactionService;
	private final String _myWalletAddress="j98WaT6ce8cfhbdMxZusiimVsesqk66DNH";//我的钱包地址，做测试用
	private final String _myWalletSecret="spkjxcNjhsf6qhADk5czxGFdmaCQ5";//我的钱包私钥，做测试用
	private final String _toWalletAddress="jEm5trpar99KVVd7cVz917ZbC1jyB3WsXG";//文本等数据的接收地址
	/**
	 * 创建钱包
	 * @return
	 */
	@RequestMapping(value="/createWallet" ,produces="application/json;charset=UTF-8")
	@ResponseBody
	public Map<String,Object> createWallet(){
		Map<String, Object> result = new HashMap<String, Object>();
		try{
			String account = this.getParameter("account", String.class);
			WalletEntity walletTemp= walletService.selectWallet(account);
			if(walletTemp ==null){
				WalletEntity wallet =JingtumApiUtil.creatWallet();
				wallet.setAccount(account);
				wallet.setWalletType("0");
				walletService.insertToWallet(wallet);
				result.put("success", "true");
				result.put("address", wallet.getAddress());
			}else{
				result.put("success", "false");
			}
			//result.put("address","1233");
		}catch(Exception ex){
			ex.printStackTrace();
			result.put("success", "false");
		}
		return result;
	}
	
	/**
	 * 激活钱包
	 * @return
	 */
	@RequestMapping(value="/activityWallet" ,produces="application/json;charset=UTF-8")
	@ResponseBody
	public Map<String, Object> activityWallet(){		
		Map<String, Object> result = new HashMap<String, Object>();
		try{
			String address = this.getParameter("address", String.class);
			Date curDate=new java.util.Date();
			String clientId="jt-act-"+curDate.getTime();
			PaymentEntity paymentEntity =new PaymentEntity();
			AmountEntity amountEntity=new AmountEntity();
			PaymentEntity.Payment payment=paymentEntity.new Payment();
			amountEntity.setCurrency("SWT");
			amountEntity.setValue("25");
			amountEntity.setIssuer("");
			paymentEntity.setClient_id(clientId);		
			paymentEntity.setSecret(_myWalletSecret);//转账人的钱包私钥，替换成自己的就可以了
			payment.setAmount(amountEntity);
			payment.setSource(_myWalletAddress);//转账人的钱包地址
			payment.setDestination(address);//要激活的钱包地址jEm5trpar99KVVd7cVz917ZbC1jyB3WsXG
			payment.setChoice("");
			payment.setMemos(new String[]{"激活钱包"});
			paymentEntity.setPayment(payment);		
			PaymentResultEntity resultEntity=JingtumApiUtil.activityWallet(paymentEntity);
			//更新钱包状态和插入交易记录表
			walletService.updateWalletActivity(address);
			ClientTransactionEntity transactionEntity=new ClientTransactionEntity();
			transactionEntity.setClientId(clientId);
			transactionEntity.setMemos(StringUtils.join(payment.getMemos(), ","));
			transactionEntity.setCurrency(amountEntity.getCurrency());
			transactionEntity.setCurrencyValue(amountEntity.getValue());
			transactionEntity.setFee(resultEntity.getFee());
			transactionEntity.setHash(resultEntity.getHash());
			transactionEntity.setPayAddress(payment.getSource());
			transactionEntity.setPayTime(resultEntity.getDate());
			transactionEntity.setToAddress(payment.getDestination());
			transactionEntity.setTransactionType("0");
			walletService.insertToTransaction(transactionEntity);
			result.put("success", "true");
		}catch(Exception ex){
			ex.printStackTrace();
			result.put("success", "false");
		}
		return result;
	}
	
	@RequestMapping(value="/getWalletList" ,produces="application/json;charset=UTF-8")
	@ResponseBody
	public Map<String,Object> getWalletList(){
		Map<String,Object> resultMap = new HashMap<String,Object>();
		int page=this.getParameter("page", Integer.class);
		int rows=this.getParameter("rows", Integer.class);
		List<WalletEntity> lst=walletService.selectAllWallet(page,rows);
		PageInfo<WalletEntity> pageInfo = new PageInfo<WalletEntity>(lst);
	    long total = pageInfo.getTotal(); //获取总记录数
		resultMap.put("total", total);
		resultMap.put("rows", lst);
		resultMap.put("status", "success");
		return resultMap;
	}
	
	@RequestMapping(value="/getBalance" ,produces="application/json;charset=UTF-8")
	@ResponseBody
	public Map<String,Object> getBalance(){
		Map<String,Object> resultMap = new HashMap<String,Object>();
		try{
			String address = this.getParameter("address", String.class);
			BalancesResultEntity balances= JingtumApiUtil.getBalances(address);
			resultMap.put("data", balances);
			resultMap.put("status", "success");
		}catch(Exception ex){
			ex.printStackTrace();
			resultMap.put("success", "false");
		}
		return resultMap;
	}
	
	/**
	 * 获取上传的文本信息列表
	 * @return
	 */
	@RequestMapping(value="/getTxtTransactionList" ,produces="application/json;charset=UTF-8")
	@ResponseBody
	public Map<String,Object> getTxtTransactionList(){
		Map<String,Object> resultMap = new HashMap<String,Object>();
		try{
			int page=this.getParameter("page", Integer.class);
			int rows=this.getParameter("rows", Integer.class);
			List<ClientTransactionEntity> lstTransaction= 
					clientTransactionService.selectTransaction(2, page, rows);
			PageInfo<ClientTransactionEntity> pageInfo = new PageInfo<ClientTransactionEntity>(lstTransaction);
		    long total = pageInfo.getTotal(); //获取总记录数
			resultMap.put("total", total);
			resultMap.put("rows", lstTransaction);
			resultMap.put("status", "success");
		}catch(Exception ex){
			ex.printStackTrace();
			resultMap.put("success", "false");
		}
		return resultMap;
	}
	
	/**
	 * 文本数据上传到区块链中
	 * @return
	 */
	@RequestMapping(value="/txtToTrain" ,produces="application/json;charset=UTF-8")
	@ResponseBody
	public Map<String, Object> txtToTrain(){		
		Map<String, Object> result = new HashMap<String, Object>();
		try{
			String txt = this.getParameter("txt", String.class);
			String address = _toWalletAddress;
			Date curDate=new java.util.Date();
			String clientId="jt-txt-"+curDate.getTime() ;
			PaymentEntity paymentEntity =new PaymentEntity();
			AmountEntity amountEntity=new AmountEntity();
			PaymentEntity.Payment payment=paymentEntity.new Payment();
			amountEntity.setCurrency("SWT");
			amountEntity.setValue("0.1");
			amountEntity.setIssuer("");
			paymentEntity.setClient_id(clientId);		
			paymentEntity.setSecret(_myWalletSecret);//转账人的钱包私钥，替换成自己的就可以了
			payment.setAmount(amountEntity);
			payment.setSource(_myWalletAddress);//转账人的钱包地址
			payment.setDestination(address);//要激活的钱包地址jEm5trpar99KVVd7cVz917ZbC1jyB3WsXG
			payment.setChoice("");
			payment.setMemos(new String[]{txt});
			paymentEntity.setPayment(payment);		
			PaymentResultEntity resultEntity=JingtumApiUtil.dataToTrain (paymentEntity);
			//插入交易记录表
			ClientTransactionEntity transactionEntity=new ClientTransactionEntity();
			transactionEntity.setClientId(resultEntity.getClient_id());
			transactionEntity.setMemos(StringUtils.join(payment.getMemos(), ","));
			transactionEntity.setCurrency(amountEntity.getCurrency());
			transactionEntity.setCurrencyValue(amountEntity.getValue());
			transactionEntity.setFee(resultEntity.getFee());
			transactionEntity.setHash(resultEntity.getHash());
			transactionEntity.setPayAddress(payment.getSource());
			transactionEntity.setPayTime(resultEntity.getDate());
			transactionEntity.setToAddress(payment.getDestination());
			transactionEntity.setTransactionType("2");
			walletService.insertToTransaction(transactionEntity);
			result.put("success", "true");
		}catch(Exception ex){
			ex.printStackTrace();
			result.put("success", "false");
		}
		return result;
	}
	
	@RequestMapping(value="/getTransactionByClentid" ,produces="application/json;charset=UTF-8")
	@ResponseBody
	public Map<String, Object> getTransactionByClentid(){		
		Map<String, Object> result = new HashMap<String, Object>();
		try{
			String clientId = this.getParameter("clientId", String.class);
			TransactionEntity transaction= JingtumApiUtil.getTransaction(_myWalletAddress, clientId);
			if(transaction !=null){
				result.put("data", transaction);
				result.put("success", "true");
			}else{
				result.put("success", "false");
			}
		}catch(Exception ex){
			ex.printStackTrace();
			result.put("success", "false");
		}
		return result;
	}
	
	/**
	 * 获取上传的图片信息列表
	 * @return
	 */
	@RequestMapping(value="/getImgTransactionList" ,produces="application/json;charset=UTF-8")
	@ResponseBody
	public Map<String,Object> getImgTransactionList(){
		Map<String,Object> resultMap = new HashMap<String,Object>();
		try{
			int page=this.getParameter("page", Integer.class);
			int rows=this.getParameter("rows", Integer.class);
			List<ClientTransactionEntity> lstTransaction= 
					clientTransactionService.selectTransaction(3, page, rows);
			PageInfo<ClientTransactionEntity> pageInfo = new PageInfo<ClientTransactionEntity>(lstTransaction);
		    long total = pageInfo.getTotal(); //获取总记录数
			resultMap.put("total", total);
			resultMap.put("rows", lstTransaction);
			resultMap.put("status", "success");
		}catch(Exception ex){
			ex.printStackTrace();
			resultMap.put("success", "false");
		}
		return resultMap;
	}
	
	/**
	 * 图片数据上传到区块链中
	 * @return
	 */
	@RequestMapping(value="/imgToTrain" ,produces="application/json;charset=UTF-8")
	@ResponseBody
	public Map<String, Object> imgToTrain(){		
		Map<String, Object> result = new HashMap<String, Object>();
		try{
			String imgHash = this.getParameter("imgHash", String.class);
			String filePath = this.getParameter("filePath", String.class);
			String address = _toWalletAddress;
			Date curDate=new java.util.Date();
			String clientId="jt-img-"+curDate.getTime() ;
			PaymentEntity paymentEntity =new PaymentEntity();
			AmountEntity amountEntity=new AmountEntity();
			PaymentEntity.Payment payment=paymentEntity.new Payment();
			amountEntity.setCurrency("SWT");
			amountEntity.setValue("0.1");
			amountEntity.setIssuer("");
			paymentEntity.setClient_id(clientId);		
			paymentEntity.setSecret(_myWalletSecret);//转账人的钱包私钥，替换成自己的就可以了
			payment.setAmount(amountEntity);
			payment.setSource(_myWalletAddress);//转账人的钱包地址
			payment.setDestination(address);//要激活的钱包地址jEm5trpar99KVVd7cVz917ZbC1jyB3WsXG
			payment.setChoice("");
			payment.setMemos(new String[]{imgHash,filePath});
			paymentEntity.setPayment(payment);		
			PaymentResultEntity resultEntity=JingtumApiUtil.dataToTrain (paymentEntity);
			//插入交易记录表
			ClientTransactionEntity transactionEntity=new ClientTransactionEntity();
			transactionEntity.setClientId(resultEntity.getClient_id());
			transactionEntity.setMemos(StringUtils.join(payment.getMemos(), ","));
			transactionEntity.setCurrency(amountEntity.getCurrency());
			transactionEntity.setCurrencyValue(amountEntity.getValue());
			transactionEntity.setFee(resultEntity.getFee());
			transactionEntity.setHash(resultEntity.getHash());
			transactionEntity.setPayAddress(payment.getSource());
			transactionEntity.setPayTime(resultEntity.getDate());
			transactionEntity.setToAddress(payment.getDestination());
			transactionEntity.setTransactionType("3");
			transactionEntity.setFilePath(filePath);
			walletService.insertToTransaction(transactionEntity);
			result.put("success", "true");
		}catch(Exception ex){
			ex.printStackTrace();
			result.put("success", "false");
		}
		return result;
	}
	/**
	 * 获取上传的音频信息列表
	 * @return
	 */
	@RequestMapping(value="/getAudioTransactionList" ,produces="application/json;charset=UTF-8")
	@ResponseBody
	public Map<String,Object> getAudioTransactionList(){
		Map<String,Object> resultMap = new HashMap<String,Object>();
		try{
			int page=this.getParameter("page", Integer.class);
			int rows=this.getParameter("rows", Integer.class);
			List<ClientTransactionEntity> lstTransaction= 
					clientTransactionService.selectTransaction(4, page, rows);
			PageInfo<ClientTransactionEntity> pageInfo = new PageInfo<ClientTransactionEntity>(lstTransaction);
		    long total = pageInfo.getTotal(); //获取总记录数
			resultMap.put("total", total);
			resultMap.put("rows", lstTransaction);
			resultMap.put("status", "success");
		}catch(Exception ex){
			ex.printStackTrace();
			resultMap.put("success", "false");
		}
		return resultMap;
	}
	
	/**
	 * 音频数据上传到区块链中
	 * @return
	 */
	@RequestMapping(value="/audioToChain" ,produces="application/json;charset=UTF-8")
	@ResponseBody
	public Map<String, Object> audioToChain(){		
		Map<String, Object> result = new HashMap<String, Object>();
		try{
			String audioHash = this.getParameter("audioHash", String.class);
			String filePath = this.getParameter("filePath", String.class);
			String address = _toWalletAddress;
			Date curDate=new java.util.Date();
			String clientId="jt-aud-"+curDate.getTime() ;
			PaymentEntity paymentEntity =new PaymentEntity();
			AmountEntity amountEntity=new AmountEntity();
			PaymentEntity.Payment payment=paymentEntity.new Payment();
			amountEntity.setCurrency("SWT");
			amountEntity.setValue("0.1");
			amountEntity.setIssuer("");
			paymentEntity.setClient_id(clientId);		
			paymentEntity.setSecret(_myWalletSecret);//转账人的钱包私钥，替换成自己的就可以了
			payment.setAmount(amountEntity);
			payment.setSource(_myWalletAddress);//转账人的钱包地址
			payment.setDestination(address);//要激活的钱包地址jEm5trpar99KVVd7cVz917ZbC1jyB3WsXG
			payment.setChoice("");
			payment.setMemos(new String[]{audioHash,filePath});
			paymentEntity.setPayment(payment);		
			PaymentResultEntity resultEntity=JingtumApiUtil.dataToTrain (paymentEntity);
			//插入交易记录表
			ClientTransactionEntity transactionEntity=new ClientTransactionEntity();
			transactionEntity.setClientId(resultEntity.getClient_id());
			transactionEntity.setMemos(StringUtils.join(payment.getMemos(), ","));
			transactionEntity.setCurrency(amountEntity.getCurrency());
			transactionEntity.setCurrencyValue(amountEntity.getValue());
			transactionEntity.setFee(resultEntity.getFee());
			transactionEntity.setHash(resultEntity.getHash());
			transactionEntity.setPayAddress(payment.getSource());
			transactionEntity.setPayTime(resultEntity.getDate());
			transactionEntity.setToAddress(payment.getDestination());
			transactionEntity.setTransactionType("4");
			transactionEntity.setFilePath(filePath);
			walletService.insertToTransaction(transactionEntity);
			result.put("success", "true");
		}catch(Exception ex){
			ex.printStackTrace();
			result.put("success", "false");
		}
		return result;
	}
}
