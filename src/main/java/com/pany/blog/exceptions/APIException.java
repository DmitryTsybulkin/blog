package com.pany.blog.exceptions;

public class APIException extends RuntimeException {

	public int errorCode;
	public String message;

	public APIException() {}

	public APIException(int errorCode, String message) {
		this.errorCode = errorCode;
		this.message = message;
	}

}
