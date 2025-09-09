/**
 * 
 */
package com.hitech.dms.web.model.admin.org.search.response;

import java.math.BigInteger;

import lombok.Getter;
import lombok.Setter;

/**
 * @author dinesh.jakhar
 *
 */
@Getter
@Setter
public class OrgLevelHierSearchResponseModel {
	private String action;
	private BigInteger id; // dealerId
	private Integer id1; // pcId
	private Integer id2; // departmentId
	private BigInteger id3; // orgHierId
	private String dealerCode;
	private String dealerName;
	private String departmentName;
	private String hierCode;
	private String hierDesc;
	private String pcDesc;
}
