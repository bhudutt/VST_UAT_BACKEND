/**
 * 
 */
package com.hitech.dms.web.entity.sales.purchase.ret;

import java.io.Serializable;
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
 * @author dinesh.jakhar
 *
 */
@Entity
@Table(name = "SA_MACHINE_VIN_MASTER")
@Data
public class SalesMachineVinMstEntity implements Serializable {
	/**
	* 
	*/
	private static final long serialVersionUID = 9059371254561964533L;
	
	@Id
	@Column(name = "vin_id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private BigInteger vinId;
	
	@Column(name = "machine_item_id")
	private BigInteger machineItemId;
	
	@Column(name = "vin_no")
	private String vinNo;

	@Column(name = "engine_no")
	private String engineNo;
	
	@Column(name = "chassis_no")
	private String chassisNo;
	
	@Column(name = "MfgDate")
	private Date mfgDate;
	
	@Column(name = "MfgInvoiceNumber")
	private String mfgInvoiceNo;
	
	@Column(name = "MfgInvoiceDate")
	private Date mfgInvoiceDate;
	
	@Column(name = "selling_dealer_code")
	private String sellingDealerCode;
	
	@Column(name = "csb_number")
	private String csbNo;
	
	@Column(name = "registration_number")
	private String registrationNumber;
	
	@Column(name = "installation_date")
	private Date installationDate;
	
	@Column(name = "unit_price")
	private BigDecimal unitPrice;
	
	@Column(name = "delivery_date")
	private Date deliveryDate;
	
	@Column(name = "retailed_flag")
	private boolean retailedFlag;
	
	@Column(name = "original_customer_master_id")
	private BigInteger originalCustomerMstId;
	
	@Column(name = "latest_customer_master_id")
	private BigInteger lastCustomerMstId;
	
	@Column(name = "created_by", updatable = false)
	private BigInteger createdBy;

	@Column(name = "created_date", updatable = false)
	private Date createdDate;

	@Column(name = "last_modified_by")
	private BigInteger modifiedBy;

	@Column(name = "last_modified_date")
	private Date modifiedDate;
}
