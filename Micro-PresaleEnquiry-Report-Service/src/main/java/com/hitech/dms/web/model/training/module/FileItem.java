package com.hitech.dms.web.model.training.module;

public class FileItem {

	private String name;
    private boolean directoryVal;
    
    public FileItem(String name, boolean isDirectory) {
        this.name = name;
        this.directoryVal = isDirectory;
    }

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public boolean isDirectoryVal() {
		return directoryVal;
	}

	public void setDirectoryVal(boolean directoryVal) {
		this.directoryVal = directoryVal;
	}

   
}
