/**
 * 
 */
package com.hitech.dms.web.entity.machinepo;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import lombok.Data;

/**
 * @author dinesh.jakhar
 *
 */
@Entity
@Table(name = "SA_MST_PO_STATUS", uniqueConstraints = { @UniqueConstraint(columnNames = "po_status_id") })
@Data
public class MachinePOStatusEntity implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -3103211697387991533L;
	@Id
	@Column(name = "po_status_id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer poStatusId;

	@Column(name = "po_status")
	private String status;

	@Column(name = "last_modified_date")
	private Date modifiedBy;
}
