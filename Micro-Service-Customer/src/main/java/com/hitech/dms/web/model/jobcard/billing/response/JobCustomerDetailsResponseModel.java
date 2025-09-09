package com.hitech.dms.web.model.jobcard.billing.response;

import java.math.BigDecimal;
import java.math.BigInteger;

import lombok.Data;

@Data
public class JobCustomerDetailsResponseModel {
  private String customerType;
  private String customerCode;
  private String customerName;
  private String mobile;
  private String state;
  private String district;
  private String tehsil;
  private String city;
  private BigInteger cityId;
  private String pinCode;
  private Integer branchId;
  
  
}
