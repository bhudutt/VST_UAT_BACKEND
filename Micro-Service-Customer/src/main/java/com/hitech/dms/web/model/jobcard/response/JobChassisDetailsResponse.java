/**
 * 
 */
package com.hitech.dms.web.model.jobcard.response;

import java.math.BigInteger;
import java.util.Date;

import lombok.Data;

/**
 * @author santosh.kumar
 *
 */
@Data
public class JobChassisDetailsResponse {
	private BigInteger vinId;
	//private BigInteger customerId;
	private BigInteger dealerId;
	private String profitcenter;
	private String pdiDoneBy;
	private String chassisNo;
	private String engineNo;
	private String vinNo;
	private String registrationNo;
	private String itemDescription;
	private BigInteger invoiceId;
	private String invoiceNumber;
	private Date invoiceDate;
	private BigInteger bookingId;
	private BigInteger serviceTypeId;
	private BigInteger serviceRepairTypeId;
	private BigInteger coustomerId; //customerId
	private String bookingNo;
	private Date bookingDate;
	private BigInteger previousHour;
	private Date lrDate;
	private String lrNumber;
	private Integer completePdi;
	private Integer pendingPdi;
	private Integer quotationId;
	private String quotationNumber;
	private Date quotationDate;
	private String customerName;
	private String mobileNo;
	private String repairOrderType;
	private String serviceType;
	private Integer inwardId;
	private String starterMotorMakeNumber;
	private String alternatorMakeNumber;
	private String flipMakeNumber;
	private String batteryMakeNumber;
	private String frontTyerMakeRHNumber;
	private String frontTyerMakeLHNumber;
	private String rearTyerMakeRHNumber;
	private String rearTypeMakeLHNumber;
	private String representativeType;
	private String representativeName;

	

}
