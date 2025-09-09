package com.hitech.dms.web.model.export.response;

import lombok.Data;

/**
 * 
 * @author dinesh.jakhar
 *
 */
@Data
public class ExportResponseModel {
	private String msg;
	private String fileName;
	private boolean isFileCreated;
}
