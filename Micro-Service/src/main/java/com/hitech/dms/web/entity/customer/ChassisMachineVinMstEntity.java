/**
 * 
 */
package com.hitech.dms.web.entity.customer;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.SecondaryTable;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.CascadeType;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.hitech.dms.app.utils.DateHandler;

import lombok.Data;

/**
 * @author Sunil.Singh
 *
 */
@Entity
@Table(name = "STG_SA_MACHINE_VIN_MASTER")
//@SecondaryTable(name="CM_CUST_HDR")
@Data
public class ChassisMachineVinMstEntity implements Serializable {
	/**
	* 
	*/
	private static final long serialVersionUID = 9059371254561964537L;
	
	@Id
	@Column(name = "vin_id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private BigInteger vinId;
	
	@JsonProperty(value="machineItemId")
	@Column(name = "machine_item_id")
	private BigInteger machineItemId;
	
	@JsonProperty(value="vinNo")
	@Column(name = "vin_no")
	private String vinNo;

	@JsonProperty(value="engineNo")
	@Column(name = "engine_no")
	private String engineNo;
	
	@JsonProperty(value="chassisNo")
	@Column(name = "chassis_no")
	private String chassisNo;
	
	@JsonProperty(value="mfgDate")
	@Column(name = "MfgDate")
	private Date mfgDate;
	
	@JsonProperty(value="mfgInvoiceNo")
	@Column(name = "MfgInvoiceNumber")
	private String mfgInvoiceNo;
	
	@JsonProperty(value="mfgInvoiceDate")
	@Column(name = "MfgInvoiceDate")
	private Date mfgInvoiceDate;
	
	@JsonProperty(value="sellingDealerCode")
	@Column(name = "selling_dealer_code")
	private String sellingDealerCode;
	
	@JsonProperty(value="csbNo")
	@Column(name = "csb_number")
	private String csbNo;
	
	@JsonProperty(value="registrationNumber")
	@Column(name = "registration_number")
	private String registrationNumber;
	
	@JsonProperty(value="installationDate")
	@Column(name = "installation_date")
	private Date installationDate;
	
	@JsonProperty(value="unitPrice")
	@Column(name = "unit_price")
	private BigDecimal unitPrice;
	
	@JsonProperty(value="deliveryDate")
	@Column(name = "delivery_date")
	private Date deliveryDate;
	
	@JsonProperty(value="retailedFlag")
	@Column(name = "retailed_flag")
	private boolean retailedFlag;
	
	@JsonProperty(value="customermstId")
	@Column(name = "original_customer_master_id")
	private BigInteger customermstId;
	
	@JsonProperty(value="lastCustomerMstId")
	@Column(name = "latest_customer_master_id")
	private BigInteger lastCustomerMstId;
	
	@JsonProperty(value="createdBy")
	@Column(name = "created_by", updatable = false)
	private BigInteger createdBy;
    
	@JsonProperty(value="createdDate")
	@Column(name = "created_date", updatable = false)
	@JsonDeserialize(using = DateHandler.class)
	private Date createdDate =new Date();

	@JsonProperty(value="modifiedBy")
	@Column(name = "last_modified_by")
	private BigInteger modifiedBy;
    
	@JsonProperty(value="modifiedDate")
	@Column(name = "last_modified_date")
	private Date modifiedDate;
	
	@JsonProperty(value="saleDate")
	@Column(name="Sale_Date")
	@JsonDeserialize(using = DateHandler.class)
	private Date saleDate =new Date();
	
	@JsonProperty(value="status")
	@Column(name = "Status")
	private String status;
	
	@Column(name = "Old_Chassis_Flag")
	private Integer oldchassisFlag;
	
	@Column(name = "Branch_id")
	@JsonProperty(value="branchId")
	private BigInteger branchId;
	
	@Column(name = "plant_code")
	@JsonProperty(value="plantCode")
	private String plantCode;
	
	
}
