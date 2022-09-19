/**
 * @author �赿��
 *
 * 2012.09.09
 */
package com.shinhan.review.exception;

public class GooleAPIException extends RuntimeException {
	private static final long serialVersionUID = 1L;

	public GooleAPIException() {

	}

	public GooleAPIException(String message) {
		super("[GooleAPIException] "+message);
	}

	public GooleAPIException(String message, Exception e) {
		super("[GooleAPIException] "+message, e);
	}

	public GooleAPIException(Throwable e){
		super(e);
	}
}
