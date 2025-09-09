/**
 * 
 */
package com.hitech.dms.web.entity.activityPlan.source;

import java.io.Serializable;
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
@Table(name = "SA_MST_ENQ_SOURCE")
@Entity
@Data
public class SourceMstEntity implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1860205738883048832L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "Enq_Source_Id")
	private Integer enqSourceId;

	@Column(name = "SourceCode")
	private String sourceCode;
	
	@Column(name = "SourceDescription")
	private String sourceDescription;
	
	@Column(name = "IsSubSourceRequired")
	@Type(type ="yes_no")
	private Boolean isSubSourceRequired;
	
	@Column(name = "ApplicableForWeb")
	@Type(type ="yes_no")
	private Boolean applicableForWeb;
	
	@Column(name = "ApplicableForMobileApp")
	@Type(type ="yes_no")
	private Boolean applicableForMobileApp;
	
	@Column(name = "IsActive")
	@Type(type ="yes_no")
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
