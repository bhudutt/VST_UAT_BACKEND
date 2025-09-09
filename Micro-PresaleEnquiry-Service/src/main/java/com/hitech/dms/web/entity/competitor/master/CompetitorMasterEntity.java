package com.hitech.dms.web.entity.competitor.master;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;

@Entity
@Table(name="COMPETITOR_MASTER")
@Data
public class CompetitorMasterEntity implements Serializable{
	
	private static final long serialVersionUID = 1867514944801926183L;
	
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="ID")
    private Integer id;
	@Column(name="pc_id")
	private Integer profitCenterId;
	@Column(name="brand_id")
	private Integer brandId;
	@Column(name="IsActive")
	private Character isActive;

}
