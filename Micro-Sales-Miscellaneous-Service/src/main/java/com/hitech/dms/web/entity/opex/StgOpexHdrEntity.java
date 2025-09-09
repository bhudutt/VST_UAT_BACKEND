/**
 * 
 */
package com.hitech.dms.web.entity.opex;

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

import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Data;

/**
 * @author dinesh.jakhar
 *
 */
@Table(name = "STG_SA_MIS_OPEX_BUDGET_HDR")
@Entity
@Data
public class StgOpexHdrEntity implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -2156133462116872372L;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "stg_opex_Id")
	@JsonIgnore
	private BigInteger stgOpexId;

	@Column(name = "pc_id")
	private Integer pcId;

	@Column(name = "state_id")
	private BigInteger stateId;

	@Column(name = "opex_number", nullable = false, updatable = false)
	private String opexNumber;

	@Column(name = "opex_date", nullable = false)
	private Date opexDate;

	@Column(name = "opex_status")
	private String opexStatus;

	@Column(name = "opex_updated_date")
	private Date opexUpdatedDate;

	@Column(name = "remarks")
	private String remarks;

	@Column(name = "opex_fin_year")
	private String opexFinYr;

	@Column(name = "CreatedBy", updatable = false)
	@JsonIgnore
	private BigInteger createdBy;

	@Column(name = "CreatedDate", updatable = false)
	@JsonIgnore
	private Date createdDate;

	@Column(name = "ModifiedBy")
	@JsonIgnore
	private BigInteger modifiedBy;

	@Column(name = "ModifiedDate")
	@JsonIgnore
	private Date modifiedDate;

	@OneToMany(fetch = FetchType.EAGER, mappedBy = "opex", cascade = CascadeType.ALL)
	@Fetch(value = FetchMode.SUBSELECT)
	private List<StgOpexBudgetDtlEntity> opexBudgetDtlList;
}
