/**
 * 
 */
package com.hitech.dms.web.model.stock.receipt.search.response;

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
public class ReceiptSearchMainResponseModel {
	private List<ReceiptSearchResponseModel> searchList;
	private Integer recordCount;
}
