/**
 * 
 */
package com.hitech.dms.web.entity.stock.indent.issue;

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
@Table(name = "SA_MACHINE_TR_INDENT_ISSUE_HDR", uniqueConstraints = { @UniqueConstraint(columnNames = "issue_id") })
@Data
public class IssueHdrEntity implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -2252684212017426439L;

	@Id
	@Column(name = "issue_id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private BigInteger issueId;

	@Column(name = "pc_id")
	private Integer pcId;

	@Column(name = "branch_id")
	private BigInteger branchId;

	@Column(name = "indent_id")
	private BigInteger indentId;

	@Column(name = "issue_number", updatable = false)
	private String issueNumber;

	@Column(name = "issue_date")
	private Date issueDate;

	@Column(name = "to_branch_id")
	private BigInteger toBranchId;

	@Column(name = "issue_by")
	private BigInteger issueBy;

	@Column(name = "issue_remarks")
	private String remarks;

	@Column(name = "created_by", updatable = false)
	private BigInteger createdBy;

	@Column(name = "created_date", updatable = false)
	private Date createdDate;

	@Column(name = "last_modified_by")
	private BigInteger modifiedBy;

	@Column(name = "last_modified_date")
	private Date modifiedDate;

	@OneToMany(fetch = FetchType.EAGER, mappedBy = "issueHdr", cascade = CascadeType.ALL)
	@Fetch(value = FetchMode.SUBSELECT)
	private List<IssueDtlEntity> issueDtlList;

	@OneToMany(fetch = FetchType.EAGER, mappedBy = "issueHdr", cascade = CascadeType.ALL)
	@Fetch(value = FetchMode.SUBSELECT)
	private List<IssueItemEntity> issueItemList;
}
