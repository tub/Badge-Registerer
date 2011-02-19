package com.buildbrighton.badge.db;

import java.util.HashMap;
import java.util.Map;

import org.codehaus.jackson.map.ser.MapSerializer;
import org.junit.Ignore;

import com.buildbrighton.badge.Badge;

public class User {
	private int id;
	private String name;
	private String emailAddress;
	private String twitterId;
	private boolean announcements;
	private Map<Integer, Integer> colours = new HashMap<Integer, Integer>();

	public User() {
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		checkIdValid(id);
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

	public String getTwitterId() {
    	return twitterId;
    }

	public void setTwitterId(String twitterId) {
    	this.twitterId = twitterId;
    }

	public boolean isAnnouncements() {
    	return announcements;
    }

	public void setAnnouncements(boolean announcements) {
    	this.announcements = announcements;
    }

	public Map<Integer, Integer> getColours() {
		return colours;
	}

	public void setColours(Map<Integer, Integer> colours) {
		this.colours = colours;
	}

	public int hue() {
		return (id * 360) / Badge.MAX_ID;
	}
	
	public int hue(int id) {
		return (id * 360) / Badge.MAX_ID;
	}
	
	private void checkIdValid(int id){
		if (id > 240) {
			throw new IllegalArgumentException("ID is greater than 240");
		}
		if (id < 1) {
			throw new IllegalArgumentException("ID is less than 1");
		}
	}
}
