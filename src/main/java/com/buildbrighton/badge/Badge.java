package com.buildbrighton.badge;

import java.util.Map;

import com.buildbrighton.badge.BadgeDataListener.Mode;

public interface Badge {
	public int getId();

	public Mode getMode();
	
	public int getValue();

	public Map<Integer, Integer> getColours();
}
