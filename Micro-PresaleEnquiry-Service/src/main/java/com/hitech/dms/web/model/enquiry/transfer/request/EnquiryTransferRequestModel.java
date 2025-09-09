/**
 * 
 */
package com.hitech.dms.web.model.enquiry.transfer.request;

import java.math.BigInteger;
import java.util.List;

import lombok.Data;

/**
 * @author dinesh.jakhar
 *
 */
@Data
public class EnquiryTransferRequestModel {
	private BigInteger branchId;
	private BigInteger transferFromId;
	private BigInteger transferToId;
	private List<BigInteger> enquiryIdList;
}
