/**
 * 
 */
package com.hitech.dms.web.model.inv.cancel.request;

import java.math.BigInteger;
import java.util.Date;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.hitech.dms.app.utils.DateHandler;

import lombok.Data;

/**
 * @author dinesh.jakhar
 *
 */
@Data
public class InvCancelRequestModel {
	private Integer pcId;
	private BigInteger dealerId;
	private BigInteger branchId;
	private BigInteger salesInvoiceHdrId;
	private String invoiceNumber;
	private BigInteger invCancelReasonId;
	@JsonDeserialize(using = DateHandler.class)
	private Date invCancelDate;
	private String invCancelRemark;
	private String invCancelType;
}
