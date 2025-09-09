/**
 * 
 */
package com.hitech.dms.web.model.issue.create.request;

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
public class IssueCreateRequestModel {
	@JsonProperty(value = "dealerId", required = true)
	@NotNull(message = "Dealer must not be blank.")
	private BigInteger dealerId;
	@JsonProperty(value = "branchId", required = true)
	@NotNull(message = "Issue From Branch must not be blank.")
	private Integer branchId;
	@JsonProperty(value = "pcId", required = true)
	@NotNull(message = "Profit Center must not be blank.")
	private Integer pcId;
	@JsonProperty(value = "indentId", required = true)
	@NotNull(message = "Indent Number must not be blank.")
	private BigInteger indentId;
	@JsonProperty(value = "issueDate", required = true)
	@NotNull(message = "Issue Date must not be blank.")
	@JsonDeserialize(using = DateHandler.class)
	private Date issueDate;
	@NotNull(message = "Issue By must not be blank.")
	@JsonProperty(value = "issueBy", required = true)
	private BigInteger issueBy;
	@JsonProperty(value = "toBranchId", required = true)
	@NotNull(message = "Issue To Branch must not be blank.")
	private BigInteger toBranchId;
	@JsonProperty(value = "issueDtlList", required = true)
	private List<IssueDtlCreateRequestModel> issueDtlList;
//	@JsonProperty(value = "issueItemList", required = true)
	private List<IssueItemCreateRequestModel> issueItemList;
}
