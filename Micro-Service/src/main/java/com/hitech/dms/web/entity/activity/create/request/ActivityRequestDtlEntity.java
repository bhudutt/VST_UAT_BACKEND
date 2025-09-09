package com.hitech.dms.web.entity.activity.create.request;


import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;


import lombok.Data;

@Table(name = "SV_Activity_DTL")
@Entity
@Data
public class ActivityRequestDtlEntity {

	@Id
	@Column(name = "ActivityDtlId")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer activityDtlId;
	
	@Column(name = "JobCardId")
	private Integer jobCardId;
	
	
	@ManyToOne
	@JoinColumn(name ="activityId")
	@Cascade(CascadeType.ALL)
	private ActivityRequestEntity activityEntity;
	
	@Column(name = "CreatedDate", updatable = false)
	private Date createdDate;

	@Column(name = "CreatedBy", updatable = false)
	private String createdBy;

	@Column(name = "ModifiedDate")
	private Date modifiedDate;

	@Column(name = "ModifiedBy")
	private String modifiedBy;
	
	@Transient
	private Boolean check;
	
	
	
}
