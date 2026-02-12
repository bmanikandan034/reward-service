package com.rewards.service.error;

/***
 * @author Manikandan B
 */
public class ResourceNotFoundException extends RuntimeException {
	public ResourceNotFoundException(String msg) {
		super(msg);
	}
}