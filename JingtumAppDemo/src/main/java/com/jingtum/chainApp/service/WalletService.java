package com.jingtum.chainApp.service;

import java.util.List;

import com.jingtum.chainApp.entity.ClientTransactionEntity;
import com.jingtum.chainApp.entity.WalletEntity;

public interface WalletService {
	public void insertToWallet(WalletEntity wallet);
	public WalletEntity selectWallet(String account);
	public List<WalletEntity> selectAllWallet(int page,int rows);
	//更新钱包激活状态
	public void updateWalletActivity(String address);
	public void  insertToTransaction(ClientTransactionEntity transactionEntity);
}
