/**
 * 
 */
package com.hitech.dms.web.model.jobcard.save.request;

import lombok.Data;

/**
 * @author santosh.kumar
 *
 */
@Data
public class PdiChecklistRequest {
	    private String fieldType;
	    private String defaultTick;
	    private String checkpointDesc;
	    private Integer checkpointId;
	    private Integer checkpointSequenceNo;
	    private Integer checklist_mapping_id;
	    private Integer aggregateId;
	    private String observedSpecification;
	    private int pdiDtlId;
	    private String remark;
	    private String activeStatus;
	    private String aggregate;
	    private int aggregateSequenceNo;
	    private String specification;
}
