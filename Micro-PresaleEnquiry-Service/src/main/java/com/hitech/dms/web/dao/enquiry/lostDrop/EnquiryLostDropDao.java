package com.hitech.dms.web.dao.enquiry.lostDrop;

import java.util.List;

import com.hitech.dms.web.model.enquiry.lostDrop.request.LostDropResponseModel;

public interface EnquiryLostDropDao {

	List<LostDropResponseModel> getLostDropReason(String flag);
}
