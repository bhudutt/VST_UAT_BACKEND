/**
 * 
 */
package com.hitech.dms.web.entity.enquiry;

import java.io.Serializable;
import java.math.BigDecimal;
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
@Table(name = "SA_ENQ_EXCHANGE_DTL")
@Entity
@Data
public class EnquiryExchangeDTLEntity implements Serializable {
	/**
	* 
	*/
	private static final long serialVersionUID = -1937649827836650089L;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "enq_exc_dtl_id")
	private BigInteger enquiryExcDTLId;

	@JoinColumn(name = "enquiry_id")
	@ManyToOne(fetch = FetchType.LAZY)
	private EnquiryHdrEntity enquiryHdr;

	@Column(name = "brand_id")
	private BigInteger brandId;

	@Column(name = "model_name")
	private String modelName;

	@Column(name = "model_year")
	private Integer modelYear;

	@Column(name = "estimated_exchange_price")
	private BigDecimal estimatedExchangePrice;
	
	@Column(name = "Inv_In_Date")
	private Date invInDate;

	@Type(type = "yes_no")
	@Column(name = "machine_received")
	private Boolean machineReceived;

	@Column(name = "CreatedBy", updatable = false)
	private BigInteger createdBy;
	@Column(name = "CreatedDate", updatable = false)
	private Date createdDate;
	@Column(name = "ModifiedBy")
	private BigInteger modifiedBy;
	@Column(name = "ModifiedDate")
	private Date modifiedDate;
}
