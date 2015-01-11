package com.easylite.model;

import java.util.Date;

import com.easylite.annotation.Table;
import com.easylite.annotation.Id;

@Table(name = "Note")
public class Note {
	@Id
	public int id;
	public String body;
	public String author;
	public boolean sent;
	public Date date;
}
