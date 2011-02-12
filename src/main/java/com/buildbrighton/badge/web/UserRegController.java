package com.buildbrighton.badge.web;

import java.util.HashSet;
import java.util.Set;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.buildbrighton.badge.Badge;
import com.buildbrighton.badge.db.User;
import com.buildbrighton.badge.db.UserDao;

@Controller
public class UserRegController extends BaseController {
	private Badge badge;

	private UserDao userDao;

	@RequestMapping("/register.html")
	public ModelAndView test(ModelAndView mav) {
		mav.setViewName("register");
		mav.addObject("badge", badge);
		mav.addObject("user", new User());
		mav.addObject("contextPath", contextPath);
		return mav;
	}

	@Autowired
	@Required
	public void setBadge(Badge badge) {
		this.badge = badge;
	}

	@Autowired
	@Required
	public void setUserDao(UserDao userDao) {
		this.userDao = userDao;
	}

	@RequestMapping(value = "/saveUser.html", method = RequestMethod.POST)
	public ModelAndView saveUser(@RequestParam int id,
	        @RequestParam String name, @RequestParam String emailAddress) {

		User user = new User();
		user.setName(name);
		user.setId(id);
		user.setEmailAddress(emailAddress);
		
		userDao.saveUser(user);

		return new ModelAndView(String.format("redirect:/users/%s.html",id));
	}

	@RequestMapping(value = "/users/{user}.html", method = RequestMethod.GET)
	public ModelAndView viewUser(ModelAndView mav, @PathVariable Integer user) {
		mav.addObject("user", userDao.getUserById(user));
		mav.addObject("contextPath", contextPath);
		mav.setViewName("showUser");
		return mav;
	}
	
	@RequestMapping(value = "/currentBadge.json", method = RequestMethod.GET)
	public ModelAndView currentBadge(ModelAndView mav, HttpServletResponse rsp) {
		rsp.setContentType("application/json");
		mav.addObject("user", userDao.getUserById(badge.getId()));
		mav.addObject("badge", badge);
		mav.addObject("contextPath", contextPath);
		mav.setViewName("currentBadgeJson");
		return mav;
	}

	@RequestMapping(value = "/users.html", method = RequestMethod.GET)
	public ModelAndView viewUsers(ModelAndView mav) {
		Set<Integer> userIds = userDao.getUserIds();
		mav.addObject("userIds", userIds);
		Set<User> users = new HashSet<User>();
		for(Integer uid:userIds){
			users.add(userDao.getUserById(uid));
		}
		mav.addObject("users",users);
		mav.addObject("contextPath", contextPath);
		mav.setViewName("showUsers");
		return mav;
	}

}
