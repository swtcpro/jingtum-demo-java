package com.jingtum.chainApp.service;

import java.util.List;

import com.jingtum.chainApp.entity.ClientTransactionEntity;

public interface ClientTransactionService {
	public List<ClientTransactionEntity> selectTransaction(int transactionType,int page,int rows);
}
