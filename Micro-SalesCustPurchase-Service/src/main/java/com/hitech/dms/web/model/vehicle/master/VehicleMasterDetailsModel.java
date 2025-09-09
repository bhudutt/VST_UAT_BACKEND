package com.hitech.dms.web.model.vehicle.master;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Data;
@Data
public class VehicleMasterDetailsModel {

	private String chassisNo;
    private String engineNo;
    private String vinNo;
    private String mfgInvoiceNo;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
    private Date mfgInvoiceDate;
    private String profitCenter;
    private String model;
    private String series;
    private String segment;
    private String variant;
    private String itemNo;
    private String itemDescription;
    private String inPdiNo;
    private String grnNo;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
    private Date grnDate;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
    private Date inPdiDate;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private String outPdiDate;
    private String installationNo;
    private String outPdiNo;
    private String soldBy;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
    private String dateOfInstallation;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
    private Date saleDate;
    private String operatorManualNo;
    private String lastKmReported;
    private String registrationNo;
    private String lastServiceType;
    private String lastHrReported;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
    private Date lastServiceDate;
    private String nextDueService;

    // Customer Details
    private String customerCode;
    private String customerName;
    private String mobileNo;
    private String prospectCategory;
    private String address1;
    private String whatsappNo;
    private String alternateMobileNo;
    private String address2;
    private String district;
    private String emailId;
    private String address3;
    private String tehsilTalukaMandal;
    private String cityVillage;
    private String pincode;
    private String state;

    // Battery Details
    private String batteryMake;
    private String batteryNo;

    // FIP Details
    private String fipMake;
    private String fipNo;

    // Starter Details
    private String starterMake;
    private String starterNo;

    // Alternator Details
    private String alternatorMake;
    private String alternatorNo;

    // Tyres Details
    private String frontTyreRhMake;
    private String frontTyreRhNo;
    private String frontTyreLhMake;
    private String frontTyreLhNo;
    private String rearTyreRhMake;
    private String rearTyreRhNo;
    private String rearTyreLhMake;
    private String rearTyreLhNo;

    // Standard Warranty
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
    private Date standardWarrantyStartDate;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
    private Date standardWarrantyEndDate;
    
    // EXTENDED Warranty
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
    private String extendedWarrantyStartDate;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
    private String extendedWarrantyEndDate;
    // AMC Details
    private String amcPolicy;
    private String amcPolicyNo;
    private String amcStatus;
    private String amcExpiry;
    // Insurance Details
    private String insuranceCompany;
    private String insuranceDate;
    private String insuranceCoverNoteNo;
    private String insuranceExpiry;
    
    
}
