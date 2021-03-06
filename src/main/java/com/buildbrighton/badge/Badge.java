package com.buildbrighton.badge;

import java.util.Map;

public interface Badge {
	
	public static int MAX_ID = 240;
	
	public enum Mode {
		INIT((byte) 0x01), ZOMBIE((byte) 0x03), INFECTED((byte) 0x04), 
		SENDING_EEPROM((byte) 0x05), CYCLE((byte) 0x07), SEND_ME_EEPROM((byte) 0x08), PROGRAM_BADGE_ID((byte) 0x09);

		private final byte mode;

		Mode(byte mode) {
			this.mode = mode;
		}

		public byte mode() {
			return mode;
		}
		
		public static Mode valueOf(byte b){
			for(Mode m :Mode.values()){
				if(m.mode() == b){
					return m;
				}
			}
			return null;
		}
	}
	
	public int getId();

	public Mode getMode();
	
	public int getValue();

	public Map<Integer, Integer> getColours();
	
	public long getLatestEventTimestamp();
	
	public boolean isColoursReceived();
}
