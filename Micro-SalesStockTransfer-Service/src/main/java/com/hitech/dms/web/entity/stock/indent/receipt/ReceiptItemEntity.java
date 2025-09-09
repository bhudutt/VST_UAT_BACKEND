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
@Table(name = "SA_MACHINE_TR_INDENT_RECEIPT_ITEMS", uniqueConstraints = { @UniqueConstraint(columnNames = "receipt_item_id") })
@Data
public class ReceiptItemEntity implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -180921529570234267L;
	
	@Id
	@Column(name = "receipt_item_id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private BigInteger receiptItemId;

	@JoinColumn(name = "receipt_id")
	@ManyToOne(fetch = FetchType.LAZY)
	private ReceiptHdrEntity receiptHdr;
	
	@Column(name = "issue_item_id")
	private BigInteger issueItemId;
	
	@Column(name = "receipt_qty")
	private Integer receiptQty;
}
