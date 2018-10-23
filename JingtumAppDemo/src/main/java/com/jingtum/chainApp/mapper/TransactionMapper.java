package com.jingtum.chainApp.mapper;

import java.util.List;

import com.jingtum.chainApp.entity.ClientTransactionEntity;

public interface TransactionMapper {
	public List<ClientTransactionEntity> selectTransaction(int transactionType);
}
