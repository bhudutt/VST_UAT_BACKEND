/**
 * 
 */
package com.hitech.dms.web.model.stock.receipt.request;

import java.math.BigInteger;
import java.util.Date;
import java.util.List;

import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.hitech.dms.app.utils.DateHandler;

import lombok.Data;

/**
 * @author dinesh.jakhar
 *
 */
@Data
public class ReceiptCreateRequestModel {
	@JsonProperty(value = "dealerId", required = true)
	@NotNull(message = "Dealer must not be blank.")
	private BigInteger dealerId;
	@JsonProperty(value = "branchId", required = true)
	@NotNull(message = "Receipt From Branch must not be blank.")
	private Integer branchId;
	@JsonProperty(value = "pcId", required = true)
	@NotNull(message = "Profit Center must not be blank.")
	private Integer pcId;
	@JsonProperty(value = "issueId", required = true)
	@NotNull(message = "Issue Number must not be blank.")
	private BigInteger issueId;
	@JsonProperty(value = "receiptDate", required = true)
	@NotNull(message = "Receipt Date must not be blank.")
	@JsonDeserialize(using = DateHandler.class)
	private Date receiptDate;
	@JsonProperty(value = "receiptBy", required = true)
	@NotNull(message = "Receipt By must not be blank.")
	private BigInteger receiptBy;
	@JsonProperty(value = "toBranchId", required = true)
	@NotNull(message = "Receipt To Branch must not be blank.")
	private BigInteger toBranchId;
	@JsonProperty(value = "receiptDtlList", required = true)
	private List<ReceiptDtlCreateRequestModel> receiptDtlList;
//	@JsonProperty(value = "receiptItemList", required = true)
	private List<ReceiptItemCreateRequestModel> receiptItemList;
}
