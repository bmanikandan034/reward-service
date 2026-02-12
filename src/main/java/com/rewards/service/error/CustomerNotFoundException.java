package com.rewards.service.error;

/***
 * @author Manikandan B
 */
public class CustomerNotFoundException extends RuntimeException {
	public CustomerNotFoundException(String message) {
		super(message);
	}
}