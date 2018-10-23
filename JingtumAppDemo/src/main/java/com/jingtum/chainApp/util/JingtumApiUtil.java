package com.jingtum.chainApp.util;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.jingtum.chainApp.entity.AmountEntity;
import com.jingtum.chainApp.entity.BalanceEntity;
import com.jingtum.chainApp.entity.BalancesResultEntity;
import com.jingtum.chainApp.entity.PaymentEntity;
import com.jingtum.chainApp.entity.PaymentResultEntity;
import com.jingtum.chainApp.entity.TransactionEntity;
import com.jingtum.chainApp.entity.WalletEntity;

/**
 * @author laiws
 * 井通API通用类
 */
public class JingtumApiUtil {
	public static void main(String[] args) {
		//创建钱包
		/*
		WalletEntity wallet =creatWallet();
		*/
		
		//激活钱包
		/*
		PaymentEntity paymentEntity =new PaymentEntity();
		AmountEntity amountEntity=new AmountEntity();
		PaymentEntity.Payment payment=paymentEntity.new Payment();
		amountEntity.setCurrency("SWT");
		amountEntity.setValue("100");
		amountEntity.setIssuer("");
		paymentEntity.setClient_id("lws_test_0012");		
		paymentEntity.setSecret("spkjxcNjhsf6qhADk5czxGFd*****");//转账人的钱包私钥，替换成自己的就可以了
		payment.setAmount(amountEntity);
		payment.setSource("j98WaT6ce8cfhbdMxZusiimVsesqk66DNH");//转账人的钱包地址
		payment.setDestination("jaMBd4yGhj1pT5BwhtiYxSFZRTzWLUNeTN");//要激活的钱包地址jEm5trpar99KVVd7cVz917ZbC1jyB3WsXG
		payment.setChoice("");
		payment.setMemos(new String[]{"激活钱包"});
		paymentEntity.setPayment(payment);		
		PaymentResultEntity resultEntity=jingtumApiUtil.activityWallet(paymentEntity);
		System.out.println(resultEntity.getClient_id());
		*/
		
		//数据上链
		/*
		PaymentEntity paymentEntity =new PaymentEntity();
		AmountEntity amountEntity=new AmountEntity();
		PaymentEntity.Payment payment=paymentEntity.new Payment();
		amountEntity.setCurrency("SWT");
		amountEntity.setValue("2");//经测试，这个值大于1才成功,反正是转到自己的另一个账户，无所谓了
		amountEntity.setIssuer("");
		paymentEntity.setClient_id("jt-txt-201804");//业务ID，以便后面用于查询		
		paymentEntity.setSecret("spkjxcNjhsf6qhADk5czxGFdmaCQ5");
		payment.setAmount(amountEntity);
		payment.setSource("j98WaT6ce8cfhbdMxZusiimVsesqk66DNH");
		payment.setDestination("jEm5trpar99KVVd7cVz917ZbC1jyB3WsXG");//业务目的钱包，整个项目可以用同一个
		payment.setChoice("");
		payment.setMemos(new String[]{"testdata","lws_test0009","上链数据"});//要上链的业务数据，字符串，根据业务需要排好数组元素次序,上链前最好先hash加密
		paymentEntity.setPayment(payment);	
		PaymentResultEntity resultEntity=JingtumApiUtil.dataToTrain(paymentEntity);
		if(resultEntity !=null){
			System.out.println(resultEntity.getClient_id() +";消耗费用："+resultEntity.getFee());
		}
		*/
		
		//查询单个交易		
		
		TransactionEntity transaction= JingtumApiUtil.getTransaction("j98WaT6ce8cfhbdMxZusiimVsesqk66DNH"
				,"jt-txt-201804");//lws_test0011
		if(transaction !=null){
			System.out.println(StringUtils.join(transaction.getMemos()) +";交易结果："+transaction.getResult());
		}
		
		
		//查询钱包余额
		/*
		BalancesResultEntity balances= JingtumApiUtil.getBalances("j98WaT6ce8cfhbdMxZusiimVsesqk66DNH");
		if(balances !=null){
			System.out.println(balances.getBalanceEntities().get(0).getValue());
		}
		*/
	}
	
	/**
	 * 创建钱包
	 * @return
	 */
	public static WalletEntity creatWallet(){
		//{  "success": true,  "status_code": "0",  
		//"wallet": {    "secret": "sssJBCQqKbzx7ZYp8e5AcuQyKRuZE",    
		//"address": "jGijKDKdyTDHwh3REduH4t95QZv2Qau5hr"  }}
		WalletEntity wallet =null;
		String apiUrl="https://tapi.jingtum.com/v2/wallet/new";
		String result= HttpUtil.sendGet(apiUrl);		
		JsonObject returnData = new JsonParser().parse(result).getAsJsonObject();
		if(returnData.get("success").toString().equalsIgnoreCase("true")){
			//System.out.println(returnData.getAsJsonObject("wallet").get("address").toString());
			JsonObject jsonObj=returnData.getAsJsonObject("wallet");
			wallet =new WalletEntity();
			wallet.setAddress(jsonObj.get("address").getAsString());
			wallet.setSecret(jsonObj.get("secret").getAsString());
		}		
		return wallet;
	}
	
