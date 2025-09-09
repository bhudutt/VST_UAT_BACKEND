/**
 * 
 */
package com.hitech.dms.web.entity.sales.grn;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import lombok.Data;

/**
 * @author dinesh.jakhar
 *
 */
@Entity
@Table(name = "SA_MACHINE_GRN", uniqueConstraints = { @UniqueConstraint(columnNames = "grn_id") })
@Data
public class SalesMachineGrnHDREntity implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1920341969395330710L;
	@Id
	@Column(name = "grn_id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private BigInteger grnId;
	
	@Column(name = "dealer_id")
	private BigInteger dealerId;
	
	@Column(name = "pc_id")
	private Integer pcId;
	
	@Column(name = "grn_type_id")
	private Integer grnTypeId;
	
	@Column(name = "grn_number", updatable = false)
	private String grnNumber;
	
	@Column(name = "grn_status")
	private String grnStatus;
	
	@Column(name = "grn_date")
	private Date grnDate;
	
	@Column(name = "co_dealer_invoice_id")
	private BigInteger coDealerInvoiceId;
	
	@Column(name = "erp_invoice_hdr_id")
	private BigInteger erpInvoiceHdrId;
	
	@Column(name = "purchase_ret_inv_id")
	private BigInteger purchaseReturnInvId;
	
	@Column(name = "InvoiceNo")
	private String invoiceNumber;
	
	@Column(name = "InvoiceDate")
	private Date invoiceDate;
	
	@Column(name = "Party_Code")
	private String partCode;
	
	@Column(name = "Party_Name")
	private String partyName;
	
	@Column(name = "transporter_Name")
	private String transporterName;
	
	@Column(name = "driver_name")
	private String driverName;
	
	@Column(name = "driver_mobile")
	private String driverMobileNo;
	
	@Column(name = "transporter_vehicle_number")
	private String transporterVehicleNo;
	
	@Column(name = "gross_total_value")
	private BigDecimal grossTotalValue;
	
	@Column(name = "created_by", updatable = false)
	private BigInteger createdBy;

	@Column(name = "created_date", updatable = false)
	private Date createdDate;

	@Column(name = "last_modified_by")
	private BigInteger modifiedBy;

	@Column(name = "last_modified_date")
	private Date modifiedDate;
	
	@Column(name = "plant_code")
	private String plantCode;
	
	@OneToMany(fetch = FetchType.EAGER, mappedBy = "salesMachineGrnHDR", cascade = CascadeType.ALL)
	@Fetch(value = FetchMode.SUBSELECT)
	private List<SalesMachineGrnDtlEntity> salesMachineGrnDtlList;
	
	@OneToMany(fetch = FetchType.EAGER, mappedBy = "salesMachineGrnHDR", cascade = CascadeType.ALL)
	@Fetch(value = FetchMode.SUBSELECT)
	private List<SalesMachineGrnImplDtlEntity> salesMachineGrnImplDtlList;
}
