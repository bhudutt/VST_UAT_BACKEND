/**
 * 
 */
package com.hitech.dms.web.model.scheme.additionalCriteria.response;

import java.util.List;
import java.util.Map;

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
public class SchemeTypeOnchangeResponseModel {
	private Map<String, Integer> monthList;
	private Map<Integer, Integer> yearList;
	private List<AdditinalCriteriaResponseModel> additionalCriteriaList;
}
