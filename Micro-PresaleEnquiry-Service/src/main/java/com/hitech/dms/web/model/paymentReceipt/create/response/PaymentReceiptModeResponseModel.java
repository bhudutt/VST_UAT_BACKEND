/**
 * @author vinay.gautam
 *
 */
package com.hitech.dms.web.model.paymentReceipt.create.response;

import java.math.BigInteger;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

/**
 * @author vinay.gautam
 *
 */
@Data
public class PaymentReceiptModeResponseModel {
	@JsonProperty(value = "receiptModeId")
	private Integer receipt_mode_id;
	
	@JsonProperty(value = "receiptMode")
	private String receipt_mode;
	
//	@JsonProperty(value = "isActive")
//	private char isActive;

}
