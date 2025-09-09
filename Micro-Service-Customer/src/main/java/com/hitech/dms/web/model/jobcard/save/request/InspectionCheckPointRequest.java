/**
 * 
 */
package com.hitech.dms.web.model.jobcard.save.request;

import java.time.LocalDateTime;
import java.util.Date;

import javax.persistence.Column;

import lombok.Data;

/**
 * @author santosh.kumar
 *
 */
@Data
public class InspectionCheckPointRequest {
	private Boolean okFlag = false;
	private Integer checkPoint;
	private Integer installDtlId;
	private Date CreatedDate;
	private String CreatedBy;

}
