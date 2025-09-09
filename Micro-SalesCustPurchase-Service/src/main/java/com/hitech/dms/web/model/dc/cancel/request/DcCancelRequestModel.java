/**
 * 
 */
package com.hitech.dms.web.model.dc.cancel.request;

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
public class DcCancelRequestModel {
	private Integer pcId;
	private BigInteger dealerId;
	private BigInteger branchId;
	private BigInteger dcId;
	private String dcNumber;
	private BigInteger dcCancelReasonId;
	@JsonDeserialize(using = DateHandler.class)
	private Date dcCancelDate;
	private String dcCancelRemark;
	private String dcCancelType;
}
