package com.ecom.proj.ecommerce_backend.exception;

public class UserNotVerifiedException extends Exception{

	private boolean newEmailSent;

	public UserNotVerifiedException(boolean newEmailSent) {
		super();
		this.newEmailSent = newEmailSent;
	}

	public boolean isNewEmailSent() {
		return newEmailSent;
	}
	
	
	
}
