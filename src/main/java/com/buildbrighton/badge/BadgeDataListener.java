package com.buildbrighton.badge;

public interface BadgeDataListener {
	public void dataAvailable(byte[] data);
	public void setBadgeEventListener(BadgeEventListener l);
}
