package com.jingtum.chainApp.entity;

/**
 * @author laiws
 * 余额实体类
 */
public class BalanceEntity {
	String value;
	String currency;
	String issuer;
	String freezed;
	
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
	public String getCurrency() {
		return currency;
	}
	public void setCurrency(String currency) {
		this.currency = currency;
	}
	public String getIssuer() {
		return issuer;
	}
	public void setIssuer(String issuer) {
		this.issuer = issuer;
	}
	public String getFreezed() {
		return freezed;
	}
	public void setFreezed(String freezed) {
		this.freezed = freezed;
	}
}
