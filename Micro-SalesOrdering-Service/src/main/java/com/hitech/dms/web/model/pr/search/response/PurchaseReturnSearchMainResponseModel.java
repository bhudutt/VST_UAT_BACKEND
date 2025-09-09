/**
 * 
 */
package com.hitech.dms.web.model.pr.search.response;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

/**
 * @author dinesh.jakhar
 *
 */
@Getter
@Setter
public class PurchaseReturnSearchMainResponseModel {
	private List<PurchaseReturnSearchResponseModel> searchList;
	private Integer recordCount;
}
