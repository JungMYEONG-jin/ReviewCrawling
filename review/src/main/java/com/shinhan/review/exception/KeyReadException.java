/**
 * @author �赿��
 *
 * 2012.09.09
 */
package com.shinhan.review.exception;

public class KeyReadException extends RuntimeException {
	private static final long serialVersionUID = 1L;

	public KeyReadException() {

	}

	public KeyReadException(String message) {
		super("[KeyReadException] "+message);
	}

	public KeyReadException(String message, Exception e) {
		super("[KeyReadException] "+message, e);
	}
}
