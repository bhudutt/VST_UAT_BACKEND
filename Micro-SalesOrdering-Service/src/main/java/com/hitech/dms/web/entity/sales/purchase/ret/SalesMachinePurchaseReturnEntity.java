/**
 * 
 */
package com.hitech.dms.web.entity.sales.purchase.ret;

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
@Table(name = "SA_MACHINE_PURCH_RET")
@Data
public class SalesMachinePurchaseReturnEntity implements Serializable {
	/**
	* 
	*/
	private static final long serialVersionUID = -261952462444735743L;

	@Id
	@Column(name = "purchase_return_id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private BigInteger purchaseReturnId;

	@Column(name = "dealer_id")
	private BigInteger dealerId;

	@Column(name = "pc_id")
	private Integer pcId;

	@Column(name = "grn_id")
	private BigInteger grnId;

	@Column(name = "purchase_return_number")
	private String purchaseReturnNumber;

	@Column(name = "purchase_return_date")
	private Date purchaseReturnDate;

	@Column(name = "purchase_return_status")
	private String purchaseReturnStatus;

	@Column(name = "gross_total_return_value")
	private BigDecimal grossTotalReturnValue;

	@Column(name = "created_by", updatable = false)
	private BigInteger createdBy;

	@Column(name = "created_date", updatable = false)
	private Date createdDate;

	@Column(name = "last_modified_by")
	private BigInteger modifiedBy;

	@Column(name = "last_modified_date")
	private Date modifiedDate;

	//@OneToMany(fetch = FetchType.EAGER, mappedBy = "salesMachinePurchaseReturn", cascade = CascadeType.ALL)
	//@Fetch(value = FetchMode.SUBSELECT)
	@Transient
	private List<SalesMachinePurchaseReturnAppEntity> salesMachinePurchaseReturnAppList;

	//@OneToMany(fetch = FetchType.EAGER, mappedBy = "salesMachinePurchaseReturn", cascade = CascadeType.ALL)
	//@Fetch(value = FetchMode.SUBSELECT)
	@Transient
	private List<SalesMachinePurchaseReturnDtlEntity> salesMachineGrnDtlList;

	//@OneToMany(fetch = FetchType.EAGER, mappedBy = "salesMachinePurchaseReturn", cascade = CascadeType.ALL)
	//@Fetch(value = FetchMode.SUBSELECT)
	@Transient
	private List<SalesMachinePurchaseReturnImplDtlEntity> salesMachineGrnImplDtlList;
}
