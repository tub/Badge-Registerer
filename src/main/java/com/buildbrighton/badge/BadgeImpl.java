package com.buildbrighton.badge;

import java.io.IOException;
import java.nio.ByteBuffer;
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
				
		if (data[1] != SENDING_DATA) {
			if (id != currentBadgeId) {
				// new badge ID. reset values
				if(id > 0 && id <= 240){
					currentBadgeId = id;
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
			byte[] data = new byte[] { (byte) 0xBB, (byte) 250, Mode.PROGRAM_BADGE_ID.mode(), (byte) randomBadgeId };
			
			byte[] inv = new byte[data.length];
			for(int i = 0; i < data.length; i++){
				int di = data[i] & 0xFF;
				di = ~di;
				inv[i] = (byte)di;
			}
			
			serial.write(new byte[]{0x00, 0x00, 0x00, 0x00});
			serial.write(data);
			serial.write(inv);
			
			log.debug(Integer.toHexString(ByteBuffer.wrap(data).getInt()));
			log.debug(Integer.toHexString(ByteBuffer.wrap(inv).getInt()));
			
		} catch (IOException e) {
			e.printStackTrace();
		}
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
