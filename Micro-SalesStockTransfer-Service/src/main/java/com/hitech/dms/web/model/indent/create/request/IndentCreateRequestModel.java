/**
 * 
 */
package com.hitech.dms.web.model.indent.create.request;

import java.math.BigInteger;
import java.util.Date;
import java.util.List;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
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
public class IndentCreateRequestModel {
	@JsonProperty(value = "dealerId", required = true)
	@NotNull(message = "Dealer must not be blank.")
	private BigInteger dealerId;
	@NotNull(message = "Indent From Branch must not be blank.")
	@JsonProperty(value = "branchId", required = true)
	private Integer branchId;
	@NotNull(message = "Profit Center must not be blank.")
	@JsonProperty(value = "pcId", required = true)
	private Integer pcId;
	@NotNull(message = "Indent Date must not be blank.")
	@JsonProperty(value = "indentDate", required = true)
	@JsonDeserialize(using = DateHandler.class)
	private Date indentDate;
	@NotNull(message = "Indent By must not be blank.")
	@JsonProperty(value = "indentBy", required = true)
	private BigInteger indentBy;
	@NotNull(message = "Indent To Branch must not be blank.")
	@JsonProperty(value = "indentToBranchId", required = true)
	private BigInteger indentToBranchId;
	@JsonProperty(value = "indentDtlList", required = true)
	private List<IndentDtlCreateRequestModel> indentDtlList;
//	@JsonProperty(value = "indentItemList", required = true)
	private List<IndentItemCreateRequestModel> indentItemList;
}
