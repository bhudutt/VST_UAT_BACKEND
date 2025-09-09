package com.hitech.dms.web.entity.branchTransfer.indent;

import java.io.Serializable;
import java.math.BigDecimal;
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

import com.fasterxml.jackson.annotation.JsonBackReference;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "PA_INDENT_DTL")
public class IndentDtlEntity implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private BigInteger id;
	
//	@Column(name = "pa_ind_hdr_id")
//	private Integer paIndHdrId;
	
	@ManyToOne(fetch = FetchType.LAZY)
    @JsonBackReference
    @JoinColumn(name = "pa_ind_hdr_id", referencedColumnName = "id")
    private IndentHdrEntity indentHdrEntity;
	
	@Column(name = "part_id")
	private BigInteger partId;
	
	@Column(name = "IndentQty")
	private Integer indentQty;
	
	@Column(name = "IssueQty")
	private Integer issueQty;
	
	@Column(name = "BasicUnitPrice")
	private BigDecimal basicUnitPrice;
	
	@Column(name = "TotalStock")
	private Integer totalStock;
	
	@Column(name = "TotalValue")
	private BigDecimal totalValue;
	
	@Column(name = "ColorCode")
	private String colorCode;
	
	@Column(name = "CreatedDate")
	private Date createdDate;
	
	@Column(name = "CreatedBy")
	private BigInteger createdBy;
	
	@Column(name = "ModifiedDate")
	private Date modifiedDate;

	@Column(name = "ModifiedBy")
	private BigInteger modifiedBy;
}
