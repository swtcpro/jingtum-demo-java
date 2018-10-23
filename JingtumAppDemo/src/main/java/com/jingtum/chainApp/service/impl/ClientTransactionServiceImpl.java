package com.jingtum.chainApp.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.pagehelper.PageHelper;
import com.jingtum.chainApp.entity.ClientTransactionEntity;
import com.jingtum.chainApp.mapper.TransactionMapper;
import com.jingtum.chainApp.service.ClientTransactionService;

@Service
public class ClientTransactionServiceImpl implements ClientTransactionService {

	@Autowired
	private TransactionMapper transactionMapper;
	
	@Override
	public List<ClientTransactionEntity> selectTransaction(int transactionType,int page,int rows) {
		PageHelper.startPage(page, rows);
		return transactionMapper.selectTransaction(transactionType);
	}

}
