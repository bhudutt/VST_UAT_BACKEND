/**
 * 
 */
package com.hitech.dms.web.model.enquiry.list.response;

import java.math.BigDecimal;
import java.math.BigInteger;

import lombok.Data;

/**
 * @author santosh.kumar
 *
 */
@Data
public class SalesEnquiryReportResponse {
	private BigInteger stateId;
    private String state;
    private String cluster;
    private String tm;
    private String dealerdship;
    private String dealerLocation;
    private String model;
    private String itemNumber;
    private int monthOpeningStock;
    private int monthBilling;
    private int monthDelivery;
    private BigDecimal monthClosingStock;

}
