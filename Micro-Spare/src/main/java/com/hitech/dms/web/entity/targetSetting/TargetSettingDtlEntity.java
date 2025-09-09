package com.hitech.dms.web.entity.targetSetting;


import java.math.BigInteger;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;


import lombok.Data;

@Data
@Entity
@Table(name = "PA_TARGET_SETTING_DTL")
public class TargetSettingDtlEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="target_dtl_id")
	private BigInteger targetDtlId;
	
	@Column(name="target_hdr_id")
	private BigInteger targetHdrId;
	
	@Column(name="party_id")
	private BigInteger partyId;

	@Column(name="apr")
	private Double apr;

	@Column(name="may")
	private Double may;

	@Column(name="jun")
	private Double jun;

	@Column(name="jul")
	private Double jul;

	@Column(name="aug")
	private Double aug;

	@Column(name="sep")
	private Double sep;

	@Column(name="oct")
	private Double oct;

	@Column(name="nov")
	private Double nov;

	@Column(name="dec")
	private Double dec;

	@Column(name="jan")
	private Double jan;

	@Column(name="feb")
	private Double feb;

	@Column(name="mar")
	private Double mar;

		
}
