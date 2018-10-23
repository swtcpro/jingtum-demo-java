package com.jingtum.chainApp.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.pagehelper.PageHelper;
import com.jingtum.chainApp.entity.ClientTransactionEntity;
import com.jingtum.chainApp.entity.WalletEntity;
import com.jingtum.chainApp.mapper.WalletMapper;
import com.jingtum.chainApp.service.WalletService;

@Service
public class WalletServiceImpl implements WalletService {

	@Autowired
	private WalletMapper walletMapper;
	
	@Override
	public void insertToWallet(WalletEntity wallet) {
		walletMapper.insertToWallet(wallet);
	}

	@Override
	public WalletEntity selectWallet(String account) {
		return walletMapper.selectWallet(account);
	}

	@Override
	public List<WalletEntity> selectAllWallet(int page,int rows) {
		PageHelper.startPage(page, rows);
		return walletMapper.selectAllWallet();
	}

	@Override
	public void updateWalletActivity(String address) {
		walletMapper.updateWalletActivity(address);
	}

	@Override
	public void insertToTransaction(ClientTransactionEntity transactionEntity) {
		walletMapper.insertToTransaction(transactionEntity);
	}

}
