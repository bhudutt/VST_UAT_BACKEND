/**
 * 
 */
package com.hitech.dms.web.model.enquiry.stages.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.Data;

/**
 * @author dinesh.jakhar
 *
 */
@Data
@JsonInclude(Include.NON_NULL)
public class EnqStageResponseModel {
	private Integer enqStageId;
	private String stageCode;
	private String stageDesc;
}
