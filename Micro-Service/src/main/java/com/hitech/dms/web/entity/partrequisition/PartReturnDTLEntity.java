package com.hitech.dms.web.entity.partrequisition;

import java.io.Serializable;
import java.math.BigDecimal;
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
@Table(name = "PA_WK_RETURN_DTL")
@Data
public class PartReturnDTLEntity implements Serializable{
	
	private static final long serialVersionUID = 1867514944801926132L;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="Wk_Return_Dtl_Id")
	private BigInteger returnDtlId;
	@Column(name="Wrk_Return_Id")
	private BigInteger returnId;
	@Column(name="Issue_Dtl_Id")
	private BigInteger issueDtlId;
	@Column(name="Stock_Bin_Id")
	private BigInteger stockBinId;
	@Column(name="ReturnedQty")
	private BigDecimal returnedQty;
	@Column(name="CreatedBy")
	private String createdBy;
	@Column(name="CreatedDate")
	private Date createdDate =new Date();
	@Column(name="ModifiedBy")
	private String modifiedBy;
	@Column(name="ModifiedDate")
	private Date modifiedDate =new Date();
	@Column(name="Chassi_SerialNo")
	private String chassiSerialNo;
	@Column(name="Version_No")
	private Integer versionNo;
	@Column(name="Remarks")
	private String returnRemarks;
}
