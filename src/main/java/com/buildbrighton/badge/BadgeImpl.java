package com.buildbrighton.badge;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.Set;

import com.buildbrighton.badge.db.User;
import com.buildbrighton.badge.db.UserDao;
import com.buildbrighton.badge.serial.BadgeSerialListener;

public class BadgeImpl implements BadgeDataListener, Badge {

	private int currentBadgeId;
	private int currentBadgeValue;

	private Mode currentBadgeMode;
	private byte BB = (byte) 0xbb;

	private static Map<Integer, Integer> colourValues = new HashMap<Integer, Integer>(127);

	private static byte SENDING_DATA = (byte) 0x00;

	private UserDao userDao;
	private BadgeSerialListener serial;
	private BadgeEventListener badgeEventListener;

	public synchronized void dataAvailable(byte[] data) {
		if (data.length < 4) {
			System.err.println(String.format("Got %s bytes from serial",
			        data.length));
			return;
		}

		if (data[0] != BB) {
			System.err.println(String.format(
			        "Got %s instead of 0xbb for device ID", data[0]));
			return;
		}
		byte id = data[1];
				
		if (id != SENDING_DATA) {
			if (id != currentBadgeId) {
				// new badge ID. reset values
				currentBadgeId = unsignedByteToInt(data[1]);
				synchronized (BadgeImpl.colourValues) {
					BadgeImpl.colourValues.clear();
				}
				//Send 'new id' event
				if(badgeEventListener != null){
					badgeEventListener.badgeIdChanged(this);
				}
			}
			
			currentBadgeMode = Mode.valueOf(data[2]);
			currentBadgeValue = unsignedByteToInt(data[3]);

			if (currentBadgeMode == Mode.INIT) {
				synchronized (this) {
					// Get new ID, store it (empty) in the database so we don't assign the same ID twice
					// then send the ID to serial/IR
					int randomBadgeId = getRandomBadgeId();
					User user = new User();
					user.setId(randomBadgeId);
					userDao.saveUser(user);
					sendNewId(randomBadgeId);
				}
			}
		} else {
			synchronized (BadgeImpl.colourValues) {
				BadgeImpl.colourValues.put(unsignedByteToInt(data[2]), unsignedByteToInt(data[3]));
			}
		}

		if (isHeader(data)) {
			synchronized (BadgeImpl.colourValues) {
				BadgeImpl.colourValues.clear();
			}
		}

	}
	
	public static int unsignedByteToInt(byte b) {
		return (int) b & 0xFF;
	}


	private void sendNewId(int randomBadgeId) {
		try {
			serial.write(new byte[] { (byte) randomBadgeId });
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private int getRandomBadgeId() {
		Set<Integer> ids = userDao.getUserIds();
		if (ids.size() == 249) {
			// Don't get into an infinite loop.
			throw new RuntimeException("Run out of badge IDs!");
		}
		Random random = new Random();
		// between 1 and 250
		int rInt = 0;
		do {
			rInt = random.nextInt(249) + 1;
		} while (ids.contains(rInt));

		return rInt;
	}

	private boolean isHeader(byte[] data) {
		return data[2] == (byte) 0x05 && data[3] == (byte) 0x00;
	}

	public int getId() {
		return currentBadgeId;
	}

	public Mode getMode() {
		return currentBadgeMode;
	}

	public int getValue() {
		return currentBadgeValue;
	}

	public Map<Integer, Integer> getColours() {
		synchronized (BadgeImpl.colourValues) {
			return new HashMap<Integer, Integer>(BadgeImpl.colourValues);
		}
	}

	public void setUserDao(UserDao userDao) {
		this.userDao = userDao;
	}

	public void setSerial(BadgeSerialListener serial) {
		this.serial = serial;
	}

	public void setBadgeEventListener(BadgeEventListener l) {
	    this.badgeEventListener = l;
    }

}
