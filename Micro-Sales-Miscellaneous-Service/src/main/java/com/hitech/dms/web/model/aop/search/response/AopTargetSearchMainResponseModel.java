/**
 * 
 */
package com.hitech.dms.web.model.aop.search.response;

import java.util.List;

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
public class AopTargetSearchMainResponseModel {
	private List<AopTargetSearchResponseModel> searchList;
	private int recordCount;
}
