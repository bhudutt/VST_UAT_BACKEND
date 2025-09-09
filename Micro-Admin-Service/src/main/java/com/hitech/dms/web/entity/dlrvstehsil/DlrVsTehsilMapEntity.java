/**
 * 
 */
package com.hitech.dms.web.entity.dlrvstehsil;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

import lombok.Data;

/**
 * @author dinesh.jakhar
 *
 */
@Data
@Table(name="ADM_BP_DEALER_TEHSIL_MAP")
@Entity
public class DlrVsTehsilMapEntity implements Serializable {
	/**
	* 
	*/
	private static final long serialVersionUID = 6678640090060411948L;

	@EmbeddedId
	private DlrVsTehsilMapPEntity dlrVsTehsilMapP;

	@Column(name = "lastUpdatedBy")
	private String modifiedBy;

	@Column(name = "lastUpdatedOn")
	private Date modifiedDate;
}
