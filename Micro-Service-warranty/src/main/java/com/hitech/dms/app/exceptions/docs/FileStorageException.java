package com.hitech.dms.app.exceptions.docs;

public class FileStorageException extends RuntimeException{
	public FileStorageException(String message) {
        super(message);
    }

    public FileStorageException(String message, Throwable cause) {
        super(message, cause);
    }

}
