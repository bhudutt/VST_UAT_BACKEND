package com.hitech.dms.web.entity.spare.customer.order;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.LocalDateTime;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;

/**
 * 
 * @author Vivek.Gupta
 *
 */
@Entity
@Table(name = "PA_Customer_Order_DTL")
@Data
public class SpareCustomerOrderDetailEntity implements Serializable{
	

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "Id")
	private BigInteger id;
	
	@Column(name = "Customer_Id")
	private BigInteger customerId;
	
	@Column(name = "Part_Id")
	private BigInteger partId;
	
	@Column(name = "Order_Qty")
	private BigInteger orderQty;
	
	@Column(name = "CreatedBy")
	private BigInteger createdBy;
	
	@Column(name = "CreatedDate")
	private Date createdDate;
	
	@Column(name = "ModifiedBy")
	private BigInteger modifiedBy;
	
	@Column(name = "ModifiedDate")
	private Date modifiedDate;
	
	@Column(name = "invoice_Qty")
	private BigInteger invoicedQty;
	
	@Column(name = "Issue_Qty")
	private BigInteger issueQty=BigInteger.ZERO;
	
	@Column(name = "dc_issue_qty")
	private BigInteger dcIssueQty=BigInteger.ZERO;
	
	@Column(name = "Balance_Qty")
	private BigInteger balanceQty;
	
	@Column(name = "partBranchId")
	private BigInteger partBranchId;
	
	@Column(name = "unit_price")
	private BigDecimal basicUnitPrice;
	
	@Column(name = "IGST")
	private Integer igst;
	
	@Column(name = "CGST")
	private Integer cgst;
	
	@Column(name = "SGST")
	private Integer sgst;
	
	
}
