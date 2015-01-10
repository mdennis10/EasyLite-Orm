package com.easylite.model;

import com.easylite.annotation.Entity;
import com.easylite.annotation.GenerationType;
import com.easylite.annotation.Id;

@Entity
public class Car {
	@Id(generationType  = GenerationType.AUTO)
	public Integer id;
}
