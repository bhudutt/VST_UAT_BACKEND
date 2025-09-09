/**
 * 
 */
package com.hitech.dms.web.model.machinepo.dtl.response;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.List;

import lombok.Getter;
import lombok.Setter;

/**
 * @author dinesh.jakhar
 *
 */
@Getter
@Setter
public class MachinePOHdrResponseModel {
	private BigInteger poHdrId;
	private String poNumber;
	private String poStatus;
	private String poReleasedDate;
	private BigInteger dealerId;
	private String dealerName;
	private Integer pcId;
	private String pcDesc;
	private Integer poTypeId;
	private String poTypeDesc;
	private Integer productDivisionId;
	private String productDivision;
	private BigInteger poToDealerId;
	private String poToPartyName;
	private String poPartyCode;
	private String poDate;
	private Integer poPlantId;
	private String poPlantDesc;
	private String poRemarks;
	private BigDecimal basicAmount;
	private BigDecimal totalGstAmount;
	private BigDecimal tcsPercent;
	private BigDecimal tcsValue;
	private BigDecimal totalAmount;
	private Integer totalQuantity;
	private String sonNo;
	private String soDate;
	private BigInteger poCancelReason;
	private String poCancelReasonDesc;
	private String poCancelRemarks;
	private Integer poCount;
	private List<MachinePODtlResponseModel> machinePODTLList;
}
