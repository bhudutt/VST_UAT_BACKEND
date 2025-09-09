/**
 * 
 */
package com.hitech.dms.web.model.activity.source.form.request;

import lombok.Data;

/**
 * @author dinesh.jakhar
 *
 */
@Data
public class SourceFormRequestModel {
	private String sourceCode;
	private String sourceDescription;
	private Boolean isActive;
	private Boolean isSubSourceRequired;
}
