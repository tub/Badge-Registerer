package com.buildbrighton.badge.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.buildbrighton.badge.Badge;

@Controller
public class DefaultController extends BaseController{
	private Badge badge;
	
	public DefaultController(){
		System.out.println("Starting.");
	}

	@RequestMapping({"/index.html", "home"})
	public ModelAndView test(ModelAndView mav){
		mav.setViewName("test");
		mav.addObject("badge", badge);
		mav.addObject("contextPath", contextPath);
		return mav;
	}

	@Autowired
	public void setBadge(Badge badge) {
	    this.badge = badge;
    }
	
}
