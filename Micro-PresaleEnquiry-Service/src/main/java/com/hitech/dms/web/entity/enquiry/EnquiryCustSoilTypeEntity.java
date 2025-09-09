/**
 * 
 */
package com.hitech.dms.web.entity.enquiry;

import java.io.Serializable;
import java.math.BigInteger;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.Type;

import lombok.Data;

/**
 * @author dinesh.jakhar
 *
 */
@Table(name = "SA_ENQ_CUST_SOIL_TYPE")
@Entity
@Data
public class EnquiryCustSoilTypeEntity implements Serializable {
	/**
	* 
	*/
	private static final long serialVersionUID = 8404406491630194830L;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "enq_soil_type_id")
	private BigInteger enquirySoilTypeId;
	
	@JoinColumn(name="enquiry_id")
    @ManyToOne(fetch = FetchType.LAZY)
	private EnquiryHdrEntity enquiryHdr;
	
	@Column(name = "soil_type")
	private String soilType;
	
	@Type(type = "org.hibernate.type.NumericBooleanType")
	@Column(name = "delete_flag", columnDefinition = "TINYINT(1)")
	private Boolean deleteFlag;
	
	@Column(name = "lastupdatedon")
	private Date lastupdatedon;	
	
}
