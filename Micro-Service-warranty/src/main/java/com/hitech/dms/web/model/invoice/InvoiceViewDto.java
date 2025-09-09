package com.hitech.dms.web.model.invoice;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;
import java.util.List;

import lombok.Data;

@Data
public class InvoiceViewDto {
	
	private BigInteger id;
	
	private String invoiceNo;
	
	private Date invoiceDate;
	
	private String wcrNo;
	
	private Date wctDate;
	
	private String material;
    private BigDecimal unitPrice;
    private BigDecimal quantity;
    private BigDecimal amount;
    private String sac;
    private BigDecimal taxPercent;
    private BigDecimal taxValue;
    private BigDecimal totalValue;
    
    private BigDecimal basePrice;
    private BigDecimal gstAmount;
    private BigDecimal invoiceAmount;
    
    private String customerInvoiceNo;
    private String customerInvoiceDate;
    private String finalSubmit;
	
	private VechileDetailsDto vechileDetailsDto;
	
	private CustomerDetailsDto customerDetailsDto;
	
//	private HandlingChargeDto handlingChargeDto;
	
	private List<PartsDetailsDto> partsDetailsDto;
	
	private List<LabourDetailsDto> labourDetailsDto;
	
	private List<LabourDetailsDto> outsideLabourDetailsDto;

}
