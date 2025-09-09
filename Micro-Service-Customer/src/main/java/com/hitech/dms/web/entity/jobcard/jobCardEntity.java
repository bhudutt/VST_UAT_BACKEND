/**
 * 
 */
package com.hitech.dms.web.entity.jobcard;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;


/**
 * @author santosh.kumar
 *
 */
@Entity
@Table(name = "SV_RO_HDR")
@Data
public class jobCardEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="ro_id")
	private BigInteger roId;
	@Column(name="branch_id")
	private Integer branchId;
	@Column(name="RONumber")
	private String roNumber;
	@Column(name="jobCard_Cat_Id")
	private Integer jobcardCatgId;
	@Column(name="Vin_Id")
	private Integer vinId;
	@Column(name="ChassisNumber")
	private String chassisNumber;
	@Column(name="Service_booking_id")
	private Integer ServiceBookingId;
	@Column(name="inword_pdi_id")
   private Integer inwardId;
	@Column(name="source")
	private String source;
	@Column(name="EngineNo")
	private String engineNo;
	@Column(name="PlaceOfService")
	private String placeOfService;
	@Column(name="Manual_jobCard_No")
	private String manualJobCard;
	@Column(name="Manual_jobCard_Date")
	private Date manualJobCardDate;
	@Column(name="OpeningDate")
	private Date jobCreationDate;
	@Column(name="JobCard_time")
	private String jobCardTime;
	@Column(name="Status")
	private String jobCardStatus;
	@Column(name="Current_Hour")
	private BigInteger currentHours;
	@Column(name="Previous_Hour")
	private BigInteger previousHours;
	@Column(name="Meter_Change_Hour")
	private BigInteger meterNoChage;
	@Column(name="Total_Hour")
	private BigInteger totalHours;
	@Column(name="service_type_id")
	private Integer serviceTypeId;
	@Column(name="repair_type_id")
	private Integer repairOrderTypeId;
	@Column(name="Service_quation_id")
	private Integer serviceQuatationId;
	@Column(name="Invoice_id")
	private Integer invoiceId;
	@Column(name="Customer_Id")
	private Integer coustemberId;
	@Column(name="Battery_voltage")
	private BigDecimal batteryVoltage;
	@Column(name="installation_done_by")
	private String installationDoneBy;
	@Column(name="Representative_type")
	private String representativeTypeId;
	@Column(name="Representative_name")
	private String  representativeName;
	@Column(name="PromisedDate")
	private Date estimateDate;
	@Column(name="CompletationTime")
	private String completationTime;
	@Column(name="Estimated_amount")
	private Double estimateAmount;
	@Column(name="Driver_Name")
	private String operatorName;
	@Column(name="Driver_Mobile_Number")
	private String operatorMobile;
	@Column(name="Application_used_id")
	private Integer applicationUsedById;
	@Column(name="Application_used_for")
	private String applicationUsedFor;
	@Column(name="ImplementType_id")
	private Integer implementTypeId;
	@Column(name="Implement_type_other")
	private String implementTypeOthers;
	@Column(name="Service_technician")
	private Integer servicetechnicianId;
	@Column(name="Mechanic_one")
	private Integer mechanic_one_id;
	@Column(name="Mechanic_two")
	private Integer mechanic_two_id;
	@Column(name="Mechanic_three")
	private Integer mechanic_three_id;
	@Column(name="ROCancelReason")
	private String cancelReson;
	@Column(name="ROCancelDate")
	private Date cancelDate;
	@Column(name="Closing_Resion")
	private String closingResion;
	@Column(name="ClosedJobCard_date")
	private Date closeJobCardDate;
	@Column(name="Closed_By")
	private String closedBy;
}
