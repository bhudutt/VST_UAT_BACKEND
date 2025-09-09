/**
 * 
 */
package com.hitech.dms.web.entity.stock.indent;

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
@Table(name = "SA_MACHINE_TR_INDENT_HDR", uniqueConstraints = { @UniqueConstraint(columnNames = "indent_id") })
@Data
public class IndentHdrEntity implements Serializable {
	/**
	* 
	*/
	private static final long serialVersionUID = -9014517364306659077L;
	
	@Id
	@Column(name = "indent_id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private BigInteger indentId;
	
	@Column(name = "pc_id")
	private Integer pcId;
	
	@Column(name = "branch_id")
	private BigInteger branchId;
	
	@Column(name = "indent_number", updatable = false)
	private String indentNumber;

	@Column(name = "indent_date")
	private Date indentDate;

	@Column(name = "indent_status")
	private String indentStatus;
	
	@Column(name = "indent_by")
	private BigInteger indentBy;
	
	@Column(name = "to_branch_id")
	private BigInteger indentToBranchId;
	
	@Column(name = "created_by", updatable = false)
	private BigInteger createdBy;

	@Column(name = "created_date", updatable = false)
	private Date createdDate;

	@Column(name = "last_modified_by")
	private BigInteger modifiedBy;

	@Column(name = "last_modified_date")
	private Date modifiedDate;
	
	@OneToMany(fetch = FetchType.EAGER, mappedBy = "indentHdr", cascade = CascadeType.ALL)
	@Fetch(value = FetchMode.SUBSELECT)
	private List<IndentDtlEntity> indentDtlList;
	
	@OneToMany(fetch = FetchType.EAGER, mappedBy = "indentHdr", cascade = CascadeType.ALL)
	@Fetch(value = FetchMode.SUBSELECT)
	private List<IndentItemEntity> indentItemList;
}
