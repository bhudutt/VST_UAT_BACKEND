/**
 * 
 */
package com.hitech.dms.web.model.common;

import java.util.Date;

/**
 * @author dinesh.jakhar
 *
 */
public class CommonModel {
	
	private String createdBy;
	private Date createdDate;
	private String modifiedBy;
	private Date modifiedDate;

	public String getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	public Date getCreatedDate() {
		if(createdDate == null) {
			createdDate = new Date();
		}
		return createdDate;
	}

	public void setCreatedDate(Date createdDate) {
		if(createdDate == null) {
			createdDate = new Date();
		}
		this.createdDate = createdDate;
	}

	public String getModifiedBy() {
		return modifiedBy;
	}

	public void setModifiedBy(String modifiedBy) {
		this.modifiedBy = modifiedBy;
	}

	public Date getModifiedDate() {
		return modifiedDate;
	}

	public void setModifiedDate(Date modifiedDate) {
		this.modifiedDate = modifiedDate;
	}
}
