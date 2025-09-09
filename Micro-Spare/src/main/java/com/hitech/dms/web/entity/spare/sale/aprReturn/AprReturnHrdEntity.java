package com.hitech.dms.web.entity.spare.sale.aprReturn;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import lombok.Data;

@Table(name = "APR_RETURN_HDR")
@Data
@Entity
public class AprReturnHrdEntity {
	
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="APR_RETURN_ID")
	private Integer aprReturedId;
	
	@Column(name="BRANCH_ID")
	private Integer branchId;
	
	@Column(name="APR_RETURN_DOC_NO")
	private String aprReturedDocNo;
	
	@Column(name="APR_RETURN_DATE")
	private Date aprReturnDate;
	
	@Column(name="PARTY_ID")
	private Integer partyId;
	
	@Column(name="PARTY_TYPE_ID")
	private Integer partyTypeId;
	
	@Column(name="INVOICE_ID")
	private Integer invoiceId;
	
	@Column(name="APR_RETURN_STATUS")
	private String aprReturnStatus;
	
	@Column(name="DELIVERY_CHALLAN_ID")
	private Integer deliveryChallanId;
	

	@Column(name="TOTAL_TAXABLE_AMOUNT")
	private BigDecimal totalTaxableAmount;
	
	@Column(name="TOTAL_GST_AMOUNT")
	private BigDecimal totalGstAmount;
	
	@Column(name="RETURN_AMOUNT")
	private BigDecimal returnAmount;
	
	// Mapping to the other table
    @OneToMany(cascade = CascadeType.ALL)
    private Set<AprReturnDtlEntity> aprReturnDtlEntity;
	 
    @Column(name="CRAETED_DATE")
	private Date createdDate;
	
    @Column(name="CREATED_BY")
	private Integer createdBy;
    
    @Column(name="MODIFIED_DATE")
  	private Date modifiedDate;
  	
    @Column(name="MODIFIED_BY")
  	private Integer modifiedBy;
    
    @Column(name="REMARK")
  	private String remark;
    
}
