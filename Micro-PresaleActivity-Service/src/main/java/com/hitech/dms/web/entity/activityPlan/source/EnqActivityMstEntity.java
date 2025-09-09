/**
 * 
 */
package com.hitech.dms.web.entity.activityPlan.source;

import java.io.Serializable;
import java.math.BigInteger;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.Type;

import lombok.Data;

/**
 * @author dinesh.jakhar
 *
 */
@Table(name = "SA_MST_ENQ_SOURCE_ACTIVITY")
@Entity
@Data
public class EnqActivityMstEntity implements Serializable {
	/**
	* 
	*/
	private static final long serialVersionUID = -7922963301297618681L;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "Activity_ID")
	private Integer activityId;

	@Column(name = "pc_id")
	private BigInteger pcId;

	@Column(name = "ActivityCode")
	private String activityCode;

	@Column(name = "ActivityDesc")
	private String activityDescription;

	@Column(name = "GL_CODE")
	private String glCode;

	@Column(name = "COST_PER_DAY")
	private Double costPerDay;

	@Column(name = "IsActive")
	@Type(type = "yes_no")
	private Boolean isActive;

	@Column(name = "CreatedBy")
	private String createdBy;

	@Column(name = "CreatedDate", updatable = false)
	private Date createdDate;

	@Column(name = "ModifiedBy")
	private String modifiedBy;

	@Column(name = "ModifiedDate")
	private Date modifiedDate;
}
