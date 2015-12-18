package com.vertx.bankpayform;
import java.io.Serializable;
import java.util.Date;


public class TwbankForm implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String payAccount;
	private Integer payMoney;
	private String title;
	private String payDeadline; //format yyyymmdd
	private String payName;
	
	public String getPayAccount() {
		return payAccount;
	}
	public void setPayAccount(String payAccount) {
		this.payAccount = payAccount;
	}
	public Integer getPayMoney() {
		return payMoney;
	}
	public void setPayMoney(Integer payMoney) {
		this.payMoney = payMoney;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	
	public String getPayDeadline() {
		return payDeadline;
	}
	public void setPayDeadline(String payDeadline) {
		this.payDeadline = payDeadline;
	}
	public String getPayName() {
		return payName;
	}
	public void setPayName(String payName) {
		this.payName = payName;
	}
	@Override
	public String toString() {
		return "TwbankForm [payAccount=" + payAccount + ", payMoney=" + payMoney + ", title=" + title + ", payDeadline="
				+ payDeadline + ", payName=" + payName + "]";
	}
	
}
