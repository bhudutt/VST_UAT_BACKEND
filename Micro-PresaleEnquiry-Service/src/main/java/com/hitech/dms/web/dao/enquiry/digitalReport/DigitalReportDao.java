package com.hitech.dms.web.dao.enquiry.digitalReport;

import java.math.BigInteger;
import java.util.Date;
import java.util.List;

import com.hitech.dms.web.model.digitalReport.request.DigitalEnquiReportModel;
import com.hitech.dms.web.model.digitalReport.request.DigitalEnquirySearchRequestModel;

public interface DigitalReportDao {

	List<?> downloadDigitalReport(String userCode, BigInteger digitalEnqHrdId);

	public List<DigitalEnquiReportModel> searchDigitalReport(DigitalEnquirySearchRequestModel requestModel, String userCode);

}
