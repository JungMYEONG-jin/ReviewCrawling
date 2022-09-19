/**
 * @author �赿��
 *
 * 2012.09.09
 */
package com.shinhan.review.exception;

public class JWTException extends RuntimeException {
	private static final long serialVersionUID = 1L;

	public JWTException() {

	}

	public JWTException(String message) {
		super("[JWTException] "+message);
	}

	public JWTException(String message, Exception e) {
		super("[JWTException] "+message, e);
	}
}
