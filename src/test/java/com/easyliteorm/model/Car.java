package com.easyliteorm.model;

import com.easyliteorm.annotation.Entity;
import com.easyliteorm.annotation.GenerationType;
import com.easyliteorm.annotation.Id;

@Entity
public class Car {
	@Id(strategy = GenerationType.AUTO)
	public Integer id;
}
