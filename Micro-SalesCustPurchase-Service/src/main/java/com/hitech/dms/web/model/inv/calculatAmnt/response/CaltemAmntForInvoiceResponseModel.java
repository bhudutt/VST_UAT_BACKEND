/**
 * 
 */
package com.hitech.dms.web.model.inv.calculatAmnt.response;

import java.math.BigDecimal;

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
public class CaltemAmntForInvoiceResponseModel {
	private BigDecimal basicPrice;
	private BigDecimal cgst_per;
	private BigDecimal sgst_per;
	private BigDecimal igst_per;
	private BigDecimal cgst_amnt;
	private BigDecimal sgst_amnt;
	private BigDecimal igst_amnt;
	private BigDecimal total_gst_per;
	private BigDecimal total_gst_amnt;
	private BigDecimal assessableAmnt;
	private BigDecimal totalAmnt;
}
