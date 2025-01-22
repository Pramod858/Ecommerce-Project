package com.project.exception;

public class CartNotFoundException extends Exception{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public CartNotFoundException(String message) {
		super(message);
	}
}
