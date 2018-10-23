package com.jingtum.chainApp.entity;

/**
 * @author laiws
 * 支付金额对象实体类，包含金额数量，货币类型，货币发行方
 */
public class AmountEntity {
	String value;
	String currency;
	String issuer;
	
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
	
}
