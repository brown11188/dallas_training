package com.api.model;
//default package
//Generated Mar 7, 2016 4:08:44 PM by Hibernate Tools 4.3.1.Final

import java.util.Date;

/**
* TblMessage generated by hbm2java
*/
public class TblMessage implements java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7050183549237121595L;
	private Integer messageId;
	private String message;
	private Date expiresTime;
	private Integer senderId;
	private Integer userId;

	public TblMessage() {
	}

	public TblMessage(Date expiresTime) {
		this.expiresTime = expiresTime;
	}

	public TblMessage(String message, Date expiresTime, Integer senderId, Integer userId) {
		this.message = message;
		this.expiresTime = expiresTime;
		this.senderId = senderId;
		this.userId = userId;
	}

	public Integer getMessageId() {
		return this.messageId;
	}

	public void setMessageId(Integer messageId) {
		this.messageId = messageId;
	}

	public String getMessage() {
		return this.message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public Date getExpiresTime() {
		return this.expiresTime;
	}

	public void setExpiresTime(Date expiresTime) {
		this.expiresTime = expiresTime;
	}

	public Integer getSenderId() {
		return this.senderId;
	}

	public void setSenderId(Integer senderId) {
		this.senderId = senderId;
	}

	public Integer getUserId() {
		return this.userId;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}

}

