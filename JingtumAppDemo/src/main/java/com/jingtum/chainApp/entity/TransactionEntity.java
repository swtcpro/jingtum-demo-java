package com.jingtum.chainApp.entity;

public class TransactionEntity {
	String date;
	String hash;
	String type;
	String fee;
	String result;
	String[] memos;//上链数据
	String counterparty;//交易对家
	AmountEntity amount;
	String[] effects;//支付的效果
	
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}
	public String getHash() {
		return hash;
	}
	public void setHash(String hash) {
		this.hash = hash;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getFee() {
		return fee;
	}
	public void setFee(String fee) {
		this.fee = fee;
	}
	public String getResult() {
		return result;
	}
	public void setResult(String result) {
		this.result = result;
	}
	public String[] getMemos() {
		return memos;
	}
	public void setMemos(String[] memos) {
		this.memos = memos;
	}
	public String getCounterparty() {
		return counterparty;
	}
	public void setCounterparty(String counterparty) {
		this.counterparty = counterparty;
	}
	public AmountEntity getAmount() {
		return amount;
	}
	public void setAmount(AmountEntity amount) {
		this.amount = amount;
	}
	public String[] getEffects() {
		return effects;
	}
	public void setEffects(String[] effects) {
		this.effects = effects;
	}
}
