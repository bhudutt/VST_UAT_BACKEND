package com.hitech.dms.web.model.spare.claim.request;

import java.math.BigInteger;
import java.util.Date;
import java.util.List;

import com.hitech.dms.web.model.spare.inventory.request.PartDetailRequest;

import lombok.Data;

@Data
public class SpareClaimRequest {

	private Integer branchId;
	private String claimType;
	private BigInteger grnClaimHdrId;
	private String claimNumber;
	private BigInteger grnHdrId;
	private String claimStatus;
	private Date claimDate;
	private List<PartDetailRequest> saveSpareClaim;
}
