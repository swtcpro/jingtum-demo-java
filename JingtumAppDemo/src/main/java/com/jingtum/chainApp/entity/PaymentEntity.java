package com.jingtum.chainApp.entity;

/**
 * @author laiws
 * 支付请求实体类
 */
public class PaymentEntity {
	String secret;
	String client_id;	
	Payment payment;
	
	public Payment getPayment() {
		return payment;
	}


	public void setPayment(Payment payment) {
		this.payment = payment;
	}


	public String getSecret() {
		return secret;
	}


	public void setSecret(String secret) {
		this.secret = secret;
	}


	public String getClient_id() {
		return client_id;
	}


	public void setClient_id(String client_id) {
		this.client_id = client_id;
	}
	
	public class Payment{
		String source;
		String destination;
		AmountEntity amount;
		String choice;
		String[] memos;
		
		public String getSource() {
			return source;
		}
		public void setSource(String source) {
			this.source = source;
		}
		public String getDestination() {
			return destination;
		}
		public void setDestination(String destination) {
			this.destination = destination;
		}
		public AmountEntity getAmount() {
			return amount;
		}
		public void setAmount(AmountEntity amount) {
			this.amount = amount;
		}
		public String getChoice() {
			return choice;
		}
		public void setChoice(String choice) {
			this.choice = choice;
		}
		public String[] getMemos() {
			return memos;
		}
		public void setMemos(String[] memos) {
			this.memos = memos;
		}
	}
}
