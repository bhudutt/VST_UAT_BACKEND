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
public class TyreDetailsRequest {
	 private String frontRhPsi;
	    private String frontLhPsi;
	    private String rearRhPsi;
	    private String rearLhPsi;
	    private int inwordPdiId;

}
