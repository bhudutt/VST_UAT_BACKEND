/**
 * 
 */
package com.hitech.dms.web.model.pr.inv.search.response;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

/**
 * @author dinesh.jakhar
 *
 */
@Getter
@Setter
public class PrForInvoiceSearchMainResponseModel {
	private List<PrForInvoiceSearchResponseModel> searchList;
	private Integer recordCount;
}
