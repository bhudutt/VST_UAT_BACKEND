
/**
 * 
 */
package com.hitech.dms.web.model.jobcard.search.response;

import lombok.Data;

/**
 * @author santosh.kumar
 *
 */
@Data
public class PaintScratchedResponse {
	private String paintScratchedCode;
    private String paintScratchedDesc;
    private int paintScratchedId;
    private String diesel;
    private String finalActionTaken;
    private String adviceToCustomer;

}
