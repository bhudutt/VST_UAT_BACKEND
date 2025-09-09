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
@Table(name = "SA_ENQ_CUST_FLT_DTL")
@Entity
@Data
public class EnquiryCustFleetDTLEntity implements Serializable {
	/**
	* 
	*/
	private static final long serialVersionUID = -5514506209197831864L;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "enq_machinery_id")
	private BigInteger enquiryMachineryId;
	
	@JoinColumn(name="enquiry_id")
    @ManyToOne(fetch = FetchType.LAZY)
	private EnquiryHdrEntity enquiryHdr;
	
	@Column(name = "brand_id")
	private BigInteger brandId;
	
	@Column(name = "Model_Name")
	private String modelName;
	
	@Column(name = "Year_of_Purchase")
	private Integer yearOfPurchase;
	
	@Type(type = "org.hibernate.type.NumericBooleanType")
	@Column(name = "delete_flag", columnDefinition = "TINYINT(1)")
	private Boolean deleteFlag;
	
	@Column(name = "lastupdatedon")
	private Date lastupdatedon;
}
