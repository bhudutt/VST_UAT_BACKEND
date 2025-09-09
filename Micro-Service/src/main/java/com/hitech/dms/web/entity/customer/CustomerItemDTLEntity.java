package com.hitech.dms.web.entity.customer;

import java.io.Serializable;
import java.math.BigInteger;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;

	@Entity
	@Table(name="cm_cust_Item_dtl")
	@Data
public class CustomerItemDTLEntity implements Serializable {

	private static final long serialVersionUID = 9059371254561964563L;
	@Id
	@Column(name = "id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)	
	private Integer id;
	@Column(name="cust_hdr_id")
	private BigInteger customermstId;
	@Column(name="PC_Id")
	private Integer pcId;
	@Column(name="Series_Id")
	private Integer seriesId;
	@Column(name="Segment_Id")
	private Integer segmentId;
	@Column(name="Mdoel_Id")
	private Integer modelId;
	@Column(name="Variant_Id")
	private Integer variantId;
	@Column(name="Item_Id")
	private Integer itemId;
}
