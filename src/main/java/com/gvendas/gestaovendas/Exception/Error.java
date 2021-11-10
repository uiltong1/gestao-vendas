package com.gvendas.gestaovendas.Exception;

public class Error {
	private String message;
	private String error;

	public Error(String message, String error) {
		this.message = message;
		this.error = error;
	}

	public String getMessage() {
		return message;
	}

	public String getError() {
		return error;
	}

}
