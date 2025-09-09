/**
 * 
 */
package com.hitech.dms.web.model.inv.search.response;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

/**
 * @author dinesh.jakhar
 *
 */
@Getter
@Setter
public class InvoiceSearchMainResponseModel {
	private List<InvoiceSearchResponseModel> searchList;
	private Integer recordCount;
}
