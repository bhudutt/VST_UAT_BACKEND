/**
 * 
 */
package com.hitech.dms.web.model.spare.create.resquest;

import lombok.Data;

/**
 * @author santosh.kumar
 *
 */
@Data
public class SparePoDealerAndDistributorRequest {
	private Integer dealerId;
	private String dealerCode;
	private String isfor;

}
