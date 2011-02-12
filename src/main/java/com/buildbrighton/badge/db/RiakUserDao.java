package com.buildbrighton.badge.db;

import java.util.HashSet;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.keyvalue.riak.core.RiakTemplate;

public class RiakUserDao implements UserDao {

	@Autowired
	private RiakTemplate riakTemplate;

	public User getUserById(int id) {
		return riakTemplate.get("users", id);
	}

	public void saveUser(User user) {
		riakTemplate.set("users", user.getId(), user);
		Set<Integer> ids = getUserIds();
		ids.add(user.getId());
		saveUserIds(ids);
	}

	public void setRiakTemplate(RiakTemplate riak) {
		this.riakTemplate = riak;
	}

	public Set<Integer> getUserIds() {
		Set<Integer> userList = riakTemplate.get("users", "userList");
		if (userList == null) {
			userList = new HashSet<Integer>(128);
		}
		return userList;
	}

	private void saveUserIds(Set<Integer> userList) {
		riakTemplate.set("users", "userList", userList);
	}
	
	public boolean deleteUser(int id){
		Set<Integer> userIds = getUserIds();
		if(userIds.remove(id)){
			riakTemplate.delete("users", id);
			return true;
		}
		return false;
	}

}
