/**
 * 
 */
package com.hitech.dms.web.entity.jobcard;

import java.math.BigInteger;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;

/**
 * @author santosh.kumar
 *
 */
@Entity
@Table(name = "SV_RO_InWardOutWard_CheckPoint")
@Data
public class PdiCheckListEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	@Column(name="RoID")
	private BigInteger roId;
	@Column(name="pdiDtlId")
	private int pdiDtlId;
	@Column(name="fieldType")
	private String fieldType;
	@Column(name="defaultTick")
	private String defaultTick;
	@Column(name="checkpointDesc")
	private String checkpointDesc;
	@Column(name="checkpointId")
	private Integer checkpointId;
	@Column(name="checkpointSequenceNo")
	private Integer checkpointSequenceNo;
	@Column(name="checklist_mapping_id")
	private Integer checklist_mapping_id;
	@Column(name="aggregateId")
	private Integer aggregateId;
	@Column(name="observedSpecification")
	private String observedSpecification;
	@Column(name="createdDate")
	private Date createdDate;
	@Column(name="createdBy")
	private String createdBy;

}
