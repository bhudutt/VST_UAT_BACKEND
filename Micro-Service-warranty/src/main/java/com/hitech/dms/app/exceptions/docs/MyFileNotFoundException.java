package com.hitech.dms.app.exceptions.docs;

public class MyFileNotFoundException extends RuntimeException{
	 public MyFileNotFoundException(String message) {
	    super(message);
	 }

	    public MyFileNotFoundException(String message, Throwable cause) {
	       super(message, cause);
	    }

}
