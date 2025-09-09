/**
 * 
 */
package com.hitech.dms.web.entity.aop;

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
@Table(name = "STG_SA_MIS_AOP_TARGET_HDR")
@Entity
@Data
public class StgAopHdrEntity implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 4345997024099650270L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "stg_aop_Id")
	@JsonIgnore
	private BigInteger stgAopId;

	@Column(name = "pc_id")
	private Integer pcId;

	@Column(name = "dealer_id")
	private BigInteger dealerId;

	@Column(name = "aop_number", nullable = false, updatable = false)
	private String aopNumber;

	@Column(name = "aop_date", nullable = false)
	private Date aopDate;

	@Column(name = "aop_status")
	private String aopStatus;

	@Column(name = "aop_updated_date")
	private Date aopUpdatedDate;

	@Column(name = "remarks")
	private String remarks;

	@Column(name = "aop_fin_year")
	private String aopFinYr;

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

	@OneToMany(fetch = FetchType.EAGER, mappedBy = "aop", cascade = CascadeType.ALL)
	@Fetch(value = FetchMode.SUBSELECT)
	private List<StgAopTargetDtlEntity> aopTargetDtlList;
}
