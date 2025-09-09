/**
 * 
 */
package com.hitech.dms.web.model.inv.common.response;

import lombok.Getter;
import lombok.Setter;

/**
 * @author dinesh.jakhar
 *
 */
@Getter
@Setter
public class InvoiceTypeResponseModel {
	private Integer invoiceTypeId;
	private String invoiceTypeCode;
	private String invoiceTypeDesc;
	private String applicableToDealer;
	private String applicableToDistributor;
}
