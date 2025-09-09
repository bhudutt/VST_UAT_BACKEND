package com.hitech.dms.web.model.spare.claim.request;

import java.math.BigInteger;
import java.util.List;

import com.hitech.dms.web.model.spare.inventory.request.PartDetailRequest;

import lombok.Data;

@Data
public class AgreeOrDisagreeClaimRequest {

	private Character isAgree;
	private BigInteger grnHdrId;
	private BigInteger claimHdrId;
	private BigInteger branchId;
	private List<PartDetailRequest> partDetailRequest;
	
}
