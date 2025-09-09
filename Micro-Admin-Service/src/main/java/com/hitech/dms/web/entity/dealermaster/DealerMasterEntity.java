package com.hitech.dms.web.entity.dealermaster;

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

/**
 * @author Sunil.Singh
 *
 */
@Entity
@Table(name = "STG_ERP_DEALER_MASTER")
@Data
    public class DealerMasterEntity implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="BATCHID")
	private BigInteger batchId;
	@Column(name="Dealer_Code")
    private String dealerCode;
	@Column(name="Dealer_Name")
    private String dealerName;
	@Column(name="Dealer_Contact_Person")
    private String dealerContactPerson;
	@Column(name="Dealer_Address_1")
    private String dealerAddress1;
	@Column(name="Dealer_Address_2")
    private String dealerAddress2;
	@Column(name="State_Code")
    private String stateCode;
	@Column(name="City")
    private String city;
	@Column(name="Pin_Code")
    private String pinCode;
	@Column(name="Mobile_Number")
    private String mobileNumber;
	@Column(name="Telephone_No")
    private String telephoneNo;
	@Column(name="GST_Number")
    private String gstNumber;
	@Column(name="PAN_Number")
    private String panNumber;
	@Column(name="Company_Code")
    private String companyCode;
	@Column(name="Account_Number")
    private String accountNumber;
	@Column(name="IFSC_Code")
    private String ifscCode;
	@Column(name="Country")
    private String country;
	@Column(name="Mail_ID")
    private String mailId;
	@Column(name="Division")
    private String division;
	@Column(name="Division_Name")
    private String divisionName;
	@Column(name="Distribution_Channel")
    private String distributionChannel;
	@Column(name="Sales_Organization")
    private String salesOrganization;
	@Column(name="Dealer_Group")
    private String dealerGroup;
	@Column(name="Dealer_Group_Name")
    private String dealerGroupName;
	@Column(name="SyncError")
    private String syncError;
	@Column(name="SyncDate")
    private Date syncDate;
	@Column(name="profit_center")
    private String profitCenter;
	@Column(name="dealerActive")
	private String dealerActive;
}
