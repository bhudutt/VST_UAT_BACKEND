/**
 * 
 */
package com.hitech.dms.web.entity.stock.indent.receipt;

import java.io.Serializable;
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
import javax.persistence.ManyToOne;
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
@Table(name = "SA_MACHINE_TR_INDENT_RECEIPT_HDR", uniqueConstraints = { @UniqueConstraint(columnNames = "receipt_id") })
@Data
public class ReceiptHdrEntity implements Serializable {
	/**
	* 
	*/
	private static final long serialVersionUID = 6860328567748982646L;
	
	@Id
	@Column(name = "receipt_id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private BigInteger receiptId;
	
	@Column(name = "pc_id")
	private Integer pcId;

	@Column(name = "branch_id")
	private BigInteger branchId;
	
	@Column(name = "issue_id")
	private BigInteger issueId;
	
	@Column(name = "receipt_number", updatable = false)
	private String receiptNumber;

	@Column(name = "receipt_date")
	private Date receiptDate;
	
	@Column(name = "to_branch_id")
	private BigInteger toBranchId;

	@Column(name = "receipt_by")
	private BigInteger receiptBy;

	@Column(name = "receipt_remarks")
	private String remarks;

	@Column(name = "created_by", updatable = false)
	private BigInteger createdBy;

	@Column(name = "created_date", updatable = false)
	private Date createdDate;

	@Column(name = "last_modified_by")
	private BigInteger modifiedBy;

	@Column(name = "last_modified_date")
	private Date modifiedDate;
	
	
	@OneToMany(fetch = FetchType.EAGER, mappedBy = "receiptHdr", cascade = CascadeType.ALL)
	@Fetch(value = FetchMode.SUBSELECT)
	private List<ReceiptDtlEntity> receiptDtlList;
	
	@OneToMany(fetch = FetchType.EAGER, mappedBy = "receiptHdr", cascade = CascadeType.ALL)
	@Fetch(value = FetchMode.SUBSELECT)
	private List<ReceiptItemEntity> receiptItemList;
}
