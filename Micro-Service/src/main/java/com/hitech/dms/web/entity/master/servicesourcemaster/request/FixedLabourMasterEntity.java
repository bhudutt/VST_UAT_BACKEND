package com.hitech.dms.web.entity.master.servicesourcemaster.request;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import lombok.Data;

@Entity
@Table(name="SV_FIXED_LABOUR_MST")
@Data
public class FixedLabourMasterEntity {

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="fixed_labour_id")
	private Integer fixedLabourId;
	
	@OneToOne
	@JoinColumn(name="Labour_Id")
	private ServiceLabourMasterEntity serviceLabourMaster;
	
	@Column(name="LabourAmt")
	private Double labourAmount;
	@Temporal(TemporalType.DATE)
	@Column(name="FromDate")
	private Date fromDate;
	@Column(name="ToDate")
	@Temporal(TemporalType.DATE)
	private Date toDate;
    @Column(name="CreatedDate")
	private Date createdDate;
    @Column(name="CreatedBy")
    private String createdBy;
}
