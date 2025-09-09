package com.hitech.dms.web.entity.dealer.employee.create;

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

import com.fasterxml.jackson.annotation.JsonBackReference;

import lombok.Data;

/**
 * @author vinay.gautam
 *
 */

@Table(name = "ADM_BP_DEALER_EMP_AD")
@Entity
@Data
public class DealerEmployeeAddressEntity implements Serializable, Cloneable {
	/**
		 * 
		 */
	private static final long serialVersionUID = -7444626201776999466L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "Emp_Ad_Id")
	private BigInteger empAddressId;

//	@Column(name = "emp_Id")
//	private BigInteger employeeId;

	@JoinColumn(name = "emp_Id")
	@ManyToOne(fetch = FetchType.LAZY)
	@JsonBackReference
	private DealerEmployeeEntity dealerEmpHdr;

	@Column(name = "CustAddLine1")
	private String custAddress1;

	@Column(name = "CustAddLine2")
	private String custAddLine2;

	@Column(name = "CustAddLine3")
	private String custAddLine3;

	@Column(name = "City_Id")
	private BigInteger cityId;

	@Column(name = "Pin_Id")
	private BigInteger pinId;

	@Column(name = "PinCode")
	private String pinCode;

	@Column(name = "CreatedBy")
	private String createdBy;

	@Column(name = "CreatedDate")
	private Date createdDate;

	@Column(name = "ModifiedBy")
	private String modifiedBy;

	@Column(name = "ModifiedDate")
	private Date modifiedDate;

	public DealerEmployeeAddressEntity() {

	}

	public DealerEmployeeAddressEntity(DealerEmployeeAddressEntity address) {
		super();
		this.custAddress1 = address.getCustAddress1();
		this.custAddLine2 = address.getCustAddLine2();
		this.custAddLine3 = address.getCustAddLine3();
		this.cityId = address.getCityId();
		this.pinId = address.getPinId();
		this.pinCode = address.getPinCode();
	}

	@Override
	public Object clone() throws CloneNotSupportedException {
		try {
			return (DealerEmployeeAddressEntity) super.clone();
		} catch (CloneNotSupportedException e) {
			return new DealerEmployeeAddressEntity(this.empAddressId, this.dealerEmpHdr, this.custAddress1,
					this.custAddLine2, this.custAddLine3, this.cityId, this.pinId, this.pinCode, this.createdBy,
					this.createdDate, this.modifiedBy, this.modifiedDate);
		}
	}

	public DealerEmployeeAddressEntity(BigInteger empAddressId, DealerEmployeeEntity dealerEmpHdr, String custAddress1,
			String custAddLine2, String custAddLine3, BigInteger cityId, BigInteger pinId, String pinCode,
			String createdBy, Date createdDate, String modifiedBy, Date modifiedDate) {
		super();
		this.empAddressId = empAddressId;
		this.dealerEmpHdr = dealerEmpHdr;
		this.custAddress1 = custAddress1;
		this.custAddLine2 = custAddLine2;
		this.custAddLine3 = custAddLine3;
		this.cityId = cityId;
		this.pinId = pinId;
		this.pinCode = pinCode;
		this.createdBy = createdBy;
		this.createdDate = createdDate;
		this.modifiedBy = modifiedBy;
		this.modifiedDate = modifiedDate;
	}

}
