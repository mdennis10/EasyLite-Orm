package com.easylite.model;

import com.easylite.annotation.Entity;
import com.easylite.annotation.GenerationType;
import com.easylite.annotation.Id;

@Entity
public class NonNumeric {
	@Id (strategy = GenerationType.MANUAL)
	private String id;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}
	
}
