/**
 * 
 */
package com.hitech.dms.web.model.org.response;

import java.math.BigInteger;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.Data;

/**
 * @author dinesh.jakhar
 *
 */
@Data
@JsonInclude(Include.NON_NULL)
public class OrgLevelByDeptResponseModel {
	private Integer levelID;
	private String levelCode;
	private String levelDesc;
	private BigInteger sqNo;
}
