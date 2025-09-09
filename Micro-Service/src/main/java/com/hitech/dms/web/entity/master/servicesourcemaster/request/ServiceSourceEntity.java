package com.hitech.dms.web.entity.master.servicesourcemaster.request;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.Type;

import lombok.Data;

@Entity
@Data
@Table(name = "CM_SOURCE")
public class ServiceSourceEntity {

	@Id
	@Column(name = "source_id")
	private Integer sourceId;
	
	@Column(name = "SourceType")
	private String sourceType;
	
	@Column(name = "Source")
	private String source;
	
	@Column(name = "IsActive")
	private Character activeStatus;
}
