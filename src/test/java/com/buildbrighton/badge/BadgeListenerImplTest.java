package com.buildbrighton.badge;

import static org.junit.Assert.*;

import java.util.Map;

import org.junit.Test;

import com.buildbrighton.badge.BadgeDataListener.Mode;

public class BadgeListenerImplTest {

	@Test
	public void testDataAvailable() {
		BadgeImpl b = new BadgeImpl();
		//Send header packet
		b.dataAvailable(new byte[]{(byte)0xbb, (byte)0x13, Mode.SENDING_EEPROM.mode(), (byte)0x00});
		b.dataAvailable(new byte[]{(byte)0xbb, (byte)0x00, (byte)0x01, (byte)0x45});
		b.dataAvailable(new byte[]{(byte)0xbb, (byte)0x00, (byte)0x02, (byte)0x47});
		b.dataAvailable(new byte[]{(byte)0xbb, (byte)0x00, (byte)0x03, (byte)0x54});
		b.dataAvailable(new byte[]{(byte)0xbb, (byte)0x00, (byte)0x04, (byte)0x42});
		b.dataAvailable(new byte[]{(byte)0xbb, (byte)0x13, Mode.SENDING_EEPROM.mode(), (byte)0xff});
		
		assertEquals((byte)0x13, b.getId());
		assertEquals(Mode.SENDING_EEPROM, b.getMode());
		
		Map<Byte, Byte> v = b.getColours();
		assertEquals(v.size(), 4);
		
		//Send header packet
		b.dataAvailable(new byte[]{(byte)0xbb, (byte)0x34, Mode.SENDING_EEPROM.mode(), (byte)0x00});
		b.dataAvailable(new byte[]{(byte)0xbb, (byte)0x00, (byte)0x01, (byte)0x49});
		b.dataAvailable(new byte[]{(byte)0xbb, (byte)0x00, (byte)0x02, (byte)0x43});
		b.dataAvailable(new byte[]{(byte)0xbb, (byte)0x34, Mode.SENDING_EEPROM.mode(), (byte)0xff});
		
		assertEquals((byte)0x34, b.getId());
		assertEquals(Mode.SENDING_EEPROM, b.getMode());
		
		Map<Byte, Byte> v2 = b.getColours();
		//Make sure we're not returning references
		assertEquals(v.size(), 4);
		
		assertEquals(v2.size(), 2);
	}

}
