package com.hitech.dms.web.dao.enquiry.search;

import java.util.Comparator;

import com.hitech.dms.web.model.enquiry.list.response.EnquiryListResponseModel;


public class StatusCustomComparator implements Comparator<EnquiryListResponseModel> {

	@Override
	public int compare(EnquiryListResponseModel o1, EnquiryListResponseModel o2) {
		// TODO Auto-generated method stub
		// Compare the names of the objects, ignoring case
        String name1 = o1.getAction();
        String name2 = o2.getAction();
        return name2.compareToIgnoreCase(name1);
	}

}
