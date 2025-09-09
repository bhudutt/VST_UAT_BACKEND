package com.hitech.dms.web.model.tm.create.response;

import java.math.BigInteger;
import java.util.List;

import lombok.Data;

@Data
public class EnquiryTmTransferRequestModel {
	private BigInteger branchId;
	private BigInteger transferFromId;
	private BigInteger transferToId;
	private List<BigInteger> enquiryIdList;
}
