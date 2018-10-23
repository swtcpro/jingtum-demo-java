package com.jingtum.chainApp.entity;

import java.util.List;

public class BalancesResultEntity {
	String sequence;//当前交易序列号（用于本地签名）
	List<BalanceEntity> balanceEntities;
	
	public String getSequence() {
		return sequence;
	}
	public void setSequence(String sequence) {
		this.sequence = sequence;
	}
	public List<BalanceEntity> getBalanceEntities() {
		return balanceEntities;
	}
	public void setBalanceEntities(List<BalanceEntity> balanceEntities) {
		this.balanceEntities = balanceEntities;
	}
	
}
