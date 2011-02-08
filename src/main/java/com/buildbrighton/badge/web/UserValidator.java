package com.buildbrighton.badge.web;

import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

import com.buildbrighton.badge.db.User;

public class UserValidator implements Validator {

	public boolean supports(Class<?> clazz) {
		return User.class == clazz;
	}

	public void validate(Object target, Errors errors) {
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "name", "required", "Field is required.");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "email", "required", "Field is required.");
	}

}
