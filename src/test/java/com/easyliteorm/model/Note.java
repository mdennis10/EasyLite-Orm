package com.easyliteorm.model;

import com.easyliteorm.annotation.Entity;
import com.easyliteorm.annotation.Foreign;
import com.easyliteorm.annotation.Id;

import java.util.Date;

@Entity(name = "Note")
public class Note {
	@Id
	public int id;
	public String body;
	public String author;
	public boolean sent;
	public Date date;
	public double price;

	@Foreign
    public Book book;
}
