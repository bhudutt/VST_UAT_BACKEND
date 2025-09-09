/**
 * 
 */
package com.hitech.dms.web.entity.stock.indent.issue;

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
@Table(name = "SA_MACHINE_TR_INDENT_ISSUE_DTL", uniqueConstraints = { @UniqueConstraint(columnNames = "issue_dtl_id") })
@Data
public class IssueDtlEntity implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1961523224320306049L;

	@Id
	@Column(name = "issue_dtl_id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private BigInteger issueDtlId;
	
	@JoinColumn(name = "issue_id")
	@ManyToOne(fetch = FetchType.LAZY)
	private IssueHdrEntity issueHdr;
	
	@Column(name = "vin_id")
	private BigInteger vinId;
	
	private transient BigInteger machineItemId;
}
