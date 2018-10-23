package com.jingtum.chainApp.entity;

public class ClientTransactionEntity {
	String clientId        ;
	String payAddress      ;
	String toAddress       ;
	String currency        ;
	String currencyValue   ;
	String transactionType ;
	String memos          ;
	String payTime         ;
	String hash            ;
	String fee             ;
	String filePath        ;
	public String getMemos() {
		return memos;
	}
	public void setMemos(String memos) {
		this.memos = memos;
	}
	public String getClientId() {
		return clientId;
	}
	public void setClientId(String clientId) {
		this.clientId = clientId;
	}
	public String getPayAddress() {
		return payAddress;
	}
	public void setPayAddress(String payAddress) {
		this.payAddress = payAddress;
	}
	public String getToAddress() {
		return toAddress;
	}
	public void setToAddress(String toAddress) {
		this.toAddress = toAddress;
	}
	public String getCurrency() {
		return currency;
	}
	public void setCurrency(String currency) {
		this.currency = currency;
	}
	public String getCurrencyValue() {
		return currencyValue;
	}
	public void setCurrencyValue(String currencyValue) {
		this.currencyValue = currencyValue;
	}
	public String getTransactionType() {
		return transactionType;
	}
	public void setTransactionType(String transactionType) {
		this.transactionType = transactionType;
	}
	
	public String getPayTime() {
		return payTime;
	}
	public void setPayTime(String payTime) {
		this.payTime = payTime;
	}
	public String getHash() {
		return hash;
	}
	public void setHash(String hash) {
		this.hash = hash;
	}
	public String getFee() {
		return fee;
	}
	public void setFee(String fee) {
		this.fee = fee;
	}
	public String getFilePath() {
		return filePath;
	}
	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}
}
