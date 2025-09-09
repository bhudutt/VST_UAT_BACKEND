/**
 * 
 */
package com.hitech.dms.web.model.scheme.additionalCriteria.response;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @author dinesh.jakhar
 *
 */
@Getter
@Setter
@ToString
public class AdditinalCriteriaResponseModel {
	private Integer id;
	private String criteriaCode;
	private String criteriaName;
	private Float criteriaPer;
}
