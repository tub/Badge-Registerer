package com.buildbrighton.badge.db;

import java.util.Set;

public interface UserDao {
	public void saveUser ( User user );
	
	public User getUserById(int id);
	
	public Set<Integer> getUserIds();
}