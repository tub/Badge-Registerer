package com.buildbrighton.badge.db;

import java.util.Map;

public class User {
	private int id;
	private String name;
	private String emailAddress;
	private Map<Integer, Integer> colours;

	public User() {
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getEmailAddress() {
		return emailAddress;
	}

	public void setEmailAddress(String emailAddress) {
		this.emailAddress = emailAddress;
	}

	public Map<Integer, Integer> getColours() {
		return colours;
	}

	public void setColours(Map<Integer, Integer> colours) {
		this.colours = colours;
	}
}
