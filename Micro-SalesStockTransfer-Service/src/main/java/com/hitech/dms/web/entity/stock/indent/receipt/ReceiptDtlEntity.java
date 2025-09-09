/**
 * 
 */
package com.hitech.dms.web.entity.stock.indent.receipt;

import java.io.Serializable;
import java.math.BigInteger;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import lombok.Data;

/**
 * @author dinesh.jakhar
 *
 */
@Entity
@Table(name = "SA_MACHINE_TR_INDENT_RECEIPT_DTL", uniqueConstraints = { @UniqueConstraint(columnNames = "receipt_dtl_id") })
@Data
public class ReceiptDtlEntity implements Serializable {
	/**
	* 
	*/
	private static final long serialVersionUID = -2695465653241631737L;
	
	@Id
	@Column(name = "receipt_dtl_id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private BigInteger receiptDtlId;

	@JoinColumn(name = "receipt_id")
	@ManyToOne(fetch = FetchType.LAZY)
	private ReceiptHdrEntity receiptHdr;
	
	@Column(name = "issue_dtl_id")
	private BigInteger issueDtlId;
}
