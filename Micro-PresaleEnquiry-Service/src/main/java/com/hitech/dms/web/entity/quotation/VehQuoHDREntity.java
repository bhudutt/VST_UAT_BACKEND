/**
 * 
 */
package com.hitech.dms.web.entity.quotation;

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
import javax.persistence.Table;

import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

import lombok.Data;

/**
 * @author dinesh.jakhar
 *
 */
@Table(name = "SA_QUOTATION")
@Entity
@Data
public class VehQuoHDREntity implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -964688683991892985L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "Quotation_id")
	private BigInteger quotationHDRId;

	@Column(name = "branch_id")
	private BigInteger branchId;

	@Column(name = "pc_id")
	private Integer pcId;

	@Column(name = "quotation_number")
	private String quotationNumber;

	@Column(name = "Customer_id")
	private BigInteger customerId;

	@Column(name = "Total_Basic_Value")
	private BigDecimal totalBasicValue;

	@Column(name = "Total_Discount")
	private BigDecimal totalDiscount;

	@Column(name = "Total_Taxable_amount")
	private BigDecimal totalTaxableAmount;

	@Column(name = "Total_GST_Amount")
	private BigDecimal totalGstAmount;

	@Column(name = "Total_Charges")
	private BigDecimal totalCharges;

	@Column(name = "Total_Amount")
	private BigDecimal totalAmount;

	@Column(name = "Created_By", updatable = false)
	private BigInteger createdBy;
	@Column(name = "Created_Date", updatable = false)
	private Date createdDate;
	@Column(name = "ModifiedBy")
	private BigInteger modifiedBy;
	@Column(name = "ModifiedDate")
	private Date modifiedDate;

	@OneToMany(mappedBy = "vehQuoHDR")
	@LazyCollection(LazyCollectionOption.FALSE)
	@Cascade(org.hibernate.annotations.CascadeType.ALL)
	private List<VehQuoChargesEntity> vehQuoChrgList;

	@OneToMany(mappedBy = "vehQuoHDR")
	@LazyCollection(LazyCollectionOption.FALSE)
	@Cascade(org.hibernate.annotations.CascadeType.ALL)
	private List<VehQuoDTLEntity> vehQuoDTLList;

	@OneToMany(mappedBy = "vehQuoHDR")
	@LazyCollection(LazyCollectionOption.FALSE)
	@Cascade(org.hibernate.annotations.CascadeType.ALL)
	private List<VehQuoImplementEntity> vehQuoImplementList;
}
