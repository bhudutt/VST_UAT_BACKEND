package com.hitech.dms.web.entity.partrequisition;

import java.io.Serializable;
import java.math.BigInteger;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;

@Entity
@Table(name = "PA_WK_RETURN")
@Data
public class PartReturnHDREntity implements Serializable{

	private static final long serialVersionUID = 1867514944801926184L;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="Wk_Return_Id")
	private BigInteger  returnId;
	@Column(name="ReturnNumber")
	private String returnNumber;
	@Column(name="Return_Date")
	private Date returnDate=new Date();
	@Column(name="Branch_Id")
	private Integer branch;
	@Column(name="To_Branch_Store_Id")
	private BigInteger branchStoreId;
	@Column(name="Remarks")
	private String remarks;
	@Column(name="CreatedBy")
	private String createdBy;
	@Column(name="CreatedDate")
	private Date createdDate =new Date();
	@Column(name="ModifiedBy")
	private String modifiedBy;
	@Column(name="ModifiedDate")
	private Date modifiedDate =new Date();
	@Column(name="View_Doc_No")
	private String viewDocNo;
	@Column(name="Version_No")
	private Integer versionNo;
	@Column(name="Return_Type")
	private Integer returnType;
	@Column(name="ReturnBy")
	private Integer returnBy;
	@Column(name="Ro_Id")
	private BigInteger roId;
	@Column(name="Reason_for_id")
	private Integer reasonId;
}
