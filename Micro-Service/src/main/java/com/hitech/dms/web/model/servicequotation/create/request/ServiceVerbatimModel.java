package com.hitech.dms.web.model.servicequotation.create.request;

import java.util.Date;

import lombok.Data;

@Data
public class ServiceVerbatimModel {

	private Integer quotationVerbtmId;
	private ServiceQuotationModel serviceQuotationModel ;
    private Integer srNo;
    private boolean check;
    private String verbatim;
	private String verbatimSource;
	private Date createdDate;
    private String createdBy;
	private Date modfiedDate;
	private String modifiedBy;
	private String customerVoice;
}
