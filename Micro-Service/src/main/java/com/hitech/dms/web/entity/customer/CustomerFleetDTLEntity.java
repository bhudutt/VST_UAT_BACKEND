/**
 * 
 */
package com.hitech.dms.web.entity.customer;

import java.io.Serializable;
import java.math.BigInteger;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.Cascade;

import lombok.Data;

/**
 * @author dinesh.jakhar
 *
 */
@Entity
@Table(name = "CM_CUST_FLT_DTL")
@Data
public class CustomerFleetDTLEntity implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -5926969124786321783L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "cust_flt_dtl_id")
	private BigInteger custFleetId;

	@ManyToOne
	@JoinColumn(name = "Customer_Id")
	@Cascade(org.hibernate.annotations.CascadeType.ALL)
	private CustomerHDREntity customerHdr;

	@Column(name = "brand_id")
	private BigInteger brandId;

	@Column(name = "Model_Name")
	private String modelName;

	@Column(name = "Year_of_Purchase")
	private Integer yearOfPurchase;

	@Column(name = "DeliveryDate")
	private Date deliveryDate;

	@Column(name = "is_sold")
	private String isSold;

	@Column(name = "Location")
	private String location;

	@Column(name = "Machine_Srl_No")
	private String machineSrlNo;

	@Column(name = "Latitude")
	private String latitude;

	@Column(name = "Longitude")
	private String longitude;

	@Column(name = "CreatedBy", updatable = false)
	private BigInteger createdBy;

	@Column(name = "CreatedDate", updatable = false)
	private Date createdDate;

	@Column(name = "ModifiedBy")
	private BigInteger modifiedBy;

	@Column(name = "ModifiedDate")
	private Date modifiedDate;

}
