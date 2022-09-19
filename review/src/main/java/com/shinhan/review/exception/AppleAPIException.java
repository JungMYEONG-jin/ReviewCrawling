/**
 * @author �赿��
 *
 * 2012.09.09
 */
package com.shinhan.review.exception;

public class AppleAPIException extends RuntimeException {
	private static final long serialVersionUID = 1L;

	public AppleAPIException() {

	}

	public AppleAPIException(String message) {
		super("[AppleAPIException] "+message);
	}

	public AppleAPIException(String message, Exception e) {
		super("[AppleAPIException] "+message, e);
	}

	public AppleAPIException(Throwable e){
		super(e);
	}
}
