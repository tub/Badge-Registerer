package com.buildbrighton.badge;

import java.util.HashMap;
import java.util.Map;

public class BadgeListenerImpl implements BadgeListener, Badge {

	private static byte currentBadgeId;
	private static byte currentBadgeValue;
	
	private static Mode currentBadgeMode;
	private static byte BB = (byte) 0xbb;

	private static Map<Byte, Byte> colourValues = new HashMap<Byte, Byte>(127);

	private static byte SENDING_DATA = (byte) 0x00;


	public synchronized void dataAvailable(byte[] data) {
		if (data.length < 4) {
			System.err.println(String.format("Got %s bytes from serial", data.length));
			return;
		}

		if (data[0] != BB) {
			System.err.println(String.format("Got %s instead of 0xbb for device ID",
			        data[0]));
			return;
		}
		byte id = data[1];
		if (id != SENDING_DATA) {
			if (id != BadgeListenerImpl.currentBadgeId) {
				//new badge ID. reset values and 
				BadgeListenerImpl.currentBadgeId = data[1];
				synchronized (BadgeListenerImpl.colourValues) {
					BadgeListenerImpl.colourValues.clear();
				}
			}
			BadgeListenerImpl.currentBadgeMode = Mode.valueOf(data[2]);
			BadgeListenerImpl.currentBadgeValue = data[3];
		} else {
			synchronized (BadgeListenerImpl.colourValues) {
				BadgeListenerImpl.colourValues.put(data[2], data[3]);
			}
		}

		if (isHeader(data)) {
			synchronized (BadgeListenerImpl.colourValues) {
				BadgeListenerImpl.colourValues.clear();
			}
		}

	}

	private boolean isHeader(byte[] data) {
		return data[2] == (byte) 0x05 && data[3] == (byte) 0x00;
	}

	public byte getId() {
		return BadgeListenerImpl.currentBadgeId;
	}

	public Mode getMode() {
		return BadgeListenerImpl.currentBadgeMode;
	}
	
	public byte getValue(){
		return BadgeListenerImpl.currentBadgeValue;
	}

	public Map<Byte, Byte> getColours() {
		synchronized (BadgeListenerImpl.colourValues) {
			return new HashMap<Byte, Byte>(BadgeListenerImpl.colourValues);
		}
	}

}
