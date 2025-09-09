/**
 * 
 */
package com.hitech.dms.web.model.activity.source.response;

import lombok.Data;

/**
 * @author dinesh.jakhar
 *
 */
@Data
public class SourceListResponseModel {
	private Integer enqSourceId;
	private String sourceCode;
	private String sourceDesccription;
	private Boolean isActive;
}
