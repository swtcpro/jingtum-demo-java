package com.jingtum.chainApp.mapper;

import java.util.List;

import com.jingtum.chainApp.entity.ClientTransactionEntity;
import com.jingtum.chainApp.entity.WalletEntity;

public interface WalletMapper {
	//创建钱包
	public void insertToWallet(WalletEntity wallet);
	//获取钱包信息
	public WalletEntity selectWallet(String account);
	//获取所有钱包
	public List<WalletEntity> selectAllWallet();
	//更新钱包激活状态
	public void updateWalletActivity(String address);
	//插入交易记录表
	public void  insertToTransaction(ClientTransactionEntity transactionEntity);
	
}
