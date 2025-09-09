/**
 * 
 */
package com.hitech.dms.web.model.jobcard.request;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;

import lombok.Data;

/**
 * @author santosh.kumar
 *
 */
@Data
public class jobCardCreateRequest {
	
	private Integer branchId;
	private Integer jobcardCatgId;
	private Integer vinId;
	private Integer ServiceBookingId;
	private Integer sourceId;
	private String engineNo;
	private Integer placeOfServiceId;
	private String manualJobCard;
	private Date manualJobCardDate;
	private Date jobCreationDate;
	private String jobCardTime;
	private String jobCardStatus;
	private BigInteger currentHours;
	private BigInteger previousHours;
	private BigInteger meterNoChage;
	private BigInteger totalHours;
	private Integer serviceTypeId;
	private Integer repairOrderTypeId;
	private Integer serviceQuatationId;
	private Integer invoiceId;
	private Integer inwardId;
	private String profitCenter;
	private Integer coustemberId;
	private BigDecimal batteryVoltage;
	private Integer installationDoneById;
	private String representativeTypeId;
	private String  representativeName;
	private Date estimateDate;
	private String completationTime;
	private Double estimateAmount;
	private String operatorName;
	private String operatorMobile;
	private Integer applicationUsedById;
	private String applicationUsedFor;
	private Integer implementTypeId;
	private String implementTypeOthers;
	private Integer servicetechnicianId;
	private Integer mechanic_one_id;
	private Integer mechanic_two_id;
	private Integer mechanic_three_id;
}
