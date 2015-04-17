package com.easylite.model;

import java.util.Date;

import com.easylite.annotation.Entity;
import com.easylite.annotation.GenerationType;
import com.easylite.annotation.Id;

@Entity
public class Book {
	@Id(strategy = GenerationType.AUTO)
	private int id;
	private String reciever;
	private boolean isRecieved;
	private Date dateRecieved;
	private int amountSent;
	
	public boolean isRecieved() {
		return isRecieved;
	}
	public void setRecieved(boolean isRecieved) {
		this.isRecieved = isRecieved;
	}
	public Date getDateRecieved() {
		return dateRecieved;
	}
	public void setDateRecieved(Date dateRecieved) {
		this.dateRecieved = dateRecieved;
	}
	public int getAmountSent() {
		return amountSent;
	}
	public void setAmountSent(int amountSent) {
		this.amountSent = amountSent;
	}
	public String getReciever() {
		return reciever;
	}
	public void setReciever(String reciever) {
		this.reciever = reciever;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
}
