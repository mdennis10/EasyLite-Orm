package com.easylite.model;

import java.util.Date;

import com.easylite.annotation.Entity;
import com.easylite.annotation.Id;

@Entity(name = "Note")
public class Note {
	@Id
	public int id;
	public String body;
	public String author;
	public boolean sent;
	public Date date;
	private String reciever;
	
	public String getReciever() {
		return reciever;
	}
	public void setReciever(String reciever) {
		this.reciever = reciever;
	}
	
}
