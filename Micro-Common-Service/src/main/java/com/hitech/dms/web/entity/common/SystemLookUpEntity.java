package com.hitech.dms.web.entity.common;

import java.io.Serializable;
import java.math.BigInteger;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.Type;

import lombok.Data;

/**
 * @author Dinesh
 *
 */
@Entity
@Table(name = "SYS_LOOKUP")
@Data
public class SystemLookUpEntity implements Serializable, Comparable<SystemLookUpEntity> {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7253213726756862L;
	@Id
	@Column(name = "lookup_id")
	private BigInteger lookUpId;
	@Column(name = "LookupTypeCode")
	private String lookTypeCode;
	@Column(name = "LookupVal")
	private String lookupVal;
	@Column(name = "LookupText")
	private String lookTypeText;
	@Column(name = "DisplayOrder")
	private Integer displayOrder;
	@Column(name = "CreatedDate")
	private Date createdDate;
	@Column(name = "CreatedBy")
	private String createdBy;
	@Column(name = "ModifiedDate")
	private Date modifiedDate;
	@Column(name = "ModifiedBy")
	private String modifiedBy;
	@Type(type = "yes_no")
	@Column(name = "ISACTIVE")
	private Boolean isActive;
	
	/**
	 * @return the lookTypeCode
	 */
	public String getLookTypeCode() {
		if (lookTypeCode != null && !lookTypeCode.equals("")) {
			lookTypeCode = lookTypeCode.trim();
		}
		return lookTypeCode;
	}

	/**
	 * @param lookTypeCode
	 *            the lookTypeCode to set
	 */
	public void setLookTypeCode(String lookTypeCode) {
		if (lookTypeCode != null && !lookTypeCode.equals("")) {
			lookTypeCode = lookTypeCode.trim();
		}
		this.lookTypeCode = lookTypeCode;
	}

	/**
	 * @return the lookupVal
	 */
	public String getLookupVal() {
		if (lookupVal != null && !lookupVal.equals("")) {
			lookupVal = lookupVal.trim();
		}
		return lookupVal;
	}

	/**
	 * @param lookupVal
	 *            the lookupVal to set
	 */
	public void setLookupVal(String lookupVal) {
		if (lookupVal != null && !lookupVal.equals("")) {
			lookupVal = lookupVal.trim();
		}
		this.lookupVal = lookupVal;
	}

	/**
	 * @return the lookTypeText
	 */
	public String getLookTypeText() {
		if (lookTypeText != null && !lookTypeText.equals("")) {
			lookTypeText = lookTypeText.trim();
		}
		return lookTypeText;
	}

	/**
	 * @param lookTypeText
	 *            the lookTypeText to set
	 */
	public void setLookTypeText(String lookTypeText) {
		if (lookTypeText != null && !lookTypeText.equals("")) {
			lookTypeText = lookTypeText.trim();
		}
		this.lookTypeText = lookTypeText;
	}

	/**
	 * @param lookupVal
	 * @param lookTypeText
	 * @param lookUpId
	 * @param displayOrder
	 */
	public SystemLookUpEntity(String lookupVal, String lookTypeText, BigInteger lookUpId, Integer displayOrder) {
		this.lookUpId = lookUpId;
		this.lookTypeText = lookTypeText;
		this.lookupVal = lookupVal;
		this.displayOrder = displayOrder;
	}

	/**
	 * @param lookupVal
	 * @param lookTypeText
	 * @param lookUpId
	 * @param displayOrder
	 * @param lookTypeCode
	 */
	public SystemLookUpEntity(String lookupVal, String lookTypeText, BigInteger lookUpId, Integer displayOrder,
			String lookTypeCode) {
		super();
		this.lookUpId = lookUpId;
		this.lookTypeCode = lookTypeCode;
		this.lookupVal = lookupVal;
		this.lookTypeText = lookTypeText;
		this.displayOrder = displayOrder;
	}

	/**
	 * @param lookTypeText
	 * @param lookUpId
	 */
	public SystemLookUpEntity(String lookTypeText, BigInteger lookUpId) {
		this.lookUpId = lookUpId;
		this.lookTypeText = lookTypeText;

	}

	/**
	 * 
	 */
	public SystemLookUpEntity() {
		super();
	}

	/**
	 * 
	 */
	public SystemLookUpEntity(String lookupVal, String lookTypeText) {
		this.lookupVal = lookupVal;
		this.lookTypeText = lookTypeText;
	}

	@Override
	public int compareTo(SystemLookUpEntity obj) {
		if (this.displayOrder == obj.displayOrder) {
			return 0;
		} else if (this.displayOrder > obj.displayOrder) {
			return 1;
		} else {
			return -1;
		}
	}

	@Override
	public String toString() {
		return "SystemLookUpEntity [lookUpId=" + lookUpId + ", lookTypeCode=" + lookTypeCode + ", lookupVal="
				+ lookupVal + ", lookTypeText=" + lookTypeText + ", displayOrder=" + displayOrder + ", isActive="
				+ isActive + "]";
	}
}