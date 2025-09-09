/**
 * 
 */
package com.hitech.dms.web.model.jobcard.response;

import java.math.BigInteger;
import java.util.Date;

import lombok.Data;

/**
 * @author santosh.kumar
 *
 */
@Data
public class JobCardServiceHistoryModelResponse {
	private BigInteger roId;
    private String roNumber;
    private Date openingDate;
    private String srvTypeDesc;
    private BigInteger totalHour;

}
