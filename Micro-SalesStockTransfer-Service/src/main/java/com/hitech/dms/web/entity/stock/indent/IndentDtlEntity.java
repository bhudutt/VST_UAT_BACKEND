/**
 * 
 */
package com.hitech.dms.web.entity.stock.indent;

import java.io.Serializable;
import java.math.BigInteger;
import java.util.Date;

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
@Table(name = "SA_MACHINE_TR_INDENT_DTL", uniqueConstraints = {
		@UniqueConstraint(columnNames = "indent_dtl_id") })
@Data
public class IndentDtlEntity implements Serializable {
	/**
	* 
	*/
	private static final long serialVersionUID = 4708789385746472024L;
	
	@Id
	@Column(name = "indent_dtl_id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private BigInteger indentDtlId;
	
	@JoinColumn(name = "indent_id")
	@ManyToOne(fetch = FetchType.LAZY)
	private IndentHdrEntity indentHdr;
	
	@Column(name = "machine_item_id")
	private BigInteger machineItemId;

	@Column(name = "indent_qty")
	private Integer indentQty;
	
	@Column(name = "issue_qty")
	private Integer issueQty;
	
	@Column(name = "pending_qty")
	private Integer pendingQty;
	
	@Column(name = "modified_by")
	private BigInteger modifiedBy;

	@Column(name = "modified_on")
	private Date modifiedDate;
}
