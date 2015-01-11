package com.easylite.model;

import com.easylite.annotation.Table;
import com.easylite.annotation.GenerationType;
import com.easylite.annotation.Id;

@Table
public class Car {
	@Id(strategy = GenerationType.AUTO)
	public Integer id;
}
