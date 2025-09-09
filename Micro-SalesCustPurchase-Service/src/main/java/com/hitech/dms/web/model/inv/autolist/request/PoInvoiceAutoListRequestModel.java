/**
 * 
 */
package com.hitech.dms.web.model.inv.autolist.request;

import java.math.BigInteger;

import lombok.Getter;
import lombok.Setter;

/**
 * @author dinesh.jakhar
 *
 */
@Getter
@Setter
public class PoInvoiceAutoListRequestModel {
	private BigInteger dealerId;
	private BigInteger branchId;
	private Integer pcId;
	private BigInteger toDealerId;
	private Integer invoiceTypeId;
	private String searchText;
	private String isFor;
}
