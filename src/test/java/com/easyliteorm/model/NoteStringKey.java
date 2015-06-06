package com.easyliteorm.model;

import java.util.Date;

import com.easyliteorm.annotation.Entity;
import com.easyliteorm.annotation.Id;

@Entity
public class NoteStringKey {
	public int id;
	public String body;
	@Id
	public String author;
	public boolean sent;
	public Date date;
}
