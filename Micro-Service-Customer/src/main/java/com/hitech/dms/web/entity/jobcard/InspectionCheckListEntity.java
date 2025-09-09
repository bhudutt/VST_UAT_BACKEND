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
@Table(name="SV_RO_Installation_CheckList")
@Data
public class InspectionCheckListEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	@Column(name="RoID")
	private Integer roId;
	@Column(name="installDtlId")
	private Integer installDtlId;
	@Column(name="InstallationParameterId")
	private Integer InstallationParameterId;
	@Column(name="Flag")
	private Boolean flag;
	@Column(name="CreatedDate")
	private Date createdDate;
	@Column(name="CreatedBy")
	private String createdBy;

}
