package com.buildbrighton.badge;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.buildbrighton.badge.db.User;
import com.buildbrighton.badge.db.UserDao;
import com.buildbrighton.badge.serial.BadgeSerialListener;

public class BadgeImpl implements BadgeDataListener, Badge {
	Logger log = LoggerFactory.getLogger(this.getClass());

	private int currentBadgeId;
	private int currentBadgeValue;

	private Mode currentBadgeMode;
	private byte BB = (byte) 0xbb;

	private static Map<Integer, Integer> colourValues = new HashMap<Integer, Integer>(127);

	private static byte SENDING_DATA = (byte) 0x00;

	private UserDao userDao;
	private BadgeSerialListener serial;
	private BadgeEventListener badgeEventListener;

	private long timestamp;

	private boolean coloursReceived;

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
		int id = unsignedByteToInt(data[1]);

		timestamp = new Date().getTime();
		
		if (data[1] != SENDING_DATA) {
			if (id != currentBadgeId) {
				// new badge ID. reset values
				if(id > 0 && id <= 240){
					currentBadgeId = id;
					coloursReceived = false;
					synchronized (BadgeImpl.colourValues) {
						BadgeImpl.colourValues.clear();
					}
					//Send 'new id' event
					if(badgeEventListener != null){
						badgeEventListener.badgeIdChanged(this);
					}
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
			}else if(currentBadgeMode == Mode.CYCLE && !coloursReceived){
				serial.sendPacket(Mode.SEND_ME_EEPROM, (byte) 0);
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
		}else if(isFooter(data)){
			coloursReceived = true;
			log.info("Received all colours.");
			User user = userDao.getUserById(currentBadgeId);
			if(user != null){
				user.setColours(colourValues);
				userDao.saveUser(user);
			}
		}

	}
	
	private boolean isHeader(byte[] data) {
		return data[2] == (byte) 0x05 && data[3] == (byte) 0x00;
	}
	
	private boolean isFooter(byte[] data) {
		return data[2] == (byte) 0x05 && data[3] == (byte) 0xFF;
    }

	public static int unsignedByteToInt(byte b) {
		return (int) b & 0xFF;
	}


	private void sendNewId(int randomBadgeId) {
			serial.sendPacket(Mode.PROGRAM_BADGE_ID, (byte)randomBadgeId);
	}

	private int getRandomBadgeId() {
		Set<Integer> ids = userDao.getUserIds();
		if (ids.size() == 239) {
			// Don't get into an infinite loop.
			throw new RuntimeException("Run out of badge IDs!");
		}
		Random random = new Random();
		// between 1 and 240
		int rInt = 0;
		do {
			rInt = random.nextInt(239) + 1;
		} while (ids.contains(rInt));

		return rInt;
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

	@Override
    public long getLatestEventTimestamp() {
		return timestamp;
    }

	public boolean isColoursReceived() {
	    return coloursReceived;
    }

}
