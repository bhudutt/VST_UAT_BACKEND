/**
 * 
 */
package com.hitech.dms.web.entity.sales.purchase.ret.inv;

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
import javax.persistence.Transient;

import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import lombok.Data;

/**
 * @author dinesh.jakhar
 *
 */
@Entity
@Table(name = "SA_MACHINE_PURCH_RET_INV")
@Data
public class SalesMachinePurchaseReturnInvEntity implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 451301062997855871L;

	@Id
	@Column(name = "purchase_ret_inv_id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private BigInteger purchaseReturnInvId;

	@Column(name = "pc_id")
	private Integer pcId;

	@Column(name = "dealer_id")
	private BigInteger dealerId;

	@Column(name = "to_dealer_id")
	private BigInteger toDealerId;

	@Column(name = "purchase_return_id")
	private BigInteger purchaseReturnId;

	@Column(name = "purchase_ret_inv_number")
	private String purchaseReturnInvNumber;

	@Column(name = "purchase_ret_inv_date")
	private Date purchaseReturnInvDate;

	@Column(name = "purchase_ret_inv_status")
	private String purchaseReturnInvStatus;

	@Column(name = "gross_total_ret_value")
	private BigDecimal grossTotalReturnValue;

	@Column(name = "pdi_done_flag")
	private boolean pdiDoneFlag;

	@Column(name = "grn_done_flag")
	private boolean grnDoneFlag;

	@Column(name = "created_by", updatable = false)
	private BigInteger createdBy;

	@Column(name = "created_date", updatable = false)
	private Date createdDate;

	@Column(name = "last_modified_by")
	private BigInteger modifiedBy;

	@Column(name = "last_modified_date")
	private Date modifiedDate;

	//@OneToMany(fetch = FetchType.EAGER, mappedBy = "salesMachinePurchaseReturnInv", cascade = CascadeType.ALL)
	//@Fetch(value = FetchMode.SUBSELECT)
	@Transient
	private List<SalesMachinePurchaseReturnInvDtlEntity> salesMachineGrnDtlList;

	//@OneToMany(fetch = FetchType.EAGER, mappedBy = "salesMachinePurchaseReturnInv", cascade = CascadeType.ALL)
	//@Fetch(value = FetchMode.SUBSELECT)
	@Transient
	private List<SalesMachinePurchaseReturnInvImplDtlEntity> salesMachineGrnImplDtlList;

}