	/**
	 * 激活钱包
	 * @param payment
	 * @return
	 */
	public static PaymentResultEntity activityWallet(PaymentEntity payment){		
		return paymentTransaction(payment);
	}
	
	public static PaymentResultEntity dataToTrain(PaymentEntity payment){		
		return paymentTransaction(payment);
	}
	
	/**
	 * 支付交易
	 * @param payment
	 * @return
	 */
	private static PaymentResultEntity paymentTransaction(PaymentEntity payment){
		PaymentResultEntity paymentResult=null;
		String apiUrl="https://tapi.jingtum.com/v2/accounts/"+ payment.getPayment().getSource()+"/payments";
		String params= new Gson().toJson(payment);
		String result= HttpUtil.postJson(apiUrl, params);
		JsonObject returnData = new JsonParser().parse(result).getAsJsonObject();
		if(returnData.get("success").toString().equalsIgnoreCase("true")){
			paymentResult= new PaymentResultEntity();
			paymentResult.setClient_id(returnData.get("client_id").getAsString());
			paymentResult.setHash(returnData.get("hash").getAsString());
			paymentResult.setFee(returnData.get("fee").getAsString());
			paymentResult.setResult(returnData.get("result").getAsString());
		}
		return paymentResult;
	}
	
	/**
	 * 获取单条交易记录信息
	 * @param walletAddress
	 * @param clientId
	 * @return
	 */
	public static TransactionEntity getTransaction(String walletAddress,String clientId){
		TransactionEntity transaction =null;
		String apiUrl="https://tapi.jingtum.com/v2/accounts/"+walletAddress
				+"/payments/"+ clientId;
		String result= HttpUtil.sendGet(apiUrl);
		JsonObject returnData = new JsonParser().parse(result).getAsJsonObject();
		if(returnData.get("success").toString().equalsIgnoreCase("true")){
			transaction =new TransactionEntity();
			transaction.setDate(returnData.get("date").toString());
			transaction.setHash(returnData.get("hash").toString());
			transaction.setType(returnData.get("type").toString());
			transaction.setFee(returnData.get("fee").toString());
			transaction.setResult(returnData.get("result").toString());
			JsonArray aMemos=returnData.getAsJsonArray("memos");
			List<String> lstMemos =new ArrayList<>();
			for(int i=0;i<aMemos.size();i++){
				lstMemos.add(aMemos.get(i).getAsString());
			}
			transaction.setMemos(lstMemos.toArray(new String[lstMemos.size()]));
			JsonArray aEffects=returnData.getAsJsonArray("effects");
			List<String> lstEffects =new ArrayList<>();
			for(int i=0;i<aEffects.size();i++){
				lstEffects.add(aEffects.get(i).toString());
			}
			transaction.setEffects(lstEffects.toArray(new String[lstEffects.size()]));
			transaction.setCounterparty(returnData.get("counterparty").toString());
			AmountEntity amount =new AmountEntity();
			JsonObject jsonObj=returnData.getAsJsonObject("amount");
			amount.setCurrency(jsonObj.get("currency").getAsString());
			amount.setValue(jsonObj.get("value").getAsString());
			amount.setIssuer(jsonObj.get("issuer").getAsString());
			transaction.setAmount(amount);
		}
		return transaction;
	}
	
	/**
	 * 获取钱包余额
	 * @param walletAddress
	 * @return
	 */
	public static BalancesResultEntity getBalances(String walletAddress){
		BalancesResultEntity balances=null;
		String apiUrl="https://tapi.jingtum.com/v2/accounts/"+walletAddress+"/balances";
		String result= HttpUtil.sendGet(apiUrl);
		JsonObject returnData = new JsonParser().parse(result).getAsJsonObject();
		if(returnData.get("success").toString().equalsIgnoreCase("true")){
			balances=new BalancesResultEntity();
			balances.setSequence(returnData.get("sequence").toString());
			//获取JSon数组
			JsonArray arrayBalances=returnData.getAsJsonArray("balances");
			List<BalanceEntity> lstBalances=new ArrayList<>();
			for(int i=0;i<arrayBalances.size();i++){
				//把字符串转成JSon对象
				JsonObject jsonObj= new JsonParser().parse(arrayBalances.get(i).toString()).getAsJsonObject();
				BalanceEntity balance=new BalanceEntity();
				balance.setCurrency(jsonObj.get("currency").getAsString());
				balance.setFreezed(jsonObj.get("freezed").getAsString());
				balance.setIssuer(jsonObj.get("issuer").getAsString());
				balance.setValue(jsonObj.get("value").getAsString());
				lstBalances.add(balance);
			}
			balances.setBalanceEntities(lstBalances);
		}
		return balances;
	}
}
