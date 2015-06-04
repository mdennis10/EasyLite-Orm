package com.easyliteorm.model;

import java.util.Date;

import com.easyliteorm.annotation.Entity;
import com.easyliteorm.annotation.Id;

@Entity(name = "Note")
public class Note {
	@Id
	public int id;
	public String body;
	public String author;
	public boolean sent;
	public Date date;
}
