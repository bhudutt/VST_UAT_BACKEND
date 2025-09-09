package com.hitech.dms.web.dao.enquiry.transfer.list;

import java.util.List;

import com.hitech.dms.web.model.enquiry.transfer.request.TransferENQRequestModel;
import com.hitech.dms.web.model.enquiry.transfer.response.TransferENQResponse;

public interface EnquiryTransferListDao {
	public List<TransferENQResponse> fetchTransferENQList(String userCode, TransferENQRequestModel enqRequestModel);
}
