package com.buildbrighton.badge;

public class BadgeUtils {
	public static int idToHue(int id) {
		if (id > 240) {
			throw new IllegalArgumentException("ID is greater than 240");
		}
		if (id < 1) {
			throw new IllegalArgumentException("ID is less than 1");
		}

		return (id / 240) * 360;
	}
}
