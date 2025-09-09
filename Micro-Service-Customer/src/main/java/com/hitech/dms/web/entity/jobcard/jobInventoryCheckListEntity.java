/**
 * 
 */
package com.hitech.dms.web.entity.jobcard;

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
@Table(name="SV_Inventory_RO_Mapping")
@Data
public class jobInventoryCheckListEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	@Column(name="RO_ID")
	private int roId;
	@Column(name="Inventory_id")
	private int inventoryId;
	@Column(name = "Createdby")
    private String createdBy;
	@Column(name = "CreatedDate")
	private Date createdDate;
}
