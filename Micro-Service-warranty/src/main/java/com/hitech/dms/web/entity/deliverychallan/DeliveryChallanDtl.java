package com.hitech.dms.web.entity.deliverychallan;

import java.math.BigInteger;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonBackReference;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "WA_DELIVERY_CHALLAN_DTL")
public class DeliveryChallanDtl {
	
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private BigInteger id;
	
	@ManyToOne(fetch = FetchType.LAZY)
    @JsonBackReference
    @JoinColumn(name = "dc_id", referencedColumnName = "id")
    private DeliveryChallanHdr deliveryChallanHdr;
	
	@Column(name = "wcr_id")
	private Long wcrId;
	
}
