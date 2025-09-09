package com.hitech.dms.web.model.quotation.create.request;

import java.math.BigDecimal;
import java.math.BigInteger;

import lombok.Data;

/**
 * 
 * @author dinesh.jakhar
 *
 */
@Data
public class VehQuoChargesRequestModel {
	private BigInteger chargeId;
	private VehQuoHDRRequestModel vehQuoHDR;
	private BigDecimal chargeAmount;
}
