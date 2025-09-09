package com.hitech.dms.web.model.dealer.employee.search.response;

import java.math.BigInteger;

import lombok.Data;

/**
 * @author vinay.gautam
 *
 */

@Data
public class DealerEmployeeSearchResponse {
	
    private BigInteger Id;

    private String action;
    
    private String employeeCode;

//    private String title;

    private Character activeStatus;

    private String employeeName;

    private String middleName;

    private String lastName;

    private String emailId;

    private String mobileNumber;

    private String alternateMobileNumber;

    private String departmentName;

    private String designation;

    private String licenceType;

    private String drivingLicenceNumber;

    private String  drivingLicenceExpiryDate;

    private String  joiningDate;

    private String latestSalary;

    private String  leavingDate;

    private String pfNumber;

    private String panNumber;

    private String esiNumber;

    private String bankAccountNumber;

    private String bankName;

    private String bankBranch;

    private String address1;

    private String address2;

    private String address3;

    private String pinCode;

    private String locality;

    private String tehsil;

    private String district;

    private String city;

    private String state;

    private String country;

}
