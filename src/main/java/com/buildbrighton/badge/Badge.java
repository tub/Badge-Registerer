package com.buildbrighton.badge;

import java.util.Map;

import com.buildbrighton.badge.BadgeListener.Mode;

public interface Badge {
	public byte getId();

	public Mode getMode();
	
	public byte getValue();

	public Map<Byte, Byte> getColours();
}
